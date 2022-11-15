package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.objects.Card;

public class DrawCardAction extends GameAction {
    // Tag for logging
    private static final String TAG = "DrawCardAction";

    // Instance Variables: the selected row and column
//    private Card card;

    public DrawCardAction(GamePlayer player) {
        super(player);

//        card = _card;
    }

//    public Card getCard() {
//        return card;
//    }

}
