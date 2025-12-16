package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record BidRequest(
        String userId,
        BigDecimal amount
) {}
