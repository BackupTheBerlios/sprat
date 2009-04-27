//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/Robot.java,v 1.1 2009/04/27 19:53:55 stollf06 Exp $
package object;

import def.Definitions;

public class Robot {
	private Grid grid;
	private Position homeBase;
	private int orientation = Direction.NORTH;
	private Junction myActualJunction;
	private Junction myNextJunction;
	private Junction otherActualJunction;
	private Junction otherNextJunction;

	public Robot(Grid grid) {
		this.grid = grid;
		Definitions def = Definitions.getInstance();
		if (def.myName.equals(Definitions.MASTER)) {
			homeBase = new Position(Definitions.masterJnctOffset, 0);
			myActualJunction = new Junction(homeBase, Junction.EMPTY);
			otherActualJunction = new Junction(new Position(Definitions.slaveJunctOffset+Definitions.masterJnctOffset, 0), Junction.HOME_BASE);
		} else {
			homeBase = new Position(Definitions.slaveJunctOffset+Definitions.slaveJunctOffset, 0);
			myActualJunction = new Junction(homeBase, Junction.HOME_BASE);
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
	public void setMyNextJunction(Junction myNextJunction) {
		myActualJunction = this.myNextJunction;
		this.myNextJunction = myNextJunction;
		grid.setJunction(myNextJunction);
	}

	public void setOtherNextJunction(Junction otherNextJunction) {
		otherActualJunction = this.otherNextJunction;
		this.otherNextJunction = otherNextJunction;
		grid.setJunction(otherNextJunction);
	}
}
/*
 * $Log: Robot.java,v $
 * Revision 1.1  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 */