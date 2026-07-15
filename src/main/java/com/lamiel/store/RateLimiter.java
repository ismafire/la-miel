package com.lamiel.store;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A simple sliding-window rate limiter.
 * Tracks how many requests each key (usually an IP address) has made
 * in the last `windowMillis` milliseconds, and rejects once `maxRequests` is hit.
 */
public class RateLimiter {

    private final int maxRequests;
    private final long windowMillis;
    private final Map<String, Deque<Long>> requestLog = new ConcurrentHashMap<>();

    public RateLimiter(int maxRequests, long windowMillis) {
        this.maxRequests = maxRequests;
        this.windowMillis = windowMillis;
    }

    public boolean isAllowed(String key) {
        long now = System.currentTimeMillis();
        Deque<Long> timestamps = requestLog.computeIfAbsent(key, k -> new ArrayDeque<>());

        synchronized (timestamps) {
            // Drop any timestamps older than our window
            while (!timestamps.isEmpty() && now - timestamps.peekFirst() > windowMillis) {
                timestamps.pollFirst();
            }
            if (timestamps.size() >= maxRequests) {
                return false; // Over the limit — reject this request
            }
            timestamps.addLast(now);
            return true;
        }
    }
}
