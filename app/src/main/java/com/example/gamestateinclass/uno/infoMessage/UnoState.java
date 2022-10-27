package com.example.gamestateinclass.uno.infoMessage;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.uno.objects.Card;

import java.io.Serializable;
import java.util.ArrayList;

public class UnoState extends GameState implements Serializable {

    /* this is where we need to initialize the game, that being the deck
    and player hands
     */
    private int turn; //index of player whose turn it is
    private PlayDirection direction;

    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> playedCards;
    private ArrayList<Card> drawDeck;

    // Default Constructor
    public UnoState() {
        // Initialize
        turn = 0;
        direction = PlayDirection.CW;

        drawDeck = generateDeck();
        playerHands = new ArrayList<ArrayList<Card>>();

        for (int i = 0; i < 4; i++) {
            playerHands.add(i, new ArrayList<Card>());
        }

        shuffleDeck(drawDeck);
        initializePlayerHands();
        playedCards = createPlayedCardsDeck(drawDeck);
    }

    // Copy Constructor
    public UnoGameState(UnoGameState previous)
    {
        turn = previous.turn;
        direction = previous.direction;
        drawDeck = new ArrayList<Card>();
        for(Card c : previous.drawDeck)
        {
            Card.Face face = c.getFace();
            Card.Color color = c.getColor();
            drawDeck.add(new Card(color, face));
        }
        playedCards = new ArrayList<Card>();
        for(Card c : previous.playedCards)
        {
            Card.Face face = c.getFace();
            Card.Color color = c.getColor();
            playedCards.add(new Card(color, face));
        }
        playerHands = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            playerHands.add(i, new ArrayList<Card>());
        }

        for(int i=0; i < previous.playerHands.size(); i++)
        {
            ArrayList<Card> newHand = previous.playerHands.get(i);

            for (Card c : newHand)
            {
                Card.Face face = c.getFace();
                Card.Color color = c.getColor();

                playerHands.get(i).add(new Card(color, face));
            }
        }
    }
}
