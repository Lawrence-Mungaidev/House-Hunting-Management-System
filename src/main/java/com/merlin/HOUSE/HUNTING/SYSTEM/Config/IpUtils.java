package com.merlin.HOUSE.HUNTING.SYSTEM.Config;


import jakarta.servlet.http.HttpServletRequest;

public class IpUtils {

    public String getClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
