package com.example.gamestateinclass.uno.players;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
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

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener, SeekBar.OnSeekBarChangeListener{
	private int layoutId;
	private UnoTableView tableView;
	private UnoHandView handView;

	private TextView actionText;

	private int selectedIndex;
	private SeekBar handSeekBar;
	private int startingHandCard;

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


		handView.setCurrentPlayerId(playerNum);

		ArrayList<Integer> displayOrder = new ArrayList<>();
		ArrayList<Integer> tempOrder = new ArrayList<>();
		boolean playerFound = false;
		for(int i=0; i < gameState.getHandsSize(); i++) {
			if(i != playerNum && !playerFound)
			{
				tempOrder.add(i);
			}
			else
			{
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

		if (motionEvent.getAction() == motionEvent.ACTION_UP) {
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
				if (card.getFace() == Face.DRAWFOUR || card.getFace() == Face.WILD) {
					tableView.setTempWildFace(card.getFace());
					tableView.setWildCardSelection(true);
					handView.setWildCardSelection(true);
					tableView.invalidate();
					handView.invalidate();
					return true;
				}

				action = new PlaceCardAction(this, card);
				game.sendAction(action);
				selectedIndex = 0;
				handView.setSelectedIndex(0);
				tableView.invalidate();
				handView.invalidate();


			// lastly, check if color wheel is tapped (black means out of bounds)
			} else if (tableView.getTappedColor(
				motionEvent.getX(), motionEvent.getY()) != CardColor.BLACK
				&& tableView.getWildCardSelection()) {

				CardColor color = tableView.getTappedColor(motionEvent.getX(), motionEvent.getY());
				Card card = myHand.get(selectedIndex);
				card.setColor(color);

				action = new PlaceCardAction(this, card);
				game.sendAction(action);
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
			for(int i=0; i < myHand.size(); i++)
			{
				Card c = myHand.get(i);
				if(c.getRender().isClicked(motionEvent.getX(), motionEvent.getY()))
				{
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

		// Sets a Listener for views and buttons users can touch
		tableView.setOnTouchListener(this);
		handView.setOnTouchListener(this);
		handSeekBar.setOnSeekBarChangeListener(this);

		//tableView.setPlayerNameText(allPlayerNames[0], allPlayerNames[1], allPlayerNames[2], allPlayerNames[3]);



	}

	// Set hand view based on progress bar
	// Gains ability for player to access cards not in initial view
	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (myHand.size() > 4) {
			handSeekBar.setMax(myHand.size() - 4);
			handView.setStartingCard(i);
			handView.invalidate();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
