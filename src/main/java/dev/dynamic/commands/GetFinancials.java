package dev.dynamic.commands;

import dev.dynamic.api.Financials;

import java.util.Map;

public class GetFinancials extends Command {
    @Override
    public String getCommand() {
        return "financials";
    }

    @Override
    public String getDescription() {
        return "Get financials for a stock symbol";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Getting financials for symbol: " + args[0]);
        Financials financials = stockData.getFinancials(args[0]);

        if (financials == null) {
            System.out.println("No financials found for symbol: " + args[0]);
            return;
        }

        Map<String, Double> metrics = financials.getMetrics();
        Map<String, Financials.RatioEntry> ratios = financials.getRatios();

        System.out.println("Metrics:");
        metrics.forEach((key, value) -> System.out.println(formatCamelCase(key) + ": " + value));

        System.out.println("\nRatios:");
        ratios.forEach((key, value) -> {
            System.out.println(formatCamelCase(key) + ":");
            System.out.println("  Period: " + value.getPeriod());
            System.out.println("  Net Profit Margin: " + value.getNetProfitMargin());
        });
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
        return new String[] { "financial", "finances", "fin" };
    }

    public String formatCamelCase(String input) {
        StringBuilder result = new StringBuilder();
        char previousChar = input.charAt(0);
        result.append(Character.toUpperCase(previousChar));

        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar) && !isTTM(input, i)) {
                result.append(" ");
                result.append(currentChar);
            } else if (Character.isDigit(currentChar) && Character.isLetter(previousChar)) {
                result.append(" ");
                result.append(currentChar);
            } else {
                result.append(Character.toLowerCase(currentChar));
            }
            previousChar = currentChar;
        }

        return result.toString().trim();
    }

    private boolean isTTM(String input, int index) {
        return index <= input.length() - 3 && input.substring(index, index + 3).equalsIgnoreCase("TTM");
    }
}
