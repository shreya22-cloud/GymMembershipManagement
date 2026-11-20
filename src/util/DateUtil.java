package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class providing helper methods used across the application.
 * All methods are static; this class is not meant to be instantiated.
 */
public final class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /** Private constructor to prevent instantiation. */
    private DateUtil() {}

    /**
     * Returns today's date as a formatted string (yyyy-MM-dd).
     *
     * @return Today's date string.
     */
    public static String today() {
        return LocalDate.now().format(FORMATTER);
    }

    /**
     * Adds a given number of months to a date string.
     *
     * @param dateStr The base date in yyyy-MM-dd format.
     * @param months  Number of months to add.
     * @return The resulting date string.
     */
    public static String addMonths(String dateStr, int months) {
        LocalDate date = LocalDate.parse(dateStr, FORMATTER);
        return date.plusMonths(months).format(FORMATTER);
    }

    /**
     * Validates that a date string is in yyyy-MM-dd format.
     *
     * @param dateStr The date string to validate.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidDate(String dateStr) {
        try {
            LocalDate.parse(dateStr, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates a 10-digit phone number (digits only).
     *
     * @param phone The phone number string.
     * @return true if valid, false otherwise.
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }
}
