package dev.dynamic.commands;

public class MarketStatus extends Command {
    @Override
    public String getCommand() {
        return "status";
    }

    @Override
    public String getDescription() {
        return "Get the current market status for an exchange";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Getting market status for exchange: " + args[0]);
        dev.dynamic.api.MarketStatus marketStatus = stockData.getMarketStatus(args[0]);

        if (marketStatus == null) {
            System.out.println("No market status found for exchange: " + args[0]);
            return;
        }

        System.out.println("Market Status:");
        System.out.println("  Exchange: " + marketStatus.getExchange());
        if (marketStatus.getHoliday() != null && !marketStatus.getHoliday().isEmpty() && !marketStatus.getHoliday().contains("null")) {
            System.out.println("  Holidays: " + marketStatus.getHoliday());
        }
        System.out.println("  Open: " + marketStatus.isOpen());
        System.out.println("  Session: " + marketStatus.getSession());
        System.out.println("  Timezone: " + marketStatus.getTimezone());

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
        return new String[] { "market", "open", "close" };
    }
}
