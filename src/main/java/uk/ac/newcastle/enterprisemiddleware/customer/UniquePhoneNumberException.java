package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * @author AryamanPatronia
 * Exception thrown when attempting to create or update a customer with a duplicate phone number.
 */
public class UniquePhoneNumberException extends WebApplicationException
{

    public UniquePhoneNumberException(String message)
    {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
