//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Forklift.java,v 1.5 2009/05/11 13:04:52 stollf06 Exp $

package action;
import lejos.nxt.Button;
import lejos.nxt.Motor;

/**
 * An object controlling the forklift functionality of the SPRAT 
 * @author $Author: stollf06 $
 */
public class Forklift {
	
	private static int SPEED = 50,
	                UP_ANGLE = -75,
	              DOWN_ANGLE = 75;
	private static boolean isDown = true; // initial position
	
	private boolean toggle = true; // testing
	
	public Forklift () {	
		Motor.C.setSpeed(SPEED);
	}
	
	/**
	 * causes the forklift going down
	 */
	public void down() {
		if(isDown){
			return;
		}	
		Motor.C.lock(0);
		Motor.C.resetTachoCount();
		Motor.C.rotateTo(DOWN_ANGLE);
		
		isDown = true;
	}
	
	/**
	 * causes the forklift going up
	 */
	public void up() {
		if(!isDown){
			return;
		}
		
		Motor.C.resetTachoCount();
		Motor.C.rotateTo(UP_ANGLE);
		Motor.C.lock(25);
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
	 * Revision 1.5  2009/05/11 13:04:52  stollf06
	 * code cleaning
	 *
	 * Revision 1.4  2009/05/06 17:17:49  stollf06
	 * eye fonctional, little updates for the forklift
	 *
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
