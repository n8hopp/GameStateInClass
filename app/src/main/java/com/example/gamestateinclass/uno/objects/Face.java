package com.example.gamestateinclass.uno.objects;

import java.io.Serializable;

/**
 * enum Face
 *
 * An enumeration holding the sixteen different possible faces a card can have.
 * These include the numbers 0 through 9 and the five special faces of REVERSE,
 * SKIP, DRAWTWO, DRAWFOUR and WILD. Each face has an associated ID integer
 * ranging from 0 to 15.
 *
 * @author Nate Hopper
 * @author Lukas Miller
 * @author Clei Paguirigan
 * @author Henry Schiff
 */

public enum Face implements Serializable {
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
	WILD(14),
	NONE(15);

	public int faceID;

	Face(int ID) {
		this.faceID = ID;
	}

}
