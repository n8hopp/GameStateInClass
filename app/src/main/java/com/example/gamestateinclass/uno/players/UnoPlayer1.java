package com.example.gamestateinclass.uno.players;

import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.views.UnoHandView;
import com.example.gamestateinclass.uno.views.UnoTableView;

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener {
	private int layoutId;
	private UnoTableView tableView;
	private UnoHandView handView;

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

		tableView.invalidate();
		handView.invalidate();

	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
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
	}
}
