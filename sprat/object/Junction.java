// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Junction.java,v 1.5 2009/05/13 14:51:25 mahanja Exp $

package object;

import def.Definitions;


/**
 * Describes a junction of two grid lines. It is defined 
 * by its X and Y coordinate and the type or absence of 
 * the object on it
 * 
 * @author $Author: mahanja $
 */
public class Junction {
	
	/* Constants for types */
	public static final int UNKNOWN = 0;
	public static final int EMPTY = 1;
	public static final int MASTER_OBJ = 2;
	public static final int SLAVE_OBJ = 3;
	public static final int COMMON_OBJ = 4;
	public static final int OUTSIDE = 5;
	public static final int HOME_BASE = 6;
	public int MY_OBJ = -1; //this value is adapted at initialization
	
	private int type = UNKNOWN;
	private Position pos = null;
	
	public Junction(Position pos, int type) {
		this.pos = pos;
		this.type = type;
		
		if (type == MY_OBJ) {
			if (Definitions.getInstance().myName.equals(Definitions.MASTER))
				type = MASTER_OBJ;
			else if(Definitions.getInstance().myName.equals(Definitions.SLAVE))
				type = SLAVE_OBJ;
			// else -> this case must never occur!!!
		}
	}
	
	/**
	 * Sets the position of this junction
	 * @param pos The position
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	/**
	 * Sets the type of this junciton. The parameter must
	 * be one of the static class-member variables.
	 * @param type one of: <br>
	 * UNKNOWN<br> 
	 * EMPTY<br>
	 * MASTER_OBJ<br>
	 * SLAVE_OBJ<br>
	 * COMMON_OBJ<br>
	 * OUTSIDE<br>
	 * HOME_BASE<br>
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Returns the position of this junction
	 * @return the position of this junction
	 */
	public Position getPosition() {
		return pos;
	}

	/**
	 * Returns the type of this junction
	 * @return the type of this junction.
	 * The type is one of: <br>
	 * UNKNOWN<br> 
	 * EMPTY<br>
	 * MASTER_OBJ<br>
	 * SLAVE_OBJ<br>
	 * COMMON_OBJ<br>
	 * OUTSIDE<br>
	 * HOME_BASE<br>
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Returns true if the given object is equal to this junction.
	 * @param true if the given object is equal to this junction.
	 */
	public boolean equals(Object o) {
		if (o instanceof Junction) {
			Junction j = (Junction)o;
			return (j.type == type &&
					j.getPosition().equals(pos));
		}
		return false;
	}
	
	/**
	 * Returns a string in the format X:\<xcoordinagte\>,Y:\<ycoordinagte\>
	 */
	public String toString() {
		String str = "X:"+pos.getX()+",Y:"+pos.getY();
		
		if (type == UNKNOWN) {
			str = str + "T:UNKNOWN";
		} else if (type == EMPTY) {
			str = str + "T:EMPTY";
		} else if (type == MASTER_OBJ) {
			str = str + "T:MASTER_OBJ";
		} else if (type == SLAVE_OBJ) {
			str = str + "SLAVE_OBJ";
		} else if (type == COMMON_OBJ) {
			str = str + "COMMON_OBJ";
		} else if (type == HOME_BASE) {
			str = str + "T:HOME_BASE";
		}
		
		return str;
	}
	
	/**
	 * Returns the first character of the string describing the type. 
	 * @return the first character of the string describing the type.
	 * This is one of: U, E, M, S, C, H. If "?" is returnded there is 
	 * a bug in the application
	 */
	public String getTypeChar() {
		if (type == UNKNOWN) {
			return "U";
		} else if (type == EMPTY) {
			return "E";
		} else if (type == MASTER_OBJ) {
			return "M";
		} else if (type == SLAVE_OBJ) {
			return "S";
		} else if (type == COMMON_OBJ) {
			return "C";
		} else if (type == HOME_BASE) {
			return "H";
		}
		
		return "?"; // should never occur!!!
	}
}

/*
 * $Log: Junction.java,v $
 * Revision 1.5  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.4  2009/05/04 20:33:18  mahanja
 * It searches a way (bug with second unknown field)
 *
 * Revision 1.3  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.2  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
