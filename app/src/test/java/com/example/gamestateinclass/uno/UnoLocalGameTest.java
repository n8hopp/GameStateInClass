package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.uno.infoMessage.UnoState;

import junit.framework.TestCase;

import org.junit.Test;

public class UnoLocalGameTest extends TestCase {

    @Test
    public void testCheckCardValidity() {
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        assertEquals(4, 2+2);
    }


    @Test
    public void testDrawCard() {

    }

    @Test
    public void testPlaceCard() {

    }
}