package com.example.gamestateinclass.uno.objects;

import java.io.Serializable;

public enum CardColor implements Serializable
{
	
	RED(0),
	BLUE(1),
	GREEN(2),
	YELLOW(3),
	BLACK(4);

	public int colorID;

	CardColor(int ID) {
		this.colorID = ID;
	}
}

