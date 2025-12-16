package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.in.web;

import com.salesdoctor.reactiveauctionhouse.application.port.in.AuctionStreamUseCase;
import com.salesdoctor.reactiveauctionhouse.application.port.in.PlaceBidUseCase;
import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
import com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.in.web.dto.BidRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private final PlaceBidUseCase placeBidUseCase;
    private final AuctionStreamUseCase streamUseCase;

    /**
     * COMMAND: Narx talif qilish
     * Bu asinxron ishlaydi. So'rov keladi, servisga topsiriladi va darhol javob qaytadi.
     */
    @PostMapping("/{auctionId}/bid")
    public Mono<ResponseEntity<Void>> placeBid(
            @PathVariable String auctionId,
            @RequestBody BidRequest request) {

        return placeBidUseCase
                .placeBid(auctionId, request.userId(), request.amount())
                .thenReturn(ResponseEntity.ok().build());
    }

    /**
     * QUERY/STREAM: Jonli Auksion Oqimi (SSE).
     * produces = TEXT_EVENT_STREAM_VALUE juda muhim!
     * Bu brauzerga aytadiki: "Aloqani uzma, men senga ma'lumot tashlab turaman".
     */
    @GetMapping(value = "/{auctionId}/stream",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BidPlacedEvent> streamAuction(@PathVariable String auctionId) {
        System.out.println("FRONTEND ULANDI: " + auctionId);
        return streamUseCase.stream(auctionId);
    }
}
