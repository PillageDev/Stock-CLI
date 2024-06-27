package dev.dynamic.api;

import lombok.Data;

@Data
public class StockSymbol {
    private String currency;
    private String description;
    private String displaySymbol;
    private String figi; // Financial Instrument Global Identifier
    private String mic; // Market Identifier Code
    private String symbol;
    private String type;
}
