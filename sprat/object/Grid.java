//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Grid.java,v 1.12 2009/05/11 13:05:20 stollf06 Exp $
package object;

import ai.AI;
import com.Communicator;
import def.Definitions;
import lejos.nxt.Button;
import tool.Console;

public class Grid {
	public static final int GRID_SIZE = 5;//3;//5; TODO!!!
	private static Grid instance = null;
	Junction grid[][];
	AI ai;
	
	//private int gridSize = -1;
	//private boolean gridSizeIsKnown = false;
	
	/**
	 * The grid should be created after the initialization of master and slave.
	 * Grid is a singleton because there is just one only and like this it is 
	 * accessible from everywhere.
	 */
	protected Grid(AI ai) {
		this.ai = ai;
		
		
		grid = new Junction[GRID_SIZE][GRID_SIZE];
		
		for (int x = 0; x < GRID_SIZE; x++)
			for (int y = 0; y < GRID_SIZE; y++)
				grid[x][y] = new Junction(new Position(x,y),Junction.UNKNOWN);
	}
	
	/**
	 * Since Grid is a singleton, use this static method insteat of the 
	 * constructor.
	 * @return the singleton Grid instance
	 */
	public static Grid getInstance(AI ai) {
		if (instance == null) {
			instance = new Grid(ai);
		}
		return instance;
	}

	/**
	 * set junction adds a new junction to the memory, if it is already existent
	 * the type is overwritten
	 * 
	 * @param junct
	 */
	public void setJunction(Junction junct, boolean informOther) {
		/*if (junct.getType() != Junction.OUTSIDE) {
			//if (grid[junct.getPosition().getX()][junct.getPosition().getY()].getType() != Junction.HOME_BASE) {
			if (grid[junct.getPosition().getX()][junct.getPosition().getY()].getType() == Junction.UNKNOWN)
				grid[junct.getPosition().getX()][junct.getPosition().getY()] = junct;
			//}
		}*/
        
		Console.println("FOUND:"+junct.getTypeChar());
		Button.waitForPress();
		
		if (grid[junct.getPosition().getX()][junct.getPosition().getY()].getType() != Junction.HOME_BASE)
			grid[junct.getPosition().getX()][junct.getPosition().getY()].setType(junct.getType());
		
        if (informOther &&
        	grid[junct.getPosition().getX()][junct.getPosition().getY()].getType() != Junction.HOME_BASE){
        	
        	try {
        		Communicator.getInstance(ai).sendDiscoveredJunction(junct);
        	} catch (Exception e) {
        		Console.println("E: send setJ");
        	}
        }
	}
	
	/*
	private void setGridSize(int size) {
		gridSize = size;
		gridSizeIsKnown = true;
	}
	*/

	/**
	 * Get a junction object according to a position on the grid.
	 * If the junction on pos isn't yet known it will be added to
	 * the grid as a junction with the type unknown.
	 * @param pos the position of the desired junction.
	 * @return Junction the junction on the given position. 
	 */
	public Junction getJunction(Position pos) {
		Junction j = (Junction)grid[pos.getX()][pos.getY()];
		if (j == null) 
			grid[pos.getX()][pos.getY()] = new Junction(pos, Junction.UNKNOWN);
		
		return grid[pos.getX()][pos.getY()];
	}

	/**
	 * gives the Position with all the possible information which the robot is
	 * facing
	 * 
	 * @return the faced Position
	 */
	public Position getNextProjectedPosition(Robot robo){
		int orientation = robo.getOrientation();

		Position actualPosition = robo.getMyActualPosition();
		
		int xOffset=0, yOffset = 0;
		if (orientation == Direction.NORTH)
			yOffset = 1;
		else if (orientation == Direction.WEST)
			xOffset = -1;
		else if (orientation == Direction.SOUTH)
			yOffset = -1;
		else if (orientation == Direction.EAST)
			xOffset = 1;
		// else -> there is no other case!!!
		
		Position pos = new Position(actualPosition.getX()+xOffset, 
				                    actualPosition.getY()+yOffset);
		return pos;
	}
		
