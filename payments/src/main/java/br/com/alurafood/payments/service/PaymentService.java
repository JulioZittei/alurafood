package br.com.alurafood.payments.service;

import br.com.alurafood.payments.dto.PaymentDTO;
import br.com.alurafood.payments.http.OrderClient;
import br.com.alurafood.payments.model.Payment;
import br.com.alurafood.payments.model.PaymentStatus;
import br.com.alurafood.payments.repository.PaymentRepository;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private ModelMapper modelMapper;

    public Page<PaymentDTO> getAll(Pageable paginantion) {
        return paymentRepository
                .findAll(paginantion)
                .map(p -> modelMapper.map(p, PaymentDTO.class));
    }

    public PaymentDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO create(PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setStatus(PaymentStatus.CREATED);
        paymentRepository.save(payment);

        return modelMapper.map(payment, PaymentDTO.class);
    }

    public PaymentDTO update(Long id, PaymentDTO paymentDTO) {
        Payment payment = modelMapper.map(paymentDTO, Payment.class);
        payment.setId(id);
        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public void delete(Long id) {
        paymentRepository.deleteById(id);
    }

    public void approvePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        payment.setStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
        orderClient.approvePayment(payment.getOrderId());
    }

    public void alterStatus(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        payment.setStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
    }
}
