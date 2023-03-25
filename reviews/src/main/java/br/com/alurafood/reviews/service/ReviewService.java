package br.com.alurafood.reviews.service;

import br.com.alurafood.reviews.amqp.message.PaymentMessage;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {


    public void sendNotification(PaymentMessage paymentMessage) {

        log.info("Validating card number {}", paymentMessage.getCardNumber());
    if (paymentMessage.getCardNumber().startsWith("0")) {
            throw new RuntimeException("Cartão inválido!");
        }

        String message = String.format("Prezado(a), %s recebemos o seu pagamento com sucesso!\n"
                + "Por favor assim que possível, avalie o seu pedido. Seu feedback é muito importante!\n"
                + "Obrigado pela preferência,\n"
                + "Equipe AluraFood", paymentMessage.getName());

        System.out.println(message);
    }

}
