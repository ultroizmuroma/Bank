package ru.barinov.bank.repository;

import org.springframework.data.repository.CrudRepository;
import ru.barinov.bank.model.CountryRestriction;

import java.util.Date;

public interface CountryRestrictionRepository extends CrudRepository<CountryRestriction, Long> {
  CountryRestriction findByCountry(String country);

  default void setRequestLimitForCountry(String country, Integer limit) {
    CountryRestriction countryRestriction = findByCountry(country);
    countryRestriction.setRequestLimit(limit);
    save(countryRestriction);
  }

  default Integer getRequestLimitForCountry(String country) {
    CountryRestriction countryRestriction = findByCountry(country);
    return countryRestriction.getRequestLimit();
  }

  default void initDefaultRestriction(String country, Integer limit) {
    CountryRestriction countryRestriction = new CountryRestriction();
    countryRestriction.setCountry(country);
    countryRestriction.setCreditCount(1);
    countryRestriction.setStartTrackingDate(new Date());
    countryRestriction.setRequestLimit(limit);
    save(countryRestriction);
  }
}
