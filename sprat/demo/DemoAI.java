package demo;

import lejos.nxt.Button;
import object.Grid;
import object.Robot;
import tool.Console;

import com.Communicator;

import action.Motion;
import ai.AI;

public class DemoAI extends AI {
	
	Grid grid;
	Robot robo;
	Motion motion;
	
	public DemoAI() throws Exception {
		grid = Grid.getInstance(this);
		robo = Robot.initInstance(this);
		motion = new Motion(robo, grid);
		Communicator.getInstance(this);
	}
}
