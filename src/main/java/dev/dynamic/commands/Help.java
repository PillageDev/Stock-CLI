package dev.dynamic.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Help extends Command {
    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Help with commands";
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Available commands:");
            Registry.getCommands().forEach(command -> {
                String requiredArgsFormatted = formatArgs(command.getRequiredArgs(), "<", ">");
                String optionalArgsFormatted = formatArgs(command.getOptionalArgs(), "[", "]");
                System.out.println(command.getCommand() + " " + requiredArgsFormatted + optionalArgsFormatted + "- " + command.getDescription());
            });
        } else {
            // Additional logic for handling specific command help
        }
    }

    private String formatArgs(String[] args, String leftBracket, String rightBracket) {
        return Arrays.stream(args)
                .map(arg -> (arg.startsWith("-") ? arg : leftBracket + arg + rightBracket) + " ")
                .collect(Collectors.joining());
    }

    @Override
    public String[] getRequiredArgs() {
        return new String[0];
    }

    @Override
    public String[] getOptionalArgs() {
        return new String[] { "command" };
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }
}
