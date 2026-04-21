package com.helpdesk.helpdesk.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {

    @Value("${app.security.login-max-attempts:5}")
    private int maxAttempts;

    @Value("${app.security.login-block-minutes:15}")
    private long blockMinutes;

    private record AttemptInfo(int count, long firstAttemptMs) {}

    private final ConcurrentHashMap<String, AttemptInfo> cache = new ConcurrentHashMap<>();

    public void loginSucceeded(String ip) {
        cache.remove(ip);
    }

    public void loginFailed(String ip) {
        cache.merge(ip,
                new AttemptInfo(1, System.currentTimeMillis()),
                (existing, novo) -> {
                    if (isWindowExpired(existing)) {
                        return new AttemptInfo(1, System.currentTimeMillis());
                    }
                    return new AttemptInfo(existing.count() + 1, existing.firstAttemptMs());
                });
    }

    public boolean isBlocked(String ip) {
        AttemptInfo info = cache.get(ip);
        if (info == null) return false;
        if (isWindowExpired(info)) {
            cache.remove(ip);
            return false;
        }
        return info.count() >= maxAttempts;
    }

    private boolean isWindowExpired(AttemptInfo info) {
        return System.currentTimeMillis() - info.firstAttemptMs() > blockMinutes * 60_000L;
    }
}
