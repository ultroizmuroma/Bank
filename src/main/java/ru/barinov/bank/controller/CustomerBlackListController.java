package ru.barinov.bank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.barinov.bank.general.ServiceAnswer;
import ru.barinov.bank.model.CustomerBlackList;
import ru.barinov.bank.repository.CustomerBlackListRepository;

@Controller
public class CustomerBlackListController {
  private final CustomerBlackListRepository customerBlackListRepository;

  @Autowired
  CustomerBlackListController(CustomerBlackListRepository customerBlackListRepository) {
    this.customerBlackListRepository = customerBlackListRepository;
  }

  @PostMapping("/customer_black_list")
  public @ResponseBody
  String addCustomer(@RequestBody CustomerBlackList requestBody) {
    customerBlackListRepository.addCustomer(requestBody.getCustomerId());
    return ServiceAnswer.OK.toString();
  }

  @DeleteMapping("/customer_black_list")
  public @ResponseBody
  String deleteCustomer(@RequestParam Long customerId) {
    customerBlackListRepository.deleteCustomer(customerId);
    return ServiceAnswer.OK.toString();
  }
}
