package com.example.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest()
@AutoConfigureMockMvc
public class CurrencyControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    CurrencyRepository currencyRepository;

    Currency CURRENCY_1 = new Currency(
            1L, Ticker.BTC, "Bitcoin", new BigDecimal("16770000"), new BigDecimal("189580000000")
    );
    Currency CURRENCY_2 = new Currency(
            2L, Ticker.ETH, "Ethereum", new BigDecimal("96710000"), new BigDecimal("69280000000")
    );
    Currency CURRENCY_3 = new Currency(
            3L, Ticker.XRP, "Ripple", new BigDecimal("38590000000"), new BigDecimal("64750000000")
    );
    Currency CURRENCY_4 = new Currency(
            4L, Ticker.BCH, "BitcoinCash", new BigDecimal("16670000"), new BigDecimal("69020000000")
    );

    @Test
    public void shouldReturnAllCurrencies() throws Exception {
        List<Currency> currencyList = new ArrayList<>(Arrays.asList(CURRENCY_1, CURRENCY_2, CURRENCY_3, CURRENCY_4));
        Page<Currency> currencyPage = new PageImpl<>(currencyList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Mockito.when(currencyRepository.findAll(pageable)).thenReturn(currencyPage);

        mockMvc.perform(get("/api/currencies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].name").value("Bitcoin"))
                .andExpect(jsonPath("$[3].name").value("BitcoinCash"));
    }

    @Test
    public void shouldReturnAllCurrenciesSortedByTickerAndLimitedBySize() throws Exception {
        List<Currency> currencyList = new ArrayList<>(Arrays.asList(CURRENCY_1, CURRENCY_4));
        Page<Currency> currencyPage = new PageImpl<>(currencyList);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("ticker"));
        Mockito.when(currencyRepository.findAll(pageable)).thenReturn(currencyPage);

        mockMvc.perform(get("/api/currencies")
                        .param("page", "0")
                        .param("size", "2")
                        .param("sort", "ticker")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ticker").value(Ticker.BTC.toString()))
                .andExpect(jsonPath("$[1].ticker").value(Ticker.BCH.toString()));
    }

    @Test
    public void shouldAddNewCurrency() throws Exception {
        Currency currency = Currency.builder()
                .ticker(Ticker.BTC)
                .name("Test")
                .numberOfCoins(new BigDecimal("350"))
                .marketCap(new BigDecimal("9001"))
                .build();

        Mockito.when(currencyRepository.save(currency)).thenReturn(currency);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(currency));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.numberOfCoins").value("350"))
                .andExpect(jsonPath("$.marketCap").value("9001"))
                .andExpect(jsonPath("$.ticker").value("BTC"));
    }

    @Test
    public void shouldGetCurrencyById() throws Exception {
        Mockito.when(currencyRepository.findById(1L)).thenReturn(java.util.Optional.of(CURRENCY_1));

        mockMvc.perform(get("/api/currencies/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value("Bitcoin"));
    }

    @Test
    public void shouldProhibitGettingNonExistingId() throws Exception {
        mockMvc.perform(get("/api/currencies/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateExistingCurrency() throws Exception {
        Currency currency = Currency.builder()
                .id(1L)
                .ticker(Ticker.XRP)
                .name("Test")
                .numberOfCoins(new BigDecimal("3333"))
                .marketCap(new BigDecimal("987654321"))
                .build();

        Mockito.when(currencyRepository.findById(1L)).thenReturn(java.util.Optional.of(CURRENCY_1));
        Mockito.when(currencyRepository.save(currency)).thenReturn(currency);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/api/currencies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(currency));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.numberOfCoins").value("3333"))
                .andExpect(jsonPath("$.marketCap").value("987654321"))
                .andExpect(jsonPath("$.ticker").value("XRP"));
    }

    @Test
    public void shouldDeleteCurrency() throws Exception {
        Mockito.when(currencyRepository.findById(4L)).thenReturn(java.util.Optional.of(CURRENCY_4));
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/currencies/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldProhibitDeletingNonExistingCurrency() throws Exception {
        Mockito.when(currencyRepository.findById(9L)).thenReturn(java.util.Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/currencies/9")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
