package uk.ac.newcastle.enterprisemiddleware.travelagent;

import org.eclipse.microprofile.openapi.annotations.Operation;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author aryamanpatronia
 */

@Path("/travelagent/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TravelAgentRestService
{

    @Inject
    TravelAgentService travelAgentService;

    @POST
    @Operation(summary = "Create a booking for flight,taxi and hotel...", description = "Creates a travel agent booking...")
    @Tag(name = "5. Travel Agent", description = "Make a booking across three commodities...")
    public Response createBooking(TravelAgentBookingRequest request)
    {
        try
        {
            var booking = travelAgentService.createBooking(
                    request.customerId,
                    request.hotelId,
                    request.taxiBookingRequest,
                    request.flightBookingRequest
            );
            return Response.status(201).entity(booking).build();
        } catch (Exception e)
        {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }
}
