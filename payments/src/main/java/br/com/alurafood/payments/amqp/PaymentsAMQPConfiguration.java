package br.com.alurafood.payments.amqp;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentsAMQPConfiguration {

//    @Bean
//    public Queue createQueue(){
////        return new Queue("payment.confirmed", false);
//        return QueueBuilder.nonDurable("payment.confirmed").build();
//    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter createMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory conn, Jackson2JsonMessageConverter converter) {
        var rabbitTemplate = new RabbitTemplate(conn);
        rabbitTemplate.setMessageConverter(converter);
        return rabbitTemplate;
    }


    @Bean
    public FanoutExchange createFanoutExchange() {
//        return new FanoutExchange("payments.ex");
        return ExchangeBuilder.fanoutExchange("payments.ex").build();
    }
}
