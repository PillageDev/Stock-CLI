package dev.dynamic.api;

import lombok.Data;

@Data
public class StockQuote {
    private double currentPrice;
    private double change;
    private double percentChange;
    private double highPrice;
    private double lowPrice;
    private double openPrice;
    private double previousClose;
}
