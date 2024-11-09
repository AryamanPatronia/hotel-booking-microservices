package uk.ac.newcastle.enterprisemiddleware.customer;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.Cache;
import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;
import uk.ac.newcastle.enterprisemiddleware.contact.UniqueEmailException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.NoResultException;
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

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

/**
 * @author AryamanPatronia
 * @see CustomerService
 * @see javax.ws.rs.core.Response
 **/
@Tag(name = "1. Customers", description = "Customer Related Operations...")
public class CustomerRestService
{
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerService service;

    /**
     * This GET operation will fetch all the customers that exist in the database...
     * @return A response containing the list of customers...
     */
    @GET
    @Operation(summary = "Fetch all customers that exist in the database...", description = "Returns a JSON array of all stored Customer objects.")
    public Response retrieveAllCustomers()
    {
        List<Customer> customers = service.findAllOrderedByName();
        return Response.ok(customers).build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------


    /**
     * @param email We will provide an email address to fetch a customer from the database...
     * @return a response containing a single customer...
     */
    @GET
    @Cache
    @Path("/email/{email:.+[%40|@].+}")
    @Operation(summary = "Fetch a customer from database by using email...", description = "Returns a JSON representation of the Customer object with the provided email.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer found!"),
            @APIResponse(responseCode = "404", description = "Customer with the provided email not found...")
    })
    public Response retrieveCustomerByEmail(
            @Parameter(description = "Email of Customer that has to be fetched", required = true)
            @PathParam("email") String email)
    {
        Customer customer;
        try
        {
            customer = service.findByEmail(email);
        } catch (NoResultException e)
        {
            throw new RestServiceException("No Customer with the email " + email + " was found!", Response.Status.NOT_FOUND);
        }
        return Response.ok(customer).build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------


    /**
     * Search for a customer in the database and fetch using ID...
     * @param id The long parameter value provided as a Customer's id...
     * @return A response containing a single Customer...
     */
    @GET
    @Cache
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Fetch a Customer from database using ID...", description = "Returns a JSON representation of the Customer object with the provided id.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer found!"),
            @APIResponse(responseCode = "404", description = "Customer with id not found...")
    })
    public Response retrieveCustomerById(
            @Parameter(description = "Id of Customer that has to be fetched...")
            @Schema(minimum = "0", required = true)
            @PathParam("id") long id)
    {
        Customer customer = service.findById(id);
        if (customer == null)
        {
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }
        log.info("findById " + id + ": found Customer = " + customer);

        return Response.ok(customer).build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------


    /**
     * Creates a new customer from the values provided.
     *
     * @param customer The Customer object to be created
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Operation(summary = "Add a new customer to the database...")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Customer created successfully!"),
            @APIResponse(responseCode = "400", description = "Invalid Customer supplied in request body..."),
            @APIResponse(responseCode = "409", description = "Customer conflicts with an existing Customer..."),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request...")
    })
    @Transactional
    public Response createCustomer(
            @Parameter(description = "JSON representation of Customer object to be added to the database!", required = true)
            Customer customer)
    {
        if (customer == null)
        {
            throw new RestServiceException("Bad Request", Response.Status.BAD_REQUEST);
        }
        Response.ResponseBuilder builder;
        try
        {
            customer.setCustomerID(null);  // Clear the ID if accidentally set...
            service.create(customer);
            builder = Response.status(Response.Status.CREATED).entity(customer);
        }
        catch (ConstraintViolationException ce)
        {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations())
            {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        } catch (UniqueEmailException e)
        {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Bad Request", responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e)
        {
            throw new RestServiceException(e);
        }
        log.info("createCustomer completed. Customer = " + customer);
        return builder.build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------


    /**
     * Updates the Customer with the ID provided in the database.
     *
     * @param customer The Customer object to be updated
     * @param id       The long parameter value provided as the id of the Customer to be updated
     * @return A Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Update a customer in the database...")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Customer updated successfully"),
            @APIResponse(responseCode = "400", description = "Invalid Customer supplied in request body"),
            @APIResponse(responseCode = "404", description = "Customer with id not found"),
            @APIResponse(responseCode = "409", description = "Customer details supplied in request body conflict with another existing Customer"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response updateCustomer(
            @Parameter(description = "Id of Customer to be updated", required = true)
            @Schema(minimum = "0")
            @PathParam("id") long id,
            @Parameter(description = "JSON representation of Customer object to be updated in the database", required = true)
            Customer customer)
    {
        if (customer == null || customer.getCustomerID() == null)
        {
            throw new RestServiceException("Invalid Customer supplied in request body", Response.Status.BAD_REQUEST);
        }

        if (customer.getCustomerID() != null && customer.getCustomerID() != id)
        {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("id", "The Customer ID in the request body must match that of the Customer being updated");
            throw new RestServiceException("Customer details conflict with another Customer", responseObj, Response.Status.CONFLICT);
        }

        if (service.findById(customer.getCustomerID()) == null)
        {
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder builder;
        try
        {
            service.update(customer);
            builder = Response.ok(customer);
        } catch (ConstraintViolationException ce)
        {
            Map<String, String> responseObj = new HashMap<>();
            for (ConstraintViolation<?> violation : ce.getConstraintViolations())
            {
                responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            throw new RestServiceException("Bad Request", responseObj, Response.Status.BAD_REQUEST, ce);
        }
        catch (UniqueEmailException e)
        {
            Map<String, String> responseObj = new HashMap<>();
            responseObj.put("email", "That email is already used, please use a unique email");
            throw new RestServiceException("Customer details conflict with another Customer", responseObj, Response.Status.CONFLICT, e);
        } catch (Exception e)
        {
            throw new RestServiceException(e);
        }

        log.info("updateCustomer completed. Customer = " + customer);
        return builder.build();
    }

    //------------COMMENTING FOR BETTER VISIBILITY--------------



    /**
     * Deletes a Customer using the ID provided.
     *
     * @param id The Long parameter value provided as the id of the Customer to be deleted
     * @return A Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id:[0-9]+}")
    @Operation(summary = "Delete a customer from the database...")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "The customer has been successfully deleted"),
            @APIResponse(responseCode = "400", description = "Invalid Customer id supplied"),
            @APIResponse(responseCode = "404", description = "Customer with id not found"),
            @APIResponse(responseCode = "500", description = "An unexpected error occurred whilst processing the request")
    })
    @Transactional
    public Response deleteCustomer(
            @Parameter(description = "Id of Customer to be deleted", required = true)
            @PathParam("id") long id)
    {
        Customer customer = service.findById(id);
        if (customer == null)
        {
            throw new RestServiceException("No Customer with the id " + id + " was found!", Response.Status.NOT_FOUND);
        }

        try
        {
            service.delete(customer);  // Pass the Customer object to the delete method
        } catch (Exception e)
        {
            throw new RestServiceException(e);
        }

        log.info("deleteCustomer completed. Customer with id " + id + " deleted.");
        return Response.noContent().build();
    }

}
