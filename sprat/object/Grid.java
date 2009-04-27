//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Grid.java,v 1.2 2009/04/27 19:53:55 stollf06 Exp $
package object;

import java.util.Vector;

import lejos.nxt.Button;
import tool.Console;

public class Grid {
	private Vector list;
	
	/**
	 * the grid should be created after the initialisation of master and slave
	 */
	public Grid() {
		list = new Vector();
	}

	/**
	 * set junction adds a new junction to the memory, if it is already existent
	 * the type is overwritten
	 * 
	 * @param junct
	 */
	
	public void setJunction(Junction junct) {
		int junctIndex = getJunctIndex(junct.getPosition());
		if (junctIndex == -1) {
			list.addElement(junct);
		} else {
			list.setElementAt( junct, junctIndex);
		}
	}

	/**
	 * get a junction object according to a position on the grid
	 * 
	 * @param pos
	 * @return Junction
	 */
	public Junction getJunction(Position pos) {
		Junction junctList;
		for (int i = 0; i < list.size(); i++) {
			junctList = (Junction) list.elementAt(i);
			if (junctList.getPosition().getX() == pos.getX()
					&& junctList.getPosition().getY() == pos.getY()) {
				return junctList;
			}
		}
		junctList = new Junction(pos, Junction.UNKNOWN);
		return junctList;
	}

	/**
	 * gets the index of a position in the memory
	 * 
	 * @param pos
	 * @return index of Junction
	 */
	private int getJunctIndex(Position pos) {
		Junction junctList;
		for (int i = 0; i < list.size(); i++) {
			junctList = (Junction) list.elementAt(i);
			if (junctList.getPosition().getX() == pos.getX()
					&& junctList.getPosition().getY() == pos.getY()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * gives the junction with all the possible information which the robot is
	 * facing
	 * 
	 * @return the faced junction
	 */
	public Junction getNextProjectedJunction(Robot robo){
		Console.println("inNextProjectJunc");
		Button.waitForPress();
		int orientation = robo.getOrientation();

		Position actualPosition = robo.getMyActualJunction().getPosition();
		
		Console.println("otheractual");
		Button.waitForPress();
		
		int xOffset=0, yOffset = 0;
		switch (orientation) {
		case Direction.NORTH:
			yOffset =1;
			break;
		case Direction.WEST:
			xOffset = 1;
			break;
		case Direction.SOUTH:
			yOffset=-1;
			break;
		case Direction.EAST:
			xOffset = -1;
			break;
		}
		Position pos = new Position(actualPosition.getX()+xOffset, actualPosition.getY()+yOffset);
		Junction junct = getJunction(pos);
		return junct;
		//return null;
	}
	public static void main(String[] args) {}
	

}
/*
 * $Log: Grid.java,v $
 * Revision 1.2  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 */