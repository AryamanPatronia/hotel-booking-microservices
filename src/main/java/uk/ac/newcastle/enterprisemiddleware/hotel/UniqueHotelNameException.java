package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.validation.ValidationException;

/**
 * @author AryamanPatronia
 * <p>ValidationException caused if a Hotel's name conflicts with that of another Hotel.</p>
 *
 * <p>This violates the uniqueness constraint.</p>
 *
 * @see Hotel
 */
public class UniqueHotelNameException extends ValidationException
{

    public UniqueHotelNameException(String message)
    {
        super(message);
    }

    public UniqueHotelNameException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public UniqueHotelNameException(Throwable cause)
    {
        super(cause);
    }
}
