package uk.ac.newcastle.enterprisemiddleware.booking;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This is a Repository class and connects the Service/Control layer with the
 * Domain/Entity Object (see {@link Booking}).<p/>
 *
 * <p>It provides various methods to interact with Booking entities in the database,
 * including find, create, update, and delete operations.</p>
 *
 * @see Booking
 * @see javax.persistence.EntityManager
 */
@RequestScoped
public class BookingRepository {

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Booking} objects, sorted by booking date.</p>
     *
     * @return List of Booking objects
     */
    List<Booking> findAllOrderedByDate() {
        TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_ALL, Booking.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Booking object, specified by a Long id.</p>
     *
     * @param id The id of the Booking to be returned
     * @return The Booking with the specified id
     */
    Booking findById(Long id) {
        return em.find(Booking.class, id);
    }

    /**
     * <p>Returns a list of Booking objects for a given hotel name.</p>
     *
     * @param hotelName The hotelName to search by
     * @return A list of Bookings for the specified hotel
     */
    List<Booking> findAllByHotelName(String hotelName) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Booking> criteria = cb.createQuery(Booking.class);
        Root<Booking> booking = criteria.from(Booking.class);
        criteria.select(booking).where(cb.equal(booking.get("hotelName"), hotelName));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * <p>Returns a list of Booking objects for a given customer email.</p>
     *
     * @param email The customer's email to search by
     * @return A list of Bookings for the specified email
     */
    List<Booking> findAllByCustomerEmail(String email) {
        TypedQuery<Booking> query = em.createNamedQuery(Booking.FIND_BY_CUSTOMER, Booking.class).setParameter("email", email);
        return query.getResultList();
    }

    /**
     * <p>Persists the provided Booking object to the database.</p>
     *
     * @param booking The Booking object to be persisted
     * @return The Booking object that has been persisted
     * @throws Exception
     */
    Booking create(Booking booking) throws Exception {
        log.info("BookingRepository.create() - Creating booking for hotel: " + booking.getHotelName());
        em.persist(booking);
        return booking;
    }

    /**
     * <p>Updates an existing Booking object in the database.</p>
     *
     * @param booking The Booking object to be merged
     * @return The updated Booking object
     * @throws Exception
     */
    Booking update(Booking booking) throws Exception {
        log.info("BookingRepository.update() - Updating booking for hotel: " + booking.getHotelName());
        em.merge(booking);
        return booking;
    }

    /**
     * <p>Deletes the provided Booking object from the database if it exists.</p>
     *
     * @param booking The Booking object to be deleted
     * @return The Booking object that has been deleted
     * @throws Exception
     */
    Booking delete(Booking booking) throws Exception {
        log.info("BookingRepository.delete() - Deleting booking for hotel: " + booking.getHotelName());

        if (booking.getId() != null) {
            em.remove(em.merge(booking));
        } else {
            log.info("BookingRepository.delete() - No ID found; cannot delete.");
        }
        return booking;
    }
}
