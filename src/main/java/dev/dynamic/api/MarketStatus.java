package dev.dynamic.api;

import lombok.Data;
import org.jetbrains.annotations.Nullable;

@Data
public class MarketStatus {
    private String exchange;
    private @Nullable String holiday;
    private boolean isOpen;
    private String session;
    private String timezone;
    private long timestamp;
}
