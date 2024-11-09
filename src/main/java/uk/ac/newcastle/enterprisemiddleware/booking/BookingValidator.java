package uk.ac.newcastle.enterprisemiddleware.booking;

import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>This class provides methods to check Booking objects against arbitrary requirements.</p>
 * @author AryamanPatronia
 * @see Booking
 * @see BookingRepository
 * @see Validator
 */
@ApplicationScoped
public class BookingValidator
{

    @Inject
    Validator validator;

    @Inject
    BookingRepository bookingRepository;

    /**
     * <p>Validates the given Booking object and throws validation exceptions based on the type of error. If the error is a
     * standard bean validation error, it throws a ConstraintViolationException with the set of violated constraints.</p>
     *
     * <p>If the error is caused because an existing booking with the same customer and hotel for the same check-in date
     * already exists, it throws a ValidationException to handle it separately.</p>
     *
     * @param booking The Booking object to be validated
     * @throws ConstraintViolationException : If Bean Validation errors exist
     * @throws ValidationException         :  If a booking with the same customer and hotel already exists for the given date
     */
    void validateBooking(Booking booking) throws ConstraintViolationException, ValidationException
    {
        Set<ConstraintViolation<Booking>> violations = validator.validate(booking);

        if (!violations.isEmpty())
        {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        if (bookingAlreadyExists(booking.getCustomer(), booking.getHotel(), booking.getCheckinDate(), booking.getBookingDurationDays()))
        {
            throw new ValidationException("Booking with the same customer, hotel, and check-in date already exists.");
        }
    }

    /**
     * <p>Checks if a booking with the same customer, hotel, and check-in date already exists in the database...</p>
     *
     * <p>This ensures that a customer cannot book the same hotel for overlapping check-in dates.</p>
     *
     * @param customer           The customer to check for an existing booking
     * @param hotel              The hotel to check for an existing booking
     * @param checkinDate        The check-in date to check for an existing booking
     * @param bookingDurationDays The duration of the booking to check for an existing booking
     * @return boolean representing whether a booking with the same customer, hotel, and check-in date exists
     */
    boolean bookingAlreadyExists(Customer customer, Hotel hotel, LocalDate checkinDate, Integer bookingDurationDays)
    {
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking existingBooking : bookings)
        {
            if (existingBooking.getCustomer().equals(customer) &&
                    existingBooking.getHotel().equals(hotel) &&
                    checkinDate.equals(existingBooking.getCheckinDate()))
            {
                return true;
            }
        }

        return false;
    }
}
