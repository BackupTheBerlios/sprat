//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Robot.java,v 1.4 2009/05/04 20:33:18 mahanja Exp $
package object;

import action.Eye;
import lejos.nxt.Button;
import tool.Console;
import def.Definitions;

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
	
	public static Robot getInstance() {
		return instance;
	}
	
	public static Robot initInstance(Grid grid) {
		if(instance == null){
			instance = new Robot(grid); 
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
		grid.setJunction(new Junction(myActualPosition, Junction.HOME_BASE));
		commonHomeBase = new Position(Definitions.commonJunctOffset, 0);
	}
	
	public void changeOrientation(boolean isLeft){
		if(isLeft){
			orientation--;
		}else{
			orientation++;
		}
		if(orientation<0){
			orientation = 3;
		}else if(orientation >3){
			orientation =0;
		}
	}
	
	// GETTERS
	public int getOrientation() {
		return orientation;
	}
	
	public Position getHomeBase() {
		return homeBase;
	}

	public Position getMyActualPosition() {
		return myActualPosition;
	}

	public Position getMyNextPosition() {
		return myNextPosition;
	}

	public Position getOtherActualPosition() {
		return otherActualPosition;
	}

	public Position getOtherNextPosition() {
		return otherNextPosition;
	}

	// SETTERS
	public void setMyNextPosition(Position myNextPos) {
		if (myNextPos.equals(myActualPosition))
			grid.setJunction(new Junction(myNextPosition, Junction.OUTSIDE));
		
		this.myNextPosition = myNextPos;
	}

	public void setOtherNextPosition(Position otherNextPos) {
		this.otherNextPosition = otherNextPos;
	}

	public void setMyActualPosition(Position myActualPos) {
		this.myActualPosition = myActualPos;
	}

	public void setOtherActualPosition(Position otherActualPos) {
		this.otherActualPosition = otherActualPos; 
	}
}
/*
 * $Log: Robot.java,v $
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
