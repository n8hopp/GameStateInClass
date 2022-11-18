package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

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
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        ArrayList<Card> hand = testState.fetchPlayerHand(0);

        int handSizeBefore = hand.size();
        testGame.drawCard(1);
        int handSizeAfter = hand.size();

        assertEquals(handSizeBefore + 1, handSizeAfter);

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

    @Test
    public void testReverseCard() {
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        int playDirectionBefore = testState.getDirection().value;

        Card reverseCard = new Card(CardColor.BLUE, Face.REVERSE);
        testGame.placeCard(reverseCard);

        int playDirectionAfter = testState.getDirection().value;

        assertEquals(playDirectionAfter, -playDirectionBefore);
    }

    public void testSkipCard() {
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        int turnBefore = testState.getTurn();

        Card skipCard = new Card(CardColor.BLUE, Face.SKIP);
        testGame.placeCard(skipCard);

        int turnAfter = testState.getTurn();

        assertEquals(turnBefore + 1, turnAfter);
    }
}