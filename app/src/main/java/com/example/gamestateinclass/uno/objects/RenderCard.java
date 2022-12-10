package com.example.gamestateinclass.uno.objects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.Serializable;

public class RenderCard implements Serializable {
	private float x,y;
	private float width;
	private float length;
	private transient Paint paint;
	private transient Paint strokePaint;
	private transient Paint mainLabelPaint;
	private transient Paint miniLabelPaint;

	private transient Drawable reverse, drawFour, drawTwo, skip;
	private Face face;
	private CardColor cardColor;

	public RenderCard(Face _face, CardColor _cardColor){
		x = y = 300;
		width = 200;
		length = 300;
		paint = new Paint();
		strokePaint = new Paint();
		mainLabelPaint = new Paint();
		miniLabelPaint = new Paint();

		face = _face;
		cardColor = _cardColor;

		/* Any time we change CardColor we need to call "setPaintFromEnum(CardColor)".
		 * This is because we want the card's color to be associated with an enum (for simplicity of naming)
		 * At some point along the line we need to translate that enum into an actual ARGB paint.
		 * That's what setPaintFromEnum does.
		 */
		setPaintfromEnum(_cardColor);

		strokePaint.setColor(Color.WHITE);
		strokePaint.setStyle(Paint.Style.STROKE);
		mainLabelPaint.setColor(Color.WHITE);
		mainLabelPaint.setTextSize(width/2);
		miniLabelPaint.setColor(Color.WHITE);
		miniLabelPaint.setTextSize(width/4);
		mainLabelPaint.setTextAlign(Paint.Align.CENTER);
		miniLabelPaint.setTextAlign(Paint.Align.CENTER);

	}

	/* DrawRect, by default, takes the distance of the left edge from the left edge of the canvas
	and vice-versa for the coordinates, instead of from a center point like drawCircle.
	By doing it this way, dividing our width and length by 2, we can define a card as the x,y center point
	and it's a little nicer to deal with
	 */
	public RenderCard(float _x, float _y, float _width, float _length, Card _card) {
		x = _x;
		y = _y;
		width = _width;
		length = _length;
		setPaintfromEnum(_card.getCardColor());
	}

	public void setCenter(float _x, float _y) {
		x = _x;
		y = _y;
	}

	public void setPaintfromEnum(CardColor _cardColor) {
		switch(_cardColor) {
			case BLACK:
				paint.setARGB(255, 0, 0,0);
				break;
			case RED:
				paint.setColor(0xFFC40C00);
				break;
			case BLUE:
				paint.setColor(0xFF0849A3);
				break;
			case GREEN:
				paint.setColor(0xFF358716);
				break;
			case YELLOW:
				paint.setColor(0xFFE5D30C);
				break;
		}
	}

	public void setHighlight(int _color) {
		if (strokePaint == null) strokePaint = new Paint();
		strokePaint.setColor(_color);
	}

	// Draws a rectangle, with the color given, the oval on top, and the text for the card type.
	public void draw(Canvas canvas) {

		float left = x-width/2;
		float right = x+width/2;
		float top = y-length/2;
		float bottom = y+length/2;
		if (paint == null)
		{
			paint = new Paint();
			strokePaint = new Paint();
			mainLabelPaint = new Paint();
			miniLabelPaint = new Paint();
			strokePaint.setColor(Color.WHITE);
			strokePaint.setStyle(Paint.Style.STROKE);
			mainLabelPaint.setColor(Color.WHITE);
			mainLabelPaint.setTextSize(width/2);
			miniLabelPaint.setColor(Color.WHITE);
			miniLabelPaint.setTextSize(width/4);
			mainLabelPaint.setTextAlign(Paint.Align.CENTER);
			miniLabelPaint.setTextAlign(Paint.Align.CENTER);
			setPaintfromEnum(cardColor);

		}
		strokePaint.setStrokeWidth(width/10);
		canvas.drawRect(left, top, right, bottom, paint);
		canvas.drawRect(left, top, right, bottom, strokePaint);
		strokePaint.setStrokeWidth(width/20);
		canvas.save();
		canvas.rotate(45, x, y);

		float ovalLeft = (float)(x-width*(0.375));
		float ovalRight = (float)(x+width*(0.375));
		float ovalTop = (float)(y-length*(0.4));
		float ovalBottom = (float)(y+length*(0.4));

		canvas.drawOval(ovalLeft, ovalTop, ovalRight, ovalBottom, strokePaint);
		canvas.restore();
		drawCardNumber(canvas);

	}

