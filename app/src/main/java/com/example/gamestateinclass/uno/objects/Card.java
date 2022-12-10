package com.example.gamestateinclass.uno.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

public class Card implements Serializable {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = 893542931190030342L;

	private CardColor color;
	private Face face;
	private RenderCard render;

	public Card() {
		face = Face.ZERO;
		color = CardColor.BLACK;
		render = new RenderCard(face, color);
	}

	// Basic constructor that takes a color and a face
	public Card(CardColor _color, Face _face) {
		color = _color;
		face = _face;
		render = new RenderCard(face, color);
	}

	public void setColor(CardColor c) {
		color = c;
		 render.setPaintfromEnum(c);
	}

	public void setFace(Face _face) {
		face = _face;
	}

	public CardColor getCardColor() {
		return this.color;
	}

	public Face getFace() {
		return this.face;
	}

	public RenderCard getRender(){
		return this.render;
	}
}
