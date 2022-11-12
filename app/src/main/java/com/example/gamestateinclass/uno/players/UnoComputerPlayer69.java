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
 * This is Clei's attempt at trying to implement a really dumb computer player.
 * Here are the goals that Clei will be trying to achieve.
 *
 * 50% chance of picking drawing a card when its the computer player's turn
 *
 */

public class UnoComputerPlayer69 extends GameComputerPlayer {
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
		UnoState gameInfo = ((UnoState) info);

		if (info instanceof NotYourTurnInfo) {
			Logger.log("UnoComputer 69", "My turn!");
			// hmm i don't really know what i should add here

			// Does the AI draw a card or not?
			double does = Math.random();

//			if (does < 0.5) {
//				game.sendAction(new DrawCardAction(this)); // There's a problem here
//			}
//			else {
//				game.sendAction(new PlaceCardAction(this)); // There's a similar problem here
//			}
//
//			// This simulates the computer thinking (might be longer bc its dumb)
//			sleep(5);
//
//			game.sendAction(new UnoMoveAction(this))
		}

	}
}
