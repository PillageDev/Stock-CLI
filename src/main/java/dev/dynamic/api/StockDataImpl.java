package dev.dynamic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class StockDataImpl implements StockData {
    private static final String BASE_URL = "https://finnhub.io/api/v1/";
    private static final String API_KEY = "cpum7e1r01qhicnal3pgcpum7e1r01qhicnal3q0";
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public List<StockSearchResult> search(String keyword) {
        String endpoint = "search?q=" + keyword;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        List<StockSearchResult> results = new ArrayList<>();

        try {
            JsonNode rootNode = mapper.readTree(json);
            JsonNode resultNode = rootNode.get("result");

            if (resultNode.isArray()) {
                for (JsonNode node : resultNode) {
                    StockSearchResult result = new StockSearchResult();
                    result.setDescription(node.get("description").asText());
                    result.setDisplaySymbol(node.get("displaySymbol").asText());
                    result.setSymbol(node.get("symbol").asText());
                    result.setType(node.get("type").asText().isEmpty() ? "N/A" : node.get("type").asText());
                    result.setQuery(keyword);
                    results.add(result);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return results;
    }

    @Override
    public List<StockSymbol> getStockSymbols(String exchange) {
        return List.of();
    }

    @Override
    public MarketStatus getMarketStatus(String exchange) {
        return null;
    }

    @Override
    public MarketHolidays getMarketHolidays(String exchange) {
        return null;
    }

    @Override
    public CompanyProfile getCompanyProfile(String symbol) {
        return null;
    }

    @Override
    public List<News> getNews(NewsCategory category) {
        return List.of();
    }

    @Override
    public List<News> getCompanyNews(String symbol, SearchDate from, SearchDate to) {
        return List.of();
    }

    @Override
    public List<Recommendations> getRecommendations(String symbol) {
        String endpoint = "stock/recommendation?symbol=" + symbol;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        List<Recommendations> recommendations = new ArrayList<>();

        try {
            JsonNode rootNode = mapper.readTree(json);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    int buy = node.get("buy").asInt();
                    int hold = node.get("hold").asInt();
                    String period = node.get("period").asText();
                    int sell = node.get("sell").asInt();
                    int strongBuy = node.get("strongBuy").asInt();
                    int strongSell = node.get("strongSell").asInt();
                    String foundSymbol = node.get("symbol").asText();

                    Recommendations recommendation = new Recommendations();
                    recommendation.setBuyRecommendations(buy);
                    recommendation.setHoldRecommendations(hold);
                    recommendation.setPeriod(period);
                    recommendation.setSellRecommendations(sell);
                    recommendation.setStrongBuyRecommendations(strongBuy);
                    recommendation.setStrongSellRecommendations(strongSell);
                    recommendation.setSymbol(foundSymbol);

                    recommendations.add(recommendation);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return recommendations;
    }

    @Override
    public StockQuote getStockQuote(String symbol) {
        return null;
    }

    @Override
    public Financials getFinancials(String symbol) {
        return null;
    }

    private String getRawJson(String endpoint) throws IOException, URISyntaxException {
        String url = BASE_URL + endpoint + "&token=" + API_KEY;

        HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        connection.disconnect();

        return result.toString();
    }
}
