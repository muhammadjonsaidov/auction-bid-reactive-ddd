package com.salesdoctor.reactiveauctionhouse.application.service;

import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
import com.salesdoctor.reactiveauctionhouse.domain.model.vo.Money;
import com.salesdoctor.reactiveauctionhouse.domain.port.AuctionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BiddingService {

    private final AuctionRepository auctionRepository;

    // "Hot Stream" - Barcha ulanganlarga xabar tarqatuvchi (Multicast)
    private final Sinks.Many<BidPlacedEvent> eventSink = Sinks.many().multicast().onBackpressureBuffer();

    @Transactional // Reactive tranzaksiya
    public Mono<Void> placeBid(String auctionId, String bidderId, BigDecimal amount) {
        return auctionRepository.findById(auctionId)
                .switchIfEmpty(Mono.error(new RuntimeException("Auksion topilmadi: " + auctionId)))
                .flatMap(auction -> {
                    BidPlacedEvent event = auction.placeBid(bidderId, Money.of(amount));

                    log.info("Yangi taklif qabul qilindi: {} -> {}", auctionId, event);

                    return auctionRepository.save(auction)
                            .doOnSuccess(saved -> eventSink.tryEmitNext(event))
                            .then();
                });
    }

    // SSE (Server-Sent Events) uchun oqim
    public Flux<BidPlacedEvent> getAuctionStream(String auctionId) {
        return eventSink.asFlux()
                .filter(event -> event.auctionId().equals(auctionId));
    }
}
