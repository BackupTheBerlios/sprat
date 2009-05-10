//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Motion.java,v 1.11 2009/05/10 05:21:36 mahanja Exp $

package action;

import com.Communicator;

import object.Direction;
import object.Grid;
import object.Junction;
import object.Position;
import object.RemoteMove;
import object.Robot;
import tool.Console;
import def.Definitions;

public class Motion {
    private Robot robo;
    private Grid grid;
    private Forklift fork;
    // objOffset describes the overlap of an object over the junction
    private float objOffset = ((Definitions.objSize - 
Definitions.junctionSize) / 2)
            + Definitions.lsBarDist;
    private Definitions defs = Definitions.getInstance();

    public Motion(Robot robo, Grid grid) {
        this.robo = robo;
        this.grid = grid;
        fork = new Forklift();
    }

    /**
     *
     * @returns true if a way was found
     */
    public boolean isThereAWay() {
        int measure = Definitions.ls.readNormalizedValue();
        /*
         * TODO delet (debugging) LCD.clearDisplay(); 
LCD.drawString("measure:",
         * 0, 0); LCD.drawInt(measure, 9, 0);
         * LCD.drawInt(Definitions.colLine.min, 7, 1);
         * LCD.drawInt(Definitions.colLine.max, 11, 1); LCD.refresh();
         * Button.waitForPress(); LCD.clearDisplay();
         */

        int leftVal, rightVal;

        // test if there is a line
        if (Definitions.colLine.min <= measure
                && measure <= Definitions.colLine.max) {
            directionCorrection(true);
            return true;
        } else {
            // turn left for checking the brightness
            Definitions.pilot.rotate(-Definitions.corrTestAngle);
            leftVal = Definitions.ls.readNormalizedValue();
            // test if there is a line on the left side
            if (Definitions.colLine.min <= leftVal
                    && leftVal <= Definitions.colLine.max) {
                // continue path with a little correction
                Definitions.pilot.rotate(Definitions.corrTestAngle
                        - Definitions.corrAngle);
                return true;
            } else {
                // check on the right side for the line
                Definitions.pilot.rotate(2 * Definitions.corrTestAngle);
                rightVal = Definitions.ls.readNormalizedValue();
                if (Definitions.colLine.min <= rightVal
                        && rightVal <= Definitions.colLine.max) {
                    // continue with a little correction to the right
                    Definitions.pilot
                            .rotate(-(Definitions.corrTestAngle + 
Definitions.corrAngle));
                    return true;
                } else {
                    // no line found, go back to original position
                    Definitions.pilot.rotate(-Definitions.corrTestAngle);
                    return false;
                }
            }
        }
    }

    public boolean goToNextJunction(boolean pickUpObject) {
        directionCorrection(true);
        Position actualP = robo.getMyActualPosition();
        Position nextP = grid.getNextProjectedPosition(robo);

        // reserve the path
        robo.setMyNextPosition(nextP); // TODO: <-- ask other !!!!!

        float distanceToGo = Definitions.distBtwnJunct
                + Definitions.junctionSize;
        // go to
        Definitions.pilot.travel(distanceToGo / 2);

        // out of grid
        if (!isThereAWay()) {
            // Console.println("no way found");
            // Button.waitForPress();
            Definitions.pilot.travel(-distanceToGo / 2);
            // change the path
            // robo.setMyNextPosition(robo.getMyActualPosition());
            robo.setMyNextPosition(actualP);
            robo.setMyActualPosition(actualP);
            return false;
        }

        Definitions.pilot.travel((distanceToGo / 2) - objOffset);
        int junctType = Eye.getType();

        if (junctType == Junction.MASTER_OBJ || 
        	junctType == Junction.SLAVE_OBJ/*  || 
        	junctType == Junction.COMMON_OBJ*/) {
            
        	Junction nextJunct = new Junction(nextP, junctType);
            grid.setJunction(nextJunct, true);
			
            // do what to do
            if(pickUpObject){
                Definitions.pilot.travel(objOffset);
                fork.up();
            }else{
                Definitions.pilot.travel(-distanceToGo + objOffset);
                robo.setMyNextPosition(actualP);
                return false;
            }
            
        }

        // all ok
        Definitions.pilot.travel(objOffset);
        junctType = Eye.getType();
        grid.setJunction(new Junction(nextP, junctType), true);
        if(junctType != Junction.EMPTY){
            Definitions.pilot.travel(1);
        }
        robo.setMyActualPosition(nextP);
        return true;
    }


//  not in class diagram ...
    public boolean goNJunctions(int n) {
        for (int i = 0; i < n; i++) {
            if (!goToNextJunction(false)) {
                return false;
            }
        }
        return true;
    }

