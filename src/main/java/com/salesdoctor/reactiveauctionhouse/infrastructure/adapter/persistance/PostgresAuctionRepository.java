package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.persistance;

import com.salesdoctor.reactiveauctionhouse.domain.model.Auction;
import com.salesdoctor.reactiveauctionhouse.domain.model.vo.Money;
import com.salesdoctor.reactiveauctionhouse.domain.port.AuctionRepository;
import com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.persistance.entity.AuctionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PostgresAuctionRepository implements AuctionRepository {

    private final SpringDataAuctionRepo r2dbcRepo;

    @Override
    public Mono<Auction> findById(String id) {
        return r2dbcRepo.findById(id)
                .map(this::toDomain); // Entity -> Domain
    }

    @Override
    public Mono<Auction> save(Auction auction) {
        return r2dbcRepo.save(toEntity(auction))
                .map(this::toDomain); // Domain -> Entity -> Domain
    }

    // Mapper: Entity -> Domain
    private Auction toDomain(AuctionEntity entity) {

        return Auction.restore(
                entity.getId(),
                Money.of(entity.getCurrentPrice()),
                Money.of(entity.getMinStep()),
                entity.getEndTime(),
                entity.isFinished(),
                entity.getVersion()
        );
    }

    // Mapper: Domain -> Entity
    private AuctionEntity toEntity(Auction domain) {

        AuctionEntity entity = new AuctionEntity();
        entity.setId(domain.getId());
        entity.setCurrentPrice(domain.getCurrentPrice().amount());
        entity.setMinStep(domain.getMinStep().amount());
        entity.setEndTime(domain.getEndTime());
        entity.setFinished(domain.isFinished());
        entity.setVersion(domain.getVersion());

        if (domain.getVersion() == null) {
            entity.markAsNew();
        }

        return entity;
    }
}
