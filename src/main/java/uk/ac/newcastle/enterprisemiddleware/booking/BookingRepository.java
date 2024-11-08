package uk.ac.newcastle.enterprisemiddleware.booking;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This is a Repository class that connects the Service/Control layer (see {@link BookingService}) with the
 * Domain/Entity Object (see {@link Booking}).</p>
 *
 * <p>The methods are 'package' scope and should only be accessed by a Service/Control object.</p>
 * @author AryamanPatronia
 * @see Booking
 * @see javax.persistence.EntityManager
 */
@RequestScoped
public class BookingRepository
{

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Booking} objects, sorted by ID.</p>
     *
     * @return List of Booking objects
     */
    public List<Booking> findAll()
    {
        TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_ALL, Booking.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Booking object, specified by a Long id.</p>
     *
     * @param id The ID of the Booking to be returned
     * @return The Booking with the specified ID
     */
    public Booking findById(Long id)
    {
        return em.find(Booking.class, id);
    }

    /**
     * <p>Persists the provided Booking object to the application database using the EntityManager.</p>
     *
     * @param booking The Booking object to be persisted
     * @return The Booking object that has been persisted
     * @throws ConstraintViolationException, Exception
     */
    public Booking create(Booking booking) throws Exception
    {
        log.info("BookingRepository.create() - Creating booking for Customer ID: " + booking.getCustomer().getCustomerID() +
                " and Hotel ID: " + booking.getHotel().getId());

        em.persist(booking);

        return booking;
    }

    /**
     * <p>Updates an existing Booking object in the application database with the provided Booking object.</p>
     *
     * @param booking The Booking object to be merged with an existing Booking
     * @return The Booking that has been merged
     * @throws ConstraintViolationException, Exception
     */
    public Booking update(Booking booking) throws Exception
    {
        log.info("BookingRepository.update() - Updating booking with ID: " + booking.getId());

        em.merge(booking);

        return booking;
    }

    /**
     * <p>Deletes the provided Booking object from the application database if found there.</p>
     *
     * @param booking The Booking object to be removed from the application database
     * @return The Booking object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Booking delete(Booking booking) throws Exception
    {
        log.info("BookingRepository.delete() - Deleting booking with ID: " + booking.getId());

        if (booking.getId() != null)
        {
            em.remove(em.merge(booking));
        } else
        {
            log.info("BookingRepository.delete() - No ID was found so can't Delete.");
        }

        return booking;
        }
    }
