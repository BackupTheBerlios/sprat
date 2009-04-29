//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Robot.java,v 1.2 2009/04/29 16:06:24 stollf06 Exp $
package object;

import lejos.nxt.Button;
import tool.Console;
import def.Definitions;

public class Robot {
	private Grid grid;
	private Position homeBase;
	private int orientation = Direction.NORTH;
	private Junction myActualJunction;
	private Junction myNextJunction;
	private Junction otherActualJunction;
	private Junction otherNextJunction;

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
			myActualJunction = new Junction(homeBase, Junction.EMPTY);
			Position nextPos = new Position(homeBase.getX(), homeBase.getY()+1);
			myNextJunction = new Junction(nextPos, Junction.UNKNOWN);
			otherActualJunction = new Junction(new Position(Definitions.slaveJunctOffset+Definitions.masterJnctOffset, 0), Junction.HOME_BASE);
		} else {
			homeBase = new Position(Definitions.slaveJunctOffset+Definitions.slaveJunctOffset, 0);
			myActualJunction = new Junction(homeBase, Junction.HOME_BASE);
			Position nextPos = new Position(homeBase.getX(), homeBase.getY()+1);
			myNextJunction = new Junction(nextPos, Junction.UNKNOWN);
			otherActualJunction = new Junction(new Position(Definitions.masterJnctOffset, 0), Junction.HOME_BASE);
		}
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
	

	/**
	 * 
	 * @return Position of homebase
	 */
	public Position getHomeBase() {
		return homeBase;
	}

	public Junction getMyActualJunction() {
		return myActualJunction;
	}

	public Junction getMyNextJunction() {
		return myNextJunction;
	}

	public Junction getOtherActualJunction() {
		return otherActualJunction;
	}

	public Junction getOtherNextJunction() {
		return otherNextJunction;
	}

	

	// SETTERS
	public void setMyActualJunction(Junction junct) {
		this.myActualJunction = junct;
	}
	
	public void setMyNextJunction(Junction myNextJunction) {
		this.myNextJunction= myNextJunction;
		//this.myActualJunction = this.myNextJunction;
		//Console.println("inSetMyNext");
		//Button.waitForPress();
		//this.myNextJunction = myNextJunction;
		//grid.setJunction(myNextJunction);
	}

	public void setOtherNextJunction(Junction otherNextJunction) {
		otherActualJunction = this.otherNextJunction;
		this.otherNextJunction = otherNextJunction;
		//grid.setJunction(otherNextJunction);
	}
}
/*
 * $Log: Robot.java,v $
 * Revision 1.2  2009/04/29 16:06:24  stollf06
 * better handling of next junction and actualjunction
 *
 * Revision 1.1  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 */