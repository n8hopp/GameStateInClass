package com.example.gamestateinclass.uno.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Card {

	private CardColor color;
	private Face face;

	public Card()
	{
		face = Face.ZERO;
		color = CardColor.BLACK;
	}
	// Basic constructor that takes a color and a face
	public Card(CardColor _color, Face _face)
	{
		color = _color;
		face = _face;
	}

	public void setColor(CardColor c){
		color = c;
	}

	public void setFace(Face _face){
		face = _face;
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
