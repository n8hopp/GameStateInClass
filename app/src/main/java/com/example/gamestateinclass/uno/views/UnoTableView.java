package com.example.gamestateinclass.uno.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.gamestateinclass.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;
import com.example.gamestateinclass.uno.objects.RenderCard;

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
	private int arrowDirection;

	protected UnoState state;


	//	For the sake of these text strings, the human player is p0, and other player
	//	numbers count up in order, clockwise
	private String p0hand;
	private String p1hand;
	private String p2hand;
	private String p3hand;

//	private RenderCard fakeDrawCardRender;
	private Card fakeDrawCard;

	public int arrowPos; // 0: human player. Increases clockwise

	public UnoTableView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);

		cardPaint = new Paint();
		textPaint = new Paint();
		arrowPaint = new Paint();
		faceUp = new Paint();
		tableColor = new Paint();
		textPaint2 = new Paint();
		testCard = new Card();
		arrowPos = 0;
		cardPaint.setARGB(255, 0, 0, 0); // Set default color of black face down uno card
		textPaint.setARGB(255, 255, 255, 255); // Text color white
		textPaint2.setARGB(255,255,255,255);

		tableColor.setARGB(255, 66, 143, 70);
		tableColor.setStyle(Paint.Style.FILL);

		arrowPaint.setARGB(255, 0, 255, 255);
		arrowPaint.setStyle(Paint.Style.FILL);
		arrowPath = new Path();

		faceUp.setColor(0xFFFF0000);  //red

		cardPaint.setTextAlign(Paint.Align.CENTER);
		textPaint.setTextAlign(Paint.Align.CENTER);

		textPaint2.setTextSize(100); // Only text this paint uses currently is the number on top of face up card
		textPaint2.setFakeBoldText(true);
		textPaint.setTextSize(45);

		p0hand = "7 Cards";
		p1hand = "7 Cards";
		p2hand = "7 Cards";
		p3hand = "7 Cards";

