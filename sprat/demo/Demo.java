//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/demo/Demo.java,v 1.1 2009/04/23 14:48:15 stollf06 Exp $

package demo;

import action.Motion;
import def.Calibration;
import def.Definitions;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Demo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Demo demo = new Demo();
		//demo.twoStepSquare();
		//demo.calibrationTest();
		demo.pathFinding();
	}

	//stupid running in circle
	public void twoStepSquare() {
		Definitions.wayFinderOn=false;
		Motion motion = new Motion();
		
		for (int i = 0; i < 4; i++) {
			motion.goNJunctions(2);
			motion.turn(false);
		}
	}
	
	public void pathFinding(){
		calibrationTest();
		Definitions.wayFinderOn=true;
		Motion motion = new Motion();

		
		for (int i = 0; i < 4; i++) {
			while(motion.goToNextJunction()){
				
			}
			motion.turn(false);
		}
	}
	
	public void calibrationTest(){
		Calibration calib = new Calibration();
		LCD.clear();
		
		LCD.drawString("white:", 0, 0);
		LCD.drawInt(Definitions.colWhiteGround.min, 7, 0);
		LCD.drawInt(Definitions.colWhiteGround.max, 11, 0);
		
		LCD.drawString("line: ", 0, 1);
		LCD.drawInt(Definitions.colLine.min, 7, 1);
		LCD.drawInt(Definitions.colLine.max, 11, 1);
		LCD.refresh();
		Button.waitForPress();
		LCD.clearDisplay();
		
	}

}
/*
 * $Log: Demo.java,v $
 * Revision 1.1  2009/04/23 14:48:15  stollf06
 * first upload
 *
 */
