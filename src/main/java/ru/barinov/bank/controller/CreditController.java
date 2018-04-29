package ru.barinov.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.barinov.bank.model.Credit;
import ru.barinov.bank.repository.CountryRestrictionRepository;
import ru.barinov.bank.repository.CreditRepository;
import ru.barinov.bank.repository.CustomerBlackListRepository;
import ru.barinov.bank.repository.PropertiesRepository;
import ru.barinov.bank.credit.CreditApprover;

@Controller
public class CreditController {
  private final CreditRepository creditRepository;
  private final CreditApprover creditApprover;

  @Autowired
  public CreditController(CreditRepository creditRepository,
                          CustomerBlackListRepository customerBlackListRepository,
                          CountryRestrictionRepository countryRestrictionRepository,
                          PropertiesRepository propertiesRepository) {
    this.creditRepository = creditRepository;
    this.creditApprover = new CreditApprover(creditRepository, customerBlackListRepository, countryRestrictionRepository, propertiesRepository);
  }

  @GetMapping(path = "/credit/list")
  public @ResponseBody
  Iterable<Credit> getCreditList(@RequestParam(value = "customer_id", required = false) Long customerId) {
    if (customerId == null) {
      return creditRepository.findAll();
    }
    return creditRepository.findAllByCustomerId(customerId);
  }

  @PostMapping(path = "/credit")
  public @ResponseBody
  String addCredit(@RequestBody Credit requestBody) {
    return creditApprover.processCredit(requestBody);
  }
}
