//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.13 2009/05/13 14:51:25 mahanja Exp $
package ai;

import com.Communicator;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import object.Grid;
import object.Junction;
import object.PathElement;
import object.Position;
import object.Robot;
import tool.Console;
import action.Motion;
import def.Calibration;
import def.Definitions;

/**
 * Implementation of the artificial intelligence of the robot.
 * @author greila06
 *
 */
public class AI extends Thread {
	private Motion motion;
	private Grid grid;
	private Robot robo;
	protected int mode;
	//private Eye eye;

	/**
	 * The main method of the application
	 */
	public static void main(String[] args){
		Calibration calib = new Calibration();
		Definitions defs = Definitions.getInstance();//Definitions.initInstance(Definitions.MASTER);//TODO change here
		Definitions.pilot.setSpeed(300);;
		
		AI ai = null;
		try {
			ai = new AI();
		} catch (Exception e) {
			Console.println("-------------------");
			Console.println("ERROR in Ai");
			Console.println(e.getMessage());
			Console.println("-------------------");
			Button.waitForPress();
			return;
		}

		Console.println("start ai");
		
		ai.start();
	}
	
	public Grid getGrid() {
		return grid;
	}
	
	public Robot getRobot() {
		return robo;
	}
	
	public Motion getMotion() {
		return motion;
	}
	
	/**
	 * Throws an exception if the communicator class throws one at initialization
	 */
	public AI() throws Exception {
		grid = Grid.getInstance(this);
		robo = Robot.initInstance(this);
		motion = new Motion(robo, grid);
		Communicator.getInstance(this);
	}

	/**
	 * Runs the ai...
	 */
	public void run() {
		Communicator comm = null;
		try {
			comm = Communicator.getInstance(this);
		} catch (Exception e) {
			Console.println("-------------------");
			Console.println("ERROR in Comm");
			Console.println("-------------------");
			Button.waitForPress();
			return;
		}
		while(grid.isWorkToDo()){
			PathElement p = null;
			if(motion.isObjLoaded()) {
				p = grid.getAWayBackHome(robo);
			} else if ((p = grid.getAWayToNextKnownUnCommon(robo)) == null) {
				if ((p = grid.getAWayToNextUnknown(robo)) == null) {
					/* does not work..
					if ((p = grid.getAWayToNextKnownCommon(robo)) == null) {
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// can't sleep, so do a busy waiting
						}
						continue;	// try again
					}*/
				}
			}
			Console.println("here3"); //TODO
			do {
				orient(p);
				
				// ask other
				if (!askOther(p))
					continue;	// try later again
				
				
				// if its not for me, i won't go there. (see grid.getway...)
				if (grid.getJunction(p).getType() == Junction.MASTER_OBJ ||
					grid.getJunction(p).getType() == Junction.SLAVE_OBJ) {
					// load
					motion.goToNextJunction(true);
					grid.setJunction(new Junction(robo.getMyActualPosition(), Junction.EMPTY), true);
				} else {
					// unloading loaded object
					if (motion.isObjLoaded() &&
						grid.getJunction(grid.getNextProjectedPosition(robo)).getType() == Junction.HOME_BASE) {

						motion.goToNextJunction(false);

						// orient to south
						Position orientation = grid.getNextProjectedPosition(robo);
						Position unloadPos = new Position(robo.getHomeBase().getX(), robo.getHomeBase().getY() -1);

						// unload the object
						orient(unloadPos) ;
						motion.unload();

						// orient back
						orient(orientation);
						
					// regular move
					} else 
						motion.goToNextJunction(false);
				}
				
			} while ((p = p.getNextElt()) != null);
		}
		Console.println(" - FIN - ");
		Sound.playTone(400, 1000);
		Button.waitForPress();
	}
		
	private boolean askOther(Position nextPosition) {
		try {
			Communicator comm = Communicator.getInstance(this);
			comm.resetAchnowledge();
			
			do {
				comm.sendNextPosition(nextPosition);
				//wait();	// wait until the acknowledge comes
				
				if (!comm.acknowledgeArrived()) // busy waiting // other is on this pos
					sleep(500);	// sleep for a half of a second
				
				// and try again
			} while (!comm.getAcknowledge()); // busy waiting
		} catch (Exception e) {
			// somewhere an error occurred, just try again
			return false;
		}
		return true;
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
			//motion.turnNTimes(2, true);
		}
	}
}

/*
 * $Log: AI.java,v $
 * Revision 1.13  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.12  2009/05/11 13:05:06  stollf06
 * code cleaning
 *
 * Revision 1.11  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.10  2009/05/06 20:11:49  stollf06
 * imports
 *
 * Revision 1.9  2009/05/06 20:05:10  mahanja
 * Commented out some unused methods
 *
 * Revision 1.8  2009/05/06 19:51:03  mahanja
 * It loads an obj very well. but somewhere before unloading is a bug inside.
 *
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
