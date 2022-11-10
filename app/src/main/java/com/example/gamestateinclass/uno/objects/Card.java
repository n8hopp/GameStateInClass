package com.example.gamestateinclass.uno.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Card {
	private float x,y;
	private float width;
	private float length;
	private Paint paint;
	private Paint strokePaint;
	private Paint mainLabelPaint;
	private Paint miniLabelPaint;
	private CardColor color;
	private Face face;

	public enum Face //
	{
		ZERO(0),
		ONE(1),
		TWO(2),
		THREE(3),
		FOUR(4),
		FIVE(5),
		SIX(6),
		SEVEN(7),
		EIGHT(8),
		NINE(9),
		REVERSE(10),
		SKIP(11),
		DRAWTWO(12),
		DRAWFOUR(13),
		WILD(14);

		private int faceID;

		Face(int ID) {
			this.faceID = ID;
		}

	}

	public enum CardColor
	{
		RED(0),BLUE(1),GREEN(2),YELLOW(3),BLACK(4);
		private int colorID;

		CardColor(int ID) {
			this.colorID = ID;
		}
	}
	public Card()
	{
		x = y = 300;
		width = 200;
		length = 300;
		paint = new Paint();
		strokePaint = new Paint();
		mainLabelPaint = new Paint();
		miniLabelPaint = new Paint();

		paint.setARGB(255, 0, 0, 0);
		strokePaint.setColor(Color.WHITE);
		strokePaint.setStyle(Paint.Style.STROKE);
		mainLabelPaint.setColor(Color.WHITE);
		mainLabelPaint.setTextSize(width/2);
		miniLabelPaint.setColor(Color.WHITE);
		miniLabelPaint.setTextSize(width/4);
		mainLabelPaint.setTextAlign(Paint.Align.CENTER);
		miniLabelPaint.setTextAlign(Paint.Align.CENTER);
		face = Face.ZERO;
		color = CardColor.BLACK;

		/* Any time we change CardColor we need to call "setPaintFromEnum(CardColor)".
		* This is because we want the card's color to be associated with an enum (for simplicity of naming)
		* At some point along the line we need to translate that enum into an actual ARGB paint.
		* That's what setPaintFromEnum does.
		*/
		setPaintfromEnum(CardColor.RED);
	}
	// Basic constructor that takes a color and a face
	public Card(CardColor _color, Face _face)
	{
		width = 200;
		length = 300;
		color = _color;
		face = _face;
	}

	public Card(float _x, float _y, float _width, float _length, Card _card)
	{
		x = _x;
		y = _y;
		width = _width;
		length = _length;
		setPaintfromEnum(_card.getCardColor());
	}
	/* DrawRect, by default, takes the distance of the left edge from the left edge of the canvas
	and vice-versa for the coordinates, instead of from a center point like drawCircle.
	By doing it this way, dividing our width and length by 2, we can define a card as the x,y center point
	and it's a little nicer to deal with
	 */
	public void draw(Canvas canvas){
		float left = x-width/2;
		float right = x+width/2;
		float top = y-length/2;
		float bottom = y+length/2;
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

	public void setCenter(float _x, float _y)
	{
		x = _x;
		y = _y;
	}

	public void setColor(CardColor c){
		color = c;
	}

	public void setFace(Face _face){
		face = _face;
	}

	public void setPaintfromEnum(CardColor _cardColor)
	{
		color = _cardColor;
		switch(color)
		{
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

	public void drawCardNumber(Canvas canvas)
	{
		float mainTextY = y - (mainLabelPaint.descent() + mainLabelPaint.ascent()) / 2;
		float miniTextY = y - (miniLabelPaint.descent() + miniLabelPaint.ascent()) / 2;

		switch(face) {

			case SKIP:
				canvas.drawText("SKIP", x, mainTextY, mainLabelPaint);
				break;
			case DRAWTWO:
				canvas.drawText("+2", x, mainTextY, mainLabelPaint);
				break;
			case DRAWFOUR:
				canvas.drawText("+4", x, mainTextY, mainLabelPaint);
				break;
			case WILD:
				canvas.drawText("WILD", x, mainTextY, mainLabelPaint);
				break;
			case REVERSE:
				canvas.drawText("REV", x, mainTextY, mainLabelPaint);
				break;
			default:
				canvas.drawText(String.valueOf(face.faceID), x, mainTextY, mainLabelPaint);
				canvas.drawText(String.valueOf(face.faceID), (float)(x-width*0.35), (float)(miniTextY-length*0.35), miniLabelPaint);
				canvas.drawText(String.valueOf(face.faceID), (float)(x+width*0.35), (float)(miniTextY+length*0.35), miniLabelPaint);
				break;
		}
		// if(cardNumber > 10)

	}

	public void setLengthWidth(int _length, int _width)
	{
		length = _length;
		width = _width;
	}

	public CardColor getCardColor()
	{
		return this.color;
	}

	public Face getFace()
	{
		return this.face;
	}
}
