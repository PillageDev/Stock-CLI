package dev.dynamic.api;

import lombok.Data;

@Data
public class Earnings {
    private String date;
    private double actualEPS;
    private double estimateEPS;
    private String hour;
    private int quarter;
    private double revenueActual;
    private double revenueEstimate;
    private String symbol;
    private int year;
}
