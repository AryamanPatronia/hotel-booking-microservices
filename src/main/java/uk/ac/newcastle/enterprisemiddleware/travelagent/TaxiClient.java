package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * @author aryamanpatronia
 */

@RegisterRestClient(configKey = "taxi-api") //Using the taxi service on Openshift...
public interface TaxiClient {

    @POST
    @Path("/taxis/bookings")
    @Consumes("application/json")
    @Produces("application/json")
    Response createTaxiBooking(TaxiBookingRequest request);

    @DELETE
    @Path("/taxis/bookings/{id}")
    Response cancelTaxiBooking(@PathParam("id") Long id);
}