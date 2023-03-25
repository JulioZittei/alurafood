package br.com.alurafood.orders.amqp.message;

public enum PaymentStatus {
    CREATED,
    CONFIRMED,
    PENDING,
    CANCELED
}
