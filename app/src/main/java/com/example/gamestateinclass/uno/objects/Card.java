package com.example.gamestateinclass.uno.objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.io.Serializable;

/**
 * class Card
 *
 * Card holds member variables of its face and color values, as well as a
 * separate RenderCard object for rendering it to the screen. Instances of
 * this class are fundamental to the game, being passed from the drawDeck
 * to the player's hands to the discardDeck to facilitate gameplay.
 *
 * @author Nate Hopper
 * @author Lukas Miller
 * @author Clei Paguirigan
 * @author Henry Schiff
 */

public class Card implements Serializable {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = 893542931190030342L;

	private CardColor color;
	private Face face;
	private RenderCard render;


	/**
	 * Constructor for a template card with a face of ZERO and color of BLACK.
	 *
	 * @return a new Card object.
	 */
	public Card() {
		face = Face.ZERO;
		color = CardColor.BLACK;
		render = new RenderCard(face, color);
	}


	/**
	 * Default Constructor that takes in a color and face value.
	 *
	 * @param _color the color of this card.
	 * @param _face the face of this card.
	 *
	 * @return a new Card object.
	 */
	public Card(CardColor _color, Face _face) {
		color = _color;
		face = _face;
		render = new RenderCard(face, color);
	}


	/**
	 * Setter method to change the card's color. This is primarily used
	 * when placing a WILD or DRAWFOUR card.
	 *
	 * @param c the new color of the card.
	 *
	 * @return void.
	 */
	public void setColor(CardColor c) {
		color = c;
		render.setPaintfromEnum(c);
	}


	/**
	 * Setter method to change the card's face.
	 *
	 * @param _face the new face of the card.
	 *
	 * @return void.
	 */
	public void setFace(Face _face) {
		face = _face;
	}


	/**
	 * Getter method to fetch the card's color.
	 *
	 * @return the CardColor enumeration instance belong to this card.
	 */
	public CardColor getCardColor() {
		return this.color;
	}


	/**
	 * Getter method to fetch the card's face.
	 *
	 * @return the Face enumeration instance belong to this card.
	 */
	public Face getFace() {
		return this.face;
	}


	/**
	 * Getter method to fetch the card's RenderCard object.
	 *
	 * @return the RenderCard instance belonging to this card.
	 */
	public RenderCard getRender(){
		return this.render;
	}
}
