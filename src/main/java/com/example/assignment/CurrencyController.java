package com.example.assignment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyRepository currencyRepository;
    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    CurrencyController(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    /**
     * Get the list of existing currencies
     * @param page page number to be returned, starting at page 0 with default being 0
     * @param size page size to be returned, default being 10
     * @param sort field to be sorted by (descending), default being id
     * @return the list of the existing currencies of the requested page, size and sort options
     */
    @GetMapping("/currencies")
    List<Currency> getAllCurrencies(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort
    ) {
        logger.info("GET /api/currencies call received");
        logger.info("Request params: Page = " + page + ", Size = " + size + ", Sort = " + sort);
        Pageable paging = PageRequest.of(page, size, Sort.by(sort));
        Page<Currency> pagedResult = currencyRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        }
        return new ArrayList<>();
    }

    /**
     * Creates a new currency
     * @param currency the details of the new currency
     * @return the details of the created currency
     */
    @PostMapping("/currencies")
    Currency addCurrency(@RequestBody Currency currency) {
        logger.info("POST /api/currencies call received");
        logger.info("Saving the following entity: " + currency.toString());
        return currencyRepository.save(currency);
    }

    /**
     * Get the details of an existing currency given the id of that currency
     * @param id of the existing currency whose details are requested
     * @return the details of the requested currency
     */
    @GetMapping("/currencies/{id}")
    Currency one(@PathVariable Long id) {
        logger.info("GET /api/currencies/" + id.toString() + " call received");
        return currencyRepository.findById(id)
                .orElseThrow(() ->
                    new RequestException("Currency with id " + id + " does not exist")
                );
    }

    /**
     * Update the details of an existing currency given the id of that currency
     * @param newCurrency details of the new currency
     * @param id at which the new currency details should be saved
     * @return the details of the updated currency
     */
    @PutMapping("/currencies/{id}")
    Currency replace(@RequestBody Currency newCurrency, @PathVariable Long id) {
        logger.info("PUT /api/currencies/" + id.toString() + " call received");
        logger.info("Updating the entity at that id with the following entity: " + newCurrency.toString());
        return currencyRepository.findById(id)
                .map(currency -> {
                    currency.setName(newCurrency.getName());
                    currency.setTicker(newCurrency.getTicker());
                    currency.setNumberOfCoins(newCurrency.getNumberOfCoins());
                    currency.setMarketCap(newCurrency.getMarketCap());
                    return currencyRepository.save(currency);
                })
                .orElseGet(() -> {
                    newCurrency.setId(id);
                    return currencyRepository.save(newCurrency);
                });
    }

    /**
     * Delete an existing currency given the id of that currency
     * @param id of the currency that needs to be deleted
     */
    @DeleteMapping("/currencies/{id}")
    void deleteEmployee(@PathVariable Long id) {
        logger.info("DELETE /api/currencies/" + id.toString() + " call received");
        if (currencyRepository.findById(id).isEmpty()) {
            logger.error("Currency with id " + id + " does not exist in the database");
            throw new RequestException("Currency with id " + id + " does not exist");
        }
        currencyRepository.deleteById(id);
    }

}
