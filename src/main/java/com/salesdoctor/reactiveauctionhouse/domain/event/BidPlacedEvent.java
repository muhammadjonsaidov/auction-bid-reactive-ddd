package com.salesdoctor.reactiveauctionhouse.domain.event;

import com.salesdoctor.reactiveauctionhouse.domain.model.vo.Money;

import java.time.Instant;

public record BidPlacedEvent(
        String auctionId,
        String bidderId,
        Money newAmount,
        Instant timestamp
) {
}
