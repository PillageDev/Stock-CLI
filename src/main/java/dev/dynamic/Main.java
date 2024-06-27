package dev.dynamic;

import dev.dynamic.api.StockData;
import dev.dynamic.api.StockDataImpl;
import dev.dynamic.commands.Commands;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .variable(LineReader.SECONDARY_PROMPT_PATTERN, "%M%P > ")
                .build();

        while (true) {
            String line = reader.readLine("%M%P > ");

            if ("exit".equalsIgnoreCase(line)) {
                break;
            }

            String[] parts = line.split(" ");

            String[] newParts = Arrays.copyOfRange(parts, 1, parts.length);

            boolean handled = Commands.handle(parts[0], newParts);
            if (!handled) {
                System.out.println("Unknown command: " + line);
            }
        }
    }

    public static StockData getStockData() {
        return new StockDataImpl();
    }
}