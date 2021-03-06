//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Robot.java,v 1.7 2009/05/13 14:51:25 mahanja Exp $
package object;

import action.Eye;
import ai.AI;
import lejos.nxt.Button;
import tool.Console;
import def.Definitions;

/**
 * A robot has an actual as well as a next position. This object manages
 * those position for both robots, the hosting and the other one. 
 * It knows his homebase and the place where to tidy the common objects.
 * @author greila06
 *
 */
public class Robot {
	private Grid grid;
	private Position homeBase;
	private Position commonHomeBase;
	private int orientation = Direction.NORTH;
	private Position myActualPosition;
	private Position myNextPosition;
	private Position otherActualPosition;
	private Position otherNextPosition;

	private static Robot instance = null;
	
	/** 
	 * Returns the singleton instance of this object  
	 * @return null if initInstance(AI ai) wasn't invoked before
	 */
	public static Robot getInstance() {
		return instance;
	}
	
	/**
	 * Initializes the singleton instance of this object.
	 * @param ai
	 * @return
	 */
	public static Robot initInstance(AI ai) {
		if(instance == null){
			instance = new Robot(ai.getGrid()); 
		}
		return instance;
	}
	
	private Robot(Grid grid) {
		this.grid = grid;
		Definitions def = Definitions.getInstance();
		if (def.myName.equals(Definitions.MASTER)) {
			homeBase = new Position(Definitions.masterJnctOffset, 0);
			myActualPosition = homeBase;
			otherActualPosition = new Position(Definitions.slaveJunctOffset+
							     			   Definitions.masterJnctOffset, 0); 
		} else {
			homeBase = new Position(Definitions.slaveJunctOffset+
					                Definitions.slaveJunctOffset, 0);
			myActualPosition = homeBase;
			otherActualPosition = new Position(Definitions.masterJnctOffset, 0);
		}
		grid.setJunction(new Junction(myActualPosition, Junction.HOME_BASE), false);
		commonHomeBase = new Position(Definitions.commonJunctOffset, 0);

		this.myActualPosition = homeBase;
		this.myNextPosition = homeBase;
	}
	
	/**
	 * Must be invoked if the orientation changes
	 * @param isLeft if true the robot turns left, else it turns right.
	 */
	public void changeOrientation(boolean isLeft){
		if(isLeft){
			orientation++;
		}else{
			orientation--;
		}
		if(orientation<0){
			orientation = 3;
		}else if(orientation >3){
			orientation =0;
		}
	}
	
	// GETTERS
	/**
	 * Returns the orientation
	 * @return one of the static variables of object.Direction.
	 */
	public int getOrientation() {
		return orientation;
	}
	
	/**
	 * Returns the position of the home base 
	 * @return the position of the home base
	 */
	public Position getHomeBase() {
		return homeBase;
	}

	/**
	 * Returns the actual position of the hosting robot
	 * @return the actual position of the hosting robot
	 */
	public Position getMyActualPosition() {
		return myActualPosition;
	}

	/**
	 * Returns the next position of the hosting robot
	 * @return the next position of the hosting robot
	 */
	public Position getMyNextPosition() {
		return myNextPosition;
	}
	
	/**
	 * Returns the actual position of the other robot
	 * @return the actual position of the other robot
	 */
	public Position getOtherActualPosition() {
		return otherActualPosition;
	}
	
	/**
	 * Returns the next position of the other robot
	 * @return the next position of the other robot
	 */
	public Position getOtherNextPosition() {
		return otherNextPosition;
	}

	// SETTERS
	/**
	 * Sets the next position of the hosting robot
	 * @param myNextPos the next position of the hosting robot
	 */
	public void setMyNextPosition(Position myNextPos) {
		if (myNextPos.equals(myActualPosition))
			grid.setJunction(new Junction(myNextPosition, Junction.OUTSIDE), false);
		
		this.myNextPosition = myNextPos;
	}

	/**
	 * Sets the next position of the other robot
	 * @param myNextPos the next position of the other robot
	 */
	public void setOtherNextPosition(Position otherNextPos) {
		this.otherNextPosition = otherNextPos;
	}

	/**
	 * Sets the actual position of the hosting robot
	 * @param myNextPos the actual position of the hosting robot
	 */
	public void setMyActualPosition(Position myActualPos) {
		this.myActualPosition = myActualPos;
	}

	/**
	 * Sets the actual position of the other robot
	 * @param myNextPos the actual position of the other robot
	 */
	public void setOtherActualPosition(Position otherActualPos) {
		this.otherActualPosition = otherActualPos; 
	}

}
/*
 * $Log: Robot.java,v $
 * Revision 1.7  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.6  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.5  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 * Revision 1.4  2009/05/04 20:33:18  mahanja
 * It searches a way (bug with second unknown field)
 *
 * Revision 1.3  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 * Revision 1.2  2009/04/29 16:06:24  stollf06
 * better handling of next junction and actualjunction
 *
 * Revision 1.1  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 */