	/**
	 * Used by TaskManager to find a way back to homebase 
	 * @param me the concerning robot
	 * @return a vector of junctions representing a known way to homebase
	 */
	public PathElement getAWayBackHome(Robot robot) {
		boolean map[][] = getMap();
		map[robot.getHomeBase().getX()][robot.getHomeBase().getX()] = true;	// there where we want to go must be enabled
		
		BackTracker guide = new BackTracker(map);

		PathElement wayHome = guide.findWay(robot.getMyActualPosition(), robot.getHomeBase());
				
		return wayHome;
	}
	
	/*public PathElement getAWayTo(Robot robot, Position target) {
		boolean map[][] = getMap();
		map[target.getX()][target.getY()] = true;
		
		BackTracker guide = new BackTracker(map);

		PathElement wayTo = guide.findWay(robot.getMyActualPosition(), robot.getHomeBase());
				
		return wayTo;
	}*/
	
	/**
	 * Creates a 2D boolean map where true values are empty positions
	 * @return a 2D boolean map where true values are empty positions
	 */
	private boolean[][] getMap() {
		boolean array[][] = new boolean[GRID_SIZE][GRID_SIZE];

		for (int x = 0; x < GRID_SIZE; x++)
			for (int y = 0; y < GRID_SIZE; y++)
				array[x][y] = (grid[x][y].getType() == Junction.EMPTY ||
							   grid[x][y].getType() == Junction.HOME_BASE);

		return array;
	}

	/**
	 * True if there is no known uncommon object to process
	 * @return true if no known objects (concerning to this robot) on the grid
	 * /
	public boolean nothingElseThanSearching() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = grid[currP.getX()][currP.getY()];
				if (currJ == null) // this field does not exist or isn't yet discovered
					continue;
				
				if (currJ.getType() == Junction.MASTER_OBJ &&
					Definitions.getInstance().myName.equals(Definitions.MASTER))
					return false;
				else if (currJ.getType() == Junction.SLAVE_OBJ &&
						 Definitions.getInstance().myName.equals(Definitions.SLAVE))
					return false;
				//else if (currJ.getType() == Junction.COMMON_OBJ)
					//return false;
			}
		}
		return true;
	}*/

