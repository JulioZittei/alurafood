package br.com.alurafood.payments.controller;

import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.service.PaymentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

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
    public ResponseEntity<PaymentDTO> create(@RequestBody @Valid PaymentDTO payment, UriComponentsBuilder uriBuilder) {
        PaymentDTO paymentDTO = paymentService.create(payment);
        URI address = uriBuilder.path("/payments/{id}").buildAndExpand(paymentDTO.getId()).toUri();

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
