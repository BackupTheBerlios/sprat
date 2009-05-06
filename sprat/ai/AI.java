//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.7 2009/05/06 17:17:54 mahanja Exp $
package ai;

import java.util.Vector;

import lejos.nxt.Button;
import object.Direction;
import object.Grid;
import object.Junction;
import object.PathElement;
import object.Position;
import object.Robot;
import tool.Console;
import action.Eye;
import action.Motion;
import def.Calibration;
import def.Definitions;
import ai.Task;

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
		//ai.setMode(DISCOVER_MODE);
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
		Console.println(" - RUN - ");
		life: while(grid.isWorkToDo()) {
			if (/*askedForHelp*/ false) {
				Console.println("Asked4Help");
				colaborate();
			} else {
				PathElement p = null;
				//PathElement p = grid.getAWayToNextUnknown(robo);
				if ((p = grid.getAWayToNextKnownUncommon(robo)) == null) {
Console.println("no known uncommon");					
					if ((p = grid.getAWayToNextUnknown(robo)) == null) {
Console.println("no unknown");
						/*if ((p = grid.getAWayToNextKnownCommon(robo)) == null) {
						  }*/
					}
				}
				
				if (p == null) {
//Console.println("nothing");
//Button.waitForPress();
					continue life;	// a kind of busy waiting
				}
				
				do {
Console.println("--------------------------");
Console.println("CP> x:"+robo.getMyActualPosition().getX()+" y:"+robo.getMyActualPosition().getY());
Console.println("NP> x:"+p.getX()+" y:"+p.getY());
Console.println("IF> x:"+grid.getNextProjectedPosition(robo).getX()+" y:"+grid.getNextProjectedPosition(robo).getY());
//Button.waitForPress();
					orient(p);
					//////////////////////////// ask other!!!!
					motion.goToNextJunction();					// forklift.up|down();
					grid.setJunction(new Junction(robo.getMyActualPosition(), eye.getType()));

				} while ((p = p.getNextElt()) != null);
			}
//Console.println(" - NEXT - ");
//Button.waitForPress();
		}
		
		
//Console.println(" - FIN - ");
//Button.waitForPress();
	}
	
	/**
	 * Orients the robot so that the given position is in front of.
	 * @param the position needs to be in front of
	 */
	private void orient (Position p) {
		if (grid.getRelativePositionLeft(robo).equals(p))
			motion.turn(true);
		else if (grid.getRelativePositionRight(robo).equals(p))
			motion.turn(false);
		else if (grid.getRelativePositionBehind(robo).equals(p)) {
			motion.turn(true);motion.turn(true);
		}
	}
	
	/**
	 * Sets the mode of the TaskManager
	 * @param mode influences the decision in whatToDo
	 * /
	public void setMode(int mode) {
		this.mode = mode;
	}*/

		
	public void colaborate() {
		Console.println("COLABORATE...");
	}
}

/*
 * $Log: AI.java,v $
 * Revision 1.7  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
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
