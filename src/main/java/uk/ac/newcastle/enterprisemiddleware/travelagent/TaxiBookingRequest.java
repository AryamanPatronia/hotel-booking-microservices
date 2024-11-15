package uk.ac.newcastle.enterprisemiddleware.travelagent;

/**
 * @author aryamanpatronia
 */

public class TaxiBookingRequest
{
    private String registration;
    private int numberOfSeats;

    public TaxiBookingRequest(String registration, int numberOfSeats)
    {
        this.registration = registration;
        this.numberOfSeats = numberOfSeats;
    }

    public String getRegistration()
    {
        return registration;
    }

    public void setRegistration(String registration)
    {
        this.registration = registration;
    }

    public int getNumberOfSeats()
    {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats)
    {
        this.numberOfSeats = numberOfSeats;
    }
}
