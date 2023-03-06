package edu.msudenver.city;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.msudenver.country.Country;

import javax.persistence.*;

@Entity
@Table(name = "cities")
@IdClass(CityId.class)
public class City {
    @Id
    private String postalCode;

    @Id
    @ManyToOne()
    @JoinColumn(name = "country_code")
    @JsonProperty("country")
    private Country countryCode;

    private String cityName;

    public City(String postalCode, Country countryCode, String cityName) {
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.cityName = cityName;
    }

    public City() {
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCountryCode(Country countryCode) {
        this.countryCode = countryCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Country getCountryCode() {
        return countryCode;
    }

    public String getCityName() {
        return cityName;
    }
}