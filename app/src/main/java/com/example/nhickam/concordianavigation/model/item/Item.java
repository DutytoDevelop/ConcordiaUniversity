package com.example.nhickam.concordianavigation.model.item;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhickam on 11/2/2017.
 */

@Root(name="item",strict=false)
public class Item implements Serializable{

    @Element(name="title")
    private String title;

    @Element(name="link")
    private String link;

    @Element(name="description")
    private String description;

    @Element(name="guid")
    private String guid;

    @Element(name="pubDate")
    private String pubDate;


    public Item() {
    }

    public Item(String title, String link, String description, String guid, String pubDate, List<Item> items) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.guid = guid;
        this.pubDate = pubDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", guid='" + guid + '\'' +
                ", pubDate='" + pubDate + '\'' +
                '}'+"\n"+"************************************\n\n";
    }
}
