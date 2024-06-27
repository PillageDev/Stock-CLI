package dev.dynamic.api;

import lombok.Data;

import java.util.Map;

@Data
public class Financials {
    private Map<String, RatioEntry> ratios;
    private Map<String, Double> metrics;
    private String metricType;
    private String symbol;


    @Data
    public static class RatioEntry {
        private String period;
        private double netProfitMargin;
    }
}
