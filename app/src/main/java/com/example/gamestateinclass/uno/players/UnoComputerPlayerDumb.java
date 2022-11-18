package com.example.gamestateinclass.uno.players;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameComputerPlayer;
import com.example.gamestateinclass.game.GameFramework.utilities.Logger;
import com.example.gamestateinclass.uno.DrawCardAction;
import com.example.gamestateinclass.uno.PlaceCardAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
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

public class UnoComputerPlayerDumb extends GameComputerPlayer {
	private Random rand = new Random();
	private GameAction action;
	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "The Dumb AI")
	 */
	public UnoComputerPlayerDumb(String name) {
		super(name);
	}

	@Override
	protected void receiveInfo(GameInfo info) {


		UnoState state = ((UnoState) info);

		if (state.getTurn() == playerNum) {

			Logger.log("A Dumb Computer", "My turn!");

			sleep(1.5);

			// 20% chance to draw a card without even trying to play one
			int autoDraw = rand.nextInt(10);
			if (autoDraw < 2) {

				action = new DrawCardAction(this);
				Log.i("I am drawing a card now", "");
			}

			else {
				// Try to find a viable card in computer hand
				ArrayList<Card> hand = state.fetchPlayerHand(playerNum);
				Card toPlace = null;
				for ( Card c : hand) {
					if ( checkCardValidity(c, state)){
						toPlace = c;
						break;
					}
				}
				// If a valid card was found, place it. otherwise draw
				if (toPlace != null){
					action = new PlaceCardAction(this, toPlace);
					Log.i("I am placing a "+toPlace.getCardColor().name()+toPlace.getFace(), "");
				}
				else {
					action = new DrawCardAction(this);
					Log.i("No valid, now drawing", "");
				}
			}

			game.sendAction(action);
		}

	}

	// Helper method to compare a card to the top of discard deck
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
