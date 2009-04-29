//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Forklift.java,v 1.2 2009/04/29 14:32:14 mahanja Exp $

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
	
	private boolean toggle = true;
	
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
	}
	
	/**
	 * causes the forklift going up
	 */
	public void up() {
		Motor.C.resetTachoCount();
		Motor.C.setSpeed(SPEED);
		Motor.C.rotateTo(DOWN_ANGLE);
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
	 * Revision 1.2  2009/04/29 14:32:14  mahanja
	 * Adapted to the new design of the robot.
	 *
	 * Revision 1.1  2009/04/23 21:26:00  mahanja
	 * The forklift controlling class
	 *
	 */
}
