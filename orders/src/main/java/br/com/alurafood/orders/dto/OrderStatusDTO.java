package br.com.alurafood.orders.dto;

import br.com.alurafood.orders.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusDTO {

    private OrderStatus status;

}
