package edu.msudenver.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VenueService {
    @Autowired
    private VenueRepository venueRepository;

    public List<Venue> getVenues() {
        return venueRepository.findAll();
    }

    public Venue getVenue(Integer venueId) {
        try {
            return venueRepository.findById(venueId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }

    public Venue saveVenue (Venue venue) {
        return venueRepository.save(venue);
    }

    public boolean deleteVenue(Integer venueId) {
        try {
            if(venueRepository.existsById(venueId)) {
                venueRepository.deleteById(venueId);
                return true;
            }
        } catch(IllegalArgumentException e) {}

        return false;
    }
}