	public PathElement getAWayToNextKnownUnCommon(Robot robo) {
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				if (grid[x][y].getType() == Junction.MASTER_OBJ &&
						Definitions.getInstance().myName.equals(Definitions.MASTER) ||
					grid[x][y].getType() == Junction.SLAVE_OBJ &&
						Definitions.getInstance().myName.equals(Definitions.SLAVE)) {		

					boolean map[][] = getMap();
					map[x][y] = true;	// there where we want to go must be enabled
					
					BackTracker guide = new BackTracker(map);
					PathElement path = guide.findWay(robo.getMyActualPosition(), grid[x][y].getPosition());

					if (path == null)
						continue;
					
					return path;
				}
			}
		}
		return null;
	}

	/*public PathElement getAWayToNextKnownCommon(Robot robo) {
		for (int x = 0; x < GRID_SIZE; x++) {
			for (int y = 0; y < GRID_SIZE; y++) {
				if (grid[x][y].getType() == Junction.COMMON_OBJ) {		
					boolean map[][] = getMap();
					map[x][y] = true;	// there where we want to go must be enabled
					
					int orientation getOrientation(x,y);
					BackTracker guide = new BackTracker(map);
					PathElement path = guide.findWay(robo.getMyActualPosition(), grid[x][y].getPosition());
					
					if (path == null)
						continue;
					
					return path;
				}
			}
		}
		return null;
	}*/

	
	public PathElement getAWayToNextUnknown(Robot robot) {
		if (Definitions.getInstance().isMaster) {
			for (int x = 0; x < GRID_SIZE; x++) {
				for (int y = 0; y < GRID_SIZE; y++) {
					if (grid[x][y].getType() == Junction.UNKNOWN) {
						boolean map[][] = getMap();
						map[x][y] = true;	// there where we want to go must be enabled 
						
						BackTracker guide = new BackTracker(map);
						PathElement path = guide.findWay(robot.getMyActualPosition(), grid[x][y].getPosition());
	
						if (path == null) {
	
							continue;
						}
						return path;
					}
				}
			}
		} else {
			for (int x = GRID_SIZE-1; x >= 0; x--) {
				for (int y = 0; y < GRID_SIZE; y++) {
					if (grid[x][y].getType() == Junction.UNKNOWN) {
						boolean map[][] = getMap();
						map[x][y] = true;	// there where we want to go must be enabled 
						
						BackTracker guide = new BackTracker(map);
						PathElement path = guide.findWay(robot.getMyActualPosition(), grid[x][y].getPosition());
	
						if (path == null) {
	
							continue;
						}
						return path;
					}
				}
			}
		}
		return null;
	}

	/*public boolean nothingMoreToDo() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = grid[currP.getX()][currP.getY()];
				if (currJ.getType() != Junction.EMPTY ||
					currJ.getType() != Junction.HOME_BASE ||
					currJ.getType() != Junction.OUTSIDE) {
					return false;
				}
			}
		}
		return true;
	}*/

	/**
	 * returns a way to a known object. If there is no un-common obj and 
	 * the orientation of this is not known it will return a way to the
	 * next unknown field. If the orientation is known
	 * @see getAWayToNextUnknown(Robot)
	 * @param robot used to determine the start position for the path
	 * @return a path stored in a Vector.
	 * /
	public PathElement getAWayToNextKnownUncommon(Robot robot) {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		BackTracker guide = new BackTracker(getMap());
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = grid[currP.getX()][currP.getY()];
				if (currJ.getType() == Junction.MASTER_OBJ &&
					Definitions.getInstance().myName.equals(Definitions.MASTER)) {
					
					return guide.getWay(robot.getMyActualPosition(), currP);
				} else if (currJ.getType() == Junction.SLAVE_OBJ &&
						   Definitions.getInstance().myName.equals(Definitions.SLAVE)) {
					
					return guide.getWay(robot.getMyActualPosition(), currP);
				}/*else if (currJ.getType() == Junction.COMMON_OBJ) {
					Direction orientation;
					if ((orientation = getOrientation(currJ.getPosition())) == null) {
						return getAWayToNextUnknown(robot);
					} else {
						return guide.getWay(robot.getMyActualJunction(), currJ.getPosition());
					}
				}* /
			}
		}
		return null;
	}*/
	
	/**
	 * returns the orientation of the common obj at the given position
	 * @param p the position where of the common obj of which we want to know the orientation.
	 * @return Direction.XX if the direction is known. Else null will be returned.
	 * @see object.Direction
	 * /
	private Direction getOrientation(Position p) {
		// north
		if (p.getY() + 1 < gridSize &&
			getJunction(new Position(p.getX(), p.getY()+1)).getType() == Junction.COMMON_OBJ)
			return new Direction(Direction.NORTH);
		// west
		else if(p.getX() - 1 >= 0 &&
				getJunction(new Position(p.getX() - 1, p.getY())).getType() == Junction.COMMON_OBJ)
			return new Direction(Direction.WEST);
		// south
		else if(p.getY() - 1 >= 0 &&
				getJunction(new Position(p.getX(), p.getY() - 1)).getType() == Junction.COMMON_OBJ)
			return new Direction(Direction.SOUTH);
		// east
		else if(p.getX() + 1 < gridSize &&
				getJunction(new Position(p.getX() + 1, p.getY())).getType() == Junction.COMMON_OBJ)
			return new Direction(Direction.SOUTH);
		// unknown
		else
			return null;
	}*/

	/**
	 * Returns the position of the next common object.
	 * @return the position of the next common object.
	 * /
	public Position getNextCommonObj() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = grid[currP.getX()][currP.getY()];
				if (currJ.getType() == Junction.COMMON_OBJ) {
					return currP;
				}
			}
		}
		return null;
	}*/
	
	/*public boolean isThereAnObjectForMe() {
		Position currP = new Position(-1, -1);
		Junction currJ = null;
		
		for (int x = 0; x < gridSize; x++) {
			for (int y = 0; y < gridSize; y++) {
				currP.setX(x); currP.setY(y);
				currJ = grid[currP.getX()][currP.getY()];

				if (currJ.getType() == Junction.MASTER_OBJ &&
					Definitions.getInstance().myName.equals(Definitions.MASTER))
					
					return true;
				else if (currJ.getType() == Junction.SLAVE_OBJ &&
					     Definitions.getInstance().myName.equals(Definitions.SLAVE)) 
					
					return true;
			}
		}
		return false;
	}*/

	/*public boolean isCommonObjOrientationKnown(Position p) {
		if (getOrientation(p) == null)
			return false;
		return true;
	}*/
	
	/*public Position othersPositionToHelp(Robot master) {
		Position testP = getRelativePositionLeft(master);
		if (getJunction(testP).getType() == Junction.COMMON_OBJ)
			return testP;
		else 
			return getRelativePositionRight(master);
	}*/

	/**
	 * Returns the theoretical left pos, the pos is not validated. Means
	 * probably the returned pos is outside the grid
	 * @param robo the robot from which to look
	 * @return the position on the left hand side of the given robot
	 */
	public Position getRelativePositionLeft(Robot robo) {
		if (robo.getOrientation() == Direction.NORTH)
			return new Position(robo.getMyActualPosition().getX()-1,
				             	robo.getMyActualPosition().getY());
		else if (robo.getOrientation() == Direction.WEST)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()-1);
		else if (robo.getOrientation() == Direction.EAST)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()+1);
		else //if (robo.getOrientation() == Direction.SOUTH)
			return new Position(robo.getMyActualPosition().getX()+1,
				                robo.getMyActualPosition().getY());
	}

	/**
	 * Returns the theoretical right pos, the pos is not validated. Means
	 * probably the returned pos is outside the grid
	 * @param robo the robot from which to look
	 * @return the position on the right hand side of the given robot
	 */
	public Position getRelativePositionRight(Robot robo) {
		if (robo.getOrientation() == Direction.NORTH)
			return new Position(robo.getMyActualPosition().getX()+1,
				                robo.getMyActualPosition().getY());
		else if (robo.getOrientation() == Direction.WEST)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()+1);
		else if (robo.getOrientation() == Direction.EAST)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()-1);
		else //if (robo.getOrientation() == Direction.SOUTH)
			return new Position(robo.getMyActualPosition().getX()-1,
				                robo.getMyActualPosition().getY());
	}
	

	/**
	 * Returns the theoretical behind pos, the pos is not validated. Means
	 * probably the returned pos is outside the grid
	 * @param robo the robot from which to look
	 * @return the position behind of the given robot
	 */
	public Position getRelativePositionBehind(Robot robo) {
		if (robo.getOrientation() == Direction.NORTH)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()-1);
		else if (robo.getOrientation() == Direction.WEST)
			return new Position(robo.getMyActualPosition().getX()+1,
				                robo.getMyActualPosition().getY());
		else if (robo.getOrientation() == Direction.EAST)
			return new Position(robo.getMyActualPosition().getX()-1,
				                robo.getMyActualPosition().getY());
		else //if (robo.getOrientation() == Direction.SOUTH)
			return new Position(robo.getMyActualPosition().getX(),
				                robo.getMyActualPosition().getY()+1);
	}
	
	/**
	 * This subclass provides the method getWay which finds a way from a start
	 * to an end point in a boolean array where the true values indicate valid 
	 * fields.
	 * @author greila06
	 */
	private class BackTracker {
		
		boolean[][] map;
		//PathElement shortest = null; 

		/**
		 * Instantiates a new BackTracker.
		 * @param grid The true values indicate valid fields where the way can 
		 * go over.
		 */
		public BackTracker(boolean[][] map) {
			this.map = map;
		}
		
		public boolean[][] getEmptyWay() {
			boolean[][] way = new boolean[GRID_SIZE][GRID_SIZE];
			for (int x = 0; x < GRID_SIZE; x++)
				for (int y = 0; y < GRID_SIZE; y++)
					way[x][y] = false;
			return way;
		}
		
		public PathElement findWay(Position startP, Position endP) {
			boolean[][] way = getEmptyWay();
			//way[startP.getX()][startP.getY()] = true;
			
			map[startP.getX()][startP.getY()] = true;	// setting start point
			map[endP.getX()][endP.getY()] = true;		// enabling end point

			PathElement startElt = new PathElement(startP);
			
			/*if (findWay(startElt, way, startP.getX(), startP.getY(), endP.getX(), endP.getY()))
				return shortest;
			else // no path found
				return null;*/
			if (findWay(startElt, way, endP.getX(), endP.getY()))
				return startElt.getNextElt();
			return null;
		}
		
		/** 
		 * Searches a way by exploiting the backtracking algorithm (recursive).
		 * xb, yb = target coordinate.
		 * pe is a in-out-parameter describing the point where we are now.
		 * Returns true if there is a way found.
		 * 
		 * 
		 * Optimization: To optimize the way finder, i built in 
		 * an order in which way to search. Look at the example.
		 * 1,2,3 and 4 are areas with different search preferences:
		 *   <br> <br>
		 * 4 4 3 3 3 3 3	1: east, north, south, west <br>
		 * 4 4 3 3 3 3 3	2: north, west, south, east <br>
		 * 1 X 3 3 3 3 3	3: west, south, east, north <br>
		 * 1 2 2 2 2 2 2	4: east, south, west, north <br>
		 * 1 2 2 2 2 2 2 	X: the target junction<br>
		 * 1 2 2 2 2 2 2 <br>
		 * 1 2 2 2 2 2 2 <br>
		 */
		private boolean findWay(PathElement pe, boolean[][] way, int xb, int yb) {
			int x = pe.getX(),
			    y = pe.getY();
			
			// reached the target
			if (x == xb && y == yb) {
				return true;
			}
			
			if (x +1 == xb && y == yb) {
				pe.setNextElt(new PathElement(x+1,y)); return true;
			} else if (x < xb && y <= yb) {
				// EAST
				if (x + 1 < GRID_SIZE && !way[x+1][y] && map[x+1][y]) {
					
					way[x+1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x+1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
				// NORTH
				if (y + 1 < GRID_SIZE && !way[x][y+1] && map[x][y+1]) {
					
					way[x][y+1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y+1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
				// SOUTH
				if (y - 1 >= 0 && !way[x][y-1] && map[x][y-1]) {

					way[x][y-1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y-1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x][y-1] = false;
					}
				}
				// WEST
				if (x - 1 >= 0 && !way[x-1][y] && map[x-1][y]) {
					
					way[x-1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x-1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						way[x-1][y] = true;
					}
				}
			} else if (x == xb && y + 1 == yb) {
				pe.setNextElt(new PathElement(x,y+1)); return true;
			} else if (x >= xb && y < yb) {
				// NORTH
				if (y + 1 < GRID_SIZE && !way[x][y+1] && map[x][y+1]) {
					
					way[x][y+1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y+1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
				// WEST
				if (x - 1 >= 0 && !way[x-1][y] && map[x-1][y]) {
					
					way[x-1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x-1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						way[x-1][y] = true;
					}
				}
				// SOUTH
				if (y - 1 >= 0 && !way[x][y-1] && map[x][y-1]) {

					way[x][y-1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y-1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x][y-1] = false;
					}
				}
				// EAST
				if (x + 1 < GRID_SIZE && !way[x+1][y] && map[x+1][y]) {
					
					way[x+1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x+1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
			} else if (x-1 == xb && y == yb) {
				pe.setNextElt(new PathElement(x-1,y)); return true;
			} else if (x > xb && y >= yb) {
				// WEST
				if (x - 1 >= 0 && !way[x-1][y] && map[x-1][y]) {
					
					way[x-1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x-1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						way[x-1][y] = true;
					}
				}
				// SOUTH
				if (y - 1 >= 0 && !way[x][y-1] && map[x][y-1]) {

					way[x][y-1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y-1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x][y-1] = false;
					}
				}
				// EAST
				if (x + 1 < GRID_SIZE && !way[x+1][y] && map[x+1][y]) {
					
					way[x+1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x+1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
				// NORTH
				if (y + 1 < GRID_SIZE && !way[x][y+1] && map[x][y+1]) {
					
					way[x][y+1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y+1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
			} else if (x == xb && y - 1 == yb) {
				pe.setNextElt(new PathElement(x,y-1)); return true;
			} else if (x <= xb && y > yb) {
				// SOUTH
				if (y - 1 >= 0 && !way[x][y-1] && map[x][y-1]) {

					way[x][y-1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y-1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x][y-1] = false;
					}
				}
				// EAST
				if (x + 1 < GRID_SIZE && !way[x+1][y] && map[x+1][y]) {
					
					way[x+1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x+1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
				// WEST
				if (x - 1 >= 0 && !way[x-1][y] && map[x-1][y]) {
					
					way[x-1][y] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x-1, y)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						way[x-1][y] = true;
					}
				}
				// NORTH
				if (y + 1 < GRID_SIZE && !way[x][y+1] && map[x][y+1]) {
					
					way[x][y+1] = true;
					
					if (findWay(pe.setNextElt(new PathElement(x, y+1)), way, xb, yb)) {
						return true;
					} else {
						pe.setNextElt(null);
						//way[x+1][y] = false;
					}
				}
			}
			
			// no way found
			return false;
		}
	}
	
	public void printGrid() {
		for (int y = 0; y < GRID_SIZE; y++) {
			Console.println("");
			for (int x = GRID_SIZE-1; x >= 0; x--)
				Console.print(""+grid[x][y].getTypeChar());
		}
	}

	public boolean isWorkToDo() {
		for (int y = 0; y < GRID_SIZE; y++) {
			for (int x = 0; x < GRID_SIZE; x++) {
				if (grid[x][y].getType() == Junction.COMMON_OBJ ||
					grid[x][y].getType() == Junction.UNKNOWN) {
					return true;
				} else if (Definitions.getInstance().myName == Definitions.MASTER && 
						   grid[x][y].getType() == Junction.MASTER_OBJ) {
					return true;
				} else if (Definitions.getInstance().myName == Definitions.SLAVE && 
						   grid[x][y].getType() == Junction.SLAVE_OBJ) {
					return true;
				}
		}	}
		
		return false;
	}

}
/*
 * $Log: Grid.java,v $
 * Revision 1.12  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.11  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.10  2009/05/06 20:05:09  mahanja
 * Commented out some unused methods
 *
 * Revision 1.9  2009/05/06 19:51:03  mahanja
 * It loads an obj very well. but somewhere before unloading is a bug inside.
 *
 * Revision 1.8  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 * Revision 1.7  2009/05/04 20:33:18  mahanja
 * It searches a way (bug with second unknown field)
 *
 * Revision 1.6  2009/05/04 15:34:05  mahanja
 * It compiles and the robot walks to somewhere
 *
 * Revision 1.5  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
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