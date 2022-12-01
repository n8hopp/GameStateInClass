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
import com.example.gamestateinclass.uno.objects.Face;
import com.example.gamestateinclass.uno.views.UnoHandView;
import com.example.gamestateinclass.uno.views.UnoTableView;

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

		// Set string to display and size texts for all player hands
		String p0HandSize = gameState.fetchPlayerHand(0).size() + " Cards";
		String p1HandSize = gameState.fetchPlayerHand(1).size() + " Cards";
		String p2HandSize = gameState.fetchPlayerHand(2).size() + " Cards";
		String p3HandSize = gameState.fetchPlayerHand(3).size() + " Cards";
		tableView.setPlayerHandText(p0HandSize, p1HandSize, p2HandSize, p3HandSize);

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

			Log.i("color", tableView.getTappedColor(
					motionEvent.getX(), motionEvent.getY()).name());

			Card fakeDrawCard = tableView.getFakeDrawCard();

			// if the "fake" draw card was touched, send the drawCardAction
			if (fakeDrawCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				action = new DrawCardAction(this);
				game.sendAction(action);

			// otherwise, see if topCard was touched, if so place the currently selected card in hand
			} else if (topCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				Card card = myHand.get(selectedIndex);

				if (card.getFace() == Face.DRAWFOUR || card.getFace() == Face.WILD) {
					tableView.setWildCardSelection(true);
					tableView.invalidate();
					return true;
				}

				action = new PlaceCardAction(this, card);
				game.sendAction(action);
				selectedIndex = 0;
				handView.setSelectedIndex(0);

			}

			return true;
		}

		if (view instanceof UnoHandView) {

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

		actionText = activity.findViewById(R.id.lobbyInfoText);
		handSeekBar = activity.findViewById(R.id.scrollHandSeekBar);

		// Sets a Listener for views and buttons users can touch
		tableView.setOnTouchListener(this);
		handView.setOnTouchListener(this);
		handSeekBar.setOnSeekBarChangeListener(this);


	}

	// Set hand view based on progress bar
	// Gains ability for player to access cards not in initial view
	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		if (myHand.size() > 6) {
			handSeekBar.setMax(myHand.size() - 6);
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
