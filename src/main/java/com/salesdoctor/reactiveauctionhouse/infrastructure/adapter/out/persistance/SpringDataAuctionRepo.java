package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.out.persistance;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataAuctionRepo extends R2dbcRepository<AuctionEntity, String> {}
