package com.example.nhickam.concordianavigation.model;

import com.example.nhickam.concordianavigation.model.item.Channel;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;


/**
 * Created by nhickam on 11/2/2017.
 */

@Root(name="rss",strict =false)
public class Feed implements Serializable{

    @Element(name="channel")
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
/*
    @Override
    public String toString() {
        return "Channel: [ "+channel+"]";
    }*/
}
