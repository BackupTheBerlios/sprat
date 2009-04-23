// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Junction.java,v 1.1 2009/04/23 12:08:34 mahanja Exp $

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
}

/*
 * $Log: Junction.java,v $
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
