DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;

CREATE TABLE IF NOT EXISTS auctions
(
    id            VARCHAR(36) PRIMARY KEY,
    current_price DECIMAL(19, 2) NOT NULL,
    min_step      DECIMAL(19, 2) NOT NULL,
    end_time      TIMESTAMP      NOT NULL,
    is_finished   BOOLEAN        NOT NULL,
    version       BIGINT
);

CREATE TABLE IF NOT EXISTS bids
(
    id         SERIAL PRIMARY KEY,
    auction_id VARCHAR(36)    NOT NULL,
    bidder_id  VARCHAR(36)    NOT NULL,
    amount     DECIMAL(19, 2) NOT NULL,
    bid_time   TIMESTAMP      NOT NULL,
    FOREIGN KEY (auction_id) REFERENCES auctions (id)
);