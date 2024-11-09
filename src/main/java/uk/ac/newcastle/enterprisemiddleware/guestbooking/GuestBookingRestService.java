package uk.ac.newcastle.enterprisemiddleware.guestbooking;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import uk.ac.newcastle.enterprisemiddleware.booking.Booking;
import uk.ac.newcastle.enterprisemiddleware.booking.BookingService;
import uk.ac.newcastle.enterprisemiddleware.customer.Customer;
import uk.ac.newcastle.enterprisemiddleware.customer.CustomerService;
import uk.ac.newcastle.enterprisemiddleware.hotel.Hotel;
import uk.ac.newcastle.enterprisemiddleware.hotel.HotelService;

import javax.inject.Inject;
import javax.transaction.UserTransaction;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

/**
 * @author AryamanPatronia
 * <p>This REST service handles the creation of a new customer and booking in a single transaction.</p>
 * <p>It uses the {@link CustomerService} and {@link BookingService} to create and persist customer and booking data.</p>
 */
@Path("/guest-booking")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "4. Guest Booking Rest Service", description = "Creates booking and customer in a single transaction...")
public class GuestBookingRestService
{

    @Inject
    Logger log;

    @Inject
    CustomerService customerService;

    @Inject
    BookingService bookingService;

    @Inject
    UserTransaction userTransaction;  // JTA for manual transaction demarcation

    @Inject
    HotelService hotelService;


    /**
     * <p>This method handles the creation of a new Customer and Booking within a single transaction.</p>
     *
     * @param guestBooking The GuestBooking object that contains both customer and booking details
     * @return Response containing the created Booking and status code 201 if successful
     */
    @POST
    @Operation(summary = "Perform a guest booking...", description = "Creates a booking and a customer for the specified hotel...")
    public Response createGuestBooking(GuestBooking guestBooking)
    {
        try
        {
            log.info("Creating guest booking for customer: " + guestBooking.getCustomer().getCustomerName());

            // Start transaction manually
            userTransaction.begin();

            // Create or fetch a managed Customer entity
            Customer customer = customerService.create(guestBooking.getCustomer());

            // Fetch the Hotel by ID (Ensure it is managed and valid)
            Hotel hotel = hotelService.findById(guestBooking.getBooking().getHotel().getId());
            if (hotel == null)
            {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Hotel not found with ID: " + guestBooking.getBooking().getHotel().getId())
                        .build();
            }

            // Set the customer and hotel in the booking (hotel data will not be modified)
            Booking booking = guestBooking.getBooking();
            booking.setCustomer(customer); // Managed customer entity
            booking.setHotel(hotel); // Managed hotel entity (data comes from the database)

            // Persist the booking (make sure Booking is a managed entity)
            Booking createdBooking = bookingService.create(booking);

            // Commit the transaction
            userTransaction.commit();

            return Response.status(Response.Status.CREATED).entity(createdBooking).build();

        }
        catch (Exception e)
        {
            log.severe("Error while creating guest booking: " + e.getMessage());

            // Roll back transaction in case of error
            try
            {
                userTransaction.rollback();
            }
            catch (Exception rollbackEx)
            {
                log.severe("Failed to rollback transaction: " + rollbackEx.getMessage());
            }

            // Return error response
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error while creating guest booking: " + e.getMessage())
                    .build();
        }
    }









}
