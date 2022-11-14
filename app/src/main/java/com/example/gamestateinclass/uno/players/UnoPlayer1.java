package com.example.gamestateinclass.uno.players;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.uno.PlaceCardAction;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.views.UnoHandView;
import com.example.gamestateinclass.uno.views.UnoTableView;

import java.util.ArrayList;

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener, View.OnClickListener {
	private int layoutId;
	private UnoTableView tableView;
	private UnoHandView handView;

	private Button placeButton;
	private Button selectButton;

	private int selectedIndex;

	private ArrayList<Card> myHand;

	/**
	 * constructor
	 *
	 * @param name the name of the player
	 */
	public UnoPlayer1(String name, int _layoutId) {
		super(name);
		this.layoutId = _layoutId;

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

		tableView.setState(gameState);
		handView.setState(gameState);

		String p1HandSize = gameState.fetchPlayerHand(1).size() + " Cards";
		String p2HandSize = gameState.fetchPlayerHand(2).size() + " Cards";
		String p3HandSize = gameState.fetchPlayerHand(3).size() + " Cards";
		tableView.setPlayerHandText(p1HandSize, p2HandSize, p3HandSize);

		tableView.invalidate();
		handView.invalidate();

		myHand = gameState.fetchPlayerHand(playerNum);
	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {

		if (view instanceof UnoTableView) {
			return true;
		}

		if (view instanceof  UnoHandView) {
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

		placeButton = (Button) activity.findViewById(R.id.placeButton);
		selectButton = (Button) activity.findViewById(R.id.selectButton);

		tableView.setOnTouchListener(this);
		handView.setOnTouchListener(this);

		placeButton.setOnClickListener(this);
		selectButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		GameAction action = null;

		if (view.getId() == placeButton.getId()) {
			Card card = myHand.get(selectedIndex);

			action = new PlaceCardAction(this, card);

		} else if (view.getId() == selectButton.getId()) {

			selectedIndex++;
			selectedIndex %= myHand.size();
			Log.i("selected index", ""+selectedIndex);

			handView.setSelectedIndex(selectedIndex);
			handView.invalidate();

		}



		game.sendAction(action);
	}
}
