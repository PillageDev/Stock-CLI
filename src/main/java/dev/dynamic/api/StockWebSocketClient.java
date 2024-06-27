package dev.dynamic.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dynamic.Main;
import lombok.Data;
import lombok.Getter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class StockWebSocketClient extends WebSocketClient {
    private static final ObjectMapper mapper = new ObjectMapper();

    public StockWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to server");

        String symbol = Main.getWebSocketSymbol();
        String jsonMessage = "{\"type\":\"subscribe\",\"symbol\":\"" + symbol + "\"}";
        send(jsonMessage);
    }

    private final Queue<StockWebSocketMessage> messageQueue = new PriorityQueue<>(Comparator.comparingLong(StockWebSocketMessage::getTimestamp));

    @Override
    public void onMessage(String message) {
        try {
            JsonNode rootNode = mapper.readTree(message);
            JsonNode dataNode = rootNode.get("data");
            for (JsonNode node : dataNode) {
                StockWebSocketMessage stockMessage = new StockWebSocketMessage();

                stockMessage.setSymbol(node.get("s").asText());
                stockMessage.setLastPrice(node.get("p").asDouble());
                stockMessage.setTimestamp(node.get("t").asLong());
                stockMessage.setVolume(node.get("v").asDouble());
                stockMessage.setTradeConditions(mapper.treeToValue(node.get("c"), int[].class));

                messageQueue.add(stockMessage);
            }
        } catch (JsonProcessingException ignored) {
            throw new RuntimeException("Failed to parse message: " + message);
        }

        while (!messageQueue.isEmpty()) {
            StockWebSocketMessage pollMessage = messageQueue.poll();
            String output = String.format("\rSymbol: %s, Last Price: %.2f, Timestamp: %d, Volume: %.2f, Trade Conditions: %s",
                    pollMessage.getSymbol(),
                    pollMessage.getLastPrice(),
                    pollMessage.getTimestamp(),
                    pollMessage.getVolume(),
                    Arrays.toString(pollMessage.getTradeConditionsTranslated()));
            System.out.print(output);
        }
        System.out.flush();
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {

    }

    @Override
    public void onError(Exception ex) {

    }

    @Data
    private static class StockWebSocketMessage {
        private String symbol;
        private double lastPrice;
        private long timestamp;
        private double volume;
        private int[] tradeConditions;
        private TradeConditions[] tradeConditionsTranslated;

        public void setTradeConditions(int[] tradeConditions) {
            this.tradeConditions = tradeConditions;
            this.translateTradeConditions();
        }

        private void translateTradeConditions() {
            this.tradeConditionsTranslated = new TradeConditions[this.tradeConditions.length];
            for (int i = 0; i < this.tradeConditions.length; i++) {
                this.tradeConditionsTranslated[i] = TradeConditions.values()[this.tradeConditions[i]];
            }
        }

        @Getter
        public enum TradeConditions {
            REGULAR(1),
            ACQUISITION(2),
            AVERAGE_PRICE_TRADE(3),
            BUNCHED(4),
            CASH_SALE(5),
            DISTRIBUTION(6),
            AUTOMATIC_EXECUTION(7),
            INTERMARKET_SWEEP_ORDER(8),
            BUNCHED_SOLD(9),
            PRICE_VARIATION_TRADE(10),
            CAP_ELECTION(11),
            ODD_LOT_TRADE(12),
            RULE_127(13),
            RULE_155(14),
            SOLD_LAST(15),
            MARKET_CENTER_OFFICIAL_CLOSE(16),
            NEXT_DAY(17),
            MARKET_CENTER_OPENING_TRADE(18),
            OPENING_PRINTS(19),
            MARKET_CENTER_OFFICIAL_OPEN(20),
            PRIOR_REFERENCE_PRICE(21),
            SELLER(22),
            SPLIT_TRADE(23),
            FORM_T_TRADE(24),
            EXTENDED_HOURS_SOLD_OUT_OF_SEQUENCE(25),
            CONTINGENT_TRADE(26),
            STOCK_OPTION_TRADE(27),
            CROSS_TRADE(28),
            YELLOW_FLAG(29),
            SOLD_OUT_OF_SEQUENCE(30),
            STOPPED_STOCK(31),
            DERIVATIVELY_PRICED(32),
            MARKET_CENTER_RE_OPENING_TRADE(33),
            RE_OPENING_PRINTS(34),
            MARKET_CENTER_CLOSING_TRADE(35),
            CLOSING_PRINTS(36),
            QUALIFIED_CONTIGENT_TRADE(37),
            PLACEHOLDER_FOR_611_EXEMPT(38),
            CORRECTED_CONSOLIDATED_CLOSE(39),
            OPENED(40),
            TRADE_THROUGH_EXEMPT(41);

            private final int rawCode;

            TradeConditions(int rawCode) {
                this.rawCode = rawCode;
            }
        }
    }
}
