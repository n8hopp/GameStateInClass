package com.example.gamestateinclass.uno.actionMessage;

import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.uno.objects.Card;


public class UnoMoveAction extends GameAction {

    // Instance variables will be the card trying to be played

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public UnoMoveAction(GamePlayer player, Card _card) {
        super(player);
        /*
        this.card = new Card(_card.color, _card.number);
         */
    }

}
