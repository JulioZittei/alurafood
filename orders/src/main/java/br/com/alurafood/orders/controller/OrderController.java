package br.com.alurafood.orders.controller;

import br.com.alurafood.orders.dto.OrderDTO;
import br.com.alurafood.orders.dto.OrderStatusDTO;
import br.com.alurafood.orders.service.OrderService;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDTO> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable @NotNull Long id) {
        OrderDTO orderDTO = orderService.getById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping("/port")
    public String getInstance(@Value("${local.server.port}") String port) {
        return String.format("Request responsed for instance executing on port: %s", port);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> create(@RequestBody @Valid OrderDTO orderDTO, UriComponentsBuilder uriBuilder) {
        OrderDTO savedOrder = orderService.create(orderDTO);

        URI address = uriBuilder.path("/orders/{id}").buildAndExpand(savedOrder.getId()).toUri();

        return ResponseEntity.created(address).body(savedOrder);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Long id, @RequestBody OrderStatusDTO orderStatusDTO) {
        OrderDTO orderDTO = orderService.updateStatus(id, orderStatusDTO.getStatus());

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/payed")
    public ResponseEntity<Void> approvePayment(@PathVariable @NotNull Long id) {
        orderService.approvePayment(id);

        return ResponseEntity.ok().build();
    }
}
