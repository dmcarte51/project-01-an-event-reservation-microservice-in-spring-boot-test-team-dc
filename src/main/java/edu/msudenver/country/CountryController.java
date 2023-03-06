package edu.msudenver.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<Country>> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    @GetMapping(path = "/{countryCode}", produces = "application/json")
    public ResponseEntity<Country> getCountry(@PathVariable String countryCode) {
        Country country = countryService.getCountry(String.valueOf(countryCode));
        return new ResponseEntity<>(country, country == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        try {
            return new ResponseEntity<>(countryService.saveCountry(country), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(path = "/{countryCode}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Country> updateCountry(@PathVariable String countryCode, @RequestBody Country updatedCountry) {
        Country retrievedCountry = countryService.getCountry(String.valueOf(countryCode));
        if (retrievedCountry != null) {
            retrievedCountry.setCountryName(updatedCountry.getCountryName());
            try {
                return ResponseEntity.ok(countryService.saveCountry(retrievedCountry));
            } catch(Exception e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{countryCode}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String countryCode) {
        return new ResponseEntity<>(countryService.deleteCountry(String.valueOf(countryCode)) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }
}
// im a little bit country