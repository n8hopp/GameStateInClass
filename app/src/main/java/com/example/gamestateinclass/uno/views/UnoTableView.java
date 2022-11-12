package com.example.gamestateinclass.uno.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.gamestateinclass.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gamestateinclass.uno.objects.Card;

public class UnoTableView extends FlashSurfaceView {
	/* We can "hard-code" the "hands" of each 3 opponents which will be a face down
		Uno card art. Each of those players will need a dynamic textView for their
		number of cards, which will likely need to be accessed by other classes
	 */
	private final Paint cardPaint;
	private final Paint textPaint;
	private final Paint textPaint2;
	private final Paint tableColor;
	private final Paint arrowPaint;
	private Paint faceUp;
	private Path arrowPath;
	private Card testCard;


	//	For the sake of these text views, the human player is p1, and other player
	//	numbers count up in order, clockwise

	public TextView p2hand;
	public TextView p3hand;
	public TextView p4hand;
	public int arrowPos; // 1: human player. Increases clockwise

	public UnoTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);

		cardPaint = new Paint();
		textPaint = new Paint();
		arrowPaint = new Paint();
		tableColor = new Paint();
		textPaint2 = new Paint();
		arrowPos = 1;

		p2hand.setText("7 Cards");
		p3hand.setText("7 Cards");
		p4hand.setText("7 Cards");

		tableColor.setARGB(255, 66, 143, 70);
		tableColor.setStyle(Paint.Style.FILL);

		arrowPaint.setARGB(255, 0, 255, 255);
		arrowPaint.setStyle(Paint.Style.FILL);
		arrowPath = new Path();

		cardPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextAlign(Paint.Align.CENTER);

	}


	@Override
	protected void onDraw(Canvas canvas) {

		//Background
		canvas.drawRect(0, 0, (getWidth()), (getHeight()), tableColor);

		// Top card
		canvas.drawRect((getWidth()/2)-100, 50, (getWidth()/2)+100, 350, cardPaint);
		canvas.drawText("3 cards", (getWidth()/2), 200, textPaint);

		// Left card
		canvas.drawRect(50, (getHeight()/2)-100, 350, (getHeight()/2)+100, cardPaint);
		canvas.drawText("4 cards", 200, (getHeight()/2)+5, textPaint);

		// Right card
		canvas.drawRect((getWidth()-350), (getHeight()/2)-100, (getWidth()-50), (getHeight()/2)+100, cardPaint);
		canvas.drawText("6 cards", (getWidth()-200), (getHeight()/2)+5, textPaint);

		// Face up middle card
		canvas.drawRect((getWidth()/2)+25,  (getHeight()/2)-150, (getWidth()/2)+225, (getHeight()/2)+150, cardPaint); //
		// Lukas: Added 30 pixel border to match HandView
		canvas.drawRect((getWidth()/2)+25+25,  (getHeight()/2)-150+25, (getWidth()/2)+225-25, (getHeight()/2)+150-25, faceUp);
		// Draw big number on card

		canvas.drawText("7", getWidth()/2 + 100, getHeight()/2 +30 , textPaint2);
		canvas.drawText("PLACE", getWidth()/2+125,  (getHeight()/2)+200, textPaint);

		// Face down middle card
		canvas.drawRect((getWidth()/2)-225,  (getHeight()/2)-150, (getWidth()/2)-25, (getHeight()/2)+150, cardPaint);
		canvas.drawText("DRAW", getWidth()/2-125,  (getHeight()/2)+200, textPaint);

		testCard.draw(canvas);
		// Lukas: I will implement the drawArrow function

		drawArrowPath(arrowPaint, arrowPath, arrowPos);
		canvas.drawPath(arrowPath, arrowPaint);
		arrowPath.reset();

	}

	// Dummied up
	private void drawArrowPath(Paint _arrowPaint, Path _arrowPath, int _arrowPos){
		arrowPath.moveTo(getWidth()/2, (getHeight()/8)*7); // Beginning location center of 3/4 down the screen
		arrowPath.lineTo(getWidth()/2 - 60, (getHeight()/8)*7); // Tip of arrow pointing left
		arrowPath.lineTo(getWidth()/2 - 20, (getHeight()/8)*7 - 20);
		arrowPath.lineTo(getWidth()/2 - 20, (getHeight()/8)*7 - 10);
		arrowPath.lineTo(getWidth()/2 + 60, (getHeight()/8)*7 - 10);
		arrowPath.lineTo(getWidth()/2 + 60, (getHeight()/8)*7 + 10);

		arrowPath.lineTo(getWidth()/2 - 20, (getHeight()/8)*7 + 10);
		arrowPath.lineTo(getWidth()/2 - 20, (getHeight()/8)*7 + 20);
		arrowPath.lineTo(getWidth()/2 - 60, getHeight()/8*7); // Tip of arrow pointing left

		/*
		switch (arrowPos){
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:

				break;
		}
*/

	}
}
