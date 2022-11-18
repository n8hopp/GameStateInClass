package com.example.gamestateinclass.uno.infoMessage;

import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

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
        UnoState testState = new UnoState();

        int currentPlayer = testState.fetchCurrentPlayer();

        assertEquals(0, currentPlayer);
    }

    public void testGetTopCard() {
        UnoState testState = new UnoState();
        Card c = testState.getTopCard();
        Card expected = new Card(CardColor.RED, Face.REVERSE);
        assertEquals(c.getCardColor(), CardColor.RED);
        assertEquals(c.getCardColor(), Face.REVERSE);
    }

    public void testGetDirection() {
        UnoState testState = new UnoState();

        UnoState.PlayDirection direction = testState.getDirection();

        assertEquals(UnoState.PlayDirection.CW, direction);
    }
}