package uk.ac.newcastle.enterprisemiddleware.customer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * @see Customer
 * @see CustomerRepository
 */

@ApplicationScoped
public class CustomerValidator
{

    @Inject
    Logger log;

    @Inject
    Validator validator;

    public void validateCustomer(Customer customer)
    {
        // Validates the customer using javax.validation
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty())
        {
            for (ConstraintViolation<Customer> violation : violations)
            {
                log.warning("Validation error: " + violation.getMessage());
            }
            throw new IllegalArgumentException("Validation failed for customer: " + customer);
        }
        validatePhoneNumber(customer.getPhoneNumber());
    }

    private void validatePhoneNumber(String phoneNumber)
    {
        // Simple validation to check if the phone number is numeric and in the right length range (10-15 characters)...
        if (!phoneNumber.matches("^\\+?[0-9]{10,15}$"))
        {
            throw new IllegalArgumentException("Phone number must be in a valid format (10-15 digits, optionally starting with '+').");
        }
    }
}
