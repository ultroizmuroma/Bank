insert into CREDIT (id, customer_id, customer_first_name, customer_surname, repayment_date, country, amount)
values (1, 1, 'Алексей', 'Баринов', '2018-05-31', 'Россия', '10000.55');

insert into CREDIT (id, customer_id, customer_first_name, customer_surname, repayment_date, country, amount)
values (2, 2, 'Бильбо', 'Торбинс', '2018-12-07', 'Средиземье', '10000000.99');

insert into CUSTOMER_BLACK_LIST (customer_id)
values (2);

insert into PROPERTIES (property_name, property_value)
values ('REQUEST_LIMIT', 10);

insert into PROPERTIES (property_name, property_value)
values ('TRACKING_INTERVAL', 60);