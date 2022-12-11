package com.example.gamestateinclass.uno.players;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.uno.actionMessage.DrawCardAction;
import com.example.gamestateinclass.uno.actionMessage.PlaceCardAction;
import com.example.gamestateinclass.uno.actionMessage.UnoShoutAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;
import com.example.gamestateinclass.uno.views.UnoHandView;
import com.example.gamestateinclass.uno.views.UnoTableView;

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
			currentTurn = false;
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
		tableView.setIntroText(gameState.getIntroMsg());

		tableView.invalidate();
		handView.invalidate();

		myHand = gameState.fetchPlayerHand(playerNum);
		topCard = gameState.getTopCard();

	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		GameAction action = null;

		/* Test call to see if we could grab Uno button when it wasn't your turn. Ultimately,
		* it's a huge limitation of the game framework to make out-of-move turns and it only
		* works fully properly if its currently your turn. */
		if(testIfUnoPressed(view, motionEvent))
		{
			return true;
		}

		// if it's a motion event up or not the players current turn, return false & exit
		if ((motionEvent.getAction() == motionEvent.ACTION_UP) || !currentTurn) {
			return false;
		}

		if (view instanceof UnoTableView) {

			// initialize a "fake" card so we can have a draw pile / discard pile
			Card fakeDrawCard = tableView.getFakeDrawCard();

			// if the "fake" draw card was touched, send the drawCardAction
			if (fakeDrawCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())
					&& !tableView.getWildCardSelection()) {

				action = new DrawCardAction(this);
				game.sendAction(action);

				// otherwise, see if topCard was touched, if so place the currently selected card in hand
			} else if (topCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				if (selectedIndex >= myHand.size()) {
					Log.i("selectedIndex"+selectedIndex, "myHand.size"+myHand.size());
					return false;
				}

				Card card = myHand.get(selectedIndex);

				// don't place yet if wild: instead bring up color prompt
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
				moveMade = true; // boolean to check if action was made on clientside
				currentTurn = false; // possibly redundant, but didn't want to mess w/ teammates functionality
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

			int startingCard = handView.getStartingCard();

			for (int i = startingCard; i < myHand.size(); i++) {
				Card c = myHand.get(i);
				if (c.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {
					selectedIndex = i;
					handView.setSelectedIndex(i);
					handView.invalidate();
					Log.i("index", selectedIndex+"");
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

	//External Citation
	//  Date:     01 December 2022
	//  Problem:  Could not implement a menu button with our current taskbar
	//  Resource:
	//     https://www.youtube.com/watch?v=MCeWm8qu0sw
	//  Solution: I followed along with this video

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
						/* possibly excessive. Don't know if we could write this as a saved string
						* in the resources? Either way, this works for the time being. */
						builder.setMessage
								("How To Play Uno By Three-Oh-Uno \n\n" +
										"Rules of the Game: Setup \n\n" +
										"Each Player receives seven cards at random. \n" +
										"The remaining cards are the Draw Pile, which is placed " +
										"in the center face down. \n" +
										"The top card of the Draw Pile is turned over to create the " +
										"Discard Pile. \n" +
										"A player is randomly selected to go first, and the flow " +
										"of play begins clockwise. \n\n" +
										"Rules of the Game: Core Gameplay \n\n" +
										"The objective is to be the first person to empty your " +
										"hands of cards. \n" +
										"Each turn, the current player will attempt to discard one " +
										"of their cards into the Discard Pile. \n" +
										"Only cards whose color or number matches the face card of " +
										"the Discard Pile can be placed atop it. (Exceptions occur with special cards)\n" +
										"If a player has no viable cards to place, they must draw " +
										"one card from the draw deck. \n" +
										"Drawing or discarding ends the players turn. \n\n" +
										"Rules of the Game: Special Cards \n\n" +
										"Reverse: The flow of play is reversed. \n" +
										"Skip: The next person to play has their turn skipped. \n" +
										"Draw 2: The next player must draw two cards and forfeit their turn. \n" +
										"Wildcard: The player who placed this card can select what color it is. \n" +
										"Wildcard Draw 4: The next player must draw four cards and " +
										"forfeit their turn, player who placed this card can select what color it is. \n\n" +
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

	public boolean testIfUnoPressed(View view, MotionEvent motionEvent)
	{
		GameAction action = null;

		if (motionEvent.getAction() == motionEvent.ACTION_UP) {
			return false;
		}

		if(view instanceof UnoHandView) {
			float unoButtonX = handView.getWidth();
			float unoButtonY = handView.getHeight();
			float distanceX = unoButtonX - motionEvent.getX();
			float distanceY = unoButtonY - motionEvent.getY();
			double distanceXY = Math.sqrt((distanceX * distanceX) + (distanceY * distanceY));

			if (distanceXY <= (handView.unoButtonRadius)) {
				action = new UnoShoutAction(this, playerNum);
				game.sendAction(action);
				tableView.invalidate();
				handView.invalidate();
				return true;
			}
		}
		return false;
	}

	public int getPlayerNum()
	{
		return playerNum;
	}

}

