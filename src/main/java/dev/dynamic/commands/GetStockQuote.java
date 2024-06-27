package dev.dynamic.commands;

import dev.dynamic.api.StockQuote;

public class GetStockQuote extends Command {

    @Override
    public String getCommand() {
        return "getstockquote";
    }

    @Override
    public String getDescription() {
        return "Get current pricing info and changes about a stock";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Getting data for symbol: " + args[0]);
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

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "symbol" };
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[0];
    }

    @Override
    public String[] getAliases() {
        return new String[] { "quote" };
    }
}
