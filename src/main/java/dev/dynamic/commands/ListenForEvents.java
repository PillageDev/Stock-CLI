package dev.dynamic.commands;

import dev.dynamic.Main;
import dev.dynamic.api.StockWebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

public class ListenForEvents extends Command {
    @Override
    public String getCommand() {
        return "listen";
    }

    @Override
    public String getDescription() {
        return "Listen to stock events in real-time";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Connecting to websocket...");
        System.out.println("Type 'exit' to disconnect from the websocket");

        StockWebSocketClient client = null;
        try {
            Main.setWebSocketSymbol(args[0]);
            String uri = "wss://ws.finnhub.io?token=cpum7e1r01qhicnal3pgcpum7e1r01qhicnal3q0";
            client = new StockWebSocketClient(new URI(uri));
            client.connectBlocking();
        } catch (URISyntaxException | InterruptedException e) {
            e.printStackTrace();
        }

        StockWebSocketClient finalClient = client;
        Main.overrideNextConsoleInput((line) -> {
            if ("exit".equalsIgnoreCase(line)) {
                System.out.println("Disconnecting from websocket...");
                if (finalClient != null)
                    finalClient.close();
                Main.overrideNextConsoleInput(null);
            }
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
        return new String[] { "events", "updates" };
    }
}
