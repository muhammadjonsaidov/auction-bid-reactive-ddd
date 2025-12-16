package com.salesdoctor.reactiveauctionhouse.application.service;

import com.salesdoctor.reactiveauctionhouse.application.port.in.AuctionStreamUseCase;
import com.salesdoctor.reactiveauctionhouse.application.port.in.PlaceBidUseCase;
import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
import com.salesdoctor.reactiveauctionhouse.domain.port.AuctionRepository;
import com.salesdoctor.reactiveauctionhouse.domain.vo.Money;
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
public class BiddingService implements PlaceBidUseCase, AuctionStreamUseCase {

    private final AuctionRepository auctionRepository;

    private final Sinks.Many<BidPlacedEvent> eventSink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    @Transactional
    public Mono<Void> placeBid(String auctionId, String bidderId, BigDecimal amount) {
        return auctionRepository.findById(auctionId)
                .switchIfEmpty(Mono.error(
                        new RuntimeException("Auksion topilmadi: " + auctionId)))
                .flatMap(auction -> {
                    BidPlacedEvent event =
                            auction.placeBid(bidderId, Money.of(amount));

                    return auctionRepository.save(auction)
                            .doOnSuccess(a -> eventSink.tryEmitNext(event))
                            .then();
                });
    }

    @Override
    public Flux<BidPlacedEvent> stream(String auctionId) {
        return eventSink.asFlux()
                .filter(e -> e.auctionId().equals(auctionId));
    }
}
