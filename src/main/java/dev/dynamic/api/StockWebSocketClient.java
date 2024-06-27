package dev.dynamic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dynamic.Main;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class StockWebSocketClient extends WebSocketClient {
    private static final ObjectMapper mapper = new ObjectMapper();

    public StockWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");

         String jsonMessage = "{\"type\":\"subscribe\",\"symbol\":\"" + Main.getWebSocketSymbol() + "\"}";
         send(jsonMessage);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        System.out.println("Error occurred: " + ex.getMessage());
    }
}