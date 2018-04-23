package util;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

public class DateUtil {

	/** The date pattern that is used for conversion. Change as you wish. */
	private static final String DATE_PATTERN = "dd.MM.yyyy";

	/** The date formatter. */
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	/**
	 * Returns the given date as a well formatted String. The above defined
	 * {@link DateUtil#DATE_PATTERN} is used.
	 * 
	 * @param date
	 *            the date to be returned as a string
	 * @return formatted string
	 */
	public static String format(LocalDate date) {
		if (date == null) {
			return null;
		}
		return DATE_FORMATTER.format(date);
	}

	/**
	 * Converts a String in the format of the defined {@link DateUtil#DATE_PATTERN}
	 * to a {@link LocalDate} object.
	 * 
	 * Returns null if the String could not be converted.
	 * 
	 * @param dateString
	 *            the date as String
	 * @return the date object or null if it could not be converted
	 */
	public static LocalDate parse(String dateString) {
		try {
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	
	public static LocalDate parse(Date date) {
		return date.toLocalDate();
	}

	/**
	 * Checks the String whether it is a valid date.
	 * 
	 * @param dateString
	 * @return true if the String is a valid date
	 */
	public static boolean validDate(String dateString) {
		// Try to parse the String.
		return DateUtil.parse(dateString) != null;
	}

	public static String getYear() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		return String.format("%04d", year);
	}

	public static String getMonth() {
		int month = Calendar.getInstance().get(Calendar.MONTH);
		month++;
		String monthString;
		switch (month) {
		case 1:
			monthString = "Enero";
			break;
		case 2:
			monthString = "Febrero";
			break;
		case 3:
			monthString = "Marzo";
			break;
		case 4:
			monthString = "Abril";
			break;
		case 5:
			monthString = "Mayo";
			break;
		case 6:
			monthString = "Junio";
			break;
		case 7:
			monthString = "Julio";
			break;
		case 8:
			monthString = "Agosto";
			break;
		case 9:
			monthString = "Septiembre";
			break;
		case 10:
			monthString = "Octubre";
			break;
		case 11:
			monthString = "Noviembre";
			break;
		case 12:
			monthString = "Diciembre";
			break;
		default:
			monthString = "Error";
			break;
		}
		return monthString;
	}
}
