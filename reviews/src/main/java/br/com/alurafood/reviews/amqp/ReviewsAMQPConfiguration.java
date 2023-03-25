package br.com.alurafood.reviews.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ReviewsAMQPConfiguration {

    @Bean
    public Queue createQueue() {
        return QueueBuilder.nonDurable("payments.review-details").deadLetterExchange("payments.dlx").build();
    }

    @Bean
    public Queue createDeadLetterQueue() {
        return QueueBuilder.nonDurable("payments.review-details.dlq").build();
    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return ExchangeBuilder.fanoutExchange("payments.ex").build();
    }

    @Bean
    public FanoutExchange createDeadLetterExchange() {
        return ExchangeBuilder.fanoutExchange("payments.dlx").build();
    }

    @Bean
    public Binding bindQueueAndExchangePaymentOrder() {
        return BindingBuilder.bind(createQueue()).to(createFanoutExchange());
    }

    @Bean
    public Binding bindDeadLetterQueueAndDeadLetterExchangePaymentOrder() {
        return BindingBuilder.bind(createDeadLetterQueue()).to(createDeadLetterExchange());
    }


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
}
