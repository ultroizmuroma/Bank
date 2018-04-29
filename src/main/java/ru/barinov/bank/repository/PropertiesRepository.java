package ru.barinov.bank.repository;

import org.springframework.data.repository.CrudRepository;
import ru.barinov.bank.model.Properties;

public interface PropertiesRepository extends CrudRepository<Properties, Long> {
  Properties findByPropertyName(String propertyName);

  default void setValue(String name, Integer value) {
    Properties property = findByPropertyName(name);
    property.setPropertyValue(value);
    save(property);
  }

  default Integer getValue(String name) {
    Properties property = findByPropertyName(name);
    return property != null ? property.getPropertyValue() : 0;
  }

  default void setRequestLimit(Integer limit) {
    setValue("REQUEST_LIMIT", limit);
  }

  default Integer getRequestLimit() {
    return getValue("REQUEST_LIMIT");
  }

  default void setTrackingInterval(Integer interval) {
    setValue("TRACKING_INTERVAL", interval);
  }

  default Integer getTrackingInterval() {
    return getValue("TRACKING_INTERVAL");
  }
}
