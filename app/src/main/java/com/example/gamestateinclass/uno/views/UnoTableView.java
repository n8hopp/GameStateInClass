package com.example.gamestateinclass.uno.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.example.gamestateinclass.R;
import com.example.gamestateinclass.game.GameFramework.utilities.FlashSurfaceView;
import com.example.gamestateinclass.uno.infoMessage.UnoState;
import com.example.gamestateinclass.uno.objects.Card;
import com.example.gamestateinclass.uno.objects.CardColor;
import com.example.gamestateinclass.uno.objects.Face;
import com.example.gamestateinclass.uno.objects.RenderCard;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
	private final Paint blackPaint;
	private final Paint redPaint;
	private final Paint bluePaint;
	private final Paint greenPaint;
	private final Paint yellowPaint;
	private Paint faceUp;
	private Path arrowPath;
	private Path trianglePath;
	private Card testCard;
	private int arrowDirection;

	protected UnoState state;
	private ArrayList<Integer> displayOrder;

	//	For the sake of these text strings, the human player is p0, and other player
	//	numbers count up in order, clockwise
	private String p0hand;
	private String p1hand;
	private String p2hand;
	private String p3hand;

	private String p0name;
	private String p1name;
	private String p2name;
	private String p3name;

	private String actionText;
	private final Paint actionPaint;
	private final Paint actionTextPaint;

	private String introText;
	private Paint introTextPaint;
	private final Paint introPaint;

	private Card fakeDrawCard;

	public int arrowPos; // 0: human player. Increases clockwise

	public boolean wildCardSelection; // whether or not a wild card color is being selected
	public Face tempWildFace; // this is the face to be drawn as the top card when a color is being selected
	public int colorRadius; // radius of color selection wheel

	public int colorWheelOffsetX;
	public int colorWheelOffsetY; // these are the offsets from the screen's center

	Drawable skip;
	Drawable reverse;
	Drawable drawTwo;
	Drawable drawFour;

	public int wheelCenterX;
	public int wheelCenterY; // to be declared when canvas is accessible

	public UnoTableView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setWillNotDraw(false);

		cardPaint = new Paint();
		textPaint = new Paint();
		arrowPaint = new Paint();
		faceUp = new Paint();
		tableColor = new Paint();
		textPaint2 = new Paint();
		blackPaint = new Paint();
		redPaint = new Paint();
		bluePaint = new Paint();
		greenPaint = new Paint();
		yellowPaint = new Paint();
		testCard = new Card();
		actionPaint = new Paint();
		actionTextPaint = new Paint();
		introTextPaint = new Paint();
		introPaint = new Paint();
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


		textPaint.setTextSize(45);

		blackPaint.setColor(Color.BLACK);
		redPaint.setColor(0xFFC40C00);
		greenPaint.setColor(0xFF358716);
		bluePaint.setColor(0xFF0849A3);
		yellowPaint.setColor(0xFFE5D30C);

		skip = ResourcesCompat.getDrawable(getResources(),R.drawable.uno_skip, context.getTheme());
		reverse = ResourcesCompat.getDrawable(getResources(),R.drawable.uno_reverse, context.getTheme());
		drawTwo = ResourcesCompat.getDrawable(getResources(),R.drawable.uno_drawtwo, context.getTheme());
		drawFour = ResourcesCompat.getDrawable(getResources(),R.drawable.uno_drawfour, context.getTheme());

		p0hand = "7 Cards";
		p1hand = "7 Cards";
		p2hand = "7 Cards";
		p3hand = "7 Cards";

		p0name = "";
		p1name = "";
		p2name = "";
		p3name = "";

		actionText = "test";
		actionPaint.setARGB(130, 31, 61, 12);
		actionTextPaint.setTextSize(50);
		actionTextPaint.setFakeBoldText(true);
		actionTextPaint.setARGB(255, 255, 255, 255); // Text color white
		actionTextPaint.setTextAlign(Paint.Align.CENTER);

		introText = "test";
		introPaint.setARGB(255,0,255,255);
		introTextPaint.setARGB(255, 0, 0, 0);
		introTextPaint.setFakeBoldText(true);
		introTextPaint.setTextSize(25);
		introTextPaint.setTextAlign(Paint.Align.CENTER);



		// a "fake" card that is acts as a button for drawing
		fakeDrawCard = new Card(CardColor.BLACK, Face.NONE);

		wildCardSelection = false;
		tempWildFace = null;

		colorRadius = 100;
		colorWheelOffsetX = 425;
		colorWheelOffsetY = 300;

		trianglePath = new Path(); //for wild card selection
	}


	public void setState(UnoState _state) {
		state = _state;
	}


	public Card getFakeDrawCard() {
		return fakeDrawCard;
	}


	@Override
	protected void onDraw(Canvas canvas) {

		wheelCenterX = canvas.getWidth() / 2 + colorWheelOffsetX;
		wheelCenterY = canvas.getHeight() / 2 + colorWheelOffsetY;

		//Background
		canvas.drawRect(0, 0, (getWidth()), (getHeight()), tableColor);

		// Left card
		canvas.drawRect(50, (getHeight()/2)-100, 350, (getHeight()/2)+100, cardPaint);
		canvas.drawText(p1hand, 200, (getHeight()/2)+5, textPaint);
		canvas.drawText(p1name, 200, (getHeight()/2)-110, textPaint);

		// Top card
		canvas.drawRect((getWidth()/2)-100, 50, (getWidth()/2)+100, 350, cardPaint);
		canvas.drawText(p2hand, (getWidth()/2), 200, textPaint);
		canvas.drawText(p2name, (getWidth()/2), 40, textPaint);

		// Right card
		canvas.drawRect((getWidth()-350), (getHeight()/2)-100, (getWidth()-50), (getHeight()/2)+100, cardPaint);
		canvas.drawText(p3hand, (getWidth()-200), (getHeight()/2)+5, textPaint);
		canvas.drawText(p3name, (getWidth()-200), (getHeight()/2)-110, textPaint);


		// Player hand text
		canvas.drawText(p0hand, getWidth()/2, (getHeight()/15) * 14, textPaint);
		canvas.drawText(p0name, getWidth()/2, getHeight() - 10 , textPaint);

		canvas.drawText("PLACE", getWidth()/2+125,  (getHeight()/2)+200, textPaint);

		// Draw a small "canvas" for action text to be drawn on
		canvas.drawRect(40, (getHeight()/4)*3-50, (getWidth()-40), (getHeight()/4)*3+30, actionPaint);
		canvas.drawText(actionText, getWidth()/2,  (getHeight()/4)*3+10, actionTextPaint);

        if ( !introText.equals("")) {
			canvas.drawRect(getWidth()/5, (getHeight()/4)*3+80, (getWidth()/5)*4, (getHeight()/4)*3+120, introPaint);
			canvas.drawText(introText, getWidth() / 2, (getHeight() / 4) * 3 + 110, introTextPaint);
		}


		if (state != null) {
			if (!wildCardSelection) {
				RenderCard topCardRender = state.getTopCard().getRender();
				topCardRender.setFaceBitmaps(skip, reverse, drawTwo, drawFour);
				topCardRender.setCenter(getWidth()/2+125,  getHeight()/2);
				topCardRender.draw(canvas);
			} else {
				Card wildTopCard = new Card(CardColor.BLACK, tempWildFace);
				RenderCard wildTopCardRender = wildTopCard.getRender();
				wildTopCardRender.setFaceBitmaps(skip, reverse, drawTwo, drawFour);
				wildTopCardRender.setCenter(getWidth()/2+125,  getHeight()/2);
				wildTopCardRender.draw(canvas);
			}

			arrowPos = displayOrder.indexOf(state.getTurn());
			arrowDirection = state.getDirection().value;
		}

		RenderCard fakeDrawCardRender = fakeDrawCard.getRender();
		fakeDrawCardRender.setFaceBitmaps(skip, reverse, drawTwo, drawFour);
		fakeDrawCardRender.setCenter(getWidth()/2-125,  getHeight()/2);
		fakeDrawCardRender.draw(canvas);
		canvas.drawText("DRAW", getWidth()/2-125,  (getHeight()/2)+200, textPaint);


		drawArrowPath(arrowPaint, arrowPath, arrowPos, arrowDirection);
		canvas.drawPath(arrowPath, arrowPaint);
		arrowPath.reset();

		if (wildCardSelection) {
			drawWildCardGUI(canvas);
		}
	}

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

	public void drawWildCardGUI(Canvas canvas) {
		canvas.drawCircle(wheelCenterX, wheelCenterY, 140, blackPaint);

		trianglePath.moveTo(wheelCenterX, wheelCenterY-140);
		trianglePath.lineTo(wheelCenterX-140, wheelCenterY);
		trianglePath.lineTo(getWidth()/2+150, getHeight()/2+50);
		canvas.drawPath(trianglePath, blackPaint);

		RectF rectF = new RectF();
		rectF.set(wheelCenterX - colorRadius,
				wheelCenterY - colorRadius,
				wheelCenterX + colorRadius,
				wheelCenterY + colorRadius);

		canvas.drawArc(rectF, 0, 90, true, greenPaint);
		canvas.drawArc(rectF, 90, 90, true, yellowPaint);
		canvas.drawArc(rectF, 180, 90, true, redPaint);
		canvas.drawArc(rectF, 270, 90, true, bluePaint);

		Log.i("x", ""+wheelCenterX);
		Log.i("y", ""+wheelCenterY);

	}

	public CardColor getTappedColor(float mouse_x, float mouse_y) {

		double a = mouse_x - wheelCenterX;
		double b = mouse_y - wheelCenterY;
		double distance = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));

		// checks if mouse is within circle, return black if not
		if (distance > colorRadius) {
			return CardColor.BLACK;
		}

		// if mouse is within circle, figure out which quadrant
		boolean x = mouse_x > wheelCenterX;
		boolean y = mouse_y > wheelCenterY;

		switch (x + "-" + y) {
			case "true-true":
				return CardColor.GREEN;
			case "true-false":
				return CardColor.BLUE;
			case "false-true":
				return CardColor.YELLOW;
			case "false-false":
				return CardColor.RED;
			default:
				return CardColor.BLACK;
		}
	}

	public void setWildCardSelection(boolean bool) {
		wildCardSelection = bool;
	}

	public boolean getWildCardSelection() {
		return wildCardSelection;
	}

	public void setTempWildFace(Face face) {
		tempWildFace = face;
	}

	public void setPlayerNameText(String _p0name, String _p1name, String _p2name, String _p3name){
		p0name = _p0name;
		p1name = _p1name;
		p2name = _p2name;
		p3name = _p3name;
	}

	public void setActionText(String _actionText){
		actionText = _actionText;
	}
	public void setIntroText(String _introText) { introText = _introText; }

	public UnoState getState(){
		return state;
	}

	public void setDisplayOrder(ArrayList<Integer> display) { displayOrder = display;}

	public ArrayList<Integer> getPlayerId() { return displayOrder;}
}
