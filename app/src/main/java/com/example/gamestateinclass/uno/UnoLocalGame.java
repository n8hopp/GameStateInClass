package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.infoMessage.UnoState;

public class UnoLocalGame extends LocalGame {

	private static final String TAG = "UnoLocalGame";

	public UnoLocalGame() {
		super();
		super.state = new UnoState();
	}

	public UnoLocalGame(UnoState unoState) {
		super();
		super.state = new UnoState(unoState);
	}

	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {
		p.sendInfo(new UnoState(((UnoState) state)));
	}

	@Override
	protected boolean canMove(int playerIdx) {
		return false;
	}

	@Override
	protected String checkIfGameOver() {
		UnoState state = (UnoState) super.state;
		// other Uno game specific things
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		return false;
	}
}
