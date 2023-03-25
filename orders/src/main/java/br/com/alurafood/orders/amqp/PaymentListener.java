package br.com.alurafood.orders.amqp;

import br.com.alurafood.orders.amqp.message.PaymentMessage;
import br.com.alurafood.orders.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentListener {

    private final OrderService orderService;

//    private final ObjectMapper objectMapper;

//    @RabbitListener(queues = "payment.confirmed")
//    public void consumeMessage(Message message) throws IOException {
//        byte[] content = message.getBody();
//        PaymentMessage paymentMessage =  objectMapper.readValue(objectMapper.createParser(content), PaymentMessage.class);
//
//        orderService.approvePayment(paymentMessage.getOrderId());
//    }

    @RabbitListener(queues = "payments.order-details")
    public void consumeMessage(@Payload PaymentMessage paymentMessage) throws IOException {
        orderService.approvePayment(paymentMessage.getOrderId());
    }
}
