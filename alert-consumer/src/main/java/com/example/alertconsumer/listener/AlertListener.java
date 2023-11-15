package com.example.alertconsumer.listener;

import com.example.alertproducer.avro.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AlertListener {

  private static final Logger log = LoggerFactory.getLogger(AlertListener.class);

  @Bean
  public Consumer<Alert> alerts() {
    return alert -> log.info("Received Alert! \"{}\" with color '{}' created on '{}'",
        alert.getDescription(), alert.getAlertColor(), alert.getCreatedOn());
  }
}