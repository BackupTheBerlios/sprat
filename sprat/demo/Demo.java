//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/demo/Demo.java,v 1.10 2009/05/13 14:51:25 mahanja Exp $

package demo;

import object.Direction;
import object.Grid;
import object.Position;
import object.Robot;
import tool.Console;
import action.Forklift;
import action.Motion;
import ai.AI;
import def.Calibration;
import def.Definitions;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * This class is for demonstration purposes only
 * @author greila06
 *
 */
public class Demo {
	static Definitions defs;
	private Grid grid;
	private Robot robo;
	private Motion motion;
	private AI ai;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		//Eye e = new Eye();
		//Forklift forklift = new Forklift();
		
		//demo.twoStepSquare();
		//demo.calibrationTest();
		
		//demo.turnRadiusTest();
		
		Calibration calib = new Calibration();
		Demo demo = new Demo();
		Definitions.pilot.setSpeed(300);
		Position pos = new Position(3,3);
		demo.pathFinding(pos);
		//Console.println("calibration done");
		//Definitions.wayFinderOn=true;//TODO delete, just for debugging
		//defs = Definitions.initInstance(Definitions.MASTER);//TODO change here
		//Definitions.pilot.setSpeed(300);
		//Position pos = new Position(3,3);
		//demo.pathFinding(pos);
	}

	public Demo(){
		try {
			ai = new DemoAI();
			grid = Grid.getInstance(ai);
			robo =Robot.initInstance(ai);
			motion = new Motion(robo, grid);
		} catch (Exception e) {
			Console.println("Demo init failed");
		}
	}
	
	/**
	 * Finds a path
	 */
	public void pathFinding(){

		Grid grid = Grid.getInstance(ai);
		Robot robo =Robot.initInstance(ai);
		Motion motion = new Motion(robo, grid);
		//Eye e = new Eye();
		Forklift forklift = new Forklift();
		//forklift.down();
		for (int i = 0; i < 4; i++) {
			while(motion.goToNextJunction(false)){
				/*int  junct = Eye.getType();
				Console.println("junct "+junct);
				if(defs.isMaster){
					if(junct==Junction.MASTER_OBJ){
						//this is my object
						forklift.up();
						Console.println("my object detected");
					}else if(junct==Junction.SLAVE_OBJ){
						//not my object
						motion.goBackOneJunction();
						motion.turn(false);
						Console.println("other object");
					}
				}else{
					if(junct==Junction.SLAVE_OBJ){
						//this is my object
						forklift.up();
						Console.println("my object detected");
					}else if(junct==Junction.SLAVE_OBJ){
						//not my object
						motion.goBackOneJunction();
						motion.turn(false);
						Console.println("other object");
					}
				}*/
				Console.println("go next junction");

			}
			motion.turn(false);
		}
		forklift.down();
		Definitions.pilot.travel(-20);
	}
	
	/**
	 * Finds a path to the given position
	 * @param endP the target position
	 */
	public void pathFinding(Position endP){
		//grid.isWorkToDo();
		Position actuP = robo.getMyActualPosition();
		int xOff = endP.getX()-actuP.getX();
		int yOff = endP.getY()-actuP.getY();
		Console.println("entered("+actuP.getX()+"/"+actuP.getY()+")-("+endP.getX()+"/"+endP.getY()+")");
		if(xOff == 0 && yOff == 0){
			return;
		}
		if(yOff>0){
			while(robo.getOrientation()!=Direction.NORTH){
				motion.turn(false);
			}
		}else{
			while(robo.getOrientation()!=Direction.SOUTH){
				motion.turn(false);
			}
			yOff = -yOff;
		}
		//TODO richtig ausrichten
		motion.goNJunctions(yOff);
		yOff = endP.getY()-robo.getMyActualPosition().getY();
		Console.println("new yOff"+yOff);
		boolean leftTurns = false;
		if(yOff != 0){//not there yet
			findWayThroughWall(false);
			pathFinding(endP);
			return;
		}
		if(xOff>0){
			while(robo.getOrientation()!=Direction.EAST){
				motion.turn(false);
			}
		}else{
			while(robo.getOrientation()!=Direction.WEST){
				motion.turn(false);
			}
			xOff = -xOff;
		}
		//TODO in die richtige richtung drehen
		motion.turn(false);
		motion.goNJunctions(xOff);
		xOff = endP.getX()-robo.getMyActualPosition().getX();
		if(xOff!=0){
			findWayThroughWall(false);
			pathFinding(endP);
			return;
		}
	}
	
	/**
	 * Finds an alternative way if there is an object on the way
	 * @param leftTurns
	 */
	private void findWayThroughWall(boolean leftTurns){
		motion.turn(leftTurns);
		Console.println("turned");
		//as long as i can go right
		while(motion.goToNextJunction(false)){
			Console.println("went");
			motion.turn(!leftTurns);
			Console.println("turned");
			if(motion.goToNextJunction(false)){
				//it was able to pass the y wall
				Console.println("went through");
				return;//or break
			}
			motion.turn(leftTurns);
			//Button.waitForPress();
		}
		motion.turn(!leftTurns);
		findWayThroughWall(!leftTurns);
		
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
	
	public void turnRadiusTest(){
		
		boolean isLeft = true;
		float radius = Definitions.distBtwnJunct+Definitions.junctionSize;
		if(isLeft){
			Definitions.pilot.turn(-radius, 90);
		}else{
			Definitions.pilot.turn(radius, 90 );
		}
		Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
		Button.waitForPress();
		Definitions.pilot.travel(Definitions.distBtwnLsWheel);
		
		radius = Definitions.distBtwnJunct+Definitions.junctionSize;
		if(isLeft){
			Definitions.pilot.turn(-radius, Definitions.leftJunctAngle);
		}else{
			Definitions.pilot.turn(-radius, Definitions.rightJunctAngle);
		}
		Definitions.pilot.travel(-Definitions.distBtwnLsWheel);
	}

}
/*
 * $Log: Demo.java,v $
 * Revision 1.10  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.9  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.8  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.7  2009/05/06 22:26:12  stollf06
 * example method to find a path
 *
 * Revision 1.6  2009/05/06 17:23:18  stollf06
 * updated demo
 *
 * Revision 1.5  2009/04/29 19:08:45  stollf06
 * better handling of next junction and actualjunction
 *
 * Revision 1.4  2009/04/27 20:20:09  mahanja
 * Grid becomes a singleton
 *
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
