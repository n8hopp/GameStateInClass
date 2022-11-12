package com.example.gamestateinclass.uno.players;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameComputerPlayer;
import com.example.gamestateinclass.game.GameFramework.utilities.Logger;
import com.example.gamestateinclass.uno.DrawCardAction;
import com.example.gamestateinclass.uno.PlaceCardAction;
import com.example.gamestateinclass.uno.actionMessage.UnoMoveAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.UnoLocalGame;
import com.example.gamestateinclass.uno.objects.Face;

import java.util.ArrayList;
import java.util.Random;

/**
 * This is Clei's attempt at trying to implement a really dumb computer player.
 * Here are the goals that Clei will be trying to achieve.
 *
 * 50% chance of picking drawing a card when its the computer player's turn
 *
 */

public class UnoComputerPlayer69 extends GameComputerPlayer {
	private Random rand = new Random();
	private GameAction action;
	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "The Dumb AI")
	 */
	public UnoComputerPlayer69(String name) {
		super(name);
	}

	@Override
	protected void receiveInfo(GameInfo info) {
		sleep(1);
		UnoState state = ((UnoState) info);

		if (info instanceof NotYourTurnInfo) {
			Logger.log("UnoComputer 69", "My turn!");
			// hmm i don't really know what i should add here

			// Does the AI draw a card or not?
			int autoDraw = rand.nextInt(10);

			if (autoDraw < 5) {
				action = new DrawCardAction(this, state.getTopCard()); // There's a problem here
			}

			else {
				// Try to find a viable card
				ArrayList<Card> hand = state.fetchPlayerHand(playerNum);
				Card toPlace = null;
				for ( Card c : hand) {
					if ( checkCardValidity(c, state)){
						toPlace = c;
						break;
					}
				}
				if (toPlace != null){
					action = new PlaceCardAction(this, toPlace);
				}
				else {
					action = new DrawCardAction(this, state.getTopCard());
				}
			}

			// This simulates the computer thinking (might be longer bc its dumb)
			sleep(5);

			game.sendAction(action);
		}

	}

	public boolean checkCardValidity(Card card, UnoState state) {

		if (card.getFace() == Face.WILD || card.getFace() == Face.DRAWFOUR) {
			return true;
		}

		Card discardDeckTop = state.getTopCard();

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
}
