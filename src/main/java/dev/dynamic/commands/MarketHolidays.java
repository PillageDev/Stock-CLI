package dev.dynamic.commands;

public class MarketHolidays extends Command {
    @Override
    public String getCommand() {
        return "holiday";
    }

    @Override
    public String getDescription() {
        return "Get the market holidays for a specific exchange";
    }

    @Override
    public void execute(String[] args) {
        dev.dynamic.api.MarketHolidays marketHolidays = stockData.getMarketHolidays(args[0]);

        if (marketHolidays == null) {
            System.out.println("No market holidays found for exchange: " + args[0]);
            return;
        }

        System.out.println("Market holidays for exchange: " + marketHolidays.getExchange());
        marketHolidays.getHolidays().forEach(holiday -> {
            System.out.println("Event: " + holiday.getEventName());
            System.out.println("Date: " + holiday.getAtDate());
            if (!holiday.getTradingHour().isEmpty()) {
                System.out.println("Trading Hour: " + holiday.getTradingHour());
            }
            System.out.println();
        });
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[] { "exchange" };
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
