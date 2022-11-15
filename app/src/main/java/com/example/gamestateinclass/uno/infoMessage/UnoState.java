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
    private String latestAction;

    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> discardDeck;
    private ArrayList<Card> drawDeck;

    // Default Constructor. Initializes turn to 0, direction to be clockwise,
    // generates the drawDeck, adds four Arraylist<Card> for the player hands (change to be the number of players?)
    // shuffles the drawdeck, and plays the first card from the top onto the discard deck.
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

    // Copy Constructor makes a deep copy of each individual card in our draw and discard deck, as well as
    // in each player hand. We have a separate for loop for each arraylist of cards.
    // Since enums & ints are constant values, we don't need to make a deep copy of our turn and direction variables.
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

    // shuffleDeck simply calls Collections.shuffle on the Arraylist of cards passed into it.
    // This randomizes it with a random seed.
    private void shuffleDeck(ArrayList<Card> deck) {
//        Collections.shuffle(deck, new Random(1234));
        Collections.shuffle(deck, new Random());
    }

    // This just creates a new deck from the top card of the draw deck and returns it.
    // We must make sure to add it to the new deck before we remove it from the old one, else it ends up not existing.
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
                if (f != Face.NONE) {
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

    // For each player hand, add 7 cards from the draw deck. We do this in a round robin style
    // (instead of each person just drawing 7 cards) so there's more suspension of random-ness,
    // and it's more in line with the original game's card drawing.
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

    // PlayDirection is an enum (with an integer parameter value) where 1 refers to clockwise and
    // -1 refers to counter clockwise.
    public enum PlayDirection
    {
        CW (1), CCW (-1);

        public int value;

        PlayDirection(int value) {
            this.value = value;
        }
    }

    // Returns the player hand associated with the id passed into the function.
    public ArrayList<Card> fetchPlayerHand(int id) {
        ArrayList<Card> hand = playerHands.get(id);
        return hand;
    }

    // Fetch the current turn (floorMod is equivalent to the modulus operator (%) except it
    // also works for negative values.
    // For example, -1 % 4 == -1. Math.floorMod(-1, 4) = 3. Since we decrement the turn when
    // we're going counter clockwise, we need to use floorMod instead of %.
    public int fetchCurrentPlayer() {
        return Math.floorMod(turn, 4);
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

        if (drawDeck.size() <= 5) {
            refillDrawDeck();
        }

        Log.i("draw deck size: ", drawDeck.size()+"");

        return cardsTaken;

    }

    // this function REMOVES and RETURNS the specified card from the specified player
    public Card takeCardFromHand(int playerIndex, Card card) {

        ArrayList<Card> playerHand = playerHands.get(playerIndex);

        Card cardTaken = null;

        // loops through to find card with same face and color, couldn't use
        // regular "indexOf" for some reason
        for (int i = 0; i < playerHand.size(); i++) {
            Card c = playerHand.get(i);

            if (c.getFace().equals(card.getFace()) &&
                c.getCardColor().equals(card.getCardColor())) {

                cardTaken = playerHand.get(i);
                playerHand.remove(i);
                break;
            }
        }

        playerHand.remove(card);

        return cardTaken;
    }


    public void addCardToDiscardDeck(Card card) {
        discardDeck.add(0, card);
    }

    public void setLatestAction(String _latestAction){
        latestAction = _latestAction;
    }

    public String getLatestAction() {
        return latestAction;
    }

    // This function shuffles the discardDeck if the drawDeck is empty, and fills the drawDeck
    // back up with the shuffled discardDeck.
    public void refillDrawDeck() {

        // make temps
        List<Card> subList = discardDeck.subList(1, discardDeck.size());
        ArrayList<Card> allButTop = new ArrayList<>(subList);
        Card topCard = discardDeck.get(0);

        // add all but top card to discard deck, shuffle after
        drawDeck.addAll(0, allButTop);
        shuffleDeck(drawDeck);

        // make it so only the top card remains in discard deck
        discardDeck.clear();
        discardDeck.add(topCard);

    }
}
