package dev.dynamic.commands;

import dev.dynamic.Main;
import dev.dynamic.api.Recommendations;
import dev.dynamic.api.StockData;
import dev.dynamic.api.StockQuote;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
public enum Commands {
    GET_STOCK_QUOTE(new Command("getStockQuote", "Get info about a stock, like current price, change, etc.", "symbol") {
        @Override
        public void execute(String[] args) {
            if (args.length < 1) {
                System.out.println("Usage: getStockQuote <symbol>");
                return;
            }

            System.out.println("Fetching data for symbol: " + args[0]);

            StockQuote quote = stockData.getStockQuote(args[0]);
            if (quote == null) {
                System.out.println("No data found for symbol: " + args[0] + ". Please make sure the symbol is correct.");
                return;
            }

            System.out.println("Data for symbol: " + args[0]);
            System.out.println("Current price: " + quote.getCurrentPrice());
            System.out.println("Change: " + quote.getChange());
            System.out.println("Percent change: " + quote.getPercentChange());
            System.out.println("High price: " + quote.getHighPrice());
            System.out.println("Low price: " + quote.getLowPrice());
            System.out.println("Open price: " + quote.getOpenPrice());
            System.out.println("Previous close: " + quote.getPreviousClose());
        }
    }),
    SEARCH_STOCK(new Command("search", "Search for a stock by keyword", "keyword", "-limit", "limit") {
        @Override
        public void execute(String[] args) {
            if (args.length < 1) {
                System.out.println("Usage: searchStock <keyword>");
                return;
            }

            AtomicInteger limit = new AtomicInteger(Integer.MAX_VALUE);
            if (args.length == 3 && args[1].equalsIgnoreCase("-limit")) {
                try {
                    limit.set(Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid limit: " + args[2]);
                    return;
                }
            }

            System.out.println("Searching for keyword: " + args[0]);

            stockData.search(args[0]).forEach(result -> {
                if (limit.getAndDecrement() <= 0) {
                    return;
                }

                System.out.println("Symbol: " + result.getSymbol());
                System.out.println("Display symbol: " + result.getDisplaySymbol());
                System.out.println("Description: " + result.getDescription());
                System.out.println("Type: " + result.getType());
                System.out.println();
            });
        }
    }),
    GET_RECOMMENDATIONS(new Command("recommendations", "Get recommendations for a stock", "symbol", "-limit", "limit") {
        @Override
        public void execute(String[] args) {
            if (args.length < 1) {
                System.out.println("Usage: recommendations <symbol>");
                return;
            }

            System.out.println("Fetching recommendations for symbol: " + args[0]);

            List<Recommendations> recommendations = stockData.getRecommendations(args[0]);

            if (recommendations.isEmpty()) {
                System.out.println("No data found for symbol: " + args[0] + ". Please make sure the symbol is correct.");
                return;
            }

            AtomicInteger limit = new AtomicInteger(Integer.MAX_VALUE);
            if (args.length == 3 && args[1].equalsIgnoreCase("-limit")) {
                try {
                    limit.set(Integer.parseInt(args[2]));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid limit: " + args[2]);
                    return;
                }
            }

            float runningBuyAverage = 0;
            float runningHoldAverage = 0;
            float runningSellAverage = 0;
            float runningStrongBuyAverage = 0;
            float runningStrongSellAverage = 0;
            int count = 0;

            for (Recommendations rec : recommendations) {
                if (limit.getAndDecrement() <= 0) {
                    return;
                }

                runningBuyAverage += rec.getBuyRecommendations();
                runningHoldAverage += rec.getHoldRecommendations();
                runningSellAverage += rec.getSellRecommendations();
                runningStrongBuyAverage += rec.getStrongBuyRecommendations();
                runningStrongSellAverage += rec.getStrongSellRecommendations();
                count++;

                System.out.println("Buy Recommendations: " + rec.getBuyRecommendations());
                System.out.println("Hold Recommendations: " + rec.getHoldRecommendations());
                System.out.println("Sell Recommendations: " + rec.getSellRecommendations());
                System.out.println("Strong Buy Recommendations: " + rec.getStrongBuyRecommendations());
                System.out.println("Strong Sell Recommendations: " + rec.getStrongSellRecommendations());
                System.out.println("Period: " + rec.getPeriod());
                System.out.println();
            }

            System.out.println("Average Buy Recommendations: " + runningBuyAverage / count);
            System.out.println("Average Hold Recommendations: " + runningHoldAverage / count);
            System.out.println("Average Sell Recommendations: " + runningSellAverage / count);
            System.out.println("Average Strong Buy Recommendations: " + runningStrongBuyAverage / count);
            System.out.println("Average Strong Sell Recommendations: " + runningStrongSellAverage / count);
            System.out.println("Averaged over " + count + " recommendations");
        }
    }),


    HELP(new Command("help", "View all commands, or get help for a specific command", "command") {
        @Override
        public void execute(String[] args) {
            if (args.length == 0) {
                System.out.println("Available commands:");
                System.out.println();

                Arrays.stream(Commands.values())
                        .forEach(cmd -> {
                            String argFormatted = "";
                            if (!cmd.getCommand().getArgs().isEmpty()) {
                                argFormatted = " " + String.join(" ", cmd.getCommand().getArgs());
                            }
                            System.out.println(cmd.getCommand().getName() + argFormatted);
                            System.out.println("  " + cmd.getCommand().getDescription());
                            System.out.println();
                        });
            } else {
                Commands cmd = Arrays.stream(Commands.values())
                        .filter(c -> c.getCommand().getName().equalsIgnoreCase(args[0]))
                        .findFirst()
                        .orElse(null);

                if (cmd == null) {
                    System.out.println("Unknown command: " + args[0]);
                    return;
                }

                System.out.println("Help for command: " + cmd.getCommand().getName());
                System.out.println("Description: " + cmd.getCommand().getDescription());
                System.out.println("Usage: " + cmd.getCommand().getName() + " " + cmd.getCommand().getArgs().stream().collect(Collectors.joining(" ")));
            }
        }
    });

    private final Command command;
    private static final StockData stockData = Main.getStockData();

    Commands(Command command) {
        this.command = command;
    }

    public static boolean handle(String command, String[] args) {
        Commands cmd = Arrays.stream(Commands.values())
                .filter(c -> c.getCommand().getName().equalsIgnoreCase(command))
                .findFirst()
                .orElse(null);

        if (cmd == null) {
            return false;
        }

        cmd.command.execute(args);
        return true;
    }
}
