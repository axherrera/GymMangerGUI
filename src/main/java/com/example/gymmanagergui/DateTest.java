package com.example.gymmanagergui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DateTest {

    @Test
    void monthShouldNotLessThanOneOrGreaterThanTwelve(){
        Date date = new Date("14/03/2020");
        assertFalse(date.isValid());
    }
    @Test
    void monthShouldGreaterThanOrEqualToOneOrLessThanOrEqualToTwelve(){
        Date date = new Date("4/03/2020");
        assertTrue(date.isValid());
    }

    @Test
    void dayShouldNotLessThanOneOrGreaterThanThirtyOne(){
        Date date = new Date("25/12/2022");
        assertNotEquals(true, date.isValid());
    }

    @Test
    void checkIfTheYearIsLeapYear(){
        Date date = new Date("02/29/2016");
        assertEquals(true, date.isValid());
    }

    @Test
    void checkIfTheYearIsNotLeapYear(){
        Date date = new Date("02/29/2011");
        assertNotEquals(true, date.isValid());
    }
}