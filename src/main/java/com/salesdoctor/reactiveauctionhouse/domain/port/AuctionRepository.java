package com.salesdoctor.reactiveauctionhouse.domain.port;

import com.salesdoctor.reactiveauctionhouse.domain.model.Auction;
import reactor.core.publisher.Mono;

public interface AuctionRepository {
    Mono<Auction> findById(String id);
    Mono<Auction> save(Auction auction);
}
