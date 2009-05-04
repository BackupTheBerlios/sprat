// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Junction.java,v 1.4 2009/05/04 20:33:18 mahanja Exp $

package object;

import def.Definitions;


/**
 * TODO: DESCRIPTION
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
	
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public Position getPosition() {
		return pos;
	}
	
	public int getType() {
		return type;
	}
	
	public boolean equals(Object o) {
		if (o instanceof Junction) {
			Junction j = (Junction)o;
			return (j.type == type &&
					j.getPosition().equals(pos));
		}
		return false;
	}
	
	// debug:
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
		
		return "?"; // can never occur!!!
	}
}

/*
 * $Log: Junction.java,v $
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
