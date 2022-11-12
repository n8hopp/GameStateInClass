package com.example.gamestateinclass.uno.players;

import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameComputerPlayer;
import com.example.gamestateinclass.game.GameFramework.utilities.Logger;
import com.example.gamestateinclass.uno.DrawCardAction;
import com.example.gamestateinclass.uno.PlaceCardAction;
import com.example.gamestateinclass.uno.actionMessage.UnoMoveAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;

/**
 * This is Clei's attempt at trying to implement a competent computer player.
 * Here are the goals that Clei will be trying to achieve.
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

public class UnoComputerPlayer1 extends GameComputerPlayer {
	/**
	 * constructor
	 *
	 * @param name the player's name (e.g., "John")
	 */
	public UnoComputerPlayer1(String name) {
		super(name);
	}

	@Override
	protected void receiveInfo(GameInfo info) {
		UnoState gameInfo = ((UnoState) info);

		if (info instanceof NotYourTurnInfo) {
			Logger.log("UnoComputer 1", "My turn!");
			// hmm i don't really know what i should add here

			// Tests hand deck to place deck for possible playable plays
			// While hand deck != NULL (going through the hand deck)

			// Does 1st hand card deck "match" the top deck of the place deck?
			// Match -> same color, same number, same type

			// If it goes through the whole hand deck and there are no playable plays -> draw card

		}
	} // receiveInfo
}
