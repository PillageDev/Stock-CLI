package dev.dynamic.commands;

import dev.dynamic.Main;
import dev.dynamic.api.StockSearchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Recommendations extends Command {
    private int currentPage = 0;
    private Queue<dev.dynamic.api.Recommendations> recommendations;
    private boolean lastPage = false;
    private int maxPage = 0;

    private float avgBuyRecommendations = 0;
    private float avgHoldRecommendations = 0;
    private float avgSellRecommendations = 0;
    private float avgStrongBuyRecommendations = 0;
    private float avgStrongSellRecommendations = 0;
    private float totalRecommendations = 0;

    @Override
    public String getCommand() {
        return "recommendations";
    }

    @Override
    public String getDescription() {
        return "Get recommendations for a stock";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Getting recommendations for symbol: " + args[0]);
        List<dev.dynamic.api.Recommendations> recommendations = stockData.getRecommendations(args[0]);
        if (recommendations.isEmpty()) {
            System.out.println("No data found for symbol: " + args[0] + ". Please make sure the symbol is correct.");
            return;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("-limit")) {
            try {
                int limit = Integer.parseInt(args[2]);
                if (limit < 1) {
                    System.out.println("Invalid limit: " + args[2]);
                    return;
                }

                recommendations = recommendations.subList(0, Math.min(recommendations.size(), limit));
            } catch (NumberFormatException e) {
                System.out.println("Invalid limit: " + args[2]);
                return;
            }
        }

        this.maxPage = recommendations.size() + 1;

        this.recommendations = new LinkedList<>(recommendations);
        nextPage();
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "symbol" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[] { "-limit", "limit" };
    }

    @Override
    public String[] getAliases() {
        return new String[] { "recs", "rec" };
    }

    private void nextPage() {
        if (lastPage) {
            return;
        }

        currentPage++;

        if (recommendations.isEmpty()) {
            lastPage = true;
            System.out.println("Page " + currentPage + " / " + maxPage);
            System.out.println("+---------------------------------+");
            System.out.println("Summary");
            System.out.println("Avg. Buy recommendations: " + avgBuyRecommendations / totalRecommendations);
            System.out.println("Avg. Hold recommendations: " + avgHoldRecommendations / totalRecommendations);
            System.out.println("Avg. Sell recommendations: " + avgSellRecommendations / totalRecommendations);
            System.out.println("Avg. Strong buy recommendations: " + avgStrongBuyRecommendations / totalRecommendations);
            System.out.println("Avg. Strong sell recommendations: " + avgStrongSellRecommendations / totalRecommendations);
            System.out.println("+---------------------------------+");
            return;
        }


        if (currentPage > maxPage) {
            currentPage = maxPage;
        }

        dev.dynamic.api.Recommendations result = recommendations.poll();

        int buyRecommendations = result.getBuyRecommendations();
        int holdRecommendations = result.getHoldRecommendations();
        int sellRecommendations = result.getSellRecommendations();
        int strongBuyRecommendations = result.getStrongBuyRecommendations();
        int strongSellRecommendations = result.getStrongSellRecommendations();

        avgBuyRecommendations += buyRecommendations;
        avgHoldRecommendations += holdRecommendations;
        avgSellRecommendations += sellRecommendations;
        avgStrongBuyRecommendations += strongBuyRecommendations;
        avgStrongSellRecommendations += strongSellRecommendations;
        totalRecommendations++;

        System.out.println("Page " + currentPage + " / " + maxPage);
        System.out.println("+---------------------------------+");
        System.out.println("Symbol: " + result.getSymbol());
        System.out.println("Buy Recommendations: " + buyRecommendations);
        System.out.println("Hold Recommendations: " + holdRecommendations);
        System.out.println("Sell Recommendations: " + sellRecommendations);
        System.out.println("Strong Buy Recommendations: " + strongBuyRecommendations);
        System.out.println("Strong Sell Recommendations: " + strongSellRecommendations);
        System.out.println("Period: " + result.getPeriod());
        System.out.println("+---------------------------------+");
        System.out.println("Press enter to view the next result or type 'exit' to stop viewing results");

        Main.overrideNextConsoleInput((input) -> {
            if (input.equalsIgnoreCase("exit")) {
                Main.resetOverride();
                return;
            }

            nextPage();

            Main.overrideNextConsoleInput(this::handleInput);
        });
    }

    private void handleInput(String input) {
        if (input.equalsIgnoreCase("exit")) {
            Main.resetOverride();
            return;
        }

        if (lastPage) {
            Main.resetOverride();
            return;
        }

        nextPage();

        Main.overrideNextConsoleInput(this::handleInput);
    }
}
