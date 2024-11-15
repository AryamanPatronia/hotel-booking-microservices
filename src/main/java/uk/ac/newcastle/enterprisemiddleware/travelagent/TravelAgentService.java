package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

/**
 * @author aryamanpatronia
 */

@ApplicationScoped
public class TravelAgentService
{

    @Inject
    @RestClient
    TaxiClient taxiClient;

    @Inject
    @RestClient
    FlightClient flightClient;

    @Inject
    HotelService hotelService;

    @Inject
    TravelAgentRepository travelAgentRepository;

    public TravelAgentBooking createBooking(Long customerId, Long hotelId, TaxiBookingRequest taxiRequest, FlightBookingRequest flightRequest) throws Exception {
        Long taxiBookingId = null;
        Long flightBookingId = null;

        try
        {
            /**
             * Validate hotel..
             */
            var hotel = hotelService.findById(hotelId);
            if (hotel == null)
            {
                throw new WebApplicationException("Hotel not found", 404);
            }
            /**
             * Validate Taxi
             */
            var taxiResponse = taxiClient.createTaxiBooking(taxiRequest);
            if (taxiResponse.getStatus() != 201)
            {
                throw new WebApplicationException("Failed to create Taxi booking");
            }
            taxiBookingId = taxiResponse.readEntity(Long.class);

            var flightResponse = flightClient.createFlightBooking(flightRequest);
            if (flightResponse.getStatus() != 201)
            {
                throw new WebApplicationException("Failed to create Flight booking");
            }
            flightBookingId = flightResponse.readEntity(Long.class);

            TravelAgentBooking booking = new TravelAgentBooking();
            booking.setCustomerId(customerId);
            booking.setHotel(hotel);
            booking.setTaxiBookingId(taxiBookingId);
            booking.setFlightBookingId(flightBookingId);
            return travelAgentRepository.create(booking);

        }
        catch (Exception e)
        {
            if (taxiBookingId != null) taxiClient.cancelTaxiBooking(taxiBookingId);
            if (flightBookingId != null) flightClient.cancelFlightBooking(flightBookingId);
            throw e;
        }
    }
}
