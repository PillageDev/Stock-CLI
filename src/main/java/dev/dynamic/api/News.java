package dev.dynamic.api;

import lombok.Data;

@Data
public class News {
    private String category;
    private String datetime;
    private String headline;
    private String id;
    private String image;
    private String related;
    private String source;
    private String summary;
    private String url;
}
