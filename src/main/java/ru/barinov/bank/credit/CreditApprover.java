package ru.barinov.bank.credit;

import ru.barinov.bank.general.ServiceAnswer;
import ru.barinov.bank.model.CountryRestriction;
import ru.barinov.bank.model.Credit;
import ru.barinov.bank.repository.CountryRestrictionRepository;
import ru.barinov.bank.repository.CreditRepository;
import ru.barinov.bank.repository.CustomerBlackListRepository;
import ru.barinov.bank.repository.PropertiesRepository;

import java.util.Date;

public class CreditApprover {
  private CreditRepository creditRepository;
  private CustomerBlackListRepository customerBlackListRepository;
  private CountryRestrictionRepository countryRestrictionRepository;
  private PropertiesRepository propertiesRepository;

  public CreditApprover(CreditRepository creditRepository,
                        CustomerBlackListRepository customerBlackListRepository,
                        CountryRestrictionRepository countryRestrictionRepository,
                        PropertiesRepository propertiesRepository) {
    this.creditRepository = creditRepository;
    this.customerBlackListRepository = customerBlackListRepository;
    this.countryRestrictionRepository = countryRestrictionRepository;
    this.propertiesRepository = propertiesRepository;
  }

  public String processCredit(Credit credit) {
    Long customerId = credit.getCustomerId();
    if (customerBlackListRepository.isCustomerBanned(customerId)) {
      return ServiceAnswer.REJECT_CUSTOMER_BANNED.toString();
    }
    String country = credit.getCountry();
    if (isCountryBanned(country)) {
      return ServiceAnswer.REJECT_COUNTRY_BANNED.toString();
    }
    creditRepository.save(credit);
    return ServiceAnswer.APPROVE.toString();
  }

  private Boolean isCountryBanned(String country) {
    CountryRestriction countryRestriction = countryRestrictionRepository.findByCountry(country);
    if (countryRestriction == null) {
      countryRestrictionRepository.initDefaultRestriction(country, propertiesRepository.getRequestLimit());
    } else {
      Integer creditCount = countryRestriction.getCreditCount();
      Integer requestLimit = countryRestriction.getRequestLimit();
      if (requestBelongsToInterval(countryRestriction.getStartTrackingDate(), propertiesRepository.getTrackingInterval())) {
        countryRestrictionRepository.initDefaultRestriction(country, countryRestriction.getRequestLimit());
      } else {
        if (creditCount + 1 > requestLimit) {
          return true;
        }
        creditCount++;
        countryRestriction.setCreditCount(creditCount);
        countryRestrictionRepository.save(countryRestriction);
      }
    }
    return false;
  }

  private Boolean requestBelongsToInterval(Date start, Integer interval) {
    Long startTrackingTime = start.getTime();
    Long currentTime = new Date().getTime();
    Long difference = (currentTime - startTrackingTime) / 1000;
    return difference >= interval;
  }
}