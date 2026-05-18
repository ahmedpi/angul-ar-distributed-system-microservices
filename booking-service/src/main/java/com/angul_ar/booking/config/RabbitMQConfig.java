package com.angul_ar.booking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public TopicExchange bookingExchange() {
    return new TopicExchange("booking.exchange");
  }

  @Bean
  public Queue bookingCanceledQueue() {
    return new Queue("loyalty.update.failed.queue");
  }

  @Bean
  public Binding LoyaltyUpdateFaileBinding(Queue loyaltyUpdateFailedQueue,
      TopicExchange bookingExchange) {
    return BindingBuilder.bind(loyaltyUpdateFailedQueue).to(bookingExchange)
        .with("loyalty.update.failed");
  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
