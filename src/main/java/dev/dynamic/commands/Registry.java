package dev.dynamic.commands;

import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Registry {
    @Getter
    private static final List<Command> commands = new ArrayList<>();

    public static void registerAll() {
        Reflections reflections = new Reflections("dev.dynamic.commands");
        reflections.getSubTypesOf(Command.class).forEach(command -> {
            try {
                commands.add(command.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static boolean handle(String command, String[] args) {
        Command foundCommand = commands.stream()
                .filter(c -> c.getCommand().equalsIgnoreCase(command) || c.getAliases() != null &&
                        Arrays.stream(c.getAliases()).anyMatch(alias -> alias.equalsIgnoreCase(command)))
                .findFirst()
                .orElse(null);

        if (foundCommand == null) {
            return false;
        }

        if (args.length < foundCommand.getRequiredArgs().length) {
            System.out.println("Usage: " + foundCommand.getCommand() + " " + String.join(" ", foundCommand.getRequiredArgs()));
            return true;
        }

        foundCommand.execute(args);
        return true;
    }
}
