package com.merlin.HOUSE.HUNTING.SYSTEM.Mpesa;


import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Apartment;
import com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.ApartmentRepository;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.PaymentException;
import com.merlin.HOUSE.HUNTING.SYSTEM.Exception.ResourceNotFound;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationService;
import com.merlin.HOUSE.HUNTING.SYSTEM.Notification.NotificationType;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.Status;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.Subscription;
import com.merlin.HOUSE.HUNTING.SYSTEM.Subscription.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MpesaService {

    private final WebClient webClient;
    private final SubscriptionRepository subscriptionRepository;
    private final NotificationService notificationService;
    private final ApartmentRepository apartmentRepository;


    @Value("${Mpesa.consumer-key}")
    private String consumerKey;

    @Value("${Mpesa.consumer-secret}")
    private String consumerSecret;

    @Value("${Mpesa.short-code}")
    private String shortCode;

    @Value("${Mpesa.passkey}")
    private String passkey;

    @Value("Mpesa.callback-url")
    private String callbackUrl;


    public MpesaService(WebClient webClient, SubscriptionRepository subscriptionRepository, NotificationService notificationService, ApartmentRepository apartmentRepository) {
        this.webClient = webClient;
        this.subscriptionRepository = subscriptionRepository;
        this.notificationService = notificationService;
        this.apartmentRepository = apartmentRepository;
    }


    private String getAccesToken() {
        String credentials = consumerKey + ":" + consumerSecret;
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());

        return webClient.get()
                .uri("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .header("Authorization", "Basic " + encoded)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"))
                .block();
    }

    public String initialiseSTKPush(String phoneNumber, BigDecimal totalAmount) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = Base64.getEncoder().encodeToString((shortCode + passkey + timeStamp).getBytes());
        String formattedPhone = "254" + phoneNumber.substring(1);

        Map<String, Object> request = new HashMap<>();

        request.put("BusinessShortCode", shortCode);
        request.put("Password", password);
        request.put("TimeStamp", timeStamp);
        request.put("TransactionType", "CustomerPayBillOnline");
        request.put("Amount", totalAmount.intValue() );
        request.put("PartyA", formattedPhone);
        request.put("PartyB", shortCode);
        request.put("PhoneNumber", formattedPhone);
        request.put("CallBackUrl", callbackUrl);
        request.put("AccountReference", "House hunting");
        request.put("TransactionDesc", "payment");

        return webClient.post()
                .uri("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .header("Authorization", "Bearer " + getAccesToken())
                .bodyValue(request)
                .retrieve()
                .onStatus(status-> status.is4xxClientError(), response ->
                        response.bodyToMono(String.class)
                                .doOnNext(body-> System.out.println("STK PUSH Error " + body))
                                .then(Mono.error(new PaymentException("STK PUSH FAILED"))))
                .bodyToMono(Map.class)
                .doOnNext(response -> System.out.println("STK Response: " + response))
                .map(response ->(String) response.get("CheckoutRequestID"))
                .block();

    }


    public void handleCallBack(Map<String, Object> callbackData) {
        Map<String, Object> body = (Map<String, Object>) callbackData.get("Body");
        Map<String, Object> stkCallback = (Map<String, Object>) body.get("stkCallback");
        String checkoutRequestId = (String) stkCallback.get("CheckoutRequestID");
        int resultCode = (int) stkCallback.get("ResultCode");

        Subscription subscription = subscriptionRepository.findByMpesaReference(checkoutRequestId)
                .orElseThrow(()-> new ResourceNotFound("Subscription Not Found"));

        if(resultCode != 0){
            subscription.setStatus(Status.FAILED);
            subscriptionRepository.save(subscription);


            List<Apartment> apartments = subscription.getApartment();

            for (Apartment apartment : apartments) {
                String message = "Subscription Failed for "+ apartment.getApartmentName() + "Please try again";

                notificationService.createNotification(apartment.getLandlord(), null,message, NotificationType.FAILED_SUBSCRIPTION);
            }


            return;
        }

        subscription.setStatus(Status.SUCCESSFUL);

        List<Apartment> apartmentS = subscription.getApartment();

        for (Apartment apartment : apartmentS ){

            if(apartment.getStatus().equals(com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Status.ACTIVE)){
                apartment.setExpireOn(apartment.getExpireOn().plusDays(30));
            }else {
                apartment.setExpireOn(LocalDateTime.now().plusDays(30));
            }

            apartment.setStatus(com.merlin.HOUSE.HUNTING.SYSTEM.Apartment.Status.ACTIVE);
            apartmentRepository.save(apartment);

            String message = "Subscription made Successfully, Apartment " + apartment.getApartmentName() + " will be active till " + apartment.getExpireOn();

            notificationService.createNotification(apartment.getLandlord(),null, message, NotificationType.SUCCESS_SUBSCRIPTION);

            subscriptionRepository.save(subscription);
        }






    }
}
