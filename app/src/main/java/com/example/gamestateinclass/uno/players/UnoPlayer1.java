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
		if (!(info instanceof UnoState)) {
			flash(0xFFFF0000, 200);
			return;
		}

		UnoState gameState = (UnoState) info;

		// Send state info to the views
		tableView.setState(gameState);
		handView.setState(gameState);

		// Set string to display and size texts
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

		if (view instanceof UnoTableView) {
			Card fakeDrawCard = tableView.getFakeDrawCard();

			if (fakeDrawCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				action = new DrawCardAction(this);
				game.sendAction(action);

			} else if (topCard.getRender().isClicked(motionEvent.getX(), motionEvent.getY())) {

				Card card = myHand.get(selectedIndex);
				action = new PlaceCardAction(this, card);
				game.sendAction(action);
				selectedIndex = 0;
				handView.setSelectedIndex(0);

			}

			return true;
		}

		if (view instanceof UnoHandView) {
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
	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		handSeekBar.setMax(myHand.size()-1);
		handView.setStartingCard(i);
		handView.invalidate();
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}
}
