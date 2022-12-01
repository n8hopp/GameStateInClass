package com.example.gamestateinclass.uno;

import static org.junit.Assert.assertNotEquals;

import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class UnoLocalGameTest extends TestCase {
    // In our seeded randomization, the first card turned over at the beginning
    // is a red reverse. The human player's cards from 0-6 in order are:
    // red skip, red 8, blue 8, green 3, green reverse, blue 2, red +2
    @Test
    public void testCheckCardValidity() { // Lukas
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();
        // This is a red skip
        ArrayList<Card> Hand = testState.fetchPlayerHand(0);
        Card wildTest = new Card(CardColor.BLACK, Face.WILD);
        // checkCardValidity compares card to top of discard deck which
        // is a red reverse
        assertTrue(testGame.checkCardValidity(Hand.get(0)));
        assertFalse(testGame.checkCardValidity(Hand.get(2)));
        assertTrue(testGame.checkCardValidity(Hand.get(4)));
        assertTrue(testGame.checkCardValidity(wildTest));
    }


    @Test
    public void testDrawCard() { // Lukas
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        ArrayList<Card> hand = testState.fetchPlayerHand(0);

        int handSizeBefore = hand.size();
        testGame.drawCard(1);
        int handSizeAfter = hand.size();
        // Verify that the card was draw by hand size
        assertEquals(handSizeBefore + 1, handSizeAfter);
        assertNotEquals(handSizeBefore, handSizeAfter);
        assertEquals(handSizeBefore,7);
        assertNotEquals(handSizeBefore,8);

    }

    @Test
    public void testPlaceCard() { // Lukas
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();
        // Top card of the seeded discard deck is always red
        ArrayList<Card> Hand = testState.fetchPlayerHand(0);
        Card testCard = Hand.get(0);

        // Places it's red card on deck
        testGame.placeCard(Hand.get(0));

        Card resultCard = testState.getTopCard();
        // Ensure the card is no longer in the hand
        for ( Card c : Hand ){
            if ( c == testCard ){
                assert false;
            }
            else assert true;
        }
        // Check that it made it to the top of the discard deck
        assertEquals(testCard, resultCard);

    }

    @Test
    public void testReverseCard() { // Henry
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        int playDirectionBefore = testState.getDirection().value;

        Card reverseCard = new Card(CardColor.BLUE, Face.REVERSE);
        testGame.placeCard(reverseCard);

        int playDirectionAfter = testState.getDirection().value;

        assertEquals(playDirectionAfter, -playDirectionBefore);
        assertNotEquals(playDirectionAfter, playDirectionBefore);
        assertEquals(1, playDirectionBefore);
        assertEquals(-1, playDirectionAfter);
    }

    @Test
    public void testSkipCard() { // Henry
        UnoLocalGame testGame = new UnoLocalGame();
        UnoState testState = (UnoState) testGame.getGameState();

        int turnBefore = testState.getTurn();

        Card skipCard = new Card(CardColor.RED, Face.SKIP);
        testGame.placeCard(skipCard);

        int turnAfter = testState.getTurn();

        assertEquals(0, turnBefore);
        assertEquals(1, turnAfter);
        assertEquals(turnBefore + 1, turnAfter);
        assertNotEquals(turnBefore, turnAfter);
    }


}