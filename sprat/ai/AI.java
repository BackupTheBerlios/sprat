//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.6 2009/05/04 20:33:19 mahanja Exp $
package ai;

import java.util.Vector;

import lejos.nxt.Button;
import object.Direction;
import object.Grid;
import object.Junction;
import object.Position;
import object.Robot;
import tool.Console;
import action.Eye;
import action.Motion;
import def.Calibration;
import def.Definitions;

public class AI {
	public static final int DISCOVER_MODE = 0; // if field size isn't known
	public static final int SEARCH_MODE = 1;   // field size known, searching objs by random
	public static final int GOHELP_MODE = 2;   // on the way to help to the other 
	public static final int TIDY_MODE = 3;     // bringing an obj to my home base 
	public static final int SEEKWAY_MODE = 4;  // if seaking a way to bring home common
	public static final int FINISHED_MODE = 5; // all work is done
	
	private Motion motion;
	private Grid grid;
	private Robot robo;
	protected int mode;
	private Eye eye;

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Calibration calib = new Calibration();
		Console.println("calibration done");
		Definitions.wayFinderOn=true;//TODO delete, just for debugging
		Definitions defs = Definitions.initInstance(Definitions.MASTER);//TODO change here
		Definitions.pilot.setSpeed(300);
		AI ai = new AI();
		ai.setMode(DISCOVER_MODE);
		ai.run();
		//ki.searchMode();
	}
	
	public AI(){
		grid = Grid.getInstance();
		robo = Robot.initInstance(grid);
		motion = new Motion(robo, grid);
		eye = new Eye();
		Console.println("Ai init");
		
	}

	public void run() {
		Task task = null,
	      oldtask = null;
		
		while ((task = whatToDo(oldtask)) != null) {
			
			if (!task.hasNextPosition()) {
Console.println(" FUCK ");
Button.waitForPress();
				continue; // a kind of busy waiting
			}
//Console.println(" SUPI ");
//Button.waitForPress();
			
			Position nextP = task.nextPosition();
			// turn around
Console.println(" CP: x"+robo.getMyActualPosition().getX()+"-y"+robo.getMyActualPosition().getY());
Console.println(" NP: x"+nextP.getX()+"-y"+nextP.getY());
//Button.waitForPress();
			Position inFront = grid.getNextProjectedPosition(robo);
			
			if (!nextP.equals(inFront))
				if (nextP.equals(grid.getRelativePositionLeft(robo))) {
//Console.println(" LEFT ");
//Button.waitForPress();
					motion.turn(true);
				} else if (nextP.equals(grid.getRelativePositionRight(robo))) {
//Console.println(" RIGHT ");
//Button.waitForPress();
					motion.turn(false);
				} else {
//Console.println(" BEHIND ");
//Button.waitForPress();
					motion.turn(true); motion.turn(true);
				}
			// go
			motion.goToNextJunction();
			oldtask = task;
			grid.setJunction(new Junction(robo.getMyActualPosition(), eye.getType()));
		}
		Console.println(" - FIN - ");
		Button.waitForPress();
	}
	
	/**
	 * search for objects on the grid (go forward until there is no way, turn right/left 2 times)
	 * /
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
	}*/
	
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

		if (oldTask != null)
			if (!oldTask.isBreakable() && oldTask.hasNextPosition())
				return oldTask;
		
		// oldTask seems to be finished, create a new one.
		// update mode
		if (mode != SEEKWAY_MODE && grid.isGridSizeKnown()) {
			setMode(SEARCH_MODE);
		} else {
			setMode(DISCOVER_MODE);
		}
		
		// size of the grid is unknown
		if (mode == DISCOVER_MODE) {
			// discovering an empty or myhomebase junction
			if (grid.getJunction(robo.getMyActualPosition()).getType() == Junction.EMPTY |
				grid.getJunction(robo.getMyActualPosition()).getType() == Junction.HOME_BASE) {
				Vector v = new Vector(1);
				if (grid.getJunction(robo.getMyActualPosition()).getType() == Junction.HOME_BASE &&
					robo.getOrientation() == Direction.SOUTH) { // looking towards homebase
					motion.turn(true);motion.turn(true);
				}       // turn around
				v.addElement(grid.getNextProjectedPosition(robo));
				return new Task(v, mode);
			}
		}// OK!!!
		
		
		
		if (mode == DISCOVER_MODE || mode == SEARCH_MODE || mode == SEEKWAY_MODE) {
			// found an obj belonging to me
			if (((grid.getJunction(robo.getMyActualPosition()).getType() == Junction.MASTER_OBJ) &&
			    Definitions.getInstance().myName == Definitions.MASTER) ||
			    ((grid.getJunction(robo.getMyActualPosition()).getType() == Junction.SLAVE_OBJ) &&
			    Definitions.getInstance().myName == Definitions.SLAVE)) {
				
				setMode(TIDY_MODE);
				Vector v = grid.getAWayBackHome(robo);
				return new Task(v, mode);
			} // obj not for me
		// size is known but not all junctions
		}// NOK
		
		
		
		
		if (mode == SEARCH_MODE) {
			Vector v = null; //Position p = null;
			// no cluttered obj known
			if (grid.nothingElseThanSearching()) { // does not include common obj
				if (oldTask.hasNextPosition()) {
					return oldTask; // old task not yet finished
				} else {
grid.getAWayToNextUnknown(robo);
					return new Task(grid.getAWayToNextUnknown(robo), SEARCH_MODE); // need a new task
				}
			// there is an obj for me to tidy
			}/* else if (grid.isThereAnObjectForMe()) {
				v = grid.getAWayToNextKnownUncommon(robo); // an obj concerning to me or a common one
				
				//if (((Junction)v.elementAt(v.size()-1)).getType() == Junction.MASTER_OBJ ||
					//((Junction)v.elementAt(v.size()-1)).getType() == Junction.SLAVE_OBJ)
					setMode(TIDY_MODE);
				//else
					
					// wenn er t orientation nit kenn, gihter drumum
					// wennersa kennt u de wäg frii isch stihter vorhäre -> det bruuchts näi no eppis alla a nüa modus
					// we de wäg nit frii isch, de suechterne...
			// there is only a common to tidy
			} else if ((p = grid.getNextCommonObj()) != null) {
				/*if (!grid.isCommonObjOrientationKnown(p)) {
					v = grid.getAWayAround(p);
					setMode(SEEKWAY_MODE);
				}
				
				/*
				if (!grid.isCommonObjOrientationKnown(p)) {
					v = grid.getAWayAround(p);
					setMode(SEEKWAY_MODE);
				} else (!grid.isCommonObjWayBackPossible(p)) {
					v = grid.exploreCommonObjWayBackHome(robot);
					setMode(SEEKWAY_MODE);
				} else (!grid.readyToPickUpCommonObj(robo, p)) {
					v = grid.getAWayToNextCommonObj(robo, p);
				} else { // all ok for colaboration
					colaborate();
				}* /
			}*/
			
			return new Task(v, mode);
		}/* else if (grid.nothingMoreToDo()) {
			if (grid.getJunction(robo.getMyActualPosition()).equals(robo.getHomeBase())) {
				setMode(FINISHED_MODE);
				return new Task(new Vector(), mode);
			} else {
				return new Task(grid.getAWayBackHome(robo), mode);
			}
		}*/
		// if reached the following lines it is a bug!
		Console.println("E: no task def");
		return null;
	}
	
	public void colaborate() {
		Console.println("COLABORATE...");
	}
}

/*
 * $Log: AI.java,v $
 * Revision 1.6  2009/05/04 20:33:19  mahanja
 * It searches a way (bug with second unknown field)
 *
 * Revision 1.5  2009/05/04 15:40:41  stollf06
 * took out a wrong import
 *
 * Revision 1.4  2009/05/04 15:34:06  mahanja
 * It compiles and the robot walks to somewhere
 *
 * Revision 1.3  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 * Revision 1.2  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 * Revision 1.1  2009/04/29 09:56:12  mahanja
 * Added to the repository
 *
 */
