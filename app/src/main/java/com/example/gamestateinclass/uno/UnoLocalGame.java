package com.example.gamestateinclass.uno;

import android.util.Log;

import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.actionMessage.UnoShoutAction;
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
	 * @param unoState State to copy
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

	/** Helper method to compare card to top of discard deck
	 *
	 * @param card Card to check
	 */
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
				return playerNames[i] + " has won! ";
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
		state.setIntroMsg("");

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

		/* Test function to see if we could grab Uno button when it wasn't your turn. Ultimately,
		 * it's a huge limitation of the game framework to make out-of-move turns and it only
		 * works fully properly if its currently your turn. */
		if (action instanceof UnoShoutAction)
		{
			UnoShoutAction shoutAction = (UnoShoutAction)action;

			int origTurn = state.getTurn(); // (note, will not work during Computer's thread sleep.
			int currTurn = origTurn;		//  How many syncs do we have to add to get this to work?
											// Prob not worth the performance tank.)
			currTurn -= state.getDirection().value; // Get the previous player
			currTurn = Math.floorMod(currTurn, state.getHandsSize());
			Log.i("TURN:", String.valueOf(origTurn));
			Log.i("PREVTURN:", String.valueOf(currTurn));
			state.setTurn(currTurn); // set player's turn so we can make them draw cards

			ArrayList<Card> lastPlayerHand = state.fetchPlayerHand(currTurn);

			// if it's not the player who has 1 card that called it, we want to give that player cards
			if(shoutAction.getId() != currTurn) {

				if (lastPlayerHand.size() == 1) {
					state.setLatestAction(actingPlayer + "Uno! +2");
					drawCard(2);
				} else {
					state.setLatestAction("Uno?? No Uno!");
				}
			}
			else // if it is the player who has 1 card that called it, we do not want to give them cards
			// note, does not work, see above.
			{
				if(lastPlayerHand.size() == 1)
				{
					state.setLatestAction(actingPlayer + " called Uno!");
				} else {
					state.setLatestAction(actingPlayer + " was a dummy and called Uno! when no one had Uno!");
				}
			}
			state.setTurn(origTurn);
			return true;
		}

		return false;
	}


	/** draws "n" amount of cards into current player's hand
	 *
	 * @param n number of cards to draw */
	protected boolean drawCard(int n) {

		UnoState state = (UnoState) super.state;
		int turn = state.getTurn();

		ArrayList<Card> cardsDrawn = state.drawCardsFromDeck(n);
		state.addCardsToPlayerHand(turn, cardsDrawn);

		return true;
	}


	/** checks card validity and also deals with logic of special faces
	 * @param card card to place
	 * @param cardIndex index of card in hand (workaround for weird wildcard/+4 logic)
	 * */
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
