package dev.dynamic.api;

import lombok.Data;

@Data
public class TradesUpdate {
    private String type;
    private String symbol;
    private double lastPrice;
    private long timestamp;
    private double volume;
}
