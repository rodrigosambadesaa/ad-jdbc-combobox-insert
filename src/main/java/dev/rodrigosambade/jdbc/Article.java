package dev.rodrigosambade.jdbc;

import java.math.BigDecimal;

public record Article(
        int id,
        String model,
        String description,
        BigDecimal price) {

    @Override
    public String toString() {
        return id + " — " + model;
    }
}
