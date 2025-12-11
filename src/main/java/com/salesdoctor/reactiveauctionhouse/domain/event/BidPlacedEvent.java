package com.salesdoctor.reactiveauctionhouse.domain.event;

import java.math.BigDecimal;
import java.time.Instant;

public record BidPlacedEvent(
        String auctionId,
        String bidderId,
        BigDecimal amount,
        Instant timestamp
) {
}
