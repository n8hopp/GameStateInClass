package com.example.gamestateinclass;

import static org.junit.Assert.assertTrue;

import android.view.MotionEvent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.example.gamestateinclass.game.GameFramework.actionMessage.MyNameIsAction;
import com.example.gamestateinclass.game.GameFramework.actionMessage.ReadyAction;
import com.example.gamestateinclass.game.GameFramework.players.GamePlayer;
import com.example.gamestateinclass.R;
import com.example.gamestateinclass.uno.UnoLocalGame;
import com.example.gamestateinclass.uno.UnoMainActivity;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.unoActionMessage.UnoMoveAction;

@RunWith(RobolectricTestRunner.class)
public class UnoTests {

    public UnoMainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(UnoMainActivity.class).create().resume().get();
    }

    @Test
    public void test_CopyConstructorOfState_Empty(){
        UnoState unoState1 = new UnoState();
        UnoState copyUnoState1 = new UnoState(unoState1);
        assertTrue("Copy Constructor did not produce equal States", unoState1.equals(copyUnoState1));
    }

    @Test
    public void test_Equals_State_Empty(){
        UnoState unoState = new UnoState();
        UnoState otherUnoState = new UnoState();
        assertTrue("Equals method did not agree the States were equal", unoState.equals(otherUnoState));
    }

}