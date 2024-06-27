package dev.dynamic.api;

import lombok.Data;

@Data
public class StockSearchResult {
    private String query;
    private String description;
    private String displaySymbol;
    private String symbol;
    private String type;
}
