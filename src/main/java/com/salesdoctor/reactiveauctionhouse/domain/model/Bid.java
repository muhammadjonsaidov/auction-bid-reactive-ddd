package com.salesdoctor.reactiveauctionhouse.domain.model;

import com.salesdoctor.reactiveauctionhouse.domain.vo.Money;

import java.time.Instant;

public record Bid(
        String bidderId,
        Money amount,
        Instant time
) {
}
