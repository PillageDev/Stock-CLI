package dev.dynamic.api;

import lombok.Data;

import java.util.List;

@Data
public class MarketHolidays {
    private List<Holiday> holidays;
    private String exchange;
    private String timezone;

    @Data
    public static class Holiday {
        private String eventName;
        private String atDate;
        private String tradingHour;
    }
}
