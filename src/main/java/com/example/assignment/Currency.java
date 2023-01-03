package com.example.assignment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
class Currency {

    /**
     * Unique identifier of the currency
     */
    private @Id @GeneratedValue Long id;

    /**
     * Currency ticker
     */
    private Ticker ticker;

    /**
     * Full name of the currency
     */
    private String name;

    /**
     * Total number of coins
     */
    private BigDecimal numberOfCoins;

    /**
     * The market cap of the currency
     */
    private BigDecimal marketCap;

    Currency() {}

    Currency(Long id, Ticker ticker, String name, BigDecimal numberOfCoins, BigDecimal marketCap) {
        this.id = id;
        this.ticker = ticker;
        this.name = name;
        this.numberOfCoins = numberOfCoins;
        this.marketCap = marketCap;
    }

    Currency(Ticker ticker, String name, BigDecimal numberOfCoins, BigDecimal marketCap) {
        this.ticker = ticker;
        this.name = name;
        this.numberOfCoins = numberOfCoins;
        this.marketCap = marketCap;
    }

    public Long getId() {
        return this.id;
    }

    public Ticker getTicker() {
        return this.ticker;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getNumberOfCoins() {
        return this.numberOfCoins;
    }

    public BigDecimal getMarketCap() {
        return this.marketCap;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfCoins(BigDecimal numberOfCoins) {
        this.numberOfCoins = numberOfCoins;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Currency currency))
            return false;
        return Objects.equals(this.id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.ticker, this.numberOfCoins, this.marketCap);
    }

    @Override
    public String toString() {
        return "Currency{" + "id=" + this.id + ", ticker='" + this.ticker.toString() + '\'' + ", name='" + this.name +
                '\'' + ", numberOfCoins='" + this.numberOfCoins + '\'' + ", marketCap='" + this.marketCap + '\'' + '}';
    }

    public static CurrencyBuilder builder() {
        return new CurrencyBuilder();
    }

    public static class CurrencyBuilder {
        private Long id;
        private Ticker ticker;
        private String name;
        private BigDecimal numberOfCoins;
        private BigDecimal marketCap;

        private CurrencyBuilder() {}

        public CurrencyBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public CurrencyBuilder ticker(Ticker ticker) {
            this.ticker = ticker;
            return this;
        }

        public CurrencyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CurrencyBuilder numberOfCoins(BigDecimal numberOfCoins) {
            this.numberOfCoins = numberOfCoins;
            return this;
        }

        public CurrencyBuilder marketCap(BigDecimal marketCap) {
            this.marketCap = marketCap;
            return this;
        }

        public Currency build() {
            return new Currency(id, ticker, name, numberOfCoins, marketCap);
        }
    }

}
