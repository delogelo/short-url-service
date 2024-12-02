// ShortLinkRepository.java
package com.example.shortlink.repository;

import com.example.shortlink.model.ShortLink;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ShortLinkRepository extends JpaRepository<ShortLink, Long> {
    Optional<ShortLink> findByShortUrl(String shortUrl);
    List<ShortLink> findAllByExpiryDateBefore(LocalDateTime dateTime);
}