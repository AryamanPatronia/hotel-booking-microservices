package uk.ac.newcastle.enterprisemiddleware.hotel;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
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
 * @see HotelService
 * @see javax.ws.rs.core.Response
 **/

@Tag(name = "2. Hotels", description = "Hotel Operations...")
@Path("/hotels")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class HotelRestService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelService service;

    @Inject
    HotelRepository hotelRepository;

    @Inject
    HotelValidator hotelValidator; // Injecting the validator

    /**
     * Operation to fetch all the hotels that exist in the database...s
     * @return Response of all the hotels that exist in the database...
     */

    @GET
    @Operation(summary = "Fetch all Hotels", description = "Returns a JSON array of all stored Hotel objects.")
    public Response retrieveAllHotels()
    {
        List<Hotel> hotels = hotelRepository.findAllOrderedByName();
        return Response.ok(hotels).build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------

    @GET
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Fetch a Hotel by id", description = "Returns a JSON representation of the Hotel object with the provided id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Hotel found"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found")
    })
    public Response retrieveHotelById(
            @Parameter(description = "Id of Hotel to be fetched", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id)
    {
        Hotel hotel = service.findById(id);
        if (hotel == null)
        {
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(hotel).build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------

    /**
     * Operation to add a new hotel to the database...
     * @param hotel
     * @return
     */
    @POST
    @Operation(description = "Add a new Hotel to the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Hotel created successfully."),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "409", description = "Hotel conflicts with an existing Hotel"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response createHotel(
            @Parameter(description = "JSON representation of Hotel object to be added to the database", required = true)
            Hotel hotel)
    {
        if (hotel == null)
        {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }

        Response.ResponseBuilder builder;

        try
        {
            hotel.setId(null); // Clear the ID if accidentally set
            hotelValidator.validateHotel(hotel); // Validate the hotel before creating
            service.create(hotel);
            builder = Response.status(Response.Status.CREATED).entity(hotel);

        }
        catch (ConstraintViolationException ce)
        {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations())
            {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);

        } catch (Exception e)
        {
            throw new RestServiceException(e);
        }

        log.info("createHotel completed. Hotel = " + hotel);
        return builder.build();
    }


        //------------COMMENTING FOR BETTER VISIBILITY--------------


    @PUT
    @Path("/{id:[0-9]+}")
    @Operation(description = "Update a Hotel in the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Hotel updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid Hotel supplied in request body"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateHotel(
            @Parameter(description = "Id of Hotel to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id,
            @Parameter(description = "JSON representation of Hotel object to be updated in the database", required = true)
            Hotel hotel)
    {
        if (hotel == null || hotel.getId() == null)
        {
            throw new RestServiceException("Invalid Hotel supplied in request body", Response.Status.BAD_REQUEST);
        }
        if (hotel.getId() != id)
        {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The Hotel ID in the request body must match that of the Hotel being updated");
            throw new RestServiceException("Hotel details supplied in request body conflict with another Hotel",
                    responseObj, Response.Status.CONFLICT);
        }
        if (service.findById(id) == null)
        {
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        Response.ResponseBuilder builder;
        try
        {
            hotelValidator.validateHotel(hotel); // Validate the hotel before updating
            service.update(hotel);
            builder = Response.ok(hotel);

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

        log.info("updateHotel completed. Hotel = " + hotel);
        return builder.build();
    }


    //------------COMMENTING FOR BETTER VISIBILITY--------------


    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(description = "Delete a Hotel from the database")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The hotel has been successfully deleted"),
            @APIResponse(responseCode = "404", description = "Hotel with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteHotel(
            @Parameter(description = "Id of Hotel to be deleted", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id)
    {
        Hotel hotel = service.findById(id);
        if (hotel == null)
        {
            throw new RestServiceException("No Hotel with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        try
        {
            service.delete(hotel);
            return Response.noContent().build();

        }
        catch (Exception e)
        {
            throw new RestServiceException(e);
        }
    }
}
