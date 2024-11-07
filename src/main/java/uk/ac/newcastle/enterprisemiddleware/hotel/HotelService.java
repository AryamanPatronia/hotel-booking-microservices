package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * @see HotelValidator
 * @see HotelRepository
 *
  <p>This Service assumes the Control responsibility in the ECB pattern.</p>
 *
 * <p>The validation is done here so that it may be used by other Boundary Resources. Other Business Logic would go here
 * as well.</p>
 *
 * <p>There are no access modifiers on the methods, making them 'package' scope.  They should only be accessed by a
 * Boundary / Web Service class with public methods.</p>
 *
 */
@ApplicationScoped
public class HotelService {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    HotelValidator validator;

    @Inject
    HotelRepository hotelRepository;

    /**
     * <p>Returns a List of all persisted {@link Hotel} objects.</p>
     *
     * @return List of Hotel objects
     */
    List<Hotel> findAll() {
        return hotelRepository.findAllOrderedByName();
    }

    /**
     * <p>Returns a single Hotel object, specified by a Long id.</p>
     *
     * @param id The id field of the Hotel to be returned
     * @return The Hotel with the specified id
     */
    public Hotel findById(Long id) {
        return hotelRepository.findById(id);
    }

    /**
     * <p>Returns a single Hotel object, specified by a String id (modified for String input).</p>
     *
     * @param id The id field of the Hotel to be returned (as a String)
     * @return The Hotel with the specified id
     */
    public Hotel findById(String id) {
        try {
            Long idLong = Long.valueOf(id);  // Convert String to Long
            return hotelRepository.findById(idLong);
        } catch (NumberFormatException e) {
            log.warning("Invalid ID format: " + id);
            return null;  // Return null or handle the error appropriately
        }
    }

    /**
     * <p>Writes the provided Hotel object to the application database.</p>
     *
     * <p>Validates the data in the provided Hotel object using a {@link HotelValidator} object.</p>
     *
     * @param hotel The Hotel object to be written to the database using a {@link HotelRepository} object
     * @return The Hotel object that has been successfully written to the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Hotel create(Hotel hotel) throws Exception {
        log.info("HotelService.create() - Creating hotel: " + hotel.getName());

        // Validate hotel details
        validator.validateHotel(hotel);

        // Write the hotel to the database
        return hotelRepository.create(hotel);
    }

    /**
     * <p>Updates an existing Hotel object in the application database with the provided Hotel object.</p>
     *
     * <p>Validates the data in the provided Hotel object using a HotelValidator object.</p>
     *
     * @param hotel The Hotel object to be passed as an update to the application database
     * @return The Hotel object that has been successfully updated in the application database
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    Hotel update(Hotel hotel) throws Exception {
        log.info("HotelService.update() - Updating hotel: " + hotel.getName());

        // Validate hotel details
        validator.validateHotel(hotel);

        // Update the hotel in the database
        return hotelRepository.update(hotel);
    }

    /**
     * <p>Deletes the provided Hotel object from the application database if found there.</p>
     *
     * @param hotel The Hotel object to be removed from the application database
     * @return The Hotel object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    Hotel delete(Hotel hotel) throws Exception {
        log.info("HotelService.delete() - Deleting hotel: " + hotel.getName());

        Hotel deletedHotel = null;

        if (hotel.getId() != null) {
            deletedHotel = hotelRepository.delete(hotel);
        } else {
            log.info("HotelService.delete() - No ID was found so can't Delete.");
        }

        return deletedHotel;
    }
}
