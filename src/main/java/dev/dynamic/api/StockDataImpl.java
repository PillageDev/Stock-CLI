package dev.dynamic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String endpoint = "stock/symbol?exchange=" + exchange;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        List<StockSymbol> symbols = new ArrayList<>();

        try {
            JsonNode rootNode = mapper.readTree(json);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    StockSymbol symbol = new StockSymbol();
                    symbol.setCurrency(node.get("currency").asText());
                    symbol.setDescription(node.get("description").asText());
                    symbol.setDisplaySymbol(node.get("displaySymbol").asText());
                    symbol.setSymbol(node.get("symbol").asText());
                    symbol.setType(node.get("type").asText());
                    symbols.add(symbol);
                }
            }
        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

        return symbols;
    }

    @Override
    public MarketStatus getMarketStatus(String exchange) {
        String endpoint = "stock/market-status?exchange=" + exchange;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        try {
            JsonNode rootNode = mapper.readTree(json);
            MarketStatus marketStatus = new MarketStatus();
            marketStatus.setExchange(rootNode.get("exchange").asText());
            marketStatus.setHoliday(rootNode.get("holiday").asText());
            marketStatus.setOpen(rootNode.get("isOpen").asBoolean());
            marketStatus.setSession(rootNode.get("session").asText());
            marketStatus.setTimezone(rootNode.get("timezone").asText());
            marketStatus.setTimestamp(rootNode.get("t").asLong());
            return marketStatus;
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public MarketHolidays getMarketHolidays(String exchange) {
        String endpoint = "stock/market-holiday?exchange=" + exchange;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        MarketHolidays marketHolidays = new MarketHolidays();

        try {
            JsonNode rootNode = mapper.readTree(json);
            marketHolidays.setExchange(rootNode.get("exchange").asText());
            marketHolidays.setTimezone(rootNode.get("timezone").asText());

            JsonNode dataNode = rootNode.get("data");
            List<MarketHolidays.Holiday> holidays = new ArrayList<>();

            for (JsonNode node : dataNode) {
                MarketHolidays.Holiday holiday = new MarketHolidays.Holiday();
                holiday.setAtDate(node.get("atDate").asText());
                holiday.setEventName(node.get("eventName").asText());
                holiday.setTradingHour(node.get("tradingHour").asText());
                holidays.add(holiday);
            }

            marketHolidays.setHolidays(holidays);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return marketHolidays;
    }

    @Override
    public CompanyProfile getCompanyProfile(String symbol) {
        String endpoint = "stock/profile2?symbol=" + symbol;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        CompanyProfile companyProfile = new CompanyProfile();
        try {
            JsonNode rootNode = mapper.readTree(json);
            companyProfile.setCountry(rootNode.get("country").asText());
            companyProfile.setCurrency(rootNode.get("currency").asText());
            companyProfile.setExchange(rootNode.get("exchange").asText());
            companyProfile.setIpo(rootNode.get("ipo").asText());
            companyProfile.setMarketCapitalization(rootNode.get("marketCapitalization").asText());
            companyProfile.setName(rootNode.get("name").asText());
            companyProfile.setPhone(rootNode.get("phone").asText());
            companyProfile.setShareOutstanding(rootNode.get("shareOutstanding").asText());
            companyProfile.setTicker(rootNode.get("ticker").asText());
            companyProfile.setWeburl(rootNode.get("weburl").asText());
            companyProfile.setLogo(rootNode.get("logo").asText());
            companyProfile.setFinnhubIndustry(rootNode.get("finnhubIndustry").asText());

            return companyProfile;
        } catch (JsonProcessingException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<News> getNews(NewsCategory category) {
        String endpoint = "news?category=" + category.toString().toLowerCase();
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        List<News> newsList = new ArrayList<>();

        try {
            JsonNode rootNode = mapper.readTree(json);

            for (JsonNode node : rootNode) {
                News news = new News();
                news.setCategory(node.get("category").asText());
                news.setDatetime(node.get("datetime").asText());
                news.setHeadline(node.get("headline").asText());
                news.setId(node.get("id").asText());
                news.setImage(node.get("image").asText());
                news.setRelated(node.get("related").asText());
                news.setSource(node.get("source").asText());
                news.setSummary(node.get("summary").asText());
                news.setUrl(node.get("url").asText());

                newsList.add(news);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return newsList;
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
        String endpoint = "quote?symbol=" + symbol;
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        StockQuote quote = new StockQuote();

        try {
            JsonNode rootNode = mapper.readTree(json);
            quote.setSymbol(symbol);
            quote.setOpenPrice(rootNode.get("o").asDouble());
            quote.setHighPrice(rootNode.get("h").asDouble());
            quote.setLowPrice(rootNode.get("l").asDouble());
            quote.setCurrentPrice(rootNode.get("c").asDouble());
            quote.setPreviousClose(rootNode.get("pc").asDouble());
            quote.setChange(rootNode.get("d").asDouble());
            quote.setPercentChange(rootNode.get("dp").asDouble() * 100);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }

        return quote;
    }

    @Override
    public Financials getFinancials(String symbol) {
        String endpoint = "stock/metric?symbol=" + symbol + "&metric=all";
        String json;
        try {
            json = getRawJson(endpoint);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        try {
            JsonNode rootNode = mapper.readTree(json);
            Financials financials = new Financials();

            financials.setMetricType(rootNode.get("metricType").asText());
            financials.setSymbol(rootNode.get("symbol").asText());

            JsonNode metricsNode = rootNode.get("metric");

            Map<String, Double> metrics = new HashMap<>();
            metricsNode.fieldNames().forEachRemaining(key -> metrics.put(key, metricsNode.get(key).asDouble()));
            financials.setMetrics(metrics);

            JsonNode seriesNode = rootNode.get("series").get("annual");

            Map<String, Financials.RatioEntry> ratios = new HashMap<>();
            seriesNode.fieldNames().forEachRemaining(key -> {
                JsonNode ratioArrayNode = seriesNode.get(key);
                for (JsonNode ratioNode : ratioArrayNode) {
                    Financials.RatioEntry ratioEntry = new Financials.RatioEntry();
                    ratioEntry.setPeriod(ratioNode.get("period").asText());
                    ratioEntry.setNetProfitMargin(ratioNode.get("v").asDouble());
                    ratios.put(key, ratioEntry);
                }
            });
            financials.setRatios(ratios);
            return financials;
        } catch (JsonProcessingException | NullPointerException e) {
            return null;
        }
    }

    private String getRawJson(String endpoint) throws IOException, URISyntaxException {
        String url = BASE_URL + endpoint + "&token=" + API_KEY;

        HttpURLConnection connection = (HttpURLConnection) new URI(url).toURL().openConnection();
        connection.setRequestMethod("GET");

        try {
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
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
