//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Grid.java,v 1.4 2009/04/29 19:01:23 mahanja Exp $
package object;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import def.Definitions;

import lejos.nxt.Button;
import tool.Console;

public class Grid {
	private Hashtable grid;
	private static Grid instance = null;
	
	private int gridSize = -1;
	
	/**
	 * The grid should be created after the initialization of master and slave.
	 * Grid is a singleton because there is just one only and like this it is 
	 * accessible from everywhere.
	 */
	protected Grid() {
		grid = new Hashtable();
	}
	
	/**
	 * Since Grid is a singleton, use this static method insteat of the 
	 * constructor.
	 * @return the singleton Grid instance
	 */
	public static Grid getInstance() {
		if (instance == null) {
			instance = new Grid();
		}
		return instance;
	}

	/**
	 * set junction adds a new junction to the memory, if it is already existent
	 * the type is overwritten
	 * 
	 * @param junct
	 */
	public void setJunction(Junction junct) {
		if (gridSize < 0 &&
			junct.getType() == Junction.OUTSIDE) {

			setGridSize(junct.getPosition().getX() > junct.getPosition().getY() 
						? junct.getPosition().getX() -1 : junct.getPosition().getY() -1);
		}
		
		grid.put(junct.getPosition(), junct);
	}
	
	private void setGridSize(int size) {
		gridSize = size;
	}

	/**
	 * Get a junction object according to a position on the grid.
	 * If the junction on pos isn't yet known it will be added to
	 * the grid as a junction with the type unknown.
	 * @param pos the position of the desired junction.
	 * @return Junction the junction on the given position. 
	 */
	public Junction getJunction(Position pos) {
		Junction j = (Junction)grid.get(pos);
		if (j == null) 
			grid.put(pos, new Junction(pos, Junction.UNKNOWN));
		
		return (Junction)grid.get(pos);
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
	}
		
	/**
	 * Used by TaskManager to find a way back to homebase 
	 * @param me the concerning robot
	 * @return a vector of junctions representing a known way to homebase
	 */
	public Vector getAWayBackHome(Robot robot) {
		BackTracker guide = new BackTracker(getMap());
		return guide.getWay(robot.getMyActualJunction().getPosition(),
						    robot.getHomeBase());
	}
	
	/**
	 * Creates a 2D boolean map where true values are empty positions
	 * @return a 2D boolean map where true values are empty positions
	 */
	private boolean[][] getMap() {
		boolean array[][] = new boolean[gridSize][gridSize];
		Enumeration enum = grid.keys();
		Position p;
		while (enum.hasMoreElements()) {
			p = (Position)enum.nextElement();
			array[p.getX()][p.getY()] = (getJunction(p).getType() == Junction.EMPTY);
		}
		return array;
	}
	

