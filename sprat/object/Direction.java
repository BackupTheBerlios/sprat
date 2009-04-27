// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Direction.java,v 1.2 2009/04/27 19:53:55 stollf06 Exp $

package object;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: stollf06 $
 */
public class Direction {
	public static final int NORTH = 0;
	public static final int WEST = 1;
	public static final int SOUTH = 2;
	public static final int EAST = 3;
	
	private int direction;
	
	public Direction(int direction) {
		this.direction = direction;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {  
		this.direction = direction;
	}
}

/*
 * $Log: Direction.java,v $
 * Revision 1.2  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
