package dev.dynamic.commands;

import dev.dynamic.api.StockData;

public class News extends Command {
    @Override
    public String getCommand() {
        return "news";
    }

    @Override
    public String getDescription() {
        return "Get the latest news for a news category";
    }

    @Override
    public void execute(String[] args) {
        StockData.NewsCategory newsCategory;
        try {
            newsCategory = StockData.NewsCategory.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid news category: " + args[0]);
            System.out.println("Valid news categories: General, Forex, Crypto, Merger");
            return;
        }

        System.out.println("Getting news for category: " + newsCategory);
        stockData.getNews(newsCategory).forEach(news -> {
            System.out.println("Headline: " + news.getHeadline());
            System.out.println("Source: " + news.getSource());
            System.out.println("URL: " + news.getUrl());
            System.out.println("Summary: " + news.getSummary().replaceAll("<.*?>", "")); // Get rid of html tags
            System.out.println("Related: " + news.getRelated());
            System.out.println("Image: " + news.getImage());
            System.out.println("Category: " + news.getCategory());
            System.out.println("Datetime: " + news.getDatetime());
            System.out.println();
        });
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "category" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[0];
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
