package com.example.gamestateinclass.uno.actionMessage;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.objects.Card;

public class UnoShoutAction extends GameAction {
    private int id;
    public UnoShoutAction(GamePlayer player, int id) {
        super(player);
        this.id = id;
    }

    public int getId()
    {
        return id;
    }
}
