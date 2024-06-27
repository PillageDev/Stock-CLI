package dev.dynamic.commands;

import dev.dynamic.Main;
import dev.dynamic.api.StockData;
import lombok.Data;

@Data
public abstract class Command {
    protected final StockData stockData = Main.getStockData();

    public abstract String getCommand();
    public abstract String getDescription();
    public abstract void execute(String[] args);
    public abstract String[] getRequiredArgs();
    public abstract String[] getOptionalArgs();
    public abstract String[] getAliases();
}
