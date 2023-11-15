package com.example.alertproducer.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AlertDto(@NotBlank String description, @NotNull Color color) {
  public enum Color {
    YELLOW, ORANGE, RED
  }
}