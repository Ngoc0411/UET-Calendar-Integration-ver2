package com.team2.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "events", schema = "user_management", catalog = "")
public class EventsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_id")
    private int userId;
    @Basic
    @Column(name = "event_from")
    private int eventFrom;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "start")
    private String start;
    @Basic
    @Column(name = "end")
    private String end;
    @Basic
    @Column(name = "created")
    private Timestamp created;
    @Basic
    @Column(name = "modified")
    private Timestamp modified;
    @Basic
    @Column(name = "event_id")
    private String eventId;
    
    public EventsEntity(String title, String start, String end) {
    	this.title = title;
    	this.start = start;
    	this.end = end;
    }

    public EventsEntity() {

    }

    public EventsEntity(String eventId, String title, String start, String end, int eventFrom, int userId){
        this.eventId = eventId;
        this.title = title;
        this.start = start;
        this.end = end;
        this.eventFrom = eventFrom;
        this.userId = userId;
    }

    public String toString() {
        return this.id + " " + this.title + " " + this.start + " " + this.end + " " + this.eventFrom;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventFrom() {
        return eventFrom;
    }

    public void setEventFrom(int eventFrom) {
        this.eventFrom = eventFrom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(Timestamp modified) {
        this.modified = modified;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventsEntity that = (EventsEntity) o;
        return id == that.id && userId == that.userId && eventFrom == that.eventFrom && Objects.equals(title, that.title) && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(created, that.created) && Objects.equals(modified, that.modified) && Objects.equals(eventId, that.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, eventFrom, title, start, end, created, modified, eventId);
    }
}
