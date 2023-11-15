package com.example.alertproducer.controller;

import com.example.alertproducer.avro.Alert;
import com.example.alertproducer.avro.AlertColor;
import com.example.alertproducer.emitter.AlertEmitter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

  private final AlertEmitter alertEmitter;

  public AlertController(AlertEmitter alertEmitter) {
    this.alertEmitter = alertEmitter;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public void emitAlert(@Valid @RequestBody AlertDto alertDto) {
    Alert alert = Alert.newBuilder()
        .setDescription(alertDto.description())
        .setAlertColor(AlertColor.valueOf(alertDto.color().name()))
        .setCreatedOn(Instant.now())
        .build();
    alertEmitter.emit(alert);
  }
}