package com.example.gamestateinclass.uno.actionMessage;



import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.Card.*;


public class UnoMoveAction extends GameAction {

    // Instance variables will be the card trying to be played
    private Card card;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public UnoMoveAction(GamePlayer player, Card _card) {
        super(player);

        this.card = new Card();
        card.setPaintfromEnum(_card.getCardColor());
        card.setFace(_card.getFace());

    }

    public Face getFace(){
        return card.getFace();
    }

    public CardColor getColor(){
        return card.getCardColor();
    }

}
