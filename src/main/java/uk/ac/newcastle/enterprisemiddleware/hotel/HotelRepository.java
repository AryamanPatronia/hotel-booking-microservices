package uk.ac.newcastle.enterprisemiddleware.hotel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * <p>This is a Repository class that connects the Service/Control layer (see {@link HotelService}) with the
 * Domain/Entity Object (see {@link Hotel}).</p>
 *
 * <p>The methods are 'package' scope and should only be accessed by a Service/Control object.</p>
 *
 * @see Hotel
 * @see javax.persistence.EntityManager
 */
@RequestScoped
public class HotelRepository
{
    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;

    /**
     * <p>Returns a List of all persisted {@link Hotel} objects, sorted alphabetically by name.</p>
     *
     * @return List of Hotel objects
     */
    public List<Hotel> findAllOrderedByName()
    {
        TypedQuery<Hotel> query = em.createNamedQuery(Hotel.FIND_ALL, Hotel.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Hotel object, specified by a Long id.</p>
     *
     * @param id The id field of the Hotel to be returned
     * @return The Hotel with the specified id
     */
    public Hotel findById(Long id)
    {
        return em.find(Hotel.class, id);
    }

    /**
     * <p>Persists the provided Hotel object to the application database using the EntityManager.</p>
     *
     * @param hotel The Hotel object to be persisted
     * @return The Hotel object that has been persisted
     * @throws ConstraintViolationException, Exception
     */
    public Hotel create(Hotel hotel) throws Exception
    {
        log.info("HotelRepository.create() - Creating " + hotel.getHotelName());

        // Write hotel to the database...
        em.persist(hotel);

        return hotel;
    }

    /**
     * <p>Updates an existing Hotel object in the application database with the provided Hotel object.</p>
     *
     * @param hotel The Hotel object to be merged with an existing Hotel
     * @return The Hotel that has been merged
     * @throws ConstraintViolationException, Exception
     */
    public Hotel update(Hotel hotel) throws Exception
    {
        log.info("HotelRepository.update() - Updating " + hotel.getHotelName());

        em.merge(hotel);

        return hotel;
    }

    /**
     * <p>Deletes the provided Hotel object from the application database if found there.</p>
     *
     * @param hotel The Hotel object to be removed from the application database
     * @return The Hotel object that has been successfully removed from the application database; or null
     * @throws Exception
     */
    public Hotel delete(Hotel hotel) throws Exception
    {
        log.info("HotelRepository.delete() - Deleting " + hotel.getHotelName());

        if (hotel.getId() != null)
        {
            em.remove(em.merge(hotel));
        } else
        {
            log.info("HotelRepository.delete() - No ID was found so can't Delete.");
        }

        return hotel;
    }
}
