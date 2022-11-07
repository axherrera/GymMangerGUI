package com.example.gymmanagergui;

import java.util.Calendar;

/**
 * Date class used to check member dob and membership expiration dates, it implements the Comparable interface
 * @author ALEJANDRO HERRERA-PINEDA, HURUY BELAY
 */
public class Date implements Comparable<Date> {
    public static final int MAX_YEAR = 9999;
    public static final int MIN_YEAR = 1800;
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    private int year;
    private int month;
    private int day;

    /**
     * Initializes a newly created Date object
     *
     * @param date a user input string that represents a date
     */
    public Date(String date) {
        String[] string = date.split("/");
        if (string.length != 3) {
            string = date.split("-");
            if(string.length != 3)
                throw new IllegalArgumentException("Invalid date");
            month = Integer.parseInt(string[1]);
            day = Integer.parseInt(string[2]);
            year = Integer.parseInt(string[0]);
        }
        else{
            month = Integer.parseInt(string[0]);
            day = Integer.parseInt(string[1]);
            year = Integer.parseInt(string[2]);
        }
    }


    /**
     * It overrides the CompareTo class
     *
     * @param date date object to be compared
     * @return returns negative number if compared date is after, 0 if date is the same, and positive number if compared date is before
     */
    @Override
    public int compareTo(Date date) {
        int yearDiff = this.year - date.year;
        if (yearDiff != 0) {
            return yearDiff;
        }
        int monthDiff = this.month - date.month;
        if (monthDiff != 0) {
            return monthDiff;
        }
        return this.day - date.day;
    }

    /**
     * Checks if a date is leap year or not
     *
     * @return if date falls on a leap year return true, otherwise return false
     */
    boolean isLeapYear() {
        return (year % QUADRENNIAL == 0) && (year % CENTENNIAL != 0) || (year % QUATERCENTENNIAL == 0);
    }

    /**
     * Checks if a date is a valid calendar date or not
     *
     * @return If date is valid calendar date return true, otherwise return false
     */
    public boolean isValid() {
        if (year > MAX_YEAR || year < MIN_YEAR) {
            return false;
        }
        if (month < 1 || month > 12) {
            return false;
        }
        if (day < 1 || day > 31) {
            return false;
        }
        if (month == 2) {
            if (isLeapYear()) {
                return day <= 29;
            } else {
                return day <= 28;
            }
        }
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return day <= 30;
        }
        return true;
    }

    /**
     * Method checks if the given date makes a member 18 years old
     *
     * @return if date makes member 18 years old, returns true, otherwise returns false
     */
    public boolean ofAge() {
        Calendar cd = Calendar.getInstance();
        int yrs = cd.get(Calendar.YEAR) - this.year;
        int mon = cd.get(Calendar.MONTH) + 1 - this.month;
        int day = cd.get(Calendar.DAY_OF_MONTH) - this.day;
        return (yrs > 18) ^ (yrs == 18 && ((mon == 0 && day >= 0) ^ (mon > 0)));
    }

    /**
     * Checks if a date is a future date
     *
     * @return if date is a future date, return true, otherwise return false
     */
    public boolean isFuture() {
        Calendar cd = Calendar.getInstance();
        int yrs = cd.get(Calendar.YEAR) - this.year;
        int mon = cd.get(Calendar.MONTH) + 1 - this.month;
        int day = cd.get(Calendar.DAY_OF_MONTH) - this.day;
        return (yrs < 0 ^ (yrs == 0 && (mon == 0 && day <= 0) ^ (mon < 0)));
    }

    /**
     * It overrides the toString clas
     *
     * @return year, month and day in string format
     */
    @Override
    public String toString() {
        return this.month + "/" + this.day + "/" + this.year;
    }

    /**
     * It overrides the equals method
     *
     * @param obj object of the class
     * @return true if date is the same and false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        Date d = (Date) obj;
        return (this.month == d.month) && (this.day == d.day) && (this.year == d.year);
    }

    /**
     *
     * @return returns an expiration date that is 3 months after the current date
     */
    public static Date genExpDate(){
        Calendar cd = Calendar.getInstance();
        int day = cd.get(Calendar.DATE);
        int month = ((cd.get(Calendar.MONTH)+3)%11);
        int year = month<cd.get(Calendar.MONTH)?
                cd.get(Calendar.YEAR)+1:
                cd.get(Calendar.YEAR);
        return new Date(String.format("%d/%d/%d", month, day, year));
    }


    public static void main(String[] args) {

            Date date = new Date("01/02/1990");
            Date date1 = new Date("01/02/2022");

            System.out.println(date.toString());
            System.out.println(date.isValid());
            System.out.println(date.equals(date1));
            System.out.println(date.isFuture());
            System.out.println(date.compareTo(date1));
            System.out.println(date.isLeapYear());
            System.out.println(date1.ofAge());

            System.out.println(genExpDate());
    }
}