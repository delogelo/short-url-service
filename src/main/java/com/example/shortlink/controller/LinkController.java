// LinkController.java
package com.example.shortlink.controller;

import com.example.shortlink.model.ShortLink;
import com.example.shortlink.service.LinkService;
import com.example.shortlink.request.LinkRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/links")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @PostMapping("/shorten")
    public ResponseEntity<ShortLink> createShortLink(@RequestBody LinkRequest linkRequest) {
        return ResponseEntity.ok(linkService.createShortLink(linkRequest));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirectToOriginal(@PathVariable String shortUrl) {
        String originalUrl = linkService.getOriginalUrl(shortUrl);
        if (originalUrl != null) {
            return ResponseEntity.status(302).header("Location", originalUrl).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{shortUrl}")
    public ResponseEntity<Void> deleteShortLink(@PathVariable String shortUrl, @RequestParam String userId) {
        if (linkService.deleteShortLink(shortUrl, userId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{shortUrl}/limit")
    public ResponseEntity<ShortLink> updateMaxClicks(@PathVariable String shortUrl, @RequestParam String userId, @RequestParam int newMaxClicks) {
        ShortLink updatedLink = linkService.updateMaxClicks(shortUrl, userId, newMaxClicks);
        if (updatedLink != null) {
            return ResponseEntity.ok(updatedLink);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
