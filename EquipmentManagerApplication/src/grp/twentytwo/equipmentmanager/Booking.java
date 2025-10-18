package grp.twentytwo.equipmentmanager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Stores booking data.
 *
 * @author fmw5088
 */
public class Booking {

    private final String id;
    private String userID;
    private String itemID;
    private LocalDateTime bookedDate;
    private LocalDateTime returnDate;

    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    /**
     * Creates a new booking with the specified details. Package private - only
     * for official bookings with an assigned ID
     *
     * @param id the bookings ID
     * @param userID the ID of the user who booked
     * @param bookedDate the date of the booking
     * @param returnDate the return date of the booking
     */
    Booking(String id, String userID, String itemID, LocalDateTime bookedDate, LocalDateTime returnDate) {
        this.id = id;
        this.userID = userID;
        this.itemID = itemID;
        this.bookedDate = bookedDate;
        this.returnDate = returnDate;
    }

    /**
     * Public booking constructor (no ID will be assigned)
     *
     * @param userID
     * @param itemID
     * @param bookedDate
     * @param returnDate
     */
    public Booking(String userID, String itemID, LocalDateTime bookedDate, LocalDateTime returnDate) {
        this(null, userID, itemID, bookedDate, returnDate);
    }

    /**
     *
     * @return The bookings ID
     */
    public String getID() {
        return id;
    }

    /**
     *
     * @return The ID of the user who booked.
     */
    public String getUserID() {
        return userID;
    }

    /**
     *
     * @param userID The User ID in Question
     * @return True if the user owns the booking.
     */
    public boolean isOwnedBy(String userID) {
        return this.userID.equals(userID);
    }

    /**
     *
     * @return The ID of the item being booked.
     */
    public String getItemID() {
        return itemID;
    }

    /**
     *
     * @return The date of the booking
     */
    public LocalDateTime getBookedDate() {
        return bookedDate;
    }

    /**
     *
     * @return The return date of the booking.
     */
    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public boolean overlaps(Booking other) {
        return !(returnDate.isBefore(other.bookedDate) || bookedDate.isAfter(other.returnDate));
    }

    /**
     *
     * @param date
     * @return true if a given date is outside the booked date range (inclusive
     * of booking start/end days)
     */
    public boolean isOutsideBookedDate(LocalDateTime date) {
        return (date.isBefore(this.bookedDate.plusDays(1)) || date.isEqual(this.bookedDate) || date.isEqual(this.returnDate) || date.isAfter(this.returnDate));
    }

    /**
     *
     * @param dateTime
     * @return true if a given date AND time is outside the booked time and date
     * range
     */
    public boolean isOutsideBookedDateAndTime(LocalDateTime dateTime) {
        return (dateTime.isBefore(this.bookedDate) || dateTime.isEqual(this.bookedDate) || dateTime.isEqual(this.returnDate) || dateTime.isAfter(this.returnDate));
    }

    // Test Code for vetoing DatTimes
//	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
//	ZonedDateTime bookedDate = LocalDateTime.parse("13-10-2025 02:30:00", formatter).atZone(ZoneId.systemDefault());
//	ZonedDateTime returnDate = LocalDateTime.parse("13-11-2025 04:30:00", formatter).atZone(ZoneId.systemDefault());
//	Booking testBooking = new Booking("0", "0", "0", bookedDate, returnDate);
//	DatePickerSettings datePickerSettings = dateTimePicker1.datePicker.getSettings();
//	datePickerSettings.setVetoPolicy((LocalDate localDate) -> {
//	    ZonedDateTime zonedDate = localDate.atStartOfDay(ZoneId.systemDefault());
//	    return ();
//	});
//	TimePickerSettings timePickerSettings = dateTimePicker1.timePicker.getSettings();
//	timePickerSettings.setVetoPolicy((LocalTime localTime) -> {
//	    ZonedDateTime zonedDate = dateTimePicker1.datePicker.getDate().atTime(localTime).atZone(ZoneId.systemDefault());
//	    return (returnDate.isBefore(zonedDate) || returnDate.isEqual(zonedDate) || bookedDate.isAfter(zonedDate) || bookedDate.isEqual(zonedDate));
//	});
    @Override
    public String toString() {
        return "Booking ID: " + id + ", User ID: " + userID + ", Item ID: " + itemID + ", Booked Date: " + bookedDate.format(formatter) + ", Return Date: " + returnDate.format(formatter) + ".";
    }

}
