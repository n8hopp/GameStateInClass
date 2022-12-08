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

	// Helper method to compare card to top of discard deck
	public boolean checkCardValidity(Card card) {

		if (card.getFace() == Face.WILD || card.getFace() == Face.DRAWFOUR) {
			return true;
		}

		Card discardDeckTop = ((UnoState)state).getTopCard();

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

	// If any player's hand is size 0, that player won
	@Override
	protected String checkIfGameOver() {
		UnoState tempState = ((UnoState)state); //
		for(int i = 0; i < players.length; i++)
		{
			if(tempState.fetchPlayerHand(i).size() == 0){
				return "Player " + i + " has won! ";
			}
		}
		return null;
	}


	// this function runs when the UnoPlayer1 or UnoComputerPlayerDump classes send a MoveAction
	@Override
	protected boolean makeMove(GameAction action) {

		UnoState state = (UnoState) super.state;
		String actingPlayer = playerNames[state.fetchCurrentPlayer()];
		String actionPlayed = "";

		// if its a draw action, draw 1 card and increment the turn
		if (action instanceof DrawCardAction) {

			drawCard(1);

			state.setLatestAction( actingPlayer + " drew a card.");

			int turn = state.getTurn();
			turn += state.getDirection().value;
			turn = Math.floorMod(turn, state.getHandsSize());
			state.setTurn(turn);

			return true;
		}

		// if its a place card action, see if its valid and check for special faces,
		// then increment turn
		if (action instanceof PlaceCardAction) {

			PlaceCardAction placeAction = (PlaceCardAction) action;
			Card card = placeAction.getCard();
			int cardIndex = placeAction.getCardIndex();

			if(!placeCard(card, cardIndex)) {
				return false;
			}

			state.setLatestAction( actingPlayer + " placed a " + card.getCardColor() + " " + card.getFace());
			int turn = state.getTurn();
			turn += state.getDirection().value;
			turn = Math.floorMod(turn, state.getHandsSize());
			state.setTurn(turn);

			return true;
		}

		return false;
	}


	// draws "n" amount of cards into current player's hand
	protected boolean drawCard(int n) {

		UnoState state = (UnoState) super.state;
		int turn = state.getTurn();

		ArrayList<Card> cardsDrawn = state.drawCardsFromDeck(n);
		state.addCardsToPlayerHand(turn, cardsDrawn);
		// state.setLatestAction(playerNames[state.fetchCurrentPlayer()]);

		return true;
	}


	// checks card validity and also deals with logic of special faces
	protected boolean placeCard(Card card, int cardIndex) {

		UnoState state = (UnoState) super.state;

		boolean cardValidity = checkCardValidity(card);
		if (!cardValidity) {
			return false;
			// ends function, rest of code doesn't run, throws away move (player has to make a valid one)
		}


		// get state info
		int turn = state.getTurn();
		UnoState.PlayDirection direction = state.getDirection();
		int handsSize = state.getHandsSize();

		Face face = card.getFace();

		state.takeCardFromHandByIndex(turn, cardIndex);

		// change direction, turn and card color according to card's face
		switch (face) {

			case SKIP:
				turn += direction.value;
				turn = Math.floorMod(turn,handsSize);
				state.setTurn(turn);

				break;

			// need to set direction
			case REVERSE:
				if (direction == UnoState.PlayDirection.CCW) {
					direction = UnoState.PlayDirection.CW;
				} else {
					direction = UnoState.PlayDirection.CCW;
				}

				state.setDirection(direction);
				break;

			case DRAWTWO:
				turn += direction.value;
				turn = Math.floorMod(turn,handsSize);
				state.setTurn(turn);

				drawCard(2);
				break;

			case DRAWFOUR:
				turn += direction.value;
				turn = Math.floorMod(turn,handsSize);
				state.setTurn(turn);

				drawCard(4);

				// color is set in UnoPlayer1
				break;

			case WILD:

				// color is set in UnoPlayer1
				break;
		}

		// finally, add card to discard deck
		state.addCardToDiscardDeck(card);

		return true;
	}
}
