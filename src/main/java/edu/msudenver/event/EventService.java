package edu.msudenver.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public Event getEvent(Integer eventId) {
        try {
            return eventRepository.findById(eventId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }


    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public boolean deleteEvent(Integer eventId) {
        try {
            if(eventRepository.existsById(eventId)) {
                eventRepository.deleteById(eventId);
                return true;
            }
        } catch(IllegalArgumentException e) {}

        return false;
    }
}
