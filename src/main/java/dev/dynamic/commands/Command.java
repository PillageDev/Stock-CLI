package dev.dynamic.commands;

import lombok.Data;

import java.util.List;

@Data
public abstract class Command {
    private final String name;
    private final String description;
    private final List<String> args;

    public Command(String name, String description, String... args) {
        this.name = name;
        this.description = description;
        this.args = List.of(args);
    }

    public abstract void execute(String[] args);
}
