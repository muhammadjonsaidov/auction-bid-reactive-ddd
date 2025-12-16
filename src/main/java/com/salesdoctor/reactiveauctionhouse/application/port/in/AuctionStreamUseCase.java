package com.salesdoctor.reactiveauctionhouse.application.port.in;

import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
import reactor.core.publisher.Flux;

public interface AuctionStreamUseCase {
    Flux<BidPlacedEvent> stream(String auctionId);
}