//		fakeDrawCardRender = (new Card(CardColor.BLACK, Face.ZERO)).getRender();
//		fakeDrawCardRender.setCenter(getWidth()/2-125,  getHeight()/2);
		fakeDrawCard = new Card(CardColor.BLACK, Face.NONE);
	}


	public void setState(UnoState _state) {
		state = _state;
	}


	public Card getFakeDrawCard() {
		return fakeDrawCard;
	}


	@Override
	protected void onDraw(Canvas canvas) {

		//Background
		canvas.drawRect(0, 0, (getWidth()), (getHeight()), tableColor);

		// Left card
		canvas.drawRect(50, (getHeight()/2)-100, 350, (getHeight()/2)+100, cardPaint);
		canvas.drawText(p1hand, 200, (getHeight()/2)+5, textPaint);

		// Top card
		canvas.drawRect((getWidth()/2)-100, 50, (getWidth()/2)+100, 350, cardPaint);
		canvas.drawText(p2hand, (getWidth()/2), 200, textPaint);

		// Right card
		canvas.drawRect((getWidth()-350), (getHeight()/2)-100, (getWidth()-50), (getHeight()/2)+100, cardPaint);
		canvas.drawText(p3hand, (getWidth()-200), (getHeight()/2)+5, textPaint);

		// Player hand text
		canvas.drawText(p0hand, getWidth()/2, (getHeight()/15) * 14, textPaint);

		// Face up middle card
//		canvas.drawRect((getWidth()/2)+25,  (getHeight()/2)-150, (getWidth()/2)+225, (getHeight()/2)+150, cardPaint); //
		// Lukas: Added 30 pixel border to match HandView
//		canvas.drawRect((getWidth()/2)+25+25,  (getHeight()/2)-150+25, (getWidth()/2)+225-25, (getHeight()/2)+150-25, faceUp);
		// Draw big number on card

//		canvas.drawText("7", getWidth()/2 + 100, getHeight()/2 +30 , textPaint2);
		canvas.drawText("PLACE", getWidth()/2+125,  (getHeight()/2)+200, textPaint);

		if (state != null) {
			RenderCard topCardRender = state.getTopCard().getRender();
			topCardRender.setCenter(getWidth()/2+125,  getHeight()/2);
			topCardRender.draw(canvas);

			arrowPos = state.getTurn();
			arrowDirection = state.getDirection().value;
		}

		// Face down middle card
//		canvas.drawRect((getWidth()/2)-225,  (getHeight()/2)-150, (getWidth()/2)-25, (getHeight()/2)+150, cardPaint);

//		Card fakeDrawCard = new Card(CardColor.BLACK, Face.ZERO);

		RenderCard fakeDrawCardRender = fakeDrawCard.getRender();
		fakeDrawCardRender.setCenter(getWidth()/2-125,  getHeight()/2);
		fakeDrawCardRender.draw(canvas);
		canvas.drawText("DRAW", getWidth()/2-125,  (getHeight()/2)+200, textPaint);


//		testCard.getRender().draw(canvas);
		// Lukas: I will implement the drawArrow function

		drawArrowPath(arrowPaint, arrowPath, arrowPos, arrowDirection);
		canvas.drawPath(arrowPath, arrowPaint);
		arrowPath.reset();


	}

	// Dummied up
	private void drawArrowPath(Paint _arrowPaint, Path _arrowPath, int _arrowPos, int _arrowDirection) {

		// First switch statement is for clockwise direction
		if (_arrowDirection == 1) {
			switch (arrowPos) {
				case 0:
					arrowPath.moveTo(getWidth() / 2, (getHeight() / 8) * 7); // Beginning location center of just over 3/4 down the view
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 7); // Tip of arrow pointing left
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 7 + 20);
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 7 + 10);
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 7 + 10);
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 7 - 10);

					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 7 - 10);
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 7 - 20);
					arrowPath.lineTo(getWidth() / 2 - 60, getHeight() / 8 * 7); // Tip of arrow pointing left

					break;

				case 1:
					arrowPath.moveTo(getWidth() / 4, (getHeight() / 2)); // Beginning location center of ~1/4 in from left of view
					arrowPath.lineTo(getWidth() / 4, (getHeight() / 2) - 60); // Tip of arrow pointing up
					arrowPath.lineTo(getWidth() / 4 + 20, (getHeight() / 2) - 20);
					arrowPath.lineTo(getWidth() / 4 + 10, (getHeight() / 2) - 20);
					arrowPath.lineTo(getWidth() / 4 + 10, (getHeight() / 2) + 60);
					arrowPath.lineTo(getWidth() / 4 - 10, (getHeight() / 2) + 60);

					arrowPath.lineTo(getWidth() / 4 - 10, (getHeight() / 2) - 20);
					arrowPath.lineTo(getWidth() / 4 - 20, (getHeight() / 2) - 20);
					arrowPath.lineTo(getWidth() / 4, (getHeight() / 2) - 60); // Tip of arrow pointing up
					break;
				case 2:
					arrowPath.moveTo(getWidth() / 2, (getHeight() / 8) * 3); // Beginning location center of just over 1/4 down the view
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 3); // Tip of arrow pointing right
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 3 + 20);
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 3 + 10);
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 3 + 10);
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 3 - 10);

					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 3 - 10);
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 3 - 20);
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 3); // Tip of arrow pointing right

					break;
				case 3:
					arrowPath.moveTo((getWidth() / 4) * 3, (getHeight() / 2)); // Beginning location center of ~3/4 in from left of view
					arrowPath.lineTo((getWidth() / 4) * 3, (getHeight() / 2) + 60); // Tip of arrow pointing down
					arrowPath.lineTo((getWidth() / 4) * 3 - 20, (getHeight() / 2) + 20);
					arrowPath.lineTo((getWidth() / 4) * 3 - 10, (getHeight() / 2) + 20);
					arrowPath.lineTo((getWidth() / 4) * 3 - 10, (getHeight() / 2) - 60);
					arrowPath.lineTo((getWidth() / 4) * 3 + 10, (getHeight() / 2) - 60);

					arrowPath.lineTo((getWidth() / 4) * 3 + 10, (getHeight() / 2) + 20);
					arrowPath.lineTo((getWidth() / 4) * 3 + 20, (getHeight() / 2) + 20);
					arrowPath.lineTo((getWidth() / 4) * 3, (getHeight() / 2) + 60); // Tip of arrow pointing down

					break;
			}
		}

		// Second switch statement is for CCW direction
		else if (_arrowDirection == -1) {
			switch (arrowPos) {
				case 0:
					arrowPath.moveTo(getWidth() / 2, (getHeight() / 8) * 7); // Beginning location center of just over 3/4 down the view
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 7); // Tip of arrow pointing left
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 7 - 20);
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 7 - 10);
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 7 - 10);
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 7 + 10);

					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 7 + 10);
					arrowPath.lineTo(getWidth() / 2 + 20, (getHeight() / 8) * 7 + 20);
					arrowPath.lineTo(getWidth() / 2 + 60, getHeight() / 8 * 7); // Tip of arrow pointing left

					break;

				case 1:
					arrowPath.moveTo(getWidth() / 4, (getHeight() / 2)); // Beginning location center of ~1/4 in from left of view
					arrowPath.lineTo(getWidth() / 4, (getHeight() / 2) + 60); // Tip of arrow pointing up
					arrowPath.lineTo(getWidth() / 4 - 20, (getHeight() / 2) + 20);
					arrowPath.lineTo(getWidth() / 4 - 10, (getHeight() / 2) + 20);
					arrowPath.lineTo(getWidth() / 4 - 10, (getHeight() / 2) - 60);
					arrowPath.lineTo(getWidth() / 4 + 10, (getHeight() / 2) - 60);

					arrowPath.lineTo(getWidth() / 4 + 10, (getHeight() / 2) + 20);
					arrowPath.lineTo(getWidth() / 4 + 20, (getHeight() / 2) + 20);
					arrowPath.lineTo(getWidth() / 4, (getHeight() / 2) + 60); // Tip of arrow pointing up
					break;
				case 2:
					arrowPath.moveTo(getWidth() / 2, (getHeight() / 8) * 3); // Beginning location center of just over 1/4 down the view
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 3); // Tip of arrow pointing right
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 3 - 20);
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 3 - 10);
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 3 - 10);
					arrowPath.lineTo(getWidth() / 2 + 60, (getHeight() / 8) * 3 + 10);

					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 3 + 10);
					arrowPath.lineTo(getWidth() / 2 - 20, (getHeight() / 8) * 3 + 20);
					arrowPath.lineTo(getWidth() / 2 - 60, (getHeight() / 8) * 3); // Tip of arrow pointing right

					break;
				case 3:
					arrowPath.moveTo((getWidth() / 4) * 3, (getHeight() / 2)); // Beginning location center of ~3/4 in from left of view
					arrowPath.lineTo((getWidth() / 4) * 3, (getHeight() / 2) - 60); // Tip of arrow pointing down
					arrowPath.lineTo((getWidth() / 4) * 3 + 20, (getHeight() / 2) - 20);
					arrowPath.lineTo((getWidth() / 4) * 3 + 10, (getHeight() / 2) - 20);
					arrowPath.lineTo((getWidth() / 4) * 3 + 10, (getHeight() / 2) + 60);
					arrowPath.lineTo((getWidth() / 4) * 3 - 10, (getHeight() / 2) + 60);

					arrowPath.lineTo((getWidth() / 4) * 3 - 10, (getHeight() / 2) - 20);
					arrowPath.lineTo((getWidth() / 4) * 3 - 20, (getHeight() / 2) - 20);
					arrowPath.lineTo((getWidth() / 4) * 3, (getHeight() / 2) - 60); // Tip of arrow pointing down

					break;
			}
		}

	}

	public void setPlayerHandText(String _p0hand, String _p1hand, String _p2hand, String _p3hand){
		p0hand = _p0hand;
		p1hand = _p1hand;
		p2hand = _p2hand;
		p3hand = _p3hand;
	}
}
