package com.example.gamestateinclass.uno.infoMessage;

import com.example.gamestateinclass.uno.objects.Card;

import junit.framework.TestCase;

import java.util.ArrayList;

public class UnoStateTest extends TestCase {

    public void testInitializePlayerHands() {
        UnoState testState = new UnoState();

        for (ArrayList<Card> hands : testState.getAllPlayerHands()) {
            assertEquals(7, hands.size());
        }
    }

    public void testFetchCurrentPlayer() {
    }

    public void testGetTopCard() {
    }

    public void testGetDirection() {
    }
}