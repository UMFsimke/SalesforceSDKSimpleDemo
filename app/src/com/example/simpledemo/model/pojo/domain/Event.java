package com.example.simpledemo.model.pojo.domain;

import com.example.simpledemo.MainApplication;
import com.example.simpledemo.model.pojo.salesforce.SalesforceEvent;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Event extends Syncable<SalesforceEvent> {

    @SerializedName(SalesforceEvent.FIELD_DESCRIPTION) private String description;
    @SerializedName(SalesforceEvent.FIELD_LOCATION) private String location;
    @SerializedName(SalesforceEvent.FIELD_PRIVATE) private boolean privateEvent;
    @SerializedName(SalesforceEvent.FIELD_START_DATE_TIME) private Date startDateTime;
    @SerializedName(SalesforceEvent.FIELD_END_DATE_TIME) private Date endDateTime;
    @SerializedName(SalesforceEvent.FIELD_EVENT_SUBJECT) private String subject;
    @SerializedName(SalesforceEvent.FIELD_IS_ALL_DAY) private boolean allDay;
    private User createdBy;

    public static Event parse(SalesforceEvent salesforceEvent, User user) {
        Event event = MainApplication.getInstance().graph().getGson()
                .fromJson(salesforceEvent.getRawData().toString(), Event.class);
        event.updateLocalFlags();
        event.setCreatedBy(user);
        return event;
    }

    public Event() {
        super();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isPrivateEvent() {
        return privateEvent;
    }

    public void setPrivateEvent(boolean privateEvent) {
        this.privateEvent = privateEvent;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
}
