package dev.dynamic.commands;

public class Clear extends Command {
    @Override
    public String getCommand() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Clear the console output";
    }

    @Override
    public void execute(String[] args) {
        System.out.print("\033[H\033[2J"); // ANSI escape code to clear console
        System.out.flush();
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[0];
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[0];
    }

    @Override
    public String[] getAliases() {
        return new String[] { "cls", "cl" };
    }
}
