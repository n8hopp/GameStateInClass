package com.example.gamestateinclass.uno.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.gamestateinclass.game.GameFramework.utilities.FlashSurfaceView;

public class UnoSurfaceView extends FlashSurfaceView {
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


	//	For the sake of these text views, the human player is p1, and other player
	//	numbers count up in order, clockwise

	public TextView p2hand;
	public TextView p3hand;
	public TextView p4hand;

	public UnoSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWillNotDraw(false);

		cardPaint = new Paint();
		textPaint = new Paint();
		arrowPaint = new Paint();
		tableColor = new Paint();
		textPaint2 = new Paint();

		p2hand.setText("7 Cards");
		p3hand.setText("7 Cards");
		p4hand.setText("7 Cards");
	}


	@Override
	protected void onDraw(Canvas canvas) {

	}
}
