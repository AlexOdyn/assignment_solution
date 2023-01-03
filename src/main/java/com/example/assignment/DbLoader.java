package com.example.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DbLoader {

    private static final Logger logger = LoggerFactory.getLogger(DbLoader.class);

    @Bean
    CommandLineRunner populateDb(CurrencyRepository currencyRepository) {
        return args -> {
            logger.info(
                    "Populating db with: " + currencyRepository.save(
                            new Currency(
                                    Ticker.BTC, "Bitcoin", new BigDecimal("16770000"), new BigDecimal("189580000000")
                            )
                    )
            );
            logger.info(
                    "Populating db with: " + currencyRepository.save(
                            new Currency(
                                    Ticker.ETH, "Ethereum", new BigDecimal("96710000"), new BigDecimal("69280000000")
                            )
                    )
            );
            logger.info(
                    "Populating db with: " + currencyRepository.save(
                            new Currency(
                                    Ticker.XRP, "Ripple", new BigDecimal("38590000000"), new BigDecimal("64750000000")
                            )
                    )
            );
            logger.info(
                    "Populating db with: " + currencyRepository.save(
                            new Currency(
                                    Ticker.BCH, "BitcoinCash", new BigDecimal("16670000"), new BigDecimal("69020000000")
                            )
                    )
            );
        };
    }

}
