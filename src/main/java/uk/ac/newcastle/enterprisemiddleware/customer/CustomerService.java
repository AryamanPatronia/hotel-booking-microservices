package uk.ac.newcastle.enterprisemiddleware.customer;

import uk.ac.newcastle.enterprisemiddleware.util.RestServiceException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * <p>This is the CustomerService class for handling customer-related business logic...</p>
 */
/**
 * @author AryamanPatronia
 * @see CustomerRepository
 * @see CustomerValidator
 */
@ApplicationScoped
public class CustomerService
{

    @Inject
    CustomerRepository repository;

    @Inject
    CustomerValidator validator;

    /**
     * <p>Finds all customers ordered by their full name (first and last name).</p>
     *
     * @return A list of all customers, sorted by first name and last name.
     */
    public List<Customer> findAllOrderedByName()
    {
        return repository.findAllOrderedByName();
    }
    /**
     * <p>Finds a customer by their email.</p>
     *
     * @param email The email of the customer.
     * @return The customer with the specified email.
     */
    public Customer findByEmail(String email)
    {
        Customer customer = repository.findByEmail(email);
        if (customer == null)
        {
            throw new RestServiceException("A customer with this email doesn't exist: " + email, Response.Status.NOT_FOUND);
        }
        return customer;
    }

    /**
     * <p>Finds a customer by their ID.</p>
     *
     * @param id The ID of the customer.
     * @return The customer with the specified ID.
     * @throws RestServiceException If no customer is found with the given ID.
     */
    public Customer findById(long id)
    {
        Customer customer = repository.findById(id);
        if (customer == null)
        {
            throw new RestServiceException("There is no customer with the given ID: " + id, Response.Status.NOT_FOUND);
        }
        return customer;
    }
    /**
     * <p>Creates a new customer.</p>
     *
     * @param customer The customer to create.
     * @throws UniqueEmailException If the email of the customer is already in use.
     * @throws UniquePhoneNumberException If the phone number is already in use.
     */
    @Transactional
    public void create(Customer customer)
    {
        // Validate customer data...
        validator.validateCustomer(customer);

        // Check if the email is already used by another customer...
        if (repository.findByEmail(customer.getEmail()) != null)
        {
            throw new UniqueEmailException("The email " + customer.getEmail() + " is already used by another customer.");
        }

        // Check if the phone number is already used by another customer...
        if (repository.findByPhoneNumber(customer.getPhoneNumber()) != null)
        {
            throw new UniquePhoneNumberException("The phone number " + customer.getPhoneNumber() + " is already used by another customer.");
        }

        // Save the customer to the database...
        repository.create(customer);
    }
    /**
     * <p>Updates an existing customer.</p>
     *
     * @param customer The customer to update.
     * @throws RestServiceException If no customer is found with the given ID.
     * @throws UniqueEmailException If the email of the customer is already in use.
     * @throws UniquePhoneNumberException If the phone number is already in use.
     */
    @Transactional
    public void update(Customer customer)
    {
        // Validate customer data
        validator.validateCustomer(customer);

        // Check if the customer exists and if not, throw an exception...
        Customer existingCustomer = repository.findById(customer.getId());
        if (existingCustomer == null)
        {
            throw new RestServiceException("The customer with the given ID doesn't exist...: " + customer.getId(), Response.Status.NOT_FOUND);
        }

        // Check if the email is already used by another customer (unless it’s the same customer)
        if (!existingCustomer.getEmail().equals(customer.getEmail()) && repository.findByEmail(customer.getEmail()) != null)
        {
            throw new UniqueEmailException("The email " + customer.getEmail() + " is already used by another customer.");
        }

        // Check if the phone number is already used by another customer (unless it’s the same customer)
        if (!existingCustomer.getPhoneNumber().equals(customer.getPhoneNumber()) && repository.findByPhoneNumber(customer.getPhoneNumber()) != null)
        {
            throw new UniquePhoneNumberException("The phone number " + customer.getPhoneNumber() + " is already used by another customer.");
        }

        // Update the customer in the database...
        repository.update(customer);
    }
    /**
     * <p>Deletes a customer by their ID.</p>
     *
     * @param id The ID of the customer to delete.
     * @throws RestServiceException If no customer is found with the given ID.
     */
    @Transactional
    public void delete(long id)
    {
        // Find the customer by ID
        Customer customer = repository.findById(id);
        if (customer == null)
        {
            throw new RestServiceException("Customer with the given ID not found: " + id, Response.Status.NOT_FOUND);
        }

        // Delete the customer from the database using the ID
        repository.deleteById(id);
    }
}
