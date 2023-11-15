package com.example.alertproducer.emitter;

import com.example.alertproducer.avro.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class AlertEmitter {

  private static final Logger log = LoggerFactory.getLogger(AlertEmitter.class);

  private final StreamBridge streamBridge;

  public AlertEmitter(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void emit(Alert alert) {
    streamBridge.send("alerts-out-0", alert);
    log.info("Alert emitted! {}", alert);
  }
}