package uk.ac.newcastle.enterprisemiddleware.travelagent;

/**
 * @author aryamanpatronia
 */

public class FlightBookingRequest
{
    private String flightNumber;
    private String departureLocation;
    private String arrivalLocation;
    private String departureDate;

    public FlightBookingRequest(String flightNumber, String departureLocation, String arrivalLocation, String departureDate)
    {
        this.flightNumber = flightNumber;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
    }
    public String getFlightNumber()
    {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber)
    {
        this.flightNumber = flightNumber;
    }

    public String getDepartureLocation()
    {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation)
    {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation()
    {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation)
    {
        this.arrivalLocation = arrivalLocation;
    }

    public String getDepartureDate()
    {
        return departureDate;
    }

    public void setDepartureDate(String departureDate)
    {
        this.departureDate = departureDate;
    }
}
