package uk.ac.newcastle.enterprisemiddleware.booking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * @see BookingService
 * @see Response
 */

@Path("/bookings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "3. Bookings", description = "Booking Operations...")
public class BookingRestService
{

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    BookingService service;

    @Inject
    BookingValidator bookingValidator;

//    @Inject
//    CustomerService customerService;
//
//    @Inject
//    HotelService hotelService;

    /**
     * Retrieve all Bookings.
     */
    @GET
    @Operation(summary = "Fetch all bookings from the database...", description = "Returns a JSON array of all stored Booking objects.")
    public Response retrieveAllBookings()
    {
        List<Booking> bookings = service.findAll();
        return Response.ok(bookings).build();
    }

    /**
     * Retrieve a Booking by ID.
     */
    @GET
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Fetch a booking by id from the database...", description = "Returns a JSON representation of the Booking object with the provided id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Booking found!"),
            @APIResponse(responseCode = "404", description = "Booking with id not found...")
    })
    public Response retrieveBookingById(
            @Parameter(description = "Id of Booking to be fetched...", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id)
    {
        Booking booking = service.findById(id);
        if (booking == null)
        {
            throw new RestServiceException("No Booking with the id " + id + " was found...", Response.Status.NOT_FOUND);
        }
        return Response.ok(booking).build();
    }

    /**
     * Create a new Booking.
     */
    @POST
    @Operation(summary = "Create a new booking...")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Booking created successfully!"),
            @APIResponse(responseCode = "400", description = "Invalid Booking supplied in request body..."),
            @APIResponse(responseCode = "409", description = "Booking conflicts with an existing Booking..."),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request...")
    })
    @Transactional
    public Response createBooking(
            @Parameter(description = "JSON representation of Booking object to be added to the database", required = true)
            Booking booking)
    {
        if (booking == null)
        {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try
        {
            booking.setId(null); // Clear the ID if accidentally set
            bookingValidator.validateBooking(booking); // Validate the booking before creating
            service.create(booking);
            builder = Response.status(Response.Status.CREATED).entity(booking);

        }
        catch (ConstraintViolationException ce)
        {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations())
            {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        }
        catch (Exception e)
        {
            throw new RestServiceException(e);
        }

        log.info("createBooking completed. Booking = " + booking);
        return builder.build();
    }

    /**
     * Update an existing Booking.
     */
    @PUT
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Update a booking...")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Booking updated successfully!"),
            @APIResponse(responseCode = "400", description = "Invalid Booking supplied in request body..."),
            @APIResponse(responseCode = "404", description = "Booking with id not found..."),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request...")
    })
    @Transactional
    public Response updateBooking(
            @Parameter(description = "Id of Booking to be updated...", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id,
            @Parameter(description = "JSON representation of Booking object to be updated in the database", required = true)
            Booking booking)
    {
        if (booking == null || booking.getId() == null)
        {
            throw new RestServiceException("Invalid Booking supplied in request body", Response.Status.BAD_REQUEST);
        }
        if (booking.getId() != id)
        {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The Booking ID in the request body must match that of the Booking being updated");
            throw new RestServiceException("Booking details conflict with another Booking",
                    responseObj, Response.Status.CONFLICT);
        }
        if (service.findById(id) == null)
        {
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;
        try
        {
            bookingValidator.validateBooking(booking); // Validate the booking before updating
            service.update(booking);
            builder = Response.ok(booking);

        }
        catch (ConstraintViolationException ce)
        {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations())
            {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        }
        catch (Exception e)
        {
            throw new RestServiceException(e);
        }

        log.info("updateBooking completed. Booking = " + booking);
        return builder.build();
    }

    /**
     * Delete a Booking by ID.
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Delete a booking from the database...")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The booking has been successfully deleted!"),
            @APIResponse(responseCode = "404", description = "Booking with id not found..."),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request...")
    })
    @Transactional
    public Response deleteBooking(
            @Parameter(description = "Id of Booking to be deleted", required = true)
            @PathParam("id") long id)
    {
        Booking booking = service.findById(id);
        if (booking == null)
        {
            throw new RestServiceException("No Booking with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        try
        {
            service.delete(booking);
            return Response.noContent().build();

        }
        catch (Exception e)
        {
            throw new RestServiceException(e);
        }
    }
}
