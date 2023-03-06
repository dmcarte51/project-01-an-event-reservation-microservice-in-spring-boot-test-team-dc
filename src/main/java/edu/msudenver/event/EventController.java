package edu.msudenver.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Event>> getEvents() {
        return ResponseEntity.ok(eventService.getEvents());
    }

    @GetMapping(path = "/{eventId}", produces = "application/json")
    public ResponseEntity<Event> getEvent(@PathVariable Integer eventId) {
        Event event = eventService.getEvent(eventId);
        return new ResponseEntity<>(event, event == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            return new ResponseEntity<>(eventService.saveEvent(event), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{eventId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Event> updateEvent(@PathVariable Integer eventId, @RequestBody Event updatedEvent) {
        Event retrievedEvent = eventService.getEvent(eventId);
        if (retrievedEvent != null) {
            retrievedEvent.setEventTitle(updatedEvent.getEventTitle());
            retrievedEvent.setEventStart(updatedEvent.getEventStart());
            retrievedEvent.setEventEnd(updatedEvent.getEventEnd());

            try {
                return ResponseEntity.ok(eventService.saveEvent(retrievedEvent));
            } catch(Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Integer eventId) {
        return new ResponseEntity<>(eventService.deleteEvent(eventId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}