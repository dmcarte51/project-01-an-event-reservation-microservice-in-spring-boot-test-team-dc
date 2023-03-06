package edu.msudenver.event;

import edu.msudenver.venue.Venue;
import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "events")
public class Event {

private String eventTitle;
private Timestamp eventStart;

private Timestamp eventEnd;

@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
@Column(columnDefinition = "SERIAL")
private Integer eventId;

    @ManyToOne
    @JoinColumn(name = "venueId")
    private Venue venue;

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public Timestamp getEventStart() {
        return eventStart;
    }

    public void setEventStart(Timestamp eventStart) {
        this.eventStart = eventStart;
    }

    public Timestamp getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(Timestamp eventEnd) {
        this.eventEnd = eventEnd;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}