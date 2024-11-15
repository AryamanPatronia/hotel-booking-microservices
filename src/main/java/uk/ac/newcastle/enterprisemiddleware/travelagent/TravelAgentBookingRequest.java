package uk.ac.newcastle.enterprisemiddleware.travelagent;

/**
 * @author aryamanpatronia
 */

public class TravelAgentBookingRequest
{
    public Long customerId;
    public Long hotelId;
    public TaxiBookingRequest taxiBookingRequest;
    public FlightBookingRequest flightBookingRequest;

    public TravelAgentBookingRequest(Long customerId, Long hotelId, TaxiBookingRequest taxiBookingRequest, FlightBookingRequest flightBookingRequest)
    {
        this.customerId = customerId;
        this.hotelId = hotelId;
        this.taxiBookingRequest = taxiBookingRequest;
        this.flightBookingRequest = flightBookingRequest;
    }
}
