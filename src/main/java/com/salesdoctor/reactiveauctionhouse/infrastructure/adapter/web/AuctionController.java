package com.salesdoctor.reactiveauctionhouse.infrastructure.adapter.web;

import com.salesdoctor.reactiveauctionhouse.application.dto.BidRequest;
import com.salesdoctor.reactiveauctionhouse.application.service.BiddingService;
import com.salesdoctor.reactiveauctionhouse.domain.event.BidPlacedEvent;
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

    private final BiddingService biddingService;

    /**
     * COMMAND: Narx talif qilish
     * Bu asinxron ishlaydi. So'rov keladi, servisga topsiriladi va darhol javob qaytadi.
     */
    @PostMapping("/{auctionId}/bid")
    public Mono<ResponseEntity<Void>> placeBid(
            @PathVariable String auctionId,
            @RequestBody BidRequest request) {

        return biddingService.placeBid(auctionId, request.userId(), request.amount())
                .then(Mono.fromCallable(() -> ResponseEntity.ok().build()));
    }

    /**
     * QUERY/STREAM: Jonli Auksion Oqimi (SSE).
     * produces = TEXT_EVENT_STREAM_VALUE juda muhim!
     * Bu brauzerga aytadiki: "Aloqani uzma, men senga ma'lumot tashlab turaman".
     */
    @GetMapping(value = "/{auctionId}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BidPlacedEvent> streamAuction(@PathVariable String auctionId) {
        System.out.println("FRONTEND ULANDI: " + auctionId);

        return biddingService.getAuctionStream(auctionId)
                .doOnNext(e -> System.out.println("FRONTENDGA KETYAPTI: " + e));
    }
}
