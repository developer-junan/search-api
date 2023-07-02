package com.kakaobank.searchapi.dto;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class NaverSearchResult {

    private Date lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Getter
    public static class Item {
        private String title;
        private String link;
        private String description;
        private String bloggername;
        private String bloggerlink;
        private Date postdate;
    }

    public SearchResult convertSearchResult(NaverSearchResult.Item item) {
        return SearchResult.builder()
                .content(item.getDescription())
                .url(item.getLink())
                .registeredDt(item.getPostdate())
                .title(item.getTitle())
                .build();
    }
}