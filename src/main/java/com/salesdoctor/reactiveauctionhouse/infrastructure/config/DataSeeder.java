package com.salesdoctor.reactiveauctionhouse.infrastructure.config;

import com.salesdoctor.reactiveauctionhouse.domain.model.Auction;
import com.salesdoctor.reactiveauctionhouse.domain.vo.Money;
import com.salesdoctor.reactiveauctionhouse.domain.port.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final AuctionRepository auctionRepository;

    @Override
    public void run(String... args) {
        String auctionId = "12345"; // Test ID

        auctionRepository.findById(auctionId)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Test auksion yaratilmoqda...");
                    Auction auction = new Auction(
                            auctionId,
                            Money.of(100.00), // Boshlang'ich narx
                            Money.of(10.00), // Min qadam
                            Instant.now().plus(1, ChronoUnit.HOURS) // 1 soatdan keyin tugaydi
                    );
                    return auctionRepository.save(auction);
                }))
                .subscribe(
                        saved -> log.info("Auksion tayyor. ID: {}", saved.getId()),
                        error -> log.error("Xatolik bo'ldi", error)
                );
    }
}
