package com.example.gamestateinclass.uno.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;

public class UnoPlayer1 extends GameHumanPlayer implements View.OnTouchListener {

	/**
	 * constructor
	 *
	 * @param name the name of the player
	 */
	public UnoPlayer1(String name) {
		super(name);
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
