package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.out.persistance;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Table("auctions")
public class AuctionEntity implements Persistable<String> {
    @Id
    private String id;
    @Column("current_price")
    private BigDecimal currentPrice;
    @Column("min_step")
    private BigDecimal minStep;
    private Instant endTime;
    private boolean isFinished;

    @Version
    private Long version;

    @Transient
    private boolean isNew = false;

    @Override
    public boolean isNew() {
        return isNew || version == null;
    }

    public void markAsNew() {
        this.isNew = true;
    }
}
