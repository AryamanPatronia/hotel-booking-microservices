package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>This class provides methods to check Hotel objects against arbitrary requirements.</p>
 * @author AryamanPatronia
 * @see Hotel
 * @see HotelRepository
 * @see javax.validation.Validator
 */
@ApplicationScoped
public class HotelValidator
{
    @Inject
    Validator validator;

    @Inject
    HotelRepository hotelRepository;

    /**
     * <p>Validates the given Hotel object and throws validation exceptions based on the type of error. If the error is standard
     * bean validation errors, it throws a ConstraintValidationException with the set of the constraints violated.</p>
     *
     * <p>If the error is caused because an existing hotel with the same name is registered, it throws a regular validation
     * exception to handle it separately.</p>
     *
     * @param hotel The Hotel object to be validated
     * @throws ConstraintViolationException If Bean Validation errors exist
     * @throws ValidationException If a hotel with the same name already exists
     */
    void validateHotel(Hotel hotel) throws ConstraintViolationException, ValidationException
    {
        // Validate the hotel object with standard bean validation constraints
        Set<ConstraintViolation<Hotel>> violations = validator.validate(hotel);

        if (!violations.isEmpty())
        {
            throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
        }

        // Check the uniqueness of the hotel name
        if (nameAlreadyExists(hotel.getName(), hotel.getId()))
        {
            throw new UniqueHotelNameException("Unique Hotel Name Violation");
        }
    }

    /**
     * <p>Checks if a hotel with the same name is already registered. This is the only way to easily capture the
     * "@UniqueConstraint(columnNames = "name")" constraint from the Hotel class.</p>
     *
     * <p>Since updating may involve using a name already in the database, we need to ensure it is the name
     * from the record being updated.</p>
     *
     * @param name The hotel name to check for uniqueness
     * @param id The hotel id to check the name against if it was found
     * @return boolean representing whether the name was found, and if so, if it belongs to the hotel with the specified id
     */

    boolean nameAlreadyExists(String name, Long id)
    {
        List<Hotel> hotels = hotelRepository.findAllOrderedByName();

        for (Hotel hotel : hotels)
        {
            if (hotel.getName().equals(name))
            {
                // If `id` is provided, allow the name if it belongs to the hotel with the same `id`
                if (id != null && hotel.getId().equals(id))
                {
                    return false;
                }
                // Otherwise, a hotel with this name already exists
                return true;
            }
        }

        return false;
    }

}
