package com.example.gamestateinclass.uno;

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

	}

	@Override
	protected boolean canMove(int playerIdx) {
		return playerIdx == ((UnoState)state).fetchCurrentPlayer();
	}

	public boolean checkCardValidity(Card card) {

		if (card.getFace() == Face.WILD || card.getFace() == Face.DRAWFOUR) {
			return true;
		}

		Card playedCardsTop = ((UnoState)state).getTopCard();

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

		if (action instanceof DrawCardAction) {

			drawCard(1);

			return true;
		}

		if (action instanceof PlaceCardAction) {

			PlaceCardAction placeAction = (PlaceCardAction) action;
			Card card = placeAction.getCard();

			placeCard(card);

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

		CardColor color = card.getCardColor(); // we don't get color until here (for latestAction print)
		// because it may have changed during special action execution

		state.addCardToDiscardDeck(card);
		state.takeCardFromHand(turn, card);
//		playerHands.get(playerID).remove(card);

		turn += direction.value;
		turn %= handsSize;

		state.setTurn(turn);

		// Player's numerical value (1-4) is different from their ID's numerical value.
		// Maybe amend this by making player #0 null so that player 1's player id = 1?
		// Otherwise, any time we refer to a player id, we add one to translate that to what the frontfacing view knows as the player values,
		// hence why we do playerId + 1 and turn + 1.
	}
}
