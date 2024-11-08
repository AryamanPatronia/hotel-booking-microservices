package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>This Service class handles business logic for the Customer entity.</p>
 *
 * <p>It performs operations like validation, CRUD actions, and logging. It calls the CustomerRepository to persist
 * and retrieve Customer objects from the database.</p>
 *
 * @author AryamanPatronia
 * @see CustomerValidator
 * @see CustomerRepository
 */
@Dependent
public class CustomerService
{

    @Inject
    @Named("logger")
    Logger log;

    @Inject
    CustomerValidator validator;

    @Inject
    CustomerRepository crud;

    /**
     * <p>Returns a list of all persisted {@link Customer} objects, sorted alphabetically by customer name.</p>
     *
     * @return List of Customer objects
     */
    public List<Customer> findAllOrderedByName()
    {
        return crud.findAllOrderedByName();
    }

    /**
     * <p>Returns a single Customer object, specified by a Long customerID.</p>
     *
     * @param customerID The ID of the Customer to be returned
     * @return The Customer with the specified ID
     */
    public Customer findById(Long customerID)
    {
        return crud.findById(customerID);
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
        return crud.findByEmail(customerEmail);
    }
    /**
     * <p>Creates a new Customer object in the application database.</p>
     *
     * <p>Validates the data in the provided Customer object using {@link CustomerValidator}.</p>
     *
     * @param customer The Customer object to be created
     * @return The Customer object that was successfully created
     * @throws Exception if there is any error during the process
     */
    public Customer create(Customer customer) throws Exception
    {
        log.info("CustomerService.create() - Creating " + customer.getCustomerName());

        // Validate the customer before creating it
        validator.validateCustomer(customer);

        // Create the customer in the database
        return crud.create(customer);
    }
    /**
     * <p>Updates an existing Customer object in the application database.</p>
     *
     * <p>Validates the data in the provided Customer object using {@link CustomerValidator}.</p>
     *
     * @param customer The Customer object to be updated
     * @return The updated Customer object
     * @throws Exception if there is any error during the process
     */
    public Customer update(Customer customer) throws Exception
    {
        log.info("CustomerService.update() - Updating " + customer.getCustomerName());

        // Validate the customer before updating it
        validator.validateCustomer(customer);

        // Update the customer in the database
        return crud.update(customer);
    }
    /**
     * <p>Deletes the provided Customer object from the application database.</p>
     *
     * @param customer The Customer object to be deleted
     * @return The Customer object that was successfully deleted; or null
     * @throws Exception if there is any error during the process
     */
    public Customer delete(Customer customer) throws Exception
    {
        log.info("CustomerService.delete() - Deleting " + customer.getCustomerName());

        Customer deletedCustomer = null;

        if (customer.getCustomerID() != null)
        {
            deletedCustomer = crud.delete(customer);
        } else
        {
            log.info("CustomerService.delete() - No ID found, can't delete.");
        }

        return deletedCustomer;
    }
}
