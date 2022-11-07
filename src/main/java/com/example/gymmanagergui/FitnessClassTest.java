package com.example.gymmanagergui;

//import org.junit.Assert;
import org.junit.jupiter.api.Test;
//import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

class FitnessClassTest {

    @Test
    void checkInForAddingMembers() {

        Location location1 = Location.SOMERVILLE;
        Date date1 = new Date("01/02/2026");
        Member member1 = new Member("Gibre", "Lukas ", date1, date1, location1);
        FitnessClass fitnessClass = new FitnessClass(ClassType.PILATES, "John", Time.MORNING, location1);
        assertNotEquals("Gibre Lukas 01/02/2026 SOMERVILLE", fitnessClass.checkIn(member1, Operation.G));
        assertEquals("Gibre Lukas  (guest) checked into Pilates - John, 09:30, Somerville", fitnessClass.checkIn(member1, Operation.G));
    }

    @Test
    void dropClassMembers() {

        Location location1 = Location.SOMERVILLE;
        Date date1 = new Date("01/02/2026");
        Member member1 = new Member("Gibre", "Lukas ", date1, date1, location1);
        FitnessClass fitnessClass = new FitnessClass(ClassType.PILATES, "John", Time.MORNING, location1);

        assertNotEquals("", fitnessClass.dropClass(member1, Operation.G));
        assertEquals("Gibre Lukas  (guest) is not a participant in Pilates", fitnessClass.dropClass(member1, Operation.G));
    }

}