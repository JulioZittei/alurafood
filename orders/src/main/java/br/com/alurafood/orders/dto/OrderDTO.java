package br.com.alurafood.orders.dto;

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
    private StatusDTO status;
    private List<OrderItemDTO> items = new ArrayList<OrderItemDTO>();

}
