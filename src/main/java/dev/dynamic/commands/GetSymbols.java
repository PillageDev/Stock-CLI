package dev.dynamic.commands;

import dev.dynamic.api.StockSymbol;

import java.util.List;

public class GetSymbols extends Command {
    @Override
    public String getCommand() {
        return "symbols";
    }

    @Override
    public String getDescription() {
        return "Get stock symbols for a specific exchange";
    }

    @Override
    public void execute(String[] args) {
        List<StockSymbol> symbols = stockData.getStockSymbols(args[0]);

        if (symbols.isEmpty()) {
            System.out.println("No symbols found for exchange: " + args[0]);
            return;
        }

        if (args.length > 1 && args[1].equalsIgnoreCase("-limit")) {
            int limit = Integer.parseInt(args[2]);
            symbols = symbols.subList(0, Math.min(limit, symbols.size()));
        }

        System.out.println("Symbols for exchange: " + args[0]);
        symbols.forEach(symbol -> System.out.println(symbol.getSymbol() + " - " + symbol.getDescription()));
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "exchange" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[] { "-limit", "limit" };
    }

    @Override
    public String[] getAliases() {
        return new String[] { "stocks", "stock" };
    }
}
