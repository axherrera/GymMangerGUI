package com.example.gymmanagergui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GymManagerMainTest {

    @Test
    void start() {
        GymManagerMain gymManagerMain = new GymManagerMain();
        String g = gymManagerMain.toString();
        assertEquals("hello", g);
        return;
    }

    @Test
    void main() {
    }
}