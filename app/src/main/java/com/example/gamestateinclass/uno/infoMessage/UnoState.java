package com.example.gamestateinclass.uno.infoMessage;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.uno.objects.Card;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
                    drawCardFromDeck(playerHands.get(j), 1);
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

    // TODO: Remove drawcard functionality from State and put in LocalGame
    private void drawCardFromDeck(ArrayList<Card> to, int n)
    {
        for (int i = 0; i < n; i++) {
            Card nextCard = drawDeck.get(0);
            to.add(nextCard);
            drawDeck.remove(nextCard);
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

    // TODO: Remove validity functionality from State and put in LocalGame
    public boolean checkCardValidity(Card card) {

        if (card.getFace() == Face.WILD || card.getFace() == Face.DRAWFOUR) {
            return true;
        }

        Card discardDeckTop = discardDeck.get(0);

        if (discardDeckTop == null)
        {
            return false;
        }
        if (discardDeckTop.getFace() == card.getFace() ||
                discardDeckTop.getCardColor() == card.getCardColor()) {
            return true;
        }

        return false;
    }

    /**
     * Checks card validity. If valid, removes card from player hand,
     * executes special card functionalities, places card into placedCards,
     * then increments turn.
     *
     * @param playerID index of player whose placing card
     * @param card the actual card object being placed
     *
     * @return true if card is valid, false otherwise
     */
    // TODO: Remove placecard functionality from State and put in LocalGame
    public boolean placeCard(int playerID, Card card)
    {
        boolean cardValidity = checkCardValidity(card);
        if (!cardValidity) {
            return false;
            // ends function, rest of code doesn't run
        }

        int nextPlayerID;
        Face face = card.getFace();

        switch (face) {

            case SKIP:
                turn += direction.value;
                turn %= playerHands.size();
                break;

            case REVERSE:
                if (direction == PlayDirection.CCW) {
                    direction = PlayDirection.CW;
                } else {
                    direction = PlayDirection.CCW;
                }

                break;

            case DRAWTWO:
                turn += direction.value;
                turn %= playerHands.size();

                drawCardFromDeck(playerHands.get(turn), 2);
                break;

            case DRAWFOUR:
                turn += direction.value;
                turn %= playerHands.size();

                drawCardFromDeck(playerHands.get(turn), 4);

                // can change this based on demonstration
                card.setColor(CardColor.BLUE);
                break;

            case WILD:

                // same thing here
                card.setColor(CardColor.BLUE);
                break;
        }

        CardColor color = card.getCardColor(); // we don't get color until here (for latestAction print)
        // because it may have changed during special action execution
        discardDeck.add(card);
        playerHands.get(playerID).remove(card);

        turn += direction.value;
        turn %= playerHands.size();

        // Player's numerical value (1-4) is different from their ID's numerical value.
        // Maybe amend this by making player #0 null so that player 1's player id = 1?
        // Otherwise, any time we refer to a player id, we add one to translate that to what the frontfacing view knows as the player values,
        // hence why we do playerId + 1 and turn + 1.
        return true;
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

        ArrayList<Card> cardsTaken = (ArrayList<Card>) drawDeck.subList(0, n);
        drawDeck.subList(0, n).clear();

        return cardsTaken;

    }

    // this function REMOVES and RETURNS the specified card from the specified player
    public Card takeCardFromHand(int playerIndex, Card card) {

        ArrayList<Card> playerHand = playerHands.get(playerIndex);
        Card cardTaken = playerHand.get(playerHand.indexOf(card));
        playerHand.remove(card);

        return cardTaken;
    }


    public void addCardToDiscardDeck(Card card) {
        discardDeck.add(card);
    }

}
