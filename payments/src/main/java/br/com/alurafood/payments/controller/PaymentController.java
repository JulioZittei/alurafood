package br.com.alurafood.payments.controller;

import br.com.alurafood.payments.amqp.message.PaymentMessage;
import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;


    @GetMapping
    public Page<PaymentDTO> getAll(@PageableDefault(size = 10)Pageable pagination) {
        return paymentService.getAll(pagination);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getById(@PathVariable @NotNull Long id) {
        PaymentDTO paymentDTO = paymentService.getById(id);
        return ResponseEntity.ok(paymentDTO);
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentDTO payment, UriComponentsBuilder uriBuilder)
            throws JsonProcessingException {
        PaymentDTO paymentDTO = paymentService.create(payment);
        URI address = uriBuilder.path("/{id}").buildAndExpand(paymentDTO.getId()).toUri();

        PaymentMessage paymentMessage = modelMapper.map(paymentDTO, PaymentMessage.class);
//        Message message = new Message(objectMapper.writeValueAsBytes(paymentMessage));
//        rabbitTemplate.send("payment.confirmed", message);

        rabbitTemplate.convertAndSend("payments.ex", "", paymentMessage);

        return ResponseEntity.created(address).body(paymentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable @NotNull Long id, @RequestBody @Valid PaymentDTO payment) {
        PaymentDTO paymentDTO = paymentService.update(id, payment);
        return ResponseEntity.ok(paymentDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PaymentDTO> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @CircuitBreaker(name = "updateOrder", fallbackMethod = "updateOrderPending")
    public void approvePayment(@PathVariable @NotNull Long id) {
        paymentService.approvePayment(id);
    }

    public void updateOrderPending(Long id, Exception ex) {
        paymentService.alterStatus(id);
    }
}
