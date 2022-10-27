package com.example.gamestateinclass.uno.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.views.UnoSurfaceViewSouth;

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener {
	private int layoutId;

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

	}


	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void setAsGui(GameMainActivity activity) {

	}
}
