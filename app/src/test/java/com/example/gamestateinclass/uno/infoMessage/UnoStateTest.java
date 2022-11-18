package com.example.gamestateinclass.uno.infoMessage;

import junit.framework.TestCase;

public class UnoStateTest extends TestCase {

    public void testCheckVictory() {
        UnoState testState = new UnoState();
    }

    public void testFetchCurrentPlayer() {
        UnoState testState = new UnoState();

        int currentPlayer = testState.fetchCurrentPlayer();

        assertEquals(0, currentPlayer);
    }

    public void testGetTopCard() {
    }

    public void testGetDirection() {
        UnoState testState = new UnoState();

        UnoState.PlayDirection direction = testState.getDirection();

        assertEquals(UnoState.PlayDirection.CW, direction);
    }
}