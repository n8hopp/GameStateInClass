package com.example.gamestateinclass.uno.objects;

import java.io.Serializable;

/**
 * enum CardColor
 *
 * An enumeration holding the five possible card colors of RED, BLUE, GREEN,
 * YELLOW and BLACK. Each color has an associated ID integer ranging from 0 to 4.
 *
 * @author Nate Hopper
 * @author Lukas Miller
 * @author Clei Paguirigan
 * @author Henry Schiff
 */


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

