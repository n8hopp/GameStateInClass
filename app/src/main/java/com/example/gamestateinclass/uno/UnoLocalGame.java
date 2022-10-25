package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.actionMessage.GameAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;

public class UnoLocalGame extends LocalGame {
	@Override
	protected void sendUpdatedStateTo(GamePlayer p) {

	}

	@Override
	protected boolean canMove(int playerIdx) {
		return false;
	}

	@Override
	protected String checkIfGameOver() {
		return null;
	}

	@Override
	protected boolean makeMove(GameAction action) {
		return false;
	}
}