	public void drawCardNumber(Canvas canvas) {
		float mainTextY = y - (mainLabelPaint.descent() + mainLabelPaint.ascent()) / 2;
		float miniTextY = y - (miniLabelPaint.descent() + miniLabelPaint.ascent()) / 2;
		int leftBound = (int)(x-80);
		int topBound = (int)(y-80);
		int rightBound = (int)(x+80);
		int bottomBound = (int)(y+80);
		int miniTopLeftBound = (int)(x-width*0.35-20);
		int miniTopTopBound = (int)(y-length*0.35-20);
		int miniTopRightBound = (int)(x-width*0.35+20);
		int miniTopBottomBound = (int)(y-length*0.35+20);

		int miniBottomLeftBound = (int)(x+width*0.35-20);
		int miniBottomTopBound = (int)(y+length*0.35-20);
		int miniBottomRightBound = (int)(x+width*0.35+20);
		int miniBottomBottomBound = (int)(y+length*0.35+20);
		switch(face) {

			case SKIP:
				skip.setBounds(leftBound, topBound, rightBound, bottomBound);
				skip.draw(canvas);
				skip.setBounds(miniTopLeftBound, miniTopTopBound, miniTopRightBound, miniTopBottomBound);
				skip.draw(canvas);
				skip.setBounds(miniBottomLeftBound, miniBottomTopBound, miniBottomRightBound, miniBottomBottomBound);
				skip.draw(canvas);
				break;
			case DRAWTWO:
				drawTwo.setBounds(leftBound, topBound, rightBound, bottomBound);
				drawTwo.draw(canvas);
				canvas.drawText("+2", (float)(x-width*0.35), (float)(miniTextY-length*0.35), miniLabelPaint);
				canvas.drawText("+2", (float)(x+width*0.35), (float)(miniTextY+length*0.35), miniLabelPaint);
				break;
			case DRAWFOUR:
				drawFour.setBounds(leftBound, topBound, rightBound, bottomBound);
				drawFour.draw(canvas);
				canvas.drawText("+4", (float)(x-width*0.35), (float)(miniTextY-length*0.35), miniLabelPaint);
				canvas.drawText("+4", (float)(x+width*0.35), (float)(miniTextY+length*0.35), miniLabelPaint);
				break;
			case WILD:
				canvas.drawText("WILD", x, mainTextY, mainLabelPaint);
				break;
			case REVERSE:
				reverse.setBounds(leftBound, topBound, rightBound, bottomBound);
				reverse.draw(canvas);
				reverse.setBounds(miniTopLeftBound, miniTopTopBound, miniTopRightBound, miniTopBottomBound);
				reverse.draw(canvas);
				reverse.setBounds(miniBottomLeftBound, miniBottomTopBound, miniBottomRightBound, miniBottomBottomBound);
				reverse.draw(canvas);
				break;
			case NONE:
				break;
			default:
				canvas.drawText(String.valueOf(face.faceID), x, mainTextY, mainLabelPaint);
				canvas.drawText(String.valueOf(face.faceID), (float)(x-width*0.35), (float)(miniTextY-length*0.35), miniLabelPaint);
				canvas.drawText(String.valueOf(face.faceID), (float)(x+width*0.35), (float)(miniTextY+length*0.35), miniLabelPaint);
				break;
		}
		// if(cardNumber > 10)

	}

	public void setLengthWidth(int _length, int _width) {
		length = _length;
		width = _width;
	}

	public boolean isClicked(float clickX, float clickY) {
		float left = x-width/2;
		float right = x+width/2;
		float top = y-length/2;
		float bottom = y+length/2;

		return (clickX >= left) && (clickX <= right) && (clickY >= top) && (clickY <= bottom);
	}

	public void setFaceBitmaps(Drawable skip, Drawable reverse, Drawable drawTwo, Drawable drawFour)
	{
		this.skip = skip;
		this.reverse = reverse;
		this.drawTwo = drawTwo;
		this.drawFour = drawFour;
	}
}
