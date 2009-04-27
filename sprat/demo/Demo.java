//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/demo/Demo.java,v 1.3 2009/04/27 19:53:55 stollf06 Exp $

package demo;

import object.Grid;
import object.Robot;
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
	/*
	public void twoStepSquare() {
		Definitions.wayFinderOn=false;
		//Motion motion = new Motion();
		
		for (int i = 0; i < 4; i++) {
			motion.goNJunctions(2);
			motion.turn(false);
		}
	}*/
	
	public void pathFinding(){
		calibrationTest();
		Definitions.wayFinderOn=true;
		Grid grid = new Grid();
		Robot robo = new Robot(grid);
		Motion motion = new Motion(robo, grid);

		
		for (int i = 0; i < 4; i++) {
			while(motion.goToNextJunction()){
				
			}
			motion.turn(false);
		}
	}
	
	public void calibrationTest(){
		Calibration calib = new Calibration();
		LCD.clear();
		
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
 * Revision 1.3  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.2  2009/04/27 09:57:58  stollf06
 * took out calibration of whitePaper (unnecessary)
 *
 * Revision 1.1  2009/04/23 14:48:15  stollf06
 * first upload
 *
 */
