package com.example.gamestateinclass.uno;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.gameConfiguration.*;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.players.*;

import java.util.ArrayList;

public class UnoMainActivity extends GameMainActivity {

    private static final String TAG = "UnoMainActivity";
    public static final int PORT_NUMBER = 5213;

    @Override
    public GameConfig createDefaultConfig() {
        ArrayList<GamePlayerType> playerTypes = new ArrayList<GamePlayerType>();

        // Human Player
        playerTypes.add(new GamePlayerType("Local Human Player") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new UnoPlayer1(name, R.layout.human_player_1);
            }

        });

        // dumb computer player 1
        playerTypes.add(new GamePlayerType("Computer Player 1 (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new UnoComputerPlayer2(name);
            }
        });

        // smarter computer player 2
        playerTypes.add(new GamePlayerType("Computer Player 2 (smart)") {
            public GamePlayer createPlayer(String name) {
                return new UnoComputerPlayer3(name);
            }
        });

        // dumb computer player 3
        playerTypes.add(new GamePlayerType("Computer Player 3 (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new UnoComputerPlayer4(name);
            }
        });

        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 2, "Uno", PORT_NUMBER);

        defaultConfig.addPlayer("Human", 0); //
        defaultConfig.addPlayer("Computer 1", 1); // dumb computer player
        defaultConfig.addPlayer("Computer 2", 2); // smart computer player
        defaultConfig.addPlayer("Computer 3", 3); // dumb computer player

        return defaultConfig;
    }

    // Dummied up for now
    @Override
    public LocalGame createLocalGame(GameState gameState){
        if(gameState == null)
            return new UnoLocalGame();
        return new UnoLocalGame((UnoState) gameState);
    }

}