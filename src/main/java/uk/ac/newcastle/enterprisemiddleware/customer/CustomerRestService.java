package uk.ac.newcastle.enterprisemiddleware.customer;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * @see CustomerService
 * @see javax.ws.rs.core.Response
 */
@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)

@Tag(name = "1. Customer", description = "Customer Operations") //To order the entities properly...
public class CustomerRestService
{

    @Inject
    Logger log;

    @Inject
    CustomerService service;

    @Inject
    CustomerValidator validator;

    /**
     * <p>Return all Customers. They are sorted alphabetically by name.</p>
     *
     * @return A Response containing a list of Customers
     */
    @GET
    @Operation(description = "Fetch all Customers from the database")
    public Response retrieveAllCustomers()
    {
        List<Customer> customers = service.findAllOrderedByName();
        return Response.ok(customers).build();
    }

    /**
     * <p>Search for and return a Customer identified by email address.</p>
     *
     * @param email The string parameter value provided as a Customer's email
     * @return A Response containing a single Customer
     */
    @GET
    @Operation(description = "Fetch a Customer in the database using Email")
    @Path("/email/{email:.+[%40|@].+}")
    public Response retrieveCustomerByEmail(@PathParam("email") String email)
    {
        Customer customer = service.findByEmail(email);
        if (customer == null)
        {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No Customer with the given email " + email + " was found.")
                    .build();
        }
        return Response.ok(customer).build();
    }
    /**
     * <p>Create a new Customer.</p>
     *
     * @param customer The Customer object to be created
     * @return A Response indicating the outcome of the create operation
     */
    @POST
    @Operation(description = "Create a Customer in the database")
    @Transactional
    public Response createCustomer(@Valid Customer customer)
    {

        if (customer == null)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        try
        {
            validator.validateCustomer(customer); // Validate customer

            // Set the ID to null to prevent accidental overriding
            customer.setId(null);

            // Save customer to database
            service.create(customer);

            return Response.status(Response.Status.CREATED).entity(customer).build();
        }
        catch (ConstraintViolationException e)
        {
            Map<String, String> errors = new HashMap<>();
            e.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        catch (IllegalArgumentException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    /**
     * <p>Update the Customer with the given ID.</p>
     * @param id       The ID of the Customer to be updated
     * @param customer The Customer object with updated data
     * @return A Response indicating the outcome of the update operation
     */
    @PUT
    @Path("/{id}")
    @Operation(description = "Update a Customer in the database")
    @Transactional
    public Response updateCustomer(@PathParam("id") long id, @Valid Customer customer)
    {
        if (customer == null || customer.getId() != id)
        {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Customer existingCustomer = service.findById(id);
        if (existingCustomer == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        try
        {
            validator.validateCustomer(customer); // Validate customer
            service.update(customer); // Update customer in DB

            return Response.ok(customer).build();
        }
        catch (ConstraintViolationException e)
        {
            Map<String, String> errors = new HashMap<>();
            e.getConstraintViolations().forEach(violation -> errors.put(violation.getPropertyPath().toString(), violation.getMessage()));
            return Response.status(Response.Status.BAD_REQUEST).entity(errors).build();
        }
        catch (IllegalArgumentException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (UniqueEmailException | UniquePhoneNumberException e)
        {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
    /**
     * <p>Delete the Customer with the given ID.</p>
     *
     * @param id The ID of the Customer to delete
     * @return A Response indicating the outcome of the delete operation
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteCustomer(@PathParam("id") long id)
    {
        Customer customer = service.findById(id);
        if (customer == null)
        {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        service.delete(customer.getId());
        return Response.ok().build();
    }
}
