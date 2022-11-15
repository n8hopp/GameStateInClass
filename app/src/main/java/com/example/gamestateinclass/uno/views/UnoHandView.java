package com.example.gamestateinclass.uno.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.gamestateinclass.game.GameFramework.infoMessage.GameState;
import com.example.gamestateinclass.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.RenderCard;

import java.util.ArrayList;

public class UnoHandView extends FlashSurfaceView {

    public static final int cardHeight = 300;
    public static final int cardWidth = 200;
    public static final int cardBorder = 25;
    public static final int cardSpacing = 240;
    public static final int xOffset = 150;
    public static final int yOffset = 160;

    protected UnoState state;

    Paint redPaint = new Paint();
    Paint bluePaint = new Paint();
    Paint greenPaint = new Paint();
    Paint yellowPaint = new Paint();

    Paint[] cardColors = {redPaint, bluePaint, redPaint, yellowPaint, yellowPaint, greenPaint};
    int[] cardNumbers = {3, 6, 4, 8, 2, 6};

    Paint cardBorderPaint = new Paint();
    Paint backgroundPaint = new Paint();
    Paint unoTextPaint = new Paint();
    Paint numberPaint = new Paint();

    private int selectedIndex = 0;


    public UnoHandView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        redPaint.setColor(Color.RED);  //red
        redPaint.setStyle(Paint.Style.FILL);
        bluePaint.setColor(Color.BLUE);  //blue
        bluePaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(0xFF56BD46);  //green
        greenPaint.setStyle(Paint.Style.FILL);
        yellowPaint.setColor(0xFFE8C723);  //yellow
        yellowPaint.setStyle(Paint.Style.FILL);

        cardBorderPaint.setColor(0xFF000000);  //black
        cardBorderPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setARGB(255, 66, 143, 70);  //green
        backgroundPaint.setStyle(Paint.Style.FILL);

        unoTextPaint.setColor(Color.BLACK);
        unoTextPaint.setTextAlign(Paint.Align.CENTER);
        unoTextPaint.setTextSize(120);
        unoTextPaint.setFakeBoldText(true);

        numberPaint.setColor(Color.WHITE);
        numberPaint.setTextAlign(Paint.Align.CENTER);
        numberPaint.setTextSize(100);
        numberPaint.setFakeBoldText(true);


    }


    public void setState(UnoState _state) {
        state = _state;
    }


    public void setSelectedIndex(int index) {
        selectedIndex = index;
    }


    public void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), backgroundPaint);

        if (state != null) {
            ArrayList<Card> currentHand = state.fetchPlayerHand(0); // TODO: change to user id of current player

            for (int i = 0; i < currentHand.size(); i ++) {

                Card card = currentHand.get(i);
                RenderCard renderCard = card.getRender();
                renderCard.setCenter(xOffset + (i * cardSpacing), yOffset);

                if (i == selectedIndex) {
                    renderCard.setHighlight(Color.YELLOW);
                } else {
                    renderCard.setHighlight(Color.WHITE);
                }

                renderCard.draw(canvas);

        }

//            // Card border
//            canvas.drawRect( xOffset + i * (cardSpacing + cardWidth),
//                    yOffset,
//                    xOffset + i * (cardSpacing + cardWidth) + cardWidth,
//                    yOffset + cardHeight, cardBorderPaint);
//
//            // Main card rectangle
//            canvas.drawRect(xOffset + i * (cardSpacing + cardWidth) + cardBorder,
//                    yOffset + cardBorder,
//                    xOffset + i * (cardSpacing + cardWidth) + cardWidth - cardBorder,
//                    yOffset + cardHeight - cardBorder, cardColors[i]);
//
//            // Number value
//            canvas.drawText(""+cardNumbers[i],
//                    xOffset + i * (cardSpacing + cardWidth) + 100,
//                    yOffset + cardBorder + (cardHeight + cardBorder) / 2, numberPaint);

        }

        // Big UNO Button
//        int radius = getHeight();
//        canvas.drawCircle(getWidth(), getHeight(), radius, cardBorderPaint);
//        canvas.drawCircle(getWidth(), getHeight(), radius - 50, redPaint);
//
//        canvas.drawText("UNO", getWidth() - radius * 2 / 5, getHeight() - radius * 1 / 7, unoTextPaint);
    }
}

