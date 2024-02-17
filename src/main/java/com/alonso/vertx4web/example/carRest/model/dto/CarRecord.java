package com.alonso.vertx4web.example.carRest.model.dto;

import java.math.BigDecimal;

public record CarRecord(String name, String brand, BigDecimal manufacturingValue, String description) {
}
