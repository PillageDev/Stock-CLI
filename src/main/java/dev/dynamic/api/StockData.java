package dev.dynamic.api;

import lombok.Data;

import java.util.List;

public interface StockData {
    List<StockSearchResult> search(String keyword);
    List<StockSymbol> getStockSymbols(String exchange);
    MarketStatus getMarketStatus(String exchange);
    MarketHolidays getMarketHolidays(String exchange);
    CompanyProfile getCompanyProfile(String symbol);
    List<News> getNews(NewsCategory category);
    List<News> getCompanyNews(String symbol, SearchDate from, SearchDate to);
    Recommendations getRecommendations(String symbol);
    StockQuote getStockQuote(String symbol);
    Financials getFinancials(String symbol);

    enum NewsCategory {
        GENERAL,
        FOREX,
        CRYPTO,
        MERGER
    }

    @Data
    class SearchDate {
        private int year;
        private int month;
        private int day;
    }
}
