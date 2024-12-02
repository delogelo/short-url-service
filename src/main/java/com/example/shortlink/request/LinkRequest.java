// LinkRequest.java
package com.example.shortlink.request;

public class LinkRequest {
    private String originalUrl;
    private int maxClicks;
    private long expiryInHours;

    // Getters and setters
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public long getExpiryInHours() {
        return expiryInHours;
    }

    public void setExpiryInHours(long expiryInHours) {
        this.expiryInHours = expiryInHours;
    }
}