	/**
	 * True if there is no known common object or one this robot have to process
	 * @return true if no known objects (concerning to this robot) in the grid
	 */
	public boolean nothingElseThanSearching() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = (Junction)grid.get(currP);
				if (currJ.getType() == Junction.MASTER_OBJ &&
					Definitions.getInstance().myName == Definitions.MASTER)
					return false;
				else if (currJ.getType() == Junction.SLAVE_OBJ &&
						 Definitions.getInstance().myName == Definitions.SLAVE)
					return false;
				else if (currJ.getType() == Junction.COMMON_OBJ)
					return false;
			}
		}
		return true;
	}

	public Vector getAWayToNextUnknown(Robot robot) {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = (Junction)grid.get(currP);
				if (currJ.getType() == Junction.UNKNOWN) {
					BackTracker guide = new BackTracker(getMap());
					return guide.getWay(robot.getMyActualJunction().getPosition(),
									    robot.getHomeBase());
				}
			}
		}
		return new Vector();
	}

	public boolean nothingMoreToDo() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = (Junction)grid.get(currP);
				if (currJ.getType() != Junction.EMPTY ||
					currJ.getType() != Junction.HOME_BASE ||
					currJ.getType() != Junction.OUTSIDE) {
					return false;
				}
			}
		}
		return true;
	}

	public Vector getAWayToNextKnown(Robot robot) {
		/*
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		BackTracker guide = new BackTracker(getMap());
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = (Junction)grid.get(currP);
				if (currJ.getType() == Junction.MASTER_OBJ &&
					Definitions.getInstance().myName == Definitions.MASTER) {
					
					return guide.getWay(robot.getMyActualJunction().getPosition(), currP);
				} else if (currJ.getType() == Junction.SLAVE_OBJ &&
						   Definitions.getInstance().myName == Definitions.SLAVE) {
					
					return guide.getWay(robot.getMyActualJunction().getPosition(), currP);
				}else if (currJ.getType() == Junction.COMMON_OBJ) {
					
					return guide.getWay(robot.getMyActualJunction().getPosition(), currP);
				}
			}
		}
		return true;
		*/
		return null;
	}
	
	/**
	 * This subclass provides the method getWay which finds a way from a start
	 * to an end point in a boolean array where the true values indicate valid 
	 * fields.
	 * @author greila06
	 */
	private class BackTracker {
		
		boolean[][] grid;
		int gridSize;

		/**
		 * Instantiates a new BackTracker.
		 * @param grid The true values indicate valid fields where the way can 
		 * go over.
		 */
		public BackTracker(boolean[][] grid) {
			this.grid = grid;
			gridSize = grid.length;
		}
		
		/**
		 * Searches a way in the grid starting at startP and ending at endP.
		 * The path will be the chain of Positions in the returned Vector. 
		 * @param startP where to start the way
		 * @param endP where the way ends
		 * @return
		 */
		public Vector getWay(Position startP, Position endP) {
			boolean[][] way = new boolean[gridSize][gridSize];
			way[startP.getX()][startP.getY()] = true;	// setting start point
			if (getWay(way, startP.getX(), startP.getY(), endP.getX(), endP.getY()))
				return convertToPath(way, startP, endP);
			else
				return new Vector();
		}
		
		/** 
		 * Searches a way by exploiting the backtracking algorithm (recursive).
		 * x, y = start point coordinate. xb, yb = target coordinate.
		 * Way is a in-out-parameter.
		 * Returns true if there is a way found.
		 * */
		private boolean getWay(boolean[][] way, int x, int y, int xb, int yb) {
			// reached the target
			if (x == xb && y == yb)
				return true;
			
			// search in all cardinal directions
			// NORTH
			if (y + 1 < gridSize && way[x][y+1] != true) {
				way[x][y+1] = true;
				if (getWay(way, x, y+1, xb, yb))
					return true;
				else
					way[x][y+1] = false;
			}
			// WEST
			if (x - 1 >= 0 && way[x-1][y] != true) {
				way[x-1][y] = true;
				if (getWay(way, x-1, y, xb, yb))
					return true;
				else
					way[x-1][y] = false;
			}
			// SOUTH
			if (y - 1 >= 0 && way[x][y-1] != true) {
				way[x][y-1] = true;
				if (getWay(way, x, y-1, xb, yb))
					return true;
				else
					way[x][y-1] = false;
			}
			// EAST
			if (x + 1 < gridSize && way[x+1][y] != true) {
				way[x+1][y] = true;
				if (getWay(way, x+1, y, xb, yb))
					return true;
				else
					way[x+1][y] = false;
			}
			return false;	// should never occur!
		}
	}

	/**
	 * Return a chain of Positions indicating the way from the 
	 * start position to the end position 
	 * @param way the way is a boolean array, where the way is 
	 * composed of true boolean values in a 2D array representing 
	 * the grid
	 * @param start the position where to start
	 * @param end the position where to end
	 * @return a chain of Positions indicating the way from the 
	 * start position to the end position
	 */
	private Vector convertToPath(boolean[][] way, Position start, Position end) {
		int currX = start.getX(),
			currY = start.getY();
		int lastPosX = currX,
			lastPosY = currY;
		Vector path = new Vector();
		while (currX == end.getX() && currY == end.getY()) {
			// go north
			if (currY + 1 < gridSize &&
				way[currX][currY+1] &&
				currX != lastPosX &&
				currY + 1 != lastPosY) {
				
				path.addElement(new Position(currX, ++currY));
			// go west
			} else if (currX - 1 >= 0 &&
					   way[currX-1][currY] &&
					   currX - 1 != lastPosX &&
					   currY != lastPosY) {

				path.addElement(new Position(--currX, currY));
			// go south
			} else if (currY - 1 >= 0 &&
					   way[currX][currY-1] &&
					   currX != lastPosX &&
					   currY - 1 != lastPosY) {
				
				path.addElement(new Position(currX, --currY));
			// go east
			} else if (currX + 1 < gridSize &&
					   way[currX+1][currY] &&
					   currX + 1 != lastPosX &&
					   currY != lastPosY) {
				
				path.addElement(new Position(++currX, currY));
			}
		}
		return path;
	}
}
/*
 * $Log: Grid.java,v $
 * Revision 1.4  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.3  2009/04/27 20:20:09  mahanja
 * Grid becomes a singleton
 *
 * Revision 1.2  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 */