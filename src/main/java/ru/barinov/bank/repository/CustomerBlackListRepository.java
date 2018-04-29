package ru.barinov.bank.repository;

import org.springframework.data.repository.CrudRepository;
import ru.barinov.bank.model.CustomerBlackList;

public interface CustomerBlackListRepository extends CrudRepository<CustomerBlackList, Long> {
  CustomerBlackList findCustomerBlackListByCustomerId(Long customerId);

  default Boolean isCustomerBanned(Long customerId) {
    return findCustomerBlackListByCustomerId(customerId) != null;
  }

  default void addCustomer(Long customerId) {
    if (!isCustomerBanned(customerId)) {
      CustomerBlackList customerBlackList = new CustomerBlackList();
      customerBlackList.setCustomerId(customerId);
      save(customerBlackList);
    }
  }

  default void deleteCustomer(Long customerId) {
    CustomerBlackList customerBlackList = findCustomerBlackListByCustomerId(customerId);
    if (customerBlackList != null) {
      delete(customerBlackList);
    }
  }
}