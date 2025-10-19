/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package grp.twentytwo.equipmentmanager;

import grp.twentytwo.equipmentmanager.InvalidBookingRangeException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mymys
 */
public class BookingTest {

    private static Booking referenceBooking;
    private static LocalDateTime referenceDateBooked;
    private static LocalDateTime referenceDateReturned;

    public BookingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        referenceDateBooked = LocalDateTime.of(2025, 10, 5, 8, 0); // 5/10/2025 8:00am 
        referenceDateReturned = LocalDateTime.of(2025, 10, 5, 8, 30); // 5/10/2025 8:30am
        try {
            referenceBooking = new Booking("Test", "Test", referenceDateBooked, referenceDateReturned); // Year month dayofmonth hour minute
        } catch (InvalidBookingRangeException ex) {
            Logger.getLogger(BookingTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test // Date ranges are identical
    public void testOverlaps_bookingIdenticalRange() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked, referenceDateReturned);
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(true, result);
    }

    @Test // Date range is completely after the reference range
    public void testOverlaps_bookingAfterReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked.plusDays(1), referenceDateReturned.plusDays(1));
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(false, result);

    }

    @Test // Date range is completely before the reference range
    public void testOverlaps_bookingBeforeReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked.minusDays(1), referenceDateReturned.minusDays(1));
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(false, result);

    }

    @Test // Date range starts exactly on the end of the reference range
    public void testOverlaps_bookingFromExactReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateReturned, referenceDateReturned.plusMonths(1));
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(false, result); // Bookings starting on the exact return time are allowed
    }

    @Test // Date range is before and up to the exact reference range
    public void testOverlaps_bookingUpToExactReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked.minusHours(1), referenceDateBooked);
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(false, result); // Bookings ending on the exact start time are allowed
    }

    @Test // Date range spans fully over the reference range
    public void testOverlaps_bookingFullyOverReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked.minusDays(1), referenceDateReturned.plusDays(1));
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(true, result);
    }

    @Test // Date range is within the reference range
    public void testOverlaps_bookingFullyWithinReference() throws InvalidBookingRangeException { // If exception thrown test will fail
        Booking booking1 = new Booking("Test", "Test", referenceDateBooked.plusMinutes(5), referenceDateReturned.minusMinutes(5));
        boolean result = booking1.overlaps(referenceBooking);
        assertEquals(true, result);
    }

    @Test(expected = InvalidBookingRangeException.class) // Expect exception. // Return date is before the booked date
    public void testBooking_bookingRangeInvalid() throws InvalidBookingRangeException {
        Booking booking1 = new Booking("Test", "Test", referenceDateReturned, referenceDateBooked);
    }

    @Test(expected = NullPointerException.class) // Expect exception. // Booking dates are null
    public void testBooking_nullBookingRangeInvalid() throws InvalidBookingRangeException {
        Booking booking1 = new Booking("Test", "Test", referenceDateReturned, null);
    }

}
