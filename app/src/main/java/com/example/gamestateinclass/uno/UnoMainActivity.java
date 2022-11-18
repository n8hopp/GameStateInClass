package com.example.gamestateinclass.uno;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.GameMainActivity;
import com.example.gamestateinclass.game.GameFramework.LocalGame;
import com.example.gamestateinclass.game.GameFramework.gameConfiguration.*;
import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.game.GameFramework.utilities.Logger;
import com.example.gamestateinclass.game.GameFramework.utilities.Saving;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.players.*;

import java.util.ArrayList;

public class UnoMainActivity extends GameMainActivity {

    private static final String TAG = "UnoMainActivity";
    public static final int PORT_NUMBER = 5213;

    private GameConfig game = null;

    /**
     * Default configuration for our game is 1 human vs 3 computers
     */
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
                return new UnoComputerPlayerDumb(name);
            }
        });

        // dumb computer player 2
        playerTypes.add(new GamePlayerType("Computer Player 2 (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new UnoComputerPlayerDumb(name);
            }
        });

        // dumb computer player 3
        playerTypes.add(new GamePlayerType("Computer Player 3 (dumb)") {
            public GamePlayer createPlayer(String name) {
                return new UnoComputerPlayerDumb(name);
            }
        });

        GameConfig defaultConfig = new GameConfig(playerTypes, 4, 4, "Uno", PORT_NUMBER);

        defaultConfig.addPlayer("Human", 0); // human player
        defaultConfig.addPlayer("Computer 1", 1); // dumb computer player
        defaultConfig.addPlayer("Computer 2", 2); // smart computer player
        defaultConfig.addPlayer("Computer 3", 3); // dumb computer player

        game = defaultConfig;
        return defaultConfig;
    }

    /**
     * createLocalGame
     *
     * Creates a new game that runs on the server tablet,
     * @param gameState
     * 				the gameState for this game or null for a new game
     *
     * @return a new, game-specific instance of a sub-class of the LocalGame
     *         class.
     */
    @Override
    public LocalGame createLocalGame(GameState gameState){
        if(gameState == null)
            return new UnoLocalGame();
        return new UnoLocalGame((UnoState) gameState);
    }

    /**
     * saveGame, adds this games prepend to the filename
     *
     * @param gameName
     * 				Desired save name
     * @return String representation of the save
     */
    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame, adds this games prepend to the desire file to open and creates the game specific state
     * @param gameName
     * 				The file to open
     * @return The loaded GameState
     */
    @Override
    public GameState loadGame(String gameName){
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);
        return (GameState) new UnoState((UnoState) Saving.readFromFile(appName, this.getApplicationContext()));
    }


}