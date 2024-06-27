package dev.dynamic.api;

import lombok.Data;

@Data
public class Recommendations {
    private int buyRecommendations;
    private int holdRecommendations;
    private int sellRecommendations;
    private int strongBuyRecommendations;
    private int strongSellRecommendations;
    private String symbol;
    private String period;
}
