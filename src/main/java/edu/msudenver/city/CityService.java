package edu.msudenver.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public List<City> getCities() {
        return cityRepository.findAll();
    }

    public City getCity(String countryCode, String postalCode) {
        CityId cityId = new CityId(countryCode, postalCode);
        try {
            return cityRepository.findById(cityId).get();
        } catch(NoSuchElementException | IllegalArgumentException e) {
            return null;
        }
    }


    public City saveCity(City city) {
        return cityRepository.save(city);
    }

    public boolean deleteCity(String countryCode, String postalCode) {
        CityId cityId = new CityId (countryCode, postalCode);
        try {
            if(cityRepository.existsById(cityId)) {
                cityRepository.deleteById(cityId);
                return true;
            }
        } catch(IllegalArgumentException e) {}

        return false;
    }
}
