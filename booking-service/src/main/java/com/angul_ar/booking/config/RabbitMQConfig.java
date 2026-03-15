package com.angul_ar.booking.config;

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

//  @Bean
//  public Queue bookingCreatedQueue() {
//    return new Queue("booking.created.queue");
//  }

//  @Bean
//  public Binding bookingCreatedBinding(Queue bookingCreatedQueue, TopicExchange bookingExchange) {
//    return BindingBuilder.bind(bookingCreatedQueue).to(bookingExchange).with("booking.created");
//  }

  @Bean
  public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }
}
