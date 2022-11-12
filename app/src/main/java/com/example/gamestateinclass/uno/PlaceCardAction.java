package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.objects.Card;

public class PlaceCardAction extends GameAction {
    // Tag for logging
    private static final String TAG = "PlaceCardAction";

    // Instance Variables: the selected row and column
    private Card card;

    public PlaceCardAction(GamePlayer player, Card card) {
        // Invokes superclass constructor to set the player
            super(player);
        }

        public Card getCard() {
            return card;
        }


    }
