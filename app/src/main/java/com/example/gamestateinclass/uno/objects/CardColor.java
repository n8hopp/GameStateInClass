package com.example.gamestateinclass.uno.objects;

public enum CardColor
{
	BLACK(4), BLUE(1), GREEN(2), RED(0), YELLOW(3);
	public int colorID;

	CardColor(int ID) {
		this.colorID = ID;
	}
}

