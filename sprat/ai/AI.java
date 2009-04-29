//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.2 2009/04/29 19:01:23 mahanja Exp $
package ai;

import java.util.Vector;

import object.Grid;
import object.Junction;
import object.Robot;
import tool.Console;
import action.Motion;
import def.Calibration;
import def.Definitions;

public class AI {
	public static final int DISCOVER_MODE = 0; // if field size isn't known
	public static final int SEARCH_MODE = 1;   // field size known, searching objs by random
	public static final int GOHELP_MODE = 2;   // on the way to help to the other 
	public static final int TIDY_MODE = 3;     // bringing an obj to my home base 
	public static final int SEAKWAY_MODE = 4;  // if seaking a way to bring home common
	public static final int FINISHED_MODE = 5; // all work is done
	
	private Motion motion;
	private Grid grid;
	private Robot robo;
	protected int mode;

	/**
	 * @param args
	 */
	public static void main(String[] args){
		int mode = DISCOVER_MODE;	// ever the initial mode
		
		Calibration calib = new Calibration();
		Console.println("calibration done");
		Definitions.wayFinderOn=true;//TODO delete, just for debugging
		Definitions defs = Definitions.initInstance(Definitions.MASTER);//TODO change here
		Definitions.pilot.setSpeed(300);
		AI ki = new AI();
		ki.searchMode();
	}
	
	public AI(){
		grid = Grid.getInstance();
		robo = new Robot(grid);
		motion = new Motion(robo, grid);
		Console.println("KI init");
		
	}
	
	/**
	 * search for objects on the grid (go forward until there is no way, turn right/left 2 times)
	 */
	public void searchMode(){
		boolean isLeft = false;
		//int j=0;
		for (int i = 0; i < 4; i++){
			while(motion.goToNextJunction()){
				Console.println("direction:"+robo.getOrientation());
				//Button.waitForPress();
				Console.println("actual:"+
						robo.getMyActualJunction().getPosition().getX()+
						"/"+robo.getMyActualJunction().getPosition().getY());
			}
			motion.turn(isLeft);
			motion.goToNextJunction();
			motion.turn(isLeft);
			isLeft = !isLeft;
		}
	}
	
	//-------------------------------------------------
	
	/**
	 * Sets the mode of the TaskManager
	 * @param mode influences the decision in whatToDo
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	/**
	 * !!! THE MODE MUST BE SET BY: when a obj is back @ homebase | help finished | ...
	 * @param oldTask
	 * @return
	 */
	public Task whatToDo(Task oldTask) {
		// if the task can not be broken up
		if (!oldTask.isBreakable())
			return oldTask;
		
		// size of the grid is unknown
		if (mode == DISCOVER_MODE) {
			// discovering an empty or myhomebase junction
			if (robo.getMyActualJunction().getType() == Junction.EMPTY ||
				robo.getMyActualJunction().getType() == Junction.HOME_BASE) {
				Vector v = new Vector(1);
				v.addElement(grid.getNextProjectedJunction(robo));
				return new Task(v, mode);
			// found an obj belonging to me
			} else if ((robo.getMyActualJunction().getType() == Junction.MASTER_OBJ 
					    && Definitions.getInstance().myName == Definitions.MASTER) ||
					   (robo.getMyActualJunction().getType() == Junction.SLAVE_OBJ 
						&& Definitions.getInstance().myName == Definitions.SLAVE)) {
				setMode(TIDY_MODE);
				Vector v = grid.getAWayBackHome(robo);
				return new Task(v, mode);
			} /* obj not for me */
		// size is known but not all junctions
		} else if (mode == SEARCH_MODE) {
			// no cluttered obj known
			if (grid.nothingElseThanSearching()) {
				if (oldTask.hasNextPosition())
					return oldTask; // old task not yet finished
				else
					return new Task(grid.getAWayToNextUnknown(robo), SEARCH_MODE); // need a new task
			// there is something to do
			} else {
				Vector v = grid.getAWayToNextKnown(robo); // an obj concerning to me or a common one
				
				if (((Junction)v.elementAt(v.size()-1)).getType() == Junction.MASTER_OBJ ||
					((Junction)v.elementAt(v.size()-1)).getType() == Junction.SLAVE_OBJ)
					setMode(TIDY_MODE);
				else
					setMode(SEAKWAY_MODE);
				
				return new Task(v, mode);
			}
		} else if (grid.nothingMoreToDo()) {
			if (robo.getMyActualJunction().equals(robo.getHomeBase())) {
				setMode(FINISHED_MODE);
				return new Task(new Vector(), mode);
			} else {
				return new Task(grid.getAWayBackHome(robo), mode);
			}
		}
		
		// if reached the following lines it is a bug!
		Console.println("E: no task def");
		return null;
	}
}

/*
 * $Log: AI.java,v $
 * Revision 1.2  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.1  2009/04/29 09:56:12  mahanja
 * Added to the repository
 *
 */
