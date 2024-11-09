package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This is a Repository class and connects the Service/Control layer (see {@link CustomerService}) with the
 * Domain/Entity Object (see {@link Customer}).</p>
 *
 * <p>There are no access modifiers on the methods making them 'package' scope. They should only be accessed by a
 * Service/Control object.</p>
 *
 * @author AryamanPatronia
 * @see Customer
 * @see javax.persistence.EntityManager
 */
@RequestScoped
public class CustomerRepository
{

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    EntityManager em;



    /**
     * <p>Returns a List of all persisted {@link Customer} objects, sorted alphabetically by customer name.</p>
     *
     * @return List of Customer objects
     */
    public List<Customer> findAllOrderedByName()
    {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_ALL, Customer.class);
        return query.getResultList();
    }

    /**
     * <p>Returns a single Customer object, specified by a Long customerID.</p>
     *
     * @param customerID The ID of the Customer to be returned
     * @return The Customer with the specified ID
     */
    public Customer findById(Long customerID)
    {
        return em.find(Customer.class, customerID);
    }



    /**
     * <p>Returns a single Customer object, specified by a String customerEmail.</p>
     *
     * <p>If there is more than one Customer with the specified email, only the first encountered will be returned.</p>
     *
     * @param customerEmail The email of the Customer to be returned
     * @return The first Customer with the specified email
     */
    public Customer findByEmail(String customerEmail)
    {
        TypedQuery<Customer> query = em.createNamedQuery(Customer.FIND_BY_EMAIL, Customer.class)
                .setParameter("email", customerEmail);
        return query.getSingleResult();
    }

    /**
     * Returns the customer object specified by the phone number...
     * @param phoneNumber
     */

    public Customer findByPhoneNumber(String phoneNumber)
    {
        try
        {
            return em.createQuery("SELECT c FROM Customer c WHERE c.customerPhoneNumber = :phoneNumber", Customer.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
        }
        catch (NoResultException e)
        {
            return null; // No customer with that phone number
        }
    }



    /**
     * <p>Persists the provided Customer object to the application database using the EntityManager.</p>
     *
     * <p>{@link javax.persistence.EntityManager#persist(Object) persist(Object)} takes an entity instance, adds it to the
     * context and makes that instance managed (i.e., future updates to the entity will be tracked).</p>
     *
     * @param customer The Customer object to be persisted
     * @return The Customer object that has been persisted
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Customer create(Customer customer) throws Exception
    {
        log.info("CustomerRepository.create() - Creating " + customer.getCustomerName());

        // Persist the customer to the database
        em.persist(customer);

        return customer;
    }

    /**
     * <p>Updates an existing Customer object in the application database with the provided Customer object.</p>
     *
     * <p>{@link javax.persistence.EntityManager#merge(Object) merge(Object)} creates a new instance of your entity,
     * copies the state from the supplied entity, and makes the new copy managed. The instance you pass in will not be
     * managed (any changes you make will not be part of the transaction - unless you call merge again).</p>
     *
     * @param customer The Customer object to be merged with an existing Customer
     * @return The Customer that has been merged
     * @throws ConstraintViolationException, ValidationException, Exception
     */
    public Customer update(Customer customer) throws Exception
    {
        log.info("CustomerRepository.update() - Updating " + customer.getCustomerName());

        // Either update the customer or add it if it can't be found
        em.merge(customer);

        return customer;
    }

    /**
     * <p>Deletes the provided Customer object from the application database if found there.</p>
     *
     * @param customer The Customer object to be removed from the application database
     * @return The Customer object that has been successfully removed from the application database; or null
     */
    public Customer delete(Customer customer) throws Exception
    {
        log.info("CustomerRepository.delete() - Deleting " + customer.getCustomerName());

        if (customer.getCustomerID() != null)
        {
            em.remove(em.merge(customer));
        } else
        {
            log.info("CustomerRepository.delete() - No ID was found so can't Delete.");
        }

        return customer;
    }





}
