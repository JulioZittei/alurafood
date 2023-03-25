package br.com.alurafood.orders.service;

import br.com.alurafood.orders.dto.OrderDTO;
import br.com.alurafood.orders.model.Order;
import br.com.alurafood.orders.model.OrderStatus;
import br.com.alurafood.orders.repository.OrderRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private final ModelMapper modelMapper;

    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream()
                .map(o -> modelMapper.map(o, OrderDTO.class))
                .collect(Collectors.toList());
    }

    public OrderDTO getById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(order, OrderDTO.class);
    }

    public OrderDTO create(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);

        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDERED);
        order.getItems().forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepository.save(order);

        return modelMapper.map(savedOrder, OrderDTO.class);
    }

    public OrderDTO updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findByIdWithItems(orderId);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(status);
        orderRepository.updateStatus(status, order);
        return modelMapper.map(order, OrderDTO.class);
    }

    public void approvePayment(Long orderId) {
        Order order = orderRepository.findByIdWithItems(orderId);

        if (order == null) {
            throw new EntityNotFoundException();
        }

        order.setStatus(OrderStatus.PAYED);
        orderRepository.updateStatus(OrderStatus.PAYED, order);
    }
}
