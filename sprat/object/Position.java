// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Position.java,v 1.4 2009/05/13 14:51:25 mahanja Exp $

package object;


/**
 * Describes a position on a grid with an x and an y coordinate
 * 
 * @author $Author: mahanja $
 */
public class Position {
	protected int x;
	protected int y;
	
	/**
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the x coordinate of this position
	 * @param x the x coordinate of this position
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Sets the y coordinate of this position
	 * @param y the y coordinate of this position
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Returns the x coordinate of this position
	 * @return the x coordinate of this position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of this position
	 * @return the y coordinate of this position
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns true if the given object describes
	 * the exactly same position.
	 * @return true if the given object describes
	 * the exactly same position.
	 */
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position p = (Position)o;
			return (p.x == x &&
					p.y == y);
		}
		return false;
	}
}

/*
 * $Log: Position.java,v $
 * Revision 1.4  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.3  2009/05/06 17:18:02  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 * Revision 1.2  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
