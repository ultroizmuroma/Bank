package ru.barinov.bank.repository;

import org.springframework.data.repository.CrudRepository;
import ru.barinov.bank.model.Credit;

public interface CreditRepository extends CrudRepository<Credit, Long> {
  Iterable<Credit> findAllByCustomerId(Long customerId);
}
