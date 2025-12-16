package com.salesdoctor.reactiveauctionhouse.application.port.in;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PlaceBidUseCase {
    Mono<Void> placeBid(String auctionId, String bidderId, BigDecimal amount);
}
