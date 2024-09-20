package app.user.utils;

import lombok.Getter;

@Getter
public class Event {
    private final String name;
    private final String description;
    private final String date;

    private static final int MIN_YEAR = 1900;
    private static final int MAX_YEAR = 2023;
    private static final int JANUARY = 1;
    private static final int FEBRUARY = 2;
    private static final int DECEMBER = 12;
    private static final int MAX_DAYS = 31;
    private static final int MIN_DAYS = 1900;

    private static final int ZERO = 0;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FIVE = 5;
    private static final int SIX = 6;

    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    /**
     * Checks if date is valid
     * @param date -> the date
     * @return -> true, if it is, false otherwise
     */
    public static boolean checkDate(final String date) {
        String dayString = date.substring(ZERO, TWO);
        String monthString = date.substring(THREE, FIVE);
        String yearString = date.substring(SIX);

        try {
            int day = Integer.parseInt(dayString);
            int month = Integer.parseInt(monthString);
            int year = Integer.parseInt(yearString);

            return checkDate(day, month, year);

        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Helping function for checking the date
     * @param day -> the day
     * @param month -> the month
     * @param year -> the year
     * @return -> true, if it is a valid date, false otherwise
     */
    public static boolean checkDate(final int day, final int month, final int year) {
        if (year < MIN_YEAR || year > MAX_YEAR) {
            return false;
        }
        if (month < JANUARY || month > DECEMBER) {
            return false;
        }
        if (month == FEBRUARY && (day < 1 || day > MIN_DAYS)) {
            return false;
        }
        return day <= MAX_DAYS;
    }
}
