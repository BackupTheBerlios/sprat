// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Direction.java,v 1.5 2009/05/13 14:51:25 mahanja Exp $

package object;


/**
 * Discribes the four cardinal directions
 * 
 * @author $Author: mahanja $
 */
public class Direction {
	public static final int NORTH = 0;
	public static final int WEST = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;

	//public static final int VERTICAL = 4;
	//public static final int HORIZONTAL = 5;
	
	private int direction;
	
	public Direction(int direction) {
		this.direction = direction;
	}
	
	/**
	 * Returns the direction described by this object
	 * @return one of NORTH, WEST, SOUTH, EAST. See static variables
	 */
	public int getDirection() {
		return direction;
	}
	
	/**
	 * Sets the direction describing this object
	 * @param direction. Must be one of NORTH, WEST, SOUTH, EAST. See static variables
	 */
	public void setDirection(int direction) {  
		this.direction = direction;
	}
	
	/**
	 * Returns a string representation of the described cardinal direction in english 
	 */
	public String toString() {
		if (direction == NORTH) {
			return "North";
		} else if (direction == WEST) {
			return "West";
		} else if (direction == SOUTH) {
			return "South";
		} else if (direction == EAST) {
			return "East";
		}
		return "";	// should never occur!!
	}
	
}

/*
 * $Log: Direction.java,v $
 * Revision 1.5  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.4  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.3  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 * Revision 1.2  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
