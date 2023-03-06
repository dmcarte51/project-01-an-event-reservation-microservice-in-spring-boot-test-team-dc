package edu.msudenver.venue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/venues")
public class VenueController {
    @Autowired
    private VenueService venueService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Venue>> getVenues() {
        return ResponseEntity.ok(venueService.getVenues());
    }

    @GetMapping(path = "/{venueId}", produces = "application/json")
    public ResponseEntity<Venue> getVenue(@PathVariable Integer venueId) {
        Venue venue = venueService.getVenue(venueId);
        return new ResponseEntity<>(venue, venue == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        try {
            return new ResponseEntity<>(venueService.saveVenue(venue), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{venueId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Venue> updateVenue(@PathVariable Integer venueId,
                                             @RequestBody Venue updatedVenue) {
        Venue retrievedVenue = venueService.getVenue(venueId);
        if (retrievedVenue != null) {
            retrievedVenue.setVenueName(updatedVenue.getVenueName());
            retrievedVenue.setVenueType(updatedVenue.getVenueType());
            retrievedVenue.setStreetAddress(updatedVenue.getStreetAddress());
            try {
                return ResponseEntity.ok(venueService.saveVenue(retrievedVenue));
            } catch(Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{venueId}")
    public ResponseEntity<Void> deleteVenue(@PathVariable Integer venueId) {
        return new ResponseEntity<>(venueService.deleteVenue(venueId) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
