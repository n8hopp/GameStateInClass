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
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This is Clei and friends attempt at trying to implement a competent computer player.
 * Here are the goals that Clei and friends will be trying to achieve.
 *
 * Places down a card when it has a playable card to place down
 * Playable cards include:
 *
 * same color/color chosen on wildcard of card on top of discard pile
 * same number of card on top of discard pile
 * same type as well maybe? (ex. +2 red card, a +2 yellow card is playable to whoevers turn it is)
 * hand is in an array -> iterates through hand, places first playable card in array hand
 *
 * Draws a card when it has no playable card to place down
 *
 */

public class UnoComputerPlayerSmart extends GameComputerPlayer {
	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "John")
	 */

	private GameAction action;

	public UnoComputerPlayerSmart(String name) {
		super(name);
	}


	@Override
	protected void receiveInfo(GameInfo info) {
		UnoState state = ((UnoState) info);

		if (state.getTurn() == playerNum) {

			Logger.log("A Dumb Computer", "My turn!");
			// Allows a delay between actions so actions are visible to player
			sleep(0.1);

				// Try to find a viable card in computer hand
				ArrayList<Card> hand = state.fetchPlayerHand(playerNum);
				Card toPlace = null;
				int currentIndex = 0;
				int toPlaceIndex = 0;
				for ( Card c : hand) {
					if ( checkCardValidity(c, state)){
						toPlace = c;
						toPlaceIndex = currentIndex;
						break;
					}
					currentIndex++;
				}
				// If a valid card was found, place it. otherwise draw
				if (toPlace != null){
					// Smart AI: set black cards to the color it has the most of to maximize
					// opportunity to play next turn
					if (toPlace.getCardColor().equals(CardColor.BLACK)) {
						int redCt = 0;
						int blueCt = 0;
						int greenCt = 0;
						int yellowCt = 0;
						for ( Card c : hand ) {
                        	switch (c.getCardColor().colorID ){
								case 0: redCt++;
										break;
								case 1: blueCt++;
										break;
								case 2: greenCt++;
										break;
								case 3: yellowCt++;
										break;
							}
						}

						HashSet<Integer> cardCts = new HashSet<>();
						cardCts.add(redCt);
						cardCts.add(blueCt);
						cardCts.add(greenCt);
						cardCts.add(yellowCt);
						int max = 0;
						for (int val : cardCts ) {
							if (val > max){
								max = val;
							}
						}

						CardColor maxColor = null;
						if (max == redCt){
							maxColor = CardColor.RED;
						}
						else if ( max == blueCt ){
							maxColor = CardColor.BLUE;
						}
						else if ( max == greenCt ){
							maxColor = CardColor.GREEN;
						}
						else if ( max == yellowCt ){
							maxColor = CardColor.YELLOW;
						}
						toPlace.setColor(maxColor);

					}

					action = new PlaceCardAction(this, toPlace, toPlaceIndex);
					Log.i("I am placing a "+toPlace.getCardColor().name()+toPlace.getFace(), "");
				}
				// No valid card was found, draw
				else {
					action = new DrawCardAction(this);
					Log.i("No valid, now drawing", "");
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

		// This accounts for the instance that the first card of the game turned
		// over is black. Allows the player to play anything
		if (discardDeckTop.getCardColor() == CardColor.BLACK){
			return true;
		}

		if (discardDeckTop.getFace() == card.getFace() ||
				discardDeckTop.getCardColor() == card.getCardColor()) {
			return true;
		}

		return false;
	}


	public int getPlayerNum()
	{
		return playerNum;
	}
}
