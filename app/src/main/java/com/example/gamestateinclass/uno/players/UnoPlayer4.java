package com.example.gamestateinclass.uno.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameInfo;
import com.example.gamestateinclass.game.GameFramework.players.GameHumanPlayer;

public class UnoPlayer4 extends GameHumanPlayer implements View.OnTouchListener {

	/**
	 * constructor
	 *
	 * @param name the name of the player
	 */
	public UnoPlayer4(String name) {
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
