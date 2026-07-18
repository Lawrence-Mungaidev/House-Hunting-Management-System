package com.merlin.HOUSE.HUNTING.SYSTEM.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenBlackListingServices {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX = "blacklist:";

    public void blacklistTokens(String token, Date expiryDate){
        long remainingMillis = expiryDate.getTime() - System.currentTimeMillis();

        if(remainingMillis > 0){
            redisTemplate.opsForValue().set(
                    PREFIX + token,
                    "true",
                    Duration.ofMillis(remainingMillis));
        }
    }

    public boolean isBlackListed (String token){
        return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + token));
    }
}
