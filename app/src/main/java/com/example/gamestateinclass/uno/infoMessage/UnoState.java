package com.example.gamestateinclass.uno.infoMessage;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UnoState extends GameState implements Serializable {
    // in TTTState, there is:
    // the 2 constructors
    // getPiece and setPiece
    // getWhoseMove and setWhoseMove

    // In our UNOState then there should be:
    // 2 constructors
    // getTopCard and addTopCard
    // getWhoseMove and setWhoseMove
    // TAG for logging
    private static final String TAG = "UnoState";
    private int turn; //index of player whose turn it is
    private PlayDirection direction;

    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> discardDeck;
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
        discardDeck = creatediscardDeckDeck(drawDeck);
    }

    // Copy Constructor
    public UnoState(UnoState previous)
    {
        turn = previous.turn;
        direction = previous.direction;
        drawDeck = new ArrayList<Card>();
        for(Card c : previous.drawDeck)
        {
            Face face = c.getFace();
            CardColor color = c.getCardColor();
            drawDeck.add(new Card(color, face));
        }
        discardDeck = new ArrayList<Card>();
        for(Card c : previous.discardDeck)
        {
            Face face = c.getFace();
            CardColor color = c.getCardColor();
            discardDeck.add(new Card(color, face));
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
                Face face = c.getFace();
                CardColor color = c.getCardColor();

                playerHands.get(i).add(new Card(color, face));
            }
        }
    }

    private void shuffleDeck(ArrayList<Card> deck) {
        Collections.shuffle(deck, new Random(1234));
//        Collections.shuffle(deck, new Random());
    }

    private ArrayList<Card> creatediscardDeckDeck(ArrayList<Card> drawDeck) {
        Card firstCard = drawDeck.get(0);
        ArrayList<Card> discardDeckDeck = new ArrayList<>();
        discardDeckDeck.add(firstCard);
        drawDeck.remove(0);

        return discardDeckDeck;
    }

    // Generate deck of: 4 of each black card, 2 of each colored card
    // but only 1 zero of each colored card
    private ArrayList<Card> generateDeck()
    {
        ArrayList<Card> cards = new ArrayList<>();
        for ( CardColor c : CardColor.values()){
            for ( Face f : Face.values()){
                if (c == CardColor.BLACK) {
                    if(f == Face.WILD || f == Face.DRAWFOUR) {
                        for ( int i = 0; i < 4; i++) {
                            cards.add(new Card(c, f));
                        }
                    }
                }
                else if (f != Face.WILD && f != Face.DRAWFOUR)
                {
                    cards.add(new Card(c, f));
                    if (f != Face.ZERO) cards.add(new Card(c, f));
                }
            }
        }
        return cards;
    }

    // Parameters
    // fromStack: the origin of the card that is moving
    // from: the card that is moving
    // to: the place the card is going to
    // TODO: Remove Swapcards from State and put in LocalGame
    private void swapCards(ArrayList<Card> fromStack, Card from, ArrayList<Card> to)
    {
        to.add(from);
        fromStack.remove(from);
    }

    private boolean initializePlayerHands()
    {
        if(drawDeck.size() >= 7 * playerHands.size()) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < playerHands.size(); j++) {
                    ArrayList<Card> drawnCards = drawCardsFromDeck(1);
                    addCardsToPlayerHand(j, drawnCards);
                }
            }
            return true;
        }
        return false;
    }

    // TODO: Remove checkvic functionality from State and put in LocalGame
    public boolean checkVictory (ArrayList<ArrayList<Card>> playerHands) {
        if (playerHands.size() == 0) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkDrawEmpty (ArrayList<Card> drawDeck) {
        if (drawDeck.size() == 0) {
            drawDeck.addAll(discardDeck);
            Collections.shuffle(drawDeck);
            return true;
        }
        else {
            return false;
        }
    }


    public enum PlayDirection
    {
        CW (1), CCW (-1);

        public int value;

        PlayDirection(int value) {
            this.value = value;
        }
    }

    public ArrayList<Card> fetchPlayerHand(int id) {
        ArrayList<Card> hand = playerHands.get(id);
        return hand;
    }

    public int fetchCurrentPlayer() {
        return turn % 4;
    }

    public Card getTopCard() {
        return discardDeck.get(0);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int _turn) {
        turn = _turn;
    }

    public PlayDirection getDirection() {
        return direction;
    }

    public void setDirection(PlayDirection _direction) {
        direction = _direction;
    }

    public int getHandsSize() {
        return playerHands.size();
    }


    public void addCardsToPlayerHand(int playerIndex, ArrayList<Card> cards) {
        playerHands.get(playerIndex).addAll(cards);
    }


    // this function REMOVES and RETURNS "n" amount of cards from the top of drawDeck
    public ArrayList<Card> drawCardsFromDeck(int n) {

        List<Card> subList = drawDeck.subList(0, n);
        ArrayList<Card> cardsTaken = new ArrayList<>(subList);

        drawDeck.subList(0, n).clear();

        return cardsTaken;

    }

    // this function REMOVES and RETURNS the specified card from the specified player
    public Card takeCardFromHand(int playerIndex, Card card) {

        ArrayList<Card> playerHand = playerHands.get(playerIndex);
        Log.i("playerIndex", ""+playerIndex);
//        Log.i("index", playerHand.indexOf(card)+"");

        Log.i(""+card.getFace().name(), ""+card.getCardColor().name());

        Log.i("BREAK", "");

        for (Card c : playerHand) {
            Log.i(""+c.getFace().name(), ""+c.getCardColor().name());
        }

        Card cardTaken = null;

//        for (Card c : playerHand) {
        for (int i = 0; i < playerHand.size(); i++) {
            Card c = playerHand.get(i);

            if (c.getFace().equals(card.getFace()) &&
                c.getCardColor().equals(card.getCardColor())) {

                cardTaken = playerHand.get(i);
                playerHand.remove(i);
                break;
            }
        }

//        Card cardTaken = playerHand.get(playerHand.indexOf(card));
        playerHand.remove(card);

        return cardTaken;
    }


    public void addCardToDiscardDeck(Card card) {
        discardDeck.add(0, card);
    }

}
