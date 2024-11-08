package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This Service class handles business logic for the Booking entity.</p>
 *
 * <p>It performs operations like validation, CRUD actions, and logging. It calls the BookingRepository to persist
 * and retrieve Booking objects from the database.</p>
 * @author AryamanPatronia
 * @see BookingRepository
 * @see Customer
 * @see Hotel
 */
@Dependent
public class BookingService
{
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    BookingRepository crud;

    /**
     * <p>Returns a list of all persisted {@link Booking} objects, sorted by ID.</p>
     *
     * @return List of Booking objects
     */
    public List<Booking> findAll()
    {
        return crud.findAll();
    }

    /**
     * <p>Returns a single Booking object, specified by a Long bookingID.</p>
     *
     * @param bookingID The ID of the Booking to be returned
     * @return The Booking with the specified ID
     */
    public Booking findById(Long bookingID)
    {
        return crud.findById(bookingID);
    }

    /**
     * <p>Creates a new Booking object in the application database.</p>
     *
     * <p>Validates the data in the provided Booking object using Bean Validation annotations.</p>
     *
     * @param booking The Booking object to be created
     * @return The Booking object that was successfully created
     * @throws Exception if there is any error during the process
     */
    public Booking create(Booking booking) throws Exception
    {
        log.info("BookingService.create() - Creating booking for Customer ID: " + booking.getCustomer().getCustomerID() +
                " and Hotel ID: " + booking.getHotel().getId());

        // Create the booking in the database
        return crud.create(booking);
    }

    /**
     * <p>Updates an existing Booking object in the application database.</p>
     *
     * <p>Validates the data in the provided Booking object using Bean Validation annotations.</p>
     *
     * @param booking The Booking object to be updated
     * @return The updated Booking object
     * @throws Exception if there is any error during the process
     */
    public Booking update(Booking booking) throws Exception
    {
        log.info("BookingService.update() - Updating booking with ID: " + booking.getId());

        return crud.update(booking);
    }

    /**
     * <p>Deletes the provided Booking object from the application database.</p>
     *
     * @param booking The Booking object to be deleted
     * @return The Booking object that was successfully deleted; or null
     * @throws Exception if there is any error during the process
     */
    public Booking delete(Booking booking) throws Exception
    {
        log.info("BookingService.delete() - Deleting booking with ID: " + booking.getId());

        Booking deletedBooking = null;

        if (booking.getId() != null)
        {
            deletedBooking = crud.delete(booking);
        }
        else
        {
            log.info("BookingService.delete() - No ID found, can't delete.");
        }

        return deletedBooking;
    }
}
