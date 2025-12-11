package com.salesdoctor.reactiveauctionhouse.application.dto;

import java.math.BigDecimal;

public record BidRequest(
        String userId,
        BigDecimal amount
) {}
