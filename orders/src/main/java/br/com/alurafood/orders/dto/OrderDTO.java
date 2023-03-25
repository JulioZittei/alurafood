package br.com.alurafood.orders.dto;

import br.com.alurafood.orders.model.OrderStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {

    private Long id;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItemDTO> items = new ArrayList<OrderItemDTO>();

}
