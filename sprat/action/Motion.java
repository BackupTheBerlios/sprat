//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Motion.java,v 1.2 2009/04/23 21:22:00 mahanja Exp $

package action;

import def.Definitions;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Motion {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}

	public Motion() {

	}

	/**
	 * 
	 * @return
	 */
	public boolean isThereAWay() {
		if (Definitions.wayFinderOn) {
			int measure = Definitions.ls.readNormalizedValue();
			/*
			 * TODO delet (debugging) LCD.clearDisplay();
			 * LCD.drawString("measure:", 0, 0); LCD.drawInt(measure, 9, 0);
			 * LCD.drawInt(Definitions.colLine.min, 7, 1);
			 * LCD.drawInt(Definitions.colLine.max, 11, 1); LCD.refresh();
			 * Button.waitForPress(); LCD.clearDisplay();
			 */
			int leftVal, rightVal;

			if (Definitions.colLine.min <= measure
					&& measure <= Definitions.colLine.max) {
				directionCorrection();

				return true;
			} else {
				Definitions.pilot.rotate(-Definitions.corrTestAngle);
				leftVal = Definitions.ls.readNormalizedValue();
				if (Definitions.colLine.min <= leftVal
						&& leftVal <= Definitions.colLine.max) {
					Definitions.pilot.rotate(Definitions.corrTestAngle
							- Definitions.corrAngle);
					return true;
				} else {
					Definitions.pilot.rotate(2 * Definitions.corrTestAngle);
					rightVal = Definitions.ls.readNormalizedValue();
					if (Definitions.colLine.min <= rightVal
							&& rightVal <= Definitions.colLine.max) {
						// TODO correction
					}
				}
				return false;
			}
		} else {
			return true;
		}
	}

	public boolean goToNextJunction() {
		float distanceToGo = Definitions.distBtwnJunct
				+ Definitions.junctionSize;
		Definitions.pilot.travel(distanceToGo / 2);
		if (!isThereAWay()) {
			Definitions.pilot.travel(-distanceToGo / 2);
			return false;
		}

		// Definitions.pilot.travel(Definitions.distBtwnJunct / 2);
		// directionCorrection();
		Definitions.pilot.travel(distanceToGo / 2);
		return true;
	}

	public boolean goNJunctions(int n) {
		for (int i = 0; i < n; i++) {
			if (!goToNextJunction()) {
				return false;
			}
		}
		return true;
	}

	public void turn(boolean isLeft) {
		Definitions.pilot.travel(Definitions.distBtwnLsWheel);
		if (isLeft) {
			Definitions.pilot.rotate(Definitions.leftJunctAngle);
		} else {
			Definitions.pilot.rotate(Definitions.rightJunctAngle);
		}
		Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
	}

	public void directionCorrection() {
		Definitions.pilot.rotate(-Definitions.corrTestAngle);
		int leftVal = Definitions.ls.readNormalizedValue();
		Definitions.pilot.rotate(2 * Definitions.corrTestAngle);
		int rightVal = Definitions.ls.readNormalizedValue();
		/*
		 * TODO delet (debugging) LCD.clear(); LCD.drawString("left: ", 0, 0);
		 * LCD.drawInt(leftVal, 7, 0); LCD.drawString("right: ", 0, 1);
		 * LCD.drawInt(rightVal, 7, 1); LCD.refresh(); Button.waitForPress();
		 * LCD.clearDisplay();
		 */
		// TODO check closer
		if (leftVal < rightVal - Definitions.brightTolerance) {
			Definitions.pilot.rotate(-Definitions.corrTestAngle
					- Definitions.corrAngle);
		} else if (rightVal < leftVal - Definitions.brightTolerance) {
			Definitions.pilot.rotate(-Definitions.corrTestAngle
					+ Definitions.corrAngle);
		} else {
			Definitions.pilot.rotate(-Definitions.corrTestAngle);
		}

	}

	/*
	 * $Log: Motion.java,v $
	 * Revision 1.2  2009/04/23 21:22:00  mahanja
	 * added the log cvs keyword
	 *
	 */
}
