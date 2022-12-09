package com.example.gamestateinclass.uno.players;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.gameConfiguration.GameConfig;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.game.GameFramework.utilities.Logger;
import com.example.gamestateinclass.game.GameFramework.utilities.Saving;
import com.example.gamestateinclass.uno.DrawCardAction;
import com.example.gamestateinclass.uno.PlaceCardAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;
import com.example.gamestateinclass.uno.views.UnoHandView;
import com.example.gamestateinclass.uno.views.UnoTableView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {
	private int layoutId;
	private UnoTableView tableView;
	private UnoHandView handView;

	private TextView actionText;

	private int selectedIndex;
	private SeekBar handSeekBar;
	private Button exitButton;
	private int startingHandCard;

	boolean moveMade = false;
	boolean currentTurn = false;
	private ArrayList<Card> myHand;
	private Card topCard;

	/**
	 * constructor
	 *
	 * @param name the name of the player
	 */
	public UnoPlayer1(String name, int _layoutId) {
		super(name);
		this.layoutId = _layoutId;
		startingHandCard = 0;
		selectedIndex = 0;
	}

	@Override
	public View getTopView() {
		return null;
	}

	@Override
	public void receiveInfo(GameInfo info) {
		// Ensures inability for a player to do actions when it isn't it's turn
		if (!(info instanceof UnoState)) {
			return;
		}

		UnoState gameState = (UnoState) info;

		// Send state info to the views
		tableView.setState(gameState);
		handView.setState(gameState);

		if (gameState.getTurn() == playerNum) {
			moveMade = false;
			currentTurn = true;
		}

		handView.setCurrentPlayerId(playerNum);

		ArrayList<Integer> displayOrder = new ArrayList<>();
		ArrayList<Integer> tempOrder = new ArrayList<>();
		boolean playerFound = false;
		for (int i = 0; i < gameState.getHandsSize(); i++) {
			if (i != playerNum && !playerFound) {
				tempOrder.add(i);
			} else {
				displayOrder.add(i);
				playerFound = true;
			}
		}
		displayOrder.addAll(tempOrder);

		tableView.setDisplayOrder(displayOrder);
		// Set string to display and size texts for all player hands
		String p0HandSize = gameState.fetchPlayerHand(displayOrder.get(0)).size() + " Cards";
		String p1HandSize = gameState.fetchPlayerHand(displayOrder.get(1)).size() + " Cards";
		String p2HandSize = gameState.fetchPlayerHand(displayOrder.get(2)).size() + " Cards";
		String p3HandSize = gameState.fetchPlayerHand(displayOrder.get(3)).size() + " Cards";
		tableView.setPlayerHandText(p0HandSize, p1HandSize, p2HandSize, p3HandSize);
		tableView.setPlayerNameText(allPlayerNames[displayOrder.get(0)], allPlayerNames[displayOrder.get(1)], allPlayerNames[displayOrder.get(2)], allPlayerNames[displayOrder.get(3)]);
		tableView.setActionText(gameState.getLatestAction());


		tableView.invalidate();
		handView.invalidate();

		myHand = gameState.fetchPlayerHand(playerNum);
		topCard = gameState.getTopCard();

	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		GameAction action = null;

		if (motionEvent.getAction() == motionEvent.ACTION_UP || !currentTurn) {
			return false;
		}

		if (view instanceof UnoTableView) {

			Card fakeDrawCard = tableView.getFakeDrawCard();

			// if the "fake" draw card was touched, send the drawCardAction
			if (fakeDrawCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())
					&& !tableView.getWildCardSelection()) {

				action = new DrawCardAction(this);
				game.sendAction(action);

				// otherwise, see if topCard was touched, if so place the currently selected card in hand
			} else if (topCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				Card card = myHand.get(selectedIndex);

				// don't place if wild: instead bring up color prompt
				if (!moveMade) {
					if (card.getFace() == Face.DRAWFOUR || card.getFace() == Face.WILD) {
						tableView.setTempWildFace(card.getFace());
						tableView.setWildCardSelection(true);
						handView.setWildCardSelection(true);
						tableView.invalidate();
						handView.invalidate();
						return true;
					}
				}

				action = new PlaceCardAction(this, card, selectedIndex);
				game.sendAction(action);
				moveMade = true;
				currentTurn = false;
				tableView.invalidate();
				handView.invalidate();


				// lastly, check if color wheel is tapped (black means out of bounds)
			} else if (tableView.getTappedColor(
					motionEvent.getX(), motionEvent.getY()) != CardColor.BLACK
					&& tableView.getWildCardSelection()) {

				CardColor color = tableView.getTappedColor(motionEvent.getX(), motionEvent.getY());
				Card card = myHand.get(selectedIndex);
				card.setColor(color);

				action = new PlaceCardAction(this, card, selectedIndex);
				game.sendAction(action);
				currentTurn = false;
				moveMade = true;
				selectedIndex = 0;
				handView.setSelectedIndex(0);

				tableView.setWildCardSelection(false);
				handView.setWildCardSelection(false);
				tableView.invalidate();
				handView.invalidate();
			}

			return true;
		}

		if (view instanceof UnoHandView) {

			if (handView.getWildCardSelection()) {
				return false;
			}

			// check each card in hand to see if it was tapped.
			// if so, set it as selected
			for (int i = 0; i < myHand.size(); i++) {
				Card c = myHand.get(i);
				if (c.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {
					selectedIndex = i;
					handView.setSelectedIndex(i);
					handView.invalidate();
					return true;
				}
			}
			return true;
		}

		return false;
	}

	@Override
	public void setAsGui(GameMainActivity activity) {

		// remember the activity
		myActivity = activity;

		// Load the layout resource for our GUI
		activity.setContentView(R.layout.uno_layout);

		tableView = (UnoTableView) activity.findViewById(R.id.tableView);
		handView = (UnoHandView) activity.findViewById(R.id.handView);

		// actionText = activity.findViewById(R.id.lobbyInfoText);
		handSeekBar = activity.findViewById(R.id.scrollHandSeekBar);
		exitButton = activity.findViewById(R.id.exitButton);

		// Sets a Listener for views and buttons users can touch
		tableView.setOnTouchListener(this);
		handView.setOnTouchListener(this);
		handSeekBar.setOnSeekBarChangeListener(this);
		exitButton.setOnClickListener((View.OnClickListener) this);

		//tableView.setPlayerNameText(allPlayerNames[0], allPlayerNames[1], allPlayerNames[2], allPlayerNames[3]);

	}

	// Set hand view based on progress bar
	// Gains ability for player to access cards not in initial view
	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (myHand.size() > 4) {
			// plus 999 to make it easier to scroll to last card without accessing the next
			// number divisible by 1000
			handSeekBar.setMax((myHand.size() - 4)*1000+999);
			handView.setStartingCard((i)/1000);
			handView.invalidate();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onClick(View v) {
		PopupMenu popupMenu = new PopupMenu(myActivity, v);
		popupMenu.getMenuInflater().inflate(R.menu.game_main, popupMenu.getMenu());
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_help:
						final AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
						builder.setMessage
								("How To Play Uno By Three-Oh-Uno \n\n" +
										"Rules of the Game: Setup \n\n" +
										"Each Player receives seven cards at random. \n" +
										"The remaining cards are the Draw Pile, which is placed in the center face down. \n" +
										"The top card of the Draw Pile is turned over to create the Discard Pile. \n" +
										"A player is randomly selected to go first, and the flow of play begins clockwise. \n\n" +
										"Rules of the Game: Core Gameplay \n\n" +
										"The objective is to be the first person to empty your hands of cards. \n" +
										"Each turn, the current player will attempt to discard one of their cards into the Discard Pile. \n" +
										"Only cards whose color or number matches the face card of the Discard Pile can be placed atop it. (Exceptions occur with special cards)\n" +
										"If a player has no viable cards to place, they must draw one card from the draw deck. \n" +
										"Drawing or discarding ends the players turn. \n\n" +
										"Rules of the Game: Special Cards \n\n" +
										"Reverse: The flow of play is reversed. \n" +
										"Skip: The next person to play has their turn skipped. \n" +
										"Draw 2: The next player must draw two cards and forfeit their turn. \n" +
										"Wildcard: The player who placed this card can select what color it is. \n" +
										"Wildcard Draw 4: The next player must draw four cards and forfeit their turn, player who placed this card can select what color it is. \n\n" +
										"Have Fun!");
						builder.setPositiveButton("Return to game", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						});
						AlertDialog alertDialog1 = builder.create();
						alertDialog1.show();
						return true;

					case R.id.new_game:
						final AlertDialog.Builder builder2 = new AlertDialog.Builder(myActivity);
						builder2.setMessage("Are you sure you want a new game?");
						builder2.setCancelable(true);
						builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						});
						builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								myActivity.restartGame();
							}
						});
						AlertDialog alertDialog3 = builder2.create();
						alertDialog3.show();
						return true;

					case R.id.quit_game:
						final AlertDialog.Builder builder3 = new AlertDialog.Builder(myActivity);
						builder3.setMessage("Are you sure you want to exit the game?");
						builder3.setCancelable(true);
						builder3.setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								dialogInterface.cancel();
							}
						});
						builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								myActivity.finish();
							}
						});
						AlertDialog alertDialog4 = builder3.create();
						alertDialog4.show();
						return true;

					default:
						return false;
				}
			}
		});
		popupMenu.show();
	}

}

