package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * @author AryamanPatronia
 * @see Customer
 * @see javax.persistence.EntityManager
 <p>Repository for handling Customer entity operations.</p>
 * <p>This is responsible for interacting with the database to perform CRUD operations for customers.</p>
 */
@ApplicationScoped
public class CustomerRepository
{
    @PersistenceContext
    EntityManager em;

    /**
     * <p>Finds all customers, ordered by their full name (first name and last name).</p>
     *
     * @return A list of customers, ordered by first name and last name.
     */
    public List<Customer> findAllOrderedByName()
    {
        String jpql = "SELECT c FROM Customer c ORDER BY c.firstName, c.lastName";
        TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
        return query.getResultList();
    }
    /**
     * <p>Finds a customer by email.</p>
     *
     * @param email The email of the customer.
     * @return The customer with the specified email, or null if not found.
     */
    public Customer findByEmail(String email)
    {
        try
        {
            String jpql = "SELECT c FROM Customer c WHERE c.email = :email";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e)
        {
            return null;
        }
    }

    /**
     * <p>Finds a customer by their phone number.</p>
     *
     * @param phoneNumber The phone number of the customer.
     * @return The customer with the specified phone number, or null if not found.
     */
    public Customer findByPhoneNumber(String phoneNumber)
    {
        try {
            String jpql = "SELECT c FROM Customer c WHERE c.phoneNumber = :phoneNumber";
            TypedQuery<Customer> query = em.createQuery(jpql, Customer.class);
            query.setParameter("phoneNumber", phoneNumber);
            return query.getSingleResult();
        } catch (Exception e)
        {
            return null;
        }
    }
    /**
     * <p>Finds a customer by their ID.</p>
     *
     * @param id The ID of the customer.
     * @return The customer with the specified ID, or null if not found.
     */
    public Customer findById(long id) {
        return em.find(Customer.class, id);
    }
    /**
     * <p>Creates a new customer in the database.</p>
     *
     * @param customer The customer to create.
     */
    @Transactional
    public void create(Customer customer)
    {
        em.persist(customer);
    }
    /**
     * <p>Updates an existing customer in the database.</p>
     *
     * @param customer The customer to update.
     */
    @Transactional
    public void update(Customer customer)
    {
        em.merge(customer);
    }

    /**
     * <p>This method deletes a customer from the database.</p>
     *
     */
    @Transactional
    public void deleteById(long id)
    {
        Customer customer = em.find(Customer.class, id);
        if (customer != null) {
            em.remove(customer);
        } else {
            throw new RestServiceException("The customer with the given ID doesn't exist" +
                    ": " + id, Response.Status.NOT_FOUND);
        }
    }
}
