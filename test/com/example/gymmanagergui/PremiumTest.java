package com.example.gymmanagergui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PremiumTest {

    @Test
    void memberShipFee() {
        Date date1 = new Date("01/02/2026");
        Location location1 = Location.BRIDGEWATER;
        Premium guest1 = new Premium("Kaleb", "Yonas", date1, location1);
        assertEquals("659.89", guest1.MemberShipFee());
    }
    @Test
    void memberShipFeeNotEqual() {
        Date date1 = new Date("01/02/2026");
        Location location1 = Location.BRIDGEWATER;
        Premium guest1 = new Premium("Kaleb", "Yonas", date1, location1);
        assertNotEquals("650.89", guest1.MemberShipFee());
    }

}