package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;

import javax.validation.constraints.NotNull;

/**
 * @author AryamanPatronia
 * <p>This class represents a GuestBooking, which includes both a Customer and a Booking.</p>
 * <p>It is used for deserialization purposes when a client sends a request containing both customer and booking data.</p>
 */
public class GuestBooking
{

    @NotNull
    private Customer customer;

    @NotNull
    private Booking booking;

    public Customer getCustomer()
    {
        return customer;
    }

    public void setCustomer(Customer customer)
    {
        this.customer = customer;
    }

    public Booking getBooking()
    {
        return booking;
    }

    public void setBooking(Booking booking)
    {
        this.booking = booking;
    }
}
