package dev.dynamic.commands;

import dev.dynamic.Main;
import dev.dynamic.api.StockSearchResult;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Search extends Command {
    private int currentPage = 0;
    private Queue<StockSearchResult> searchResults;
    int maxPage = 0;

    @Override
    public String getCommand() {
        return "search";
    }

    @Override
    public String getDescription() {
        return "Search for a stock by keyword (name, symbol, etc.)";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Searching for keyword: " + args[0]);
        List<StockSearchResult> results = stockData.search(args[0]);
        if (results.isEmpty()) {
            System.out.println("No results found for keyword: " + args[0]);
            return;
        }

        if (args.length == 3 && args[1].equalsIgnoreCase("-limit")) {
            try {
                int limit = Integer.parseInt(args[2]);
                if (limit < 1) {
                    System.out.println("Invalid limit: " + args[2]);
                    return;
                }

                results = results.subList(0, Math.min(results.size(), limit));
            } catch (NumberFormatException e) {
                System.out.println("Invalid limit: " + args[2]);
                return;
            }
        }

        searchResults = new LinkedList<>(results);
        maxPage = searchResults.size();
        nextPage();
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "keyword" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[] { "-limit", "limit" };
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    private void nextPage() {
        if (searchResults.isEmpty()) {
            Main.resetOverride();
            return;
        }

        currentPage++;

        StockSearchResult result = searchResults.poll();

        System.out.println("Page " + currentPage + " / " + maxPage);
        System.out.println("+---------------------------------+");
        System.out.println("Symbol: " + result.getSymbol());
        System.out.println("Display symbol: " + result.getDisplaySymbol());
        System.out.println("Description: " + result.getDescription());
        System.out.println("Type: " + result.getType());
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

        if (searchResults.isEmpty()) {
            Main.resetOverride();
            return;
        }

        nextPage();

        Main.overrideNextConsoleInput(this::handleInput);
    }
}
