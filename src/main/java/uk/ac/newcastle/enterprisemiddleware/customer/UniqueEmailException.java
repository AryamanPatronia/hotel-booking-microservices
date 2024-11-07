package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * <p>Custom exception to handle unique email constraint violations.</p>
 *
 * <p>This exception is thrown when a customer tries to register with an email that already exists in the system.</p>
 *
 * @author AryamanPatronia
 */
public class UniqueEmailException extends WebApplicationException
{

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor to create a UniqueEmailException with a custom message.</p>
     *
     * @param email The email that caused the violation
     */
    public UniqueEmailException(String email)
    {
        super(Response.status(Response.Status.BAD_REQUEST)
                .entity("The provided Email address '" + email + "' is already taken.")
                .build());
    }
}
