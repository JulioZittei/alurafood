package br.com.alurafood.reviews.amqp;

import br.com.alurafood.reviews.amqp.message.PaymentMessage;
import br.com.alurafood.reviews.service.ReviewService;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentListener {

    private final ReviewService reviewService;

    @RabbitListener(queues = "payments.review-details")
    public void consumeMessage(@Payload PaymentMessage paymentMessage) throws IOException {
        reviewService.sendNotification(paymentMessage);
    }
}
