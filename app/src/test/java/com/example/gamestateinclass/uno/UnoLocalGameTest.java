package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class UnoLocalGameTest extends TestCase {

    @Test
    public void testCheckCardValidity() {
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();
        // Top card of the seeded discard deck is always red
        ArrayList<Card> Hand = testState.fetchPlayerHand(0);

        assertTrue(testGame.checkCardValidity(Hand.get(0)));
        assertFalse(testGame.checkCardValidity(Hand.get(2)));
    }


    @Test
    public void testDrawCard() {

    }

    @Test
    public void testPlaceCard() {
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();
        // Top card of the seeded discard deck is always red
        ArrayList<Card> Hand = testState.fetchPlayerHand(0);
        Card testCard = Hand.get(0);
        testGame.placeCard(Hand.get(0));
        // Places it's red card on deck
        Card resultCard = testState.getTopCard();
        assertEquals(testCard, resultCard);

    }
}