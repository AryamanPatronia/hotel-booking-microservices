package uk.ac.newcastle.enterprisemiddleware.hotel;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author AryamanPatronia
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Hotel.FIND_ALL, query = "SELECT h FROM Hotel h ORDER BY h.hotelName ASC"),
        @NamedQuery(name = Hotel.FIND_BY_LOCATION, query = "SELECT h FROM Hotel h WHERE h.hotelLocation = :location")
})
@Table(name = "hotels", uniqueConstraints = @UniqueConstraint(columnNames = "hotel_name"))
public class Hotel implements Serializable
{
    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Hotel.findAll";
    public static final String FIND_BY_LOCATION = "Hotel.findByLocation";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50, message = "Hotel name must be between 1 and 50 characters.")
    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @NotNull
    @Size(min = 1, max = 50, message = "Location must be between 1 and 50 characters.")
    @Column(name = "hotel_location", nullable = false)
    private String hotelLocation;


    // Constructors for hotel class...
    public Hotel()
    {

    }


    public Hotel(String hotelName, String hotelLocation)
    {
        this.hotelName = hotelName;
        this.hotelLocation = hotelLocation;
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName)
    {
        this.hotelName = hotelName;
    }

    public String getHotelLocation()
    {
        return hotelLocation;
    }

    public void setHotelLocation(String hotelLocation)
    {
        this.hotelLocation = hotelLocation;
    }

    // Override equals and hashCode to use hotel name as a unique identifier...
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return hotelName.equals(hotel.hotelName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(hotelName);
    }

    @Override
    public String toString()
    {
        return "Hotel{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", hotelLocation='" + hotelLocation + '\'' +
                '}';
    }
}