    public void goBackOneJunction() {
        directionCorrection(false);
        Position nextP = robo.getMyActualPosition();
        robo.setMyNextPosition(nextP);
        float distanceToGo = Definitions.distBtwnJunct
                + Definitions.junctionSize;
        // reverse it
        distanceToGo = distanceToGo * -1;

        // reserve the path
        Definitions.pilot.travel(distanceToGo / 2);
        directionCorrection(false);
        Definitions.pilot.travel(distanceToGo / 2);

        robo.setMyActualPosition(nextP);
    }

    public void turn(boolean isLeft) {
        robo.changeOrientation(isLeft);
        Definitions.pilot.travel(Definitions.distBtwnLsWheel);

        if (isLeft) {
            Definitions.pilot.rotate(Definitions.leftJunctAngle);
        } else {
            Definitions.pilot.rotate(Definitions.rightJunctAngle);
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }

    public void turnNTimes(int n, boolean isLeft) {
        robo.changeOrientation(isLeft);
        Definitions.pilot.travel(Definitions.distBtwnLsWheel);
        for (int i = 0; i < n; i++) {
            if (isLeft) {
                Definitions.pilot.rotate(Definitions.leftJunctAngle);
            } else {
                Definitions.pilot.rotate(Definitions.rightJunctAngle);
            }
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }

    /*public void turnRound(boolean isLeft) {
        Definitions.pilot.travel(Definitions.distBtwnLsWheel);
        robo.changeOrientation(isLeft);
        float radius = Definitions.distBtwnJunct + Definitions.junctionSize;
        if (isLeft) {
            Definitions.pilot.turn(radius, Definitions.leftJunctAngle);
        } else {
            Definitions.pilot.turn(radius, Definitions.rightJunctAngle);
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }

    public void turnRoundNTimes(int n, boolean isLeft) {
        Definitions.pilot.travel(Definitions.distBtwnLsWheel);
        robo.changeOrientation(isLeft);
        float radius = Definitions.distBtwnJunct + Definitions.junctionSize;
        for (int i = 0; i < n; i++) {
            if (isLeft) {
                Definitions.pilot.turn(radius, Definitions.leftJunctAngle);
            } else {
                Definitions.pilot.turn(radius, Definitions.rightJunctAngle);
            }
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }

    public void turnRoundBackwards(boolean isLeft) {
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
        robo.changeOrientation(!isLeft);
        float radius = Definitions.distBtwnJunct + Definitions.junctionSize;
        if (isLeft) {
            Definitions.pilot.turn(-radius, Definitions.leftJunctAngle);
        } else {
            Definitions.pilot.turn(-radius, Definitions.rightJunctAngle);
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }

    public void turnRoundBackwardsNTimes(int n, boolean isLeft) {
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
        robo.changeOrientation(!isLeft);
        float radius = Definitions.distBtwnJunct + Definitions.junctionSize;
        for (int i = 0; i < n; i++) {
            if (isLeft) {
                Definitions.pilot.turn(-radius, Definitions.leftJunctAngle);
            } else {
                Definitions.pilot.turn(-radius, 
Definitions.rightJunctAngle);
            }
        }
        Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
    }*/

    public void directionCorrection(boolean isForward) {
        Definitions.pilot.rotate(-Definitions.corrTestAngle);
        int leftVal = Definitions.ls.readNormalizedValue();
        Definitions.pilot.rotate(2 * Definitions.corrTestAngle);
        int rightVal = Definitions.ls.readNormalizedValue();
        /*
         * TODO delete (debugging) LCD.clear(); LCD.drawString("left: ", 
0, 0);
         * LCD.drawInt(leftVal, 7, 0); LCD.drawString("right: ", 0, 1);
         * LCD.drawInt(rightVal, 7, 1); LCD.refresh(); 
Button.waitForPress();
         * LCD.clearDisplay();
         */
        // TODO check closer
        if (leftVal < rightVal - Definitions.brightTolerance) {// left is darker
            // than right
            if (isForward) {
                // so line is on the left side-> make a correction
                Definitions.pilot.rotate(-Definitions.corrTestAngle
                        - Definitions.corrAngle);
            } else {
                Definitions.pilot.rotate(-Definitions.corrTestAngle
                        + Definitions.corrAngle);
            }
        } else if (rightVal < leftVal - Definitions.brightTolerance) {// right
            // is
            // darker
            // than
            // the
            // left
            if (isForward) {
                // line is on the right side -> make a correction
                Definitions.pilot.rotate(-Definitions.corrTestAngle
                        + Definitions.corrAngle);
            } else {
                Definitions.pilot.rotate(-Definitions.corrTestAngle
                        - Definitions.corrAngle);
            }
        } else {
            // lane is in the middle, turn back to origin
            Definitions.pilot.rotate(-Definitions.corrTestAngle);
        }
    }

	public boolean isObjLoaded() {
		return !fork.isDown();
	}

	public void unload() {
        float distanceToGo = Definitions.distBtwnJunct
        + Definitions.junctionSize;
        
        // go a little bit forward
        Definitions.pilot.travel(distanceToGo / 2);
        // unload
        fork.down();
        // go back
        Definitions.pilot.travel(-distanceToGo / 2);
	}
	
	// remote motions
	
	// REMOTE
	/*public void performRemoteMove(int move) {
		if (move == RemoteMove.GO_FORWARD) {
			goToNextJunction(false);
		} else if (move == RemoteMove.TURN_RIGHT_FORWARD) {
			turnRound(false);
		} else if (move == RemoteMove.TURN_LEFT_FORWARD) {
			turnRound(true);
		} else if (move == RemoteMove.GO_BACKWARD) {
			goBackOneJunction();
		} else if (move == RemoteMove.TURN_RIGHT_BACKWARD) {
			turnRoundBackwards(false);
		} else if (move == RemoteMove.TURN_LEFT_BACKWARD) {
			turnRoundBackwards(true);
		} else if (move == RemoteMove.TURN_RIGHT) {
			turn(false);
		} else if (move == RemoteMove.TURN_LEFT ) {
			turn(true);
		} else if (move == RemoteMove.FORKLIFT_DOWN ) {
			fork.down();
		} else if (move == RemoteMove.FORKLIFT_UP) {
			fork.up();
		}
	}*/
}
/*
 * $Log: Motion.java,v $
 * Revision 1.11  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.10  2009/05/06 19:51:03  mahanja
 * It loads an obj very well. but somewhere before unloading is a bug inside.
 * Revision 1.8 2009/05/04 15:39:19 stollf06 with the
 * motions for the slave robot
 *
 * Revision 1.7 2009/05/04 15:34:05 mahanja It compiles and the robot 
walks to
 * somewhere
 *
 * Revision 1.6 2009/05/04 15:15:17 mahanja Ai is mostly implemented but is
 * still throwing errors everywhere!
 *
 * Revision 1.5 2009/04/29 16:06:51 stollf06 better handling of next 
junction
 * and actualjunction
 *
 * Revision 1.4 2009/04/27 19:53:55 stollf06 introduction of orientation 
on the
 * grid
 *
 * Revision 1.3 2009/04/27 09:55:14 stollf06 finished isThereAWay method and
 * added commentaries Revision 1.2 2009/04/23 21:22:00 mahanja added the 
log cvs
 * keyword
 */

