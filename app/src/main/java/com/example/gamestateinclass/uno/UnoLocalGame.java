package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;

public class UnoLocalGame extends LocalGame {
	/*
		In TTTLocal Game there is:
		array of possibly played pieces
		a move count variable

		2 constructors
		start
		checkIfGameOver
		sendStateTo
		canMove and makeMove
		whoWon
*/
	/**
	 * Constructor for the UnoLocalGame.
	 */
	public UnoLocalGame() {

		// perform superclass initialization
		super();

		// create a new, unfilled-in TTTState object
		super.state = new UnoState();
	}

	/**
	 * Constructor for the UnoLocalGame with loaded UnoState
	 * @param unoState
	 */
	public UnoLocalGame(UnoState unoState){
		super();
		super.state = new UnoState(unoState);
	}
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {

	}

	@Override
	protected boolean canMove(int playerIdx) {
		return playerIdx == ((UnoState)state).fetchCurrentPlayer();
	}

	public boolean checkCardValidity(Card card) {

		if (card.getFace() == Card.Face.WILD || card.getFace() == Card.Face.DRAWFOUR) {
			return true;
		}

		//Card playedCardsTop = ((UnoState)state).playedCards.get(0);
		Card playedCardsTop = null;

		if (playedCardsTop == null)
		{
			return false;
		}
		if (playedCardsTop.getFace() == card.getFace() ||
				playedCardsTop.getCardColor() == card.getCardColor()) {
			return true;
		}

		return false;
	}
	@Override
	protected String checkIfGameOver() {
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		return false;
	}
}
