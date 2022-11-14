package com.example.gamestateinclass.uno.players;

import android.view.MotionEvent;
import android.view.SurfaceView;
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

	private Button testButton;

	private ArrayList<Card> myHand;

	/**
	 * constructor
	 *
	 * @param name the name of the player
	 */
	public UnoPlayer1(String name, int _layoutId) {
		super(name);
		this.layoutId = _layoutId;
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

		testButton = (Button) activity.findViewById(R.id.testButton);

		tableView.setOnTouchListener(this);
		handView.setOnTouchListener(this);

		testButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		GameAction action;


		action = new PlaceCardAction(this, myHand.get(0));

		game.sendAction(action);
	}
}
