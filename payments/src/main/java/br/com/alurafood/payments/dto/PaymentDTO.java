package br.com.alurafood.payments.dto;

import br.com.alurafood.payments.model.Status;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentDTO {

    private Long id;
    private BigDecimal amount;
    private String name;
    private String number;
    private String expiration;
    private String code;
    private Status status;
    private Long orderId;
    private Long paymentMethodId;
}
