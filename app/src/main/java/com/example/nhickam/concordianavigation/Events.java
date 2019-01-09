package com.example.nhickam.concordianavigation;

/**
 * Created by nhickam on 12/10/2017.
 */

public class Events {

    String eventId;
    String eventTitle;
    String eventDate;
    String eventDescription;
    String eventLink;
    String eventGuid;

    public Events(){

    }

    //Test Constructor
    public Events(String id, String title, String date, String link, String guid) {
        this.eventId= id;
        this.eventTitle = title;
        this.eventDate = date;
        this.eventLink = link;
        this.eventGuid = guid;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String getEventLink() {
        return eventLink;
    }

    public String getEventGuid() {
        return eventGuid;
    }



}
