package edu.msudenver.venue;

        import edu.msudenver.city.City;

        import javax.persistence.*;

@Entity
@Table(name = "venues")
public class Venue {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL")
    private Integer Id;
    private String venueName;
    private String streetAddress;
    private String venueType;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "country_code"),
            @JoinColumn(name = "postal_code")

    })
    private City city;

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getVenueType() {
        return venueType;
    }

    public void setVenueType(String venueType) {
        this.venueType = venueType;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }
}