package com.salesdoctor.reactiveauctionhouse.domain.model;

import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
import com.salesdoctor.reactiveauctionhouse.domain.exception.InvalidBidException;
import com.salesdoctor.reactiveauctionhouse.domain.model.vo.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Auction {
    private final String id;
    private Money currentPrice;
    private final Money minStep;
    private final Instant endTime;
    private final boolean isFinished;
    private final Long version;

    private final List<Bid> bids = new ArrayList<>();

    public Auction(String id, Money startPrice, Money minStep, Instant endTime) {
        this.id = id;
        this.currentPrice = startPrice;
        this.minStep = minStep;
        this.endTime = endTime;
        this.isFinished = false;

        this.version = null;
    }

    private Auction(String id, Money currentPrice, Money minStep, Instant endTime, boolean isFinished, Long version) {
        this.id = id;
        this.currentPrice = currentPrice;
        this.minStep = minStep;
        this.endTime = endTime;
        this.isFinished = isFinished;
        this.version = version;
    }

    public static Auction restore(String id, Money currentPrice, Money minStep, Instant endTime, boolean isFinished, Long version) {
        return new Auction(id, currentPrice, minStep, endTime, isFinished, version);
    }

    // --- Invariants ---
    public BidPlacedEvent placeBid(String bidderId, Money bidAmount) {
        // rule 1
        if (isFinished || Instant.now().isAfter(endTime)) {
            throw new InvalidBidException("Auksion allaqachon yakunlangan!");
        }

        // rule 2
        Money minimumRequired = currentPrice.add(minStep);
        if (!bidAmount.isGreaterThan(currentPrice)) {
            if (bidAmount.amount().compareTo(minimumRequired.amount()) < 0) {
                throw new InvalidBidException(String.format("Narx juda past. Minimal taklif %s bo'lishi kerak", minimumRequired.amount()));
            }
        }

        this.currentPrice = bidAmount;
        Bid newBid = new Bid(bidderId, bidAmount, Instant.now());
        this.bids.add(newBid);

        return new BidPlacedEvent(this.id, bidderId, bidAmount.amount(), Instant.now());
    }
}
