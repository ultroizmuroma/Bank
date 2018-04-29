package ru.barinov.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.barinov.bank.general.ServiceAnswer;
import ru.barinov.bank.model.CountryRestriction;
import ru.barinov.bank.repository.CountryRestrictionRepository;

@Controller
public class CountryRestrictionController {
  private final CountryRestrictionRepository countryRestrictionRepository;

  @Autowired
  public CountryRestrictionController(CountryRestrictionRepository countryRestrictionRepository) {
    this.countryRestrictionRepository = countryRestrictionRepository;
  }

  @PutMapping(path = "/country_restriction")
  public @ResponseBody
  String updateCountryRestriction(@RequestBody CountryRestriction requestBody) {
    countryRestrictionRepository.setRequestLimitForCountry(requestBody.getCountry(), requestBody.getRequestLimit());
    return ServiceAnswer.OK.toString();
  }
}
