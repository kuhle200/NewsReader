package com.example.newsreader;

public class NewsItem {

    private String title;
    private String link;

    public NewsItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }


    @Override
    public String toString() {
        return "NewsItem{" +
                "title='" + title + '\''  +
                ", link='" + link + '\'' + '}';
    }
}
