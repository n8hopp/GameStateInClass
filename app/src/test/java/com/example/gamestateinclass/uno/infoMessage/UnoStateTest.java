package com.example.gamestateinclass.uno.infoMessage;

import static org.junit.Assert.assertNotEquals;

import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class UnoStateTest extends TestCase {
    // In our seeded randomization, the first card turned over at the beginning
    // is a red reverse. The human player's cards from 0-6 in order are:
    // red skip, red 8, blue 8, green 3, green reverse, blue 2, red +2
    @Test
    public void testInitializePlayerHands() {
        // Each player should start with 7 cards
        UnoState testState = new UnoState();
        for (ArrayList<Card> hands : testState.getAllPlayerHands()) {
            assertNotEquals(0, hands.size());
            assertNotEquals(6,hands.size());
            assertEquals(7, hands.size());
            assertNotEquals(8, hands.size());
        }
    }

    @Test
    public void testFetchCurrentPlayer() {
        // Game should start with player 0
        UnoState testState = new UnoState();
        int currentPlayer = testState.fetchCurrentPlayer();
        assertEquals(0, currentPlayer);
        assertNotEquals(1, currentPlayer);
        assertNotEquals(2, currentPlayer);
        assertNotEquals(3, currentPlayer);
    }

    @Test
    public void testGetTopCard() {
        // Top Card should be a red reverse
        UnoState testState = new UnoState();
        Card c = testState.getTopCard();
        Card expected = new Card(CardColor.RED, Face.REVERSE);
        assertEquals(c.getCardColor(), CardColor.RED);
        assertEquals(c.getFace(), Face.REVERSE);
    }

    @Test
    public void testGetDirection() {
        // Starting direction should be CW
        UnoState testState = new UnoState();
        UnoState.PlayDirection direction = testState.getDirection();
        assertEquals(UnoState.PlayDirection.CW, direction);
        assertNotEquals(UnoState.PlayDirection.CCW, direction);
    }

    @Test
    public void testCopyConstructor() {
        // 2 new tests states should be different
        UnoState testState = new UnoState();
        UnoState newCopiedState = new UnoState(testState);
        assertEquals(testState, newCopiedState);

        testState.setTurn(3);
        assertNotEquals(testState, newCopiedState);


    }

}