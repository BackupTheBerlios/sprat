//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.11 2009/05/10 05:21:36 mahanja Exp $
package ai;

import java.util.Vector;

import com.Communicator;
import com.msg.RemoteMessage;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import object.Direction;
import object.Grid;
import object.Junction;
import object.PathElement;
import object.Position;
import object.RemoteMove;
import object.Robot;
import tool.Console;
import action.Eye;
import action.Forklift;
import action.Motion;
import def.Calibration;
import def.Definitions;
import ai.Task;

public class AI extends Thread {
	private Motion motion;
	private Grid grid;
	private Robot robo;
	protected int mode;
	//private Eye eye;

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Calibration calib = new Calibration();
//		Console.println("calibration done");
		//Definitions.wayFinderOn=true;//TODO delete, just for debugging
		Definitions defs = Definitions.getInstance();//Definitions.initInstance(Definitions.MASTER);//TODO change here
		Definitions.pilot.setSpeed(300);
//		Button.waitForPress();
		
		AI ai = null;
		try {
			ai = new AI();
		} catch (Exception e) {
			Console.println("-------------------");
			Console.println("ERROR in Ai");
			Console.println("-------------------");
			Button.waitForPress();
			return;
		}

		Console.println("start ai");
		//Button.waitForPress();
		
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
		
		//Console.println(" - RUN - ");
		life: while(grid.isWorkToDo()) {
			/*if (comm.askedForHelp()) {
				// the other one asked this robot for help
															Console.println("Asked4Help");
				Position p = comm.getNeedHelpPosotion();
				Direction d = comm.getNeedHelpOrientation();
				
				Position pbefore; // the position from which to come
				if (d.getDirection() == Direction.NORTH) {
					pbefore = new Position(p.getX(), p.getY()+1);
				} else if (d.getDirection() == Direction.EAST) {
					pbefore = new Position(p.getX()+1, p.getY());
				} else if (d.getDirection() == Direction.SOUTH) {
					pbefore = new Position(p.getX(), p.getY()-1);
				} else { //if (d.getDirection() == Direction.WEST) {
					pbefore = new Position(p.getX()-1, p.getY());
				}
					 
				// add the demanded position
				PathElement way = grid.getAWayTo(robo, pbefore);
				way.getLast().setNextElt(new PathElement(p));
				
				if (way.hasNextElt())
					do {
						Position currP = way.getNextElt();
						orient(currP);
						
						// ask other
						if (!askOther(currP))
							continue;	// try later again
						motion.goToNextJunction(false);
					} while ((way = way.getNextElt()) != null);
				
				try {
					comm.sendReadyToHelp();
				} catch (Exception e) {
					Console.println("E: send ready");
				}
				
				while(comm.helpFinished());	// busy waiting
			} else if (grid.getJunction(grid.getNextProjectedPosition(robo)).getType() == Junction.COMMON_OBJ) { 
				// the robot stands now in front of a common obj and will act as master
															Console.println("I am the Master");
				
				// ask other to come to help me
				try {
					comm.sendDemandHelp(grid.othersPositionToHelp(robo.getMyActualPosition()), new Direction(robo.getOrientation()));
				} catch (Exception e) {
					Console.println("E: can't demand help");
					continue;	// like this it does not gives any sense
				}
				
				while (!comm.getReadyToHelp()); // busy waiting;
				
				Position commonHomeBase = new Position(0, Definitions.commonJunctOffset);
				PathElement way = grid.getAWayTo(robo, commonHomeBase);

				if (way.hasNextElt())
					do {
						collaborativeOrient(way.getNextElt());
					} while ((way = way.getNextElt()) != null);
				
			}else {*/
				// no cooperation yet
															Console.println("found: ");
				PathElement p = null;
				if(motion.isObjLoaded()) {
					p = grid.getAWayBackHome(robo);
															Console.println("  back home");
				} else if ((p = grid.getAWayToNextKnownUnCommon(robo)) == null) {
															Console.println("  no known mine");					
					if ((p = grid.getAWayToNextUnknown(robo)) == null) {
															Console.println("  no unknown");
						/*if ((p = grid.getAWayToNextKnownCommon(robo)) == null) {
															Console.println("  no collective");
							try {
								sleep(500);
							} catch (InterruptedException e) {
								// can't sleep, so do a busy waiting
							}
							continue;	// try again
						}*/
					}
				}
				
/*				if (p == null) {
Console.println("  nothing");
					continue life; //	try again
				}*/
				
				do {
Console.println("--------------------------");
grid.printGrid();
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
			//}
//Console.println(" - NEXT - ");
//Button.waitForPress();
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
Console.println(" - TRY AGAIN - ");
//Button.waitForPress();
				
			} while (!comm.getAcknowledge()); // busy waiting
		} catch (Exception e) {
			// somewhere an error occured, just try again
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
	
	/**
	 * on collaborative orientation both robots must be 
	 * able to follow the move to not skip the payload.
	 * /
	private boolean collaborativeOrient(Position p) {
		try {
			if (grid.getRelativePositionLeft(robo).equals(p)) {
				int rm = RemoteMove.TURN_LEFT_BACKWARD;
				Communicator.getInstance(this).sendRemoteMove(rm);
				motion.turn(true);
			} else if (grid.getRelativePositionRight(robo).equals(p)) {
				int rm = RemoteMove.TURN_RIGHT_BACKWARD;
				Communicator.getInstance(this).sendRemoteMove(rm);
				motion.turn(false);
			} else if (grid.getRelativePositionBehind(robo).equals(p)) {
				int rm = RemoteMove.TURN_LEFT_BACKWARD;
				// first time...
				Communicator.getInstance(this).sendRemoteMove(rm);
				motion.turn(true);
				// second time...
				sleep(500);	// wait until the other robot had finished his move
				Communicator.getInstance(this).sendRemoteMove(rm);
				motion.turn(true);
				//motion.turnNTimes(2, true);
			}
		} catch (Exception e) {
			// Oops a problem...
			return false;
		}
		return true;
	}*/
	
	
	/**
	 * Sets the mode of the TaskManager
	 * @param mode influences the decision in whatToDo
	 * /
	public void setMode(int mode) {
		this.mode = mode;
	}*/

		
	/*public void colaborate() {
		Console.println("COLABORATE...");
	}*/
}

/*
 * $Log: AI.java,v $
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
