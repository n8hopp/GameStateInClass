package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.objects.Card;

public class PlaceCardAction extends GameAction {
    // Tag for logging
    private static final String TAG = "PlaceCardAction";

    // Instance Variables: the card being placed
    private Card card;
    private int cardIndex;

    public PlaceCardAction(GamePlayer player, Card _card, int _cardIndex) {
        // Invokes superclass constructor to set the player
        super(player);

        card = _card;
        cardIndex = _cardIndex;
    }

    // return card from this action
    public Card getCard() {
        return card;
    }

    public int getCardIndex() {
        return cardIndex;
    }
}
