CREATE TABLE payments (
 id bigint(20) NOT NULL AUTO_INCREMENT,
 amount decimal(19,2) NOT NULL,
 name varchar(100) DEFAULT NULL,
 card_number varchar(19) DEFAULT NULL,
 card_expiration varchar(7) DEFAULT NULL,
 card_code varchar(3) DEFAULT NULL,
 status varchar(255) NOT NULL,
 payment_method_id bigint(20) NOT NULL,
 order_id bigint(20) NOT NULL,
PRIMARY KEY (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;;