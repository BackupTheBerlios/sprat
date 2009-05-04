//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Forklift.java,v 1.3 2009/05/04 15:15:17 mahanja Exp $

package action;
import lejos.nxt.Button;
import lejos.nxt.Motor;

/**
 * An object controlling the forklift functionality of the SPRAT 
 * @author $Author: mahanja $
 */
public class Forklift {
	
	private static int SPEED = 50,
	                UP_ANGLE = -85,
	              DOWN_ANGLE = 85;
	private boolean isDown = true; // initial position
	
	private boolean toggle = true; // testing
	
	public Forklift () {
		//
	}
	
	/**
	 * causes the forklift going down
	 */
	public void down() {
		Motor.C.resetTachoCount();
		Motor.C.setSpeed(SPEED);
		Motor.C.rotateTo(UP_ANGLE);
		isDown = true;
	}
	
	/**
	 * causes the forklift going up
	 */
	public void up() {
		Motor.C.resetTachoCount();
		Motor.C.setSpeed(SPEED);
		Motor.C.rotateTo(DOWN_ANGLE);
		isDown = false;
	}
	
	/**
	 * returns true if forklift is down, else false
	 */
	public boolean isDown() {
		return isDown;
	}
	
	/** for testing purposes only */
	public static void main(String args[]) {
		Forklift fl = new Forklift();
		while (true) {
			Button.waitForPress();
			if (fl.toggle)
				fl.down();
			else
				fl.up();
			fl.toggle = !fl.toggle;
		}
	}

	/*
	 * $Log: Forklift.java,v $
	 * Revision 1.3  2009/05/04 15:15:17  mahanja
	 * Ai is mostly implemented but is still throwing errors everywhere!
	 *
	 * Revision 1.2  2009/04/29 14:32:14  mahanja
	 * Adapted to the new design of the robot.
	 *
	 * Revision 1.1  2009/04/23 21:26:00  mahanja
	 * The forklift controlling class
	 *
	 */
}
