package br.com.alurafood.reviews.amqp.message;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentMessage {

    private Long id;
    private BigDecimal amount;
    private String name;
    private String cardNumber;
    private String cardExpiration;
    private String cardCode;
    private PaymentStatus status;
    private Long orderId;
    private Long paymentMethodId;
}
