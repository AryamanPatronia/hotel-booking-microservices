package uk.ac.newcastle.enterprisemiddleware.hotel;

import io.smallrye.common.constraint.NotNull;
import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author AryamanPatronia
 */

@Entity
@NamedQueries({
        @NamedQuery(name = Hotel.FIND_ALL, query = "SELECT h FROM Hotel h ORDER BY h.name ASC"),
        @NamedQuery(name = Hotel.FIND_BY_LOCATION, query = "SELECT h FROM Hotel h WHERE h.location = :location")
})
@Table(name = "hotels", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String FIND_ALL = "Hotel.findAll";
    public static final String FIND_BY_LOCATION = "Hotel.findByLocation";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 50, message = "Hotel name must be between 1 and 50 characters.")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Size(min = 1, max = 50, message = "Location must be between 1 and 50 characters.")
    @Column(nullable = false)
    private String location;

    @NotNull
    @Pattern(regexp = "^(Single|Double|Suite)(,\\s?(Single|Double|Suite))*$", message = "Room types must be a comma-separated list of Single, Double, or Suite.")
    @Column(name = "room_types", nullable = false)
    private String roomTypes;

    @NotNull
    @Min(value = 0, message = "Availability cannot be negative.")
    @Column(nullable = false)
    private int availability;

    // Constructors for hotel class...
    public Hotel()
    {
    }

    public Hotel(String name, String location, String roomTypes, int availability)
    {
        this.name = name;
        this.location = location;
        this.roomTypes = roomTypes;
        this.availability = availability;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getRoomTypes()
    {
        return roomTypes;
    }

    public void setRoomTypes(String roomTypes)
    {
        this.roomTypes = roomTypes;
    }

    public int getAvailability()
    {
        return availability;
    }

    public void setAvailability(int availability)
    {
        this.availability = availability;
    }

    // Override equals and hashCode to use hotel name as a unique identifier
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return name.equals(hotel.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(name);
    }

    @Override
    public String toString()
    {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", roomTypes='" + roomTypes + '\'' +
                ", availability=" + availability +
                '}';
    }
}
