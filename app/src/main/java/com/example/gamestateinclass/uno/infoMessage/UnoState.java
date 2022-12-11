package com.example.gamestateinclass.uno.infoMessage;

import static java.sql.Types.NULL;

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

    private static final String TAG = "UnoState";
    private int turn; //index of player whose turn it is
    private PlayDirection direction;
    private String latestAction;
    private String introMsg;

    private ArrayList<ArrayList<Card>> playerHands;
    private ArrayList<Card> discardDeck;
    private ArrayList<Card> drawDeck;

    /**
     * Default Constructor. Initializes turn to 0, direction to be clockwise,
     * generates the drawDeck, adds four Arraylist<Card> for the player hands,
     * shuffles the drawDeck, and plays the first card from the top onto the discardDeck.
     *
     * @return a new UnoState object
     */
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
        discardDeck = createDiscardDeckDeck(drawDeck);
        latestAction = "Welcome! Place or draw a card to begin.";
        introMsg = "The blue arrow indicates current turn and direction of play.";
    }


    /**
     * Copy Constructor makes a deep copy of each individual card in our draw and
     * discard deck, as well as in each player hand. We have a separate for loop
     * for each arraylist of cards. Since enums & ints are constant values, we
     * don't need to make a deep copy of our turn and direction variables.
     *
     * @param previous the UnoState to be copied.
     *
     * @return a deep copied UnoState
     */
    public UnoState(UnoState previous) {
        turn = previous.turn;
        direction = previous.direction;
        latestAction = previous.latestAction;
        introMsg = previous.introMsg;;
        drawDeck = new ArrayList<Card>();
        synchronized (previous.drawDeck) {
            for (Card c : previous.drawDeck) // for each card in the drawDeck we're copying
            {
                Face face = c.getFace();
                CardColor color = c.getCardColor();
                drawDeck.add(new Card(color, face)); // deep copy
            }
        }
        discardDeck = new ArrayList<Card>();
        for (Card c : previous.discardDeck) {
            Face face = c.getFace();
            CardColor color = c.getCardColor();
            discardDeck.add(new Card(color, face));
        }
        playerHands = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            playerHands.add(i, new ArrayList<Card>());
        }

        synchronized (previous.playerHands) {
            for (int i = 0; i < previous.playerHands.size(); i++) {
                ArrayList<Card> newHand = previous.playerHands.get(i);

                ArrayList<Card> newHandCopy = (ArrayList<Card>) newHand.clone();

                for (Card c : newHandCopy) {
                    Face face = c.getFace();
                    CardColor color = c.getCardColor();

                    playerHands.get(i).add(new Card(color, face));
                }
            }
        }
    }


    /**
     * Simply calls the Collections.shuffle on the Arraylist of cards passed into it.
     *
     * @param deck the drawDeck to be shuffled.
     *
     * @return void.
     */
    private void shuffleDeck(ArrayList<Card> deck) {
        Collections.shuffle(deck, new Random());
    }


    /**
     * Creates a new ArrayList of cards to be the discardDeck, then fills it with
     * the top card of the drawDeck.
     *
     * @param drawDeck the drawDeck to take the top card from.
     *
     * @return the new ArrayList with one card.
     */
    private ArrayList<Card> createDiscardDeckDeck(ArrayList<Card> drawDeck) {
        Card firstCard = drawDeck.get(0);
        ArrayList<Card> discardDeck = new ArrayList<>();
        discardDeck.add(firstCard);
        drawDeck.remove(0);

        return discardDeck;
    }


    /**
     * Generates a deck with every card in UNO:
     *  - 4 of each black card
     *  - 2 of each colored card
     *      - Only 1 of cards with face ZERO
     *
     * @return ArrayList of the 108 cards present in UNO
     */
    private ArrayList<Card> generateDeck() {
        ArrayList<Card> cards = new ArrayList<>();
        for (CardColor c : CardColor.values()) {
            for (Face f : Face.values()) {
                if (f != Face.NONE) {
                    if (c == CardColor.BLACK) {
                        if (f == Face.WILD || f == Face.DRAWFOUR) {
                            for (int i = 0; i < 4; i++) {
                                cards.add(new Card(c, f));
                            }
                        }
                    } else if (f != Face.WILD && f != Face.DRAWFOUR) {
                        cards.add(new Card(c, f));
                        if (f != Face.ZERO) cards.add(new Card(c, f));
                    }
                }
            }
        }

        return cards;
    }


    /**
     * Fills each player hand with seven initial cards. Each player draws one card
     * at a time as opposed to drawing all 7 at once.
     *
     * @return true if the drawDeck is large enough to fill the hands, false otherwise
     */
    private boolean initializePlayerHands() {
        if (drawDeck.size() >= 7 * playerHands.size()) {
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


    /**
     * PlayDirection is an enum (with an integer parameter value)
     * where 1 refers to clockwise and -1 refers to counter clockwise.
     */
    public enum PlayDirection {
        CW(1), CCW(-1);

        public int value;

        PlayDirection(int value) {
            this.value = value;
        }
    }


    /**
     * Getter method to fetch the player hand associated with ID passed in.
     *
     * @return the specified playerHand (which is and ArrayList of cards).
     */
    public ArrayList<Card> fetchPlayerHand(int id) {
        ArrayList<Card> hand = playerHands.get(id);
        return hand;
    }


    /**
     * Getter method to fetch the current player's ID.
     *
     * NOTE: floorMod is equivalent to the modulus operator (%) except it
     * also works for negative values. For example, -1 % 4 == -1.
     * Math.floorMod(-1, 4) = 3. Since we decrement the turn when
     * we're going counter clockwise, we need to use floorMod instead of %.
     *
     * @return the integer ID of the current player.
     */
    public int fetchCurrentPlayer() {
        return Math.floorMod(turn, 4);
    }


    /**
     * Getter method to fetch the top card of the discardDeck.
     *
     * @return the 0th Card object in the discardDeck.
     */
    public Card getTopCard() {
        return discardDeck.get(0);
    }


    /**
     * Getter method to fetch the current turn.
     *
     * @return returns the integer of the turn that it is.
     */
    public int getTurn() {
        return turn;
    }


    /**
     * Getter method to fetch the
     *
     * @return true if the drawDeck is large enough to fill the hands, false otherwise
     */
    public void setTurn(int _turn) {
        turn = _turn;
    }


    /**
     * Getter method to fetch the direction of play.
     *
     * @return the direction (an instance of the enumeration PlayDirection).
     */
    public PlayDirection getDirection() {
        return direction;
    }


    /**
     * Setter method to change the direction of play.
     *
     * @param _direction the new direction of play.
     *
     * @return void.
     */
    public void setDirection(PlayDirection _direction) {
        direction = _direction;
    }


    /**
     * Getter method to fetch the size of playerHands, aka how many players there are.
     *
     * @return the size of the playerHands ArrayList.
     */
    public int getHandsSize() {
        return playerHands.size();
    }


    /**
     * Adds the provided cards to the specified player's hand.
     *
     * @param playerIndex the index of the player receiving the cards.
     * @param cards the cards to be added to the player's hand.
     *
     * @return void
     */
    public void addCardsToPlayerHand(int playerIndex, ArrayList<Card> cards) {

        ArrayList<Card> hand = playerHands.get(playerIndex);

        synchronized (hand) {
            hand.addAll(cards);
        }
    }


    /**
     * Removes and returns "n" amount of cards from the top of the drawDeck.
     *
     * @param n the number of cards to be drawn.
     *
     * @return ArrayList of cards that have been drawn.
     */
    public ArrayList<Card> drawCardsFromDeck(int n) {

        ArrayList<Card> cardsTaken;

        synchronized (drawDeck) {
            List<Card> subList = drawDeck.subList(0, n);
            cardsTaken = new ArrayList<>(subList);

            drawDeck.subList(0, n).clear();
        }

        if (drawDeck.size() <= 5) {
            refillDrawDeck();
        }

        return cardsTaken;

    }


    /**
     * Removes and returns the specified card from the specified player's hand.
     *
     * @param playerIndex index of the player whose card is being removed.
     * @param index index of the card within the player's hand.
     *
     * @return the card that's been removed.
     */
    public Card takeCardFromHandByIndex(int playerIndex, int index) {

        ArrayList<Card> playerHand = playerHands.get(playerIndex);

        Card cardTaken;

        synchronized (playerHand) {
            cardTaken = playerHand.get(index);
            playerHand.remove(index);
        }

        return cardTaken;
    }


    /**
     * Adds a card to the front (0th index) of the discardDeck.
     *
     * @param card the card to be discarded.
     *
     * @return void.
     */
    public void addCardToDiscardDeck(Card card) {
        discardDeck.add(0, card);
    }


    /**
     * Setter method to change the latest action.
     *
     * @param _latestAction string of text describing the latest action.
     *
     * @return void
     */
    public void setLatestAction(String _latestAction) {
        latestAction = _latestAction;
    }


    /**
     * Setter method for the introductory message.
     *
     * @param _introMsg the string of text introducing the player.
     *
     * @return void
     */
    public void setIntroMsg(String _introMsg) {
        introMsg = _introMsg;
    }


    /**
     * Getter method to fetch the latest action message.
     *
     * @return the latestAction message string.
     */
    public String getLatestAction() {
        return latestAction;
    }


    /**
     * Getter method to fetch the introductory message.
     *
     * @return the introMsg string.
     */
    public String getIntroMsg() {
        return introMsg;
    }


    /**
     * This function is called when the drawDeck is almost empty. It removes
     * all but the top card of the discardDeck and transfers these contents back
     * into the drawDeck, then finally shuffles it. Additionally, any WILD or
     * DRAWFOUR cards have their color reset to black since it has been altered by
     * the player who placed it.
     *
     * @return void.
     */
    public void refillDrawDeck() {

        synchronized (drawDeck) {
            // make temps
            List<Card> subList = discardDeck.subList(1, discardDeck.size());
            ArrayList<Card> allButTop = new ArrayList<>(subList);
            Card topCard = discardDeck.get(0);

            // add all but top card to discard deck, shuffle after
            drawDeck.addAll(0, allButTop);

            // if a card is WILD or DRAWFOUR, reset its color to black
            for (int i = 0; i < drawDeck.size(); i++) {
                Card c = drawDeck.get(i);
                Face cFace = c.getFace();
                if ((cFace.equals(Face.WILD)) ||
                        (cFace.equals(Face.DRAWFOUR))) {
                    c.setColor(CardColor.BLACK);
                }
            }
            shuffleDeck(drawDeck);

            // make it so only the top card remains in discard deck
            discardDeck.clear();
            discardDeck.add(topCard);
        }
    }
}
