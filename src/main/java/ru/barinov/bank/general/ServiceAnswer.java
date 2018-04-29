package ru.barinov.bank.general;

public enum ServiceAnswer {
  APPROVE("Заявка принята."),
  REJECT_CUSTOMER_BANNED("Заявка отклонена. Пользователь находится в черном списке."),
  REJECT_COUNTRY_BANNED("Заявка отклонена. Превышен лимит запросов от страны в минуту."),
  OK("Ок");

  private String message;
  ServiceAnswer(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return message;
  }
}
