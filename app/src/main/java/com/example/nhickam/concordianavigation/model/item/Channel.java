package com.example.nhickam.concordianavigation.model.item;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

/**
 * Created by nhickam on 11/2/2017.
 */

@Root(name="channel",strict =false)
public class Channel implements Serializable {

    @Element(name="description")
    private String description;

    @ElementList(inline = true,name="item")
    private List<Item> items;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Feed:  \n [Entries: " + items + "]";

    }
}
