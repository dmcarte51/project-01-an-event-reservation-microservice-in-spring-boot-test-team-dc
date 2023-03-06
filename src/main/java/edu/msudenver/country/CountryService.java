package edu.msudenver.country;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CountryService {
    @Autowired
    private CountryRepository countryRepository;

    public List<Country> getCountries() {
        return countryRepository.findAll();
    }

    public Country getCountry(String countryCode) {
        try {
            return countryRepository.findById(countryCode).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }

    public Country saveCountry(Country country) {
        return countryRepository.save(country);
    }

    public boolean deleteCountry(String countryCode) {
        try {
            if(countryRepository.existsById(countryCode)) {
                countryRepository.deleteById(countryCode);
                return true;
            }
        } catch(IllegalArgumentException e) {}

        return false;
    }
}
