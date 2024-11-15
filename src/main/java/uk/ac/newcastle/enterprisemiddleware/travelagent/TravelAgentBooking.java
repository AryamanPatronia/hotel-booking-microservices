package uk.ac.newcastle.enterprisemiddleware.travelagent;

import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author aryamanpatronia
 */

@Entity
@Table(name = "travel_agent_bookings")
public class TravelAgentBooking implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    @Column(name = "taxi_booking_id", nullable = false)
    private Long taxiBookingId;

    @Column(name = "flight_booking_id", nullable = false)
    private Long flightBookingId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(Long customerId)
    {
        this.customerId = customerId;
    }

    public Hotel getHotel()
    {
        return hotel;
    }

    public void setHotel(Hotel hotel)
    {
        this.hotel = hotel;
    }

    public Long getTaxiBookingId()
    {
        return taxiBookingId;
    }

    public void setTaxiBookingId(Long taxiBookingId)
    {
        this.taxiBookingId = taxiBookingId;
    }

    public Long getFlightBookingId()
    {
        return flightBookingId;
    }

    public void setFlightBookingId(Long flightBookingId)
    {
        this.flightBookingId = flightBookingId;
    }
}
