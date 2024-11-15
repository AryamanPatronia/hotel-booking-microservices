package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author aryamanpatronia
 */

@RegisterRestClient(configKey = "flight-api") //Using the flight service on openshift...
public interface FlightClient {

    @POST
    @Path("/flights/bookings")
    @Consumes("application/json")
    @Produces("application/json")
    Response createFlightBooking(FlightBookingRequest request);

    @DELETE
    @Path("/flights/bookings/{id}")
    Response cancelFlightBooking(@PathParam("id") Long id);
}