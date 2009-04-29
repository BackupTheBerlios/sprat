//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/AI.java,v 1.1 2009/04/29 09:56:12 mahanja Exp $
package ki;

import lejos.nxt.Button;
import object.Grid;
import object.Robot;
import tool.Console;
import action.Motion;
import def.Calibration;
import def.Definitions;

public class AI {
	private Motion motion;
	private Grid grid;
	private Robot robo;
	

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Calibration calib = new Calibration();
		Console.println("calibration done");
		Definitions.wayFinderOn=true;//TODO delete, just for debugging
		Definitions defs = Definitions.initInstance(Definitions.MASTER);//TODO change here
		Definitions.pilot.setSpeed(300);
		AI ki = new AI();
		ki.searchMode();
	}
	
	public AI(){
		grid = Grid.getInstance();
		robo = new Robot(grid);
		motion = new Motion(robo, grid);
		Console.println("KI init");
		
	}
	
	/**
	 * search for objects on the grid (go forward untill there is no way, turn right/left 2 times)
	 */
	public void searchMode(){
		boolean isLeft = false;
		//int j=0;
		for (int i = 0; i < 4; i++){
			while(motion.goToNextJunction()){
				Console.println("direction:"+robo.getOrientation());
				//Button.waitForPress();
				Console.println("actual:"+
						robo.getMyActualJunction().getPosition().getX()+
						"/"+robo.getMyActualJunction().getPosition().getY());
			}
			motion.turn(isLeft);
			motion.goToNextJunction();
			motion.turn(isLeft);
			isLeft = !isLeft;
		}
	}
}

/*
 * $Log: AI.java,v $
 * Revision 1.1  2009/04/29 09:56:12  mahanja
 * Added to the repository
 *
 */
