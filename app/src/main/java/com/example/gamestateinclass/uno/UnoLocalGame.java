package com.example.gamestateinclass.uno;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;

import java.util.ArrayList;

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
		UnoState stateCopy = new UnoState((UnoState) super.state);
		p.sendInfo(stateCopy);
	}


	@Override
	protected boolean canMove(int playerIdx) {
		return playerIdx == ((UnoState)state).fetchCurrentPlayer();
	}


	public boolean checkCardValidity(Card card) {

		if (card.getFace() == Face.WILD || card.getFace() == Face.DRAWFOUR) {
			return true;
		}

		Card discardDeckTop = ((UnoState)state).getTopCard();

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


	@Override
	protected String checkIfGameOver() {
		UnoState tempState = ((UnoState)state); //
		if (tempState.fetchPlayerHand(tempState.fetchCurrentPlayer()).size() == 0){
			return "Player " + tempState.fetchCurrentPlayer() + " has won!";
		}
		return null;
	}


	@Override
	protected boolean makeMove(GameAction action) {

		if (action instanceof DrawCardAction) {

			drawCard(1);
			

			return true;
		}

		if (action instanceof PlaceCardAction) {

			PlaceCardAction placeAction = (PlaceCardAction) action;
			Card card = placeAction.getCard();

			placeCard(card);

			checkIfGameOver();

			return true;
		}

		return false;
	}


	protected boolean drawCard(int n) {

		UnoState state = (UnoState) super.state;
		int turn = state.getTurn();

		ArrayList<Card> cardsDrawn = state.drawCardsFromDeck(n);
		state.addCardsToPlayerHand(turn, cardsDrawn);

		return true;
	}


	protected boolean placeCard(Card card) {

		UnoState state = (UnoState) super.state;

		boolean cardValidity = checkCardValidity(card);
		if (!cardValidity) {
			return false;
			// ends function, rest of code doesn't run
		}


		int turn = state.getTurn();
		UnoState.PlayDirection direction = state.getDirection();
		int handsSize = state.getHandsSize();

		Face face = card.getFace();

		state.takeCardFromHand(turn, card);

		switch (face) {

			case SKIP:
				turn += direction.value;
				turn %= handsSize;

				break;

			case REVERSE:
				if (direction == UnoState.PlayDirection.CCW) {
					direction = UnoState.PlayDirection.CW;
				} else {
					direction = UnoState.PlayDirection.CCW;
				}

				break;

			case DRAWTWO:
				turn += direction.value;
				turn %= handsSize;

				drawCard(2);
				break;

			case DRAWFOUR:
				turn += direction.value;
				turn %= handsSize;

				drawCard(4);

				// can change this based on demonstration
				card.setColor(CardColor.BLUE);
				break;

			case WILD:

				// same thing here
				card.setColor(CardColor.BLUE);
				break;
		}

		state.addCardToDiscardDeck(card);

		Log.i("top card", "");
		Log.i(state.getTopCard().getCardColor().name(), state.getTopCard().getFace().name());

		turn += direction.value;
		turn %= handsSize;

		state.setTurn(turn);

		Log.i("turn", turn+"");

		return true;
	}
}
