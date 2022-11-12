package com.example.gamestateinclass.uno.unoActionMessage;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;

public class UnoMoveAction extends GameAction {

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public UnoMoveAction(GamePlayer player) {
        super(player);
    }
}
