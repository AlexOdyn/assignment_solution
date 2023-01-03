package com.example.assignment;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

interface CurrencyRepository extends ListCrudRepository<Currency, Long>, PagingAndSortingRepository<Currency, Long> {

}
