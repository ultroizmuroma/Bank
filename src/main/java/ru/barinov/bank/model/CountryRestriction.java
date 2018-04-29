package ru.barinov.bank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class CountryRestriction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String country;
  private Integer creditCount;
  private Date startTrackingDate;
  private Integer requestLimit;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Integer getCreditCount() {
    return creditCount;
  }

  public void setCreditCount(Integer creditCount) {
    this.creditCount = creditCount;
  }

  public Date getStartTrackingDate() {
    return startTrackingDate;
  }

  public void setStartTrackingDate(Date startTrackingDate) {
    this.startTrackingDate = startTrackingDate;
  }

  public Integer getRequestLimit() {
    return requestLimit;
  }

  public void setRequestLimit(Integer requestLimit) {
    this.requestLimit = requestLimit;
  }
}
