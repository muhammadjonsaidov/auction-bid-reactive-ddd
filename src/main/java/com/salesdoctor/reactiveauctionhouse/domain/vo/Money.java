package com.salesdoctor.reactiveauctionhouse.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record Money(
        @JsonProperty("amount")
        BigDecimal amount
) {

    public Money {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Pul miqdori manfiy bo'lishi mumkin emas");
        }
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public boolean isGreaterThan(Money money) {
        return this.amount.compareTo(money.amount) > 0;
    }

    public Money add(Money money) {
        return new Money(this.amount.add(money.amount));
    }
}
