// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Position.java,v 1.2 2009/04/29 19:01:23 mahanja Exp $

package object;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class Position {
	private int x, y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
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
 * Revision 1.2  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
