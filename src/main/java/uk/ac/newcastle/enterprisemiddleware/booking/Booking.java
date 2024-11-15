package uk.ac.newcastle.enterprisemiddleware.booking;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * <p>The Booking class represents a reservation for a customer at a hotel.</p>
 * <p>It includes references to Customer and Hotel, and stores details such as
 * check-in date and booking duration in days.</p>
 * We also have enabled cascade deletion. If a customer or hotel is deleted,
 * the booking will also be deleted...
 *
 * @author AryamanPatronia
 */

@Entity
@Table(name = "bookings")
@NamedQuery(name = Booking.FIND_ALL, query = "SELECT b FROM Booking b ORDER BY b.id")
public class Booking implements Serializable
{
    public static final String FIND_ALL = "Booking.findAll";
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // CASCADE DELETION FOR CUSTOMER...
    private Customer customer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // CASCADE DELETION FOR HOTEL...
    private Hotel hotel;

    @NotNull
    @Min(value = 1, message = "Number of days must be at least 1.")
    @Column(name = "booking_duration_days")
    private Integer bookingDurationDays;

    @NotNull
    @Column(name = "checkin_date")
    private LocalDate checkinDate;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Hotel getHotel()
    {
        return hotel;
    }


    public void setHotel(Hotel hotel)
    {
        this.hotel = hotel;
    }

    public Integer getBookingDurationDays()
    {
        return bookingDurationDays;
    }

    public void setBookingDurationDays(Integer bookingDurationDays)
    {
        this.bookingDurationDays = bookingDurationDays;
    }

    public LocalDate getCheckinDate()
    {
        return checkinDate;
    }

    public void setCheckinDate(LocalDate checkinDate)
    {
        this.checkinDate = checkinDate;
    }


    // Override equals and hashCode to use id for equality...
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id);
    }

    // ToString method for debugging...
    @Override
    public String toString()
    {
        return "Booking{" +
                "id=" + id +
                ", customer=" + customer +
                ", hotel=" + hotel +
                ", bookingDurationDays=" + bookingDurationDays +
                ", checkinDate=" + checkinDate +
                '}';
    }
}
