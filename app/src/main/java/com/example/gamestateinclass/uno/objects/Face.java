package com.example.gamestateinclass.uno.objects;

public enum Face {
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
