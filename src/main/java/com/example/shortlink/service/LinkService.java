// LinkService.java
package com.example.shortlink.service;

import com.example.shortlink.model.ShortLink;
import com.example.shortlink.repository.ShortLinkRepository;
import com.example.shortlink.request.LinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LinkService {
    @Autowired
    private ShortLinkRepository shortLinkRepository;

    private static final String BASE_URL = "http://clck.ru/";
    private final Map<String, String> shortToOriginalMap = new ConcurrentHashMap<>();

    public ShortLink createShortLink(LinkRequest linkRequest) {
        ShortLink shortLink = new ShortLink();
        String shortUrl = generateShortUrl(linkRequest.getOriginalUrl());
        shortLink.setShortUrl(shortUrl);
        shortLink.setOriginalUrl(linkRequest.getOriginalUrl());
        shortLink.setUserId(UUID.randomUUID().toString());
        shortLink.setExpiryDate(LocalDateTime.now().plusHours(linkRequest.getExpiryInHours()));
        shortLink.setMaxClicks(linkRequest.getMaxClicks());
        shortLink.setClickCount(0);
        shortLinkRepository.save(shortLink);
        return shortLink;
    }

    public String getOriginalUrl(String shortUrl) {
        Optional<ShortLink> shortLinkOpt = shortLinkRepository.findByShortUrl(shortUrl);
        if (shortLinkOpt.isPresent()) {
            ShortLink shortLink = shortLinkOpt.get();
            if (shortLink.getClickCount() < shortLink.getMaxClicks() && shortLink.getExpiryDate().isAfter(LocalDateTime.now())) {
                shortLink.setClickCount(shortLink.getClickCount() + 1);
                shortLinkRepository.save(shortLink);
                return shortLink.getOriginalUrl();
            }
        }
        return null;
    }

    public boolean deleteShortLink(String shortUrl, String userId) {
        Optional<ShortLink> shortLinkOpt = shortLinkRepository.findByShortUrl(shortUrl);
        if (shortLinkOpt.isPresent() && shortLinkOpt.get().getUserId().equals(userId)) {
            shortLinkRepository.delete(shortLinkOpt.get());
            return true;
        }
        return false;
    }

    public ShortLink updateMaxClicks(String shortUrl, String userId, int newMaxClicks) {
        Optional<ShortLink> shortLinkOpt = shortLinkRepository.findByShortUrl(shortUrl);
        if (shortLinkOpt.isPresent() && shortLinkOpt.get().getUserId().equals(userId)) {
            ShortLink shortLink = shortLinkOpt.get();
            shortLink.setMaxClicks(newMaxClicks);
            shortLinkRepository.save(shortLink);
            return shortLink;
        }
        return null;
    }

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deleteExpiredLinks() {
        List<ShortLink> expiredLinks = shortLinkRepository.findAllByExpiryDateBefore(LocalDateTime.now());
        shortLinkRepository.deleteAll(expiredLinks);
    }

    private String generateShortUrl(String originalUrl) {
        return BASE_URL + UUID.randomUUID().toString().substring(0, 8);
    }
}
