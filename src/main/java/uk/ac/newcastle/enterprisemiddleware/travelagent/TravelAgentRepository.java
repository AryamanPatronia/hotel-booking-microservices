package uk.ac.newcastle.enterprisemiddleware.travelagent;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * @author aryamanpatronia
 */

@ApplicationScoped
public class TravelAgentRepository
{

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public TravelAgentBooking create(TravelAgentBooking booking)
    {
        em.persist(booking);
        return booking;
    }

    public List<TravelAgentBooking> findAll()
    {
        return em.createQuery("SELECT b FROM TravelAgentBooking b", TravelAgentBooking.class).getResultList();
    }

    public TravelAgentBooking findById(Long id)
    {
        return em.find(TravelAgentBooking.class, id);
    }

    @Transactional
    public void delete(Long id)
    {
        TravelAgentBooking booking = findById(id);
        if (booking != null)
        {
            em.remove(booking);
        }
    }
}
