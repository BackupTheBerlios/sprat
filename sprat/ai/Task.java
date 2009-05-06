//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/Task.java,v 1.3 2009/05/06 20:05:10 mahanja Exp $

package ai;

import java.util.Vector;

import object.Position;
import tool.MyEnumeration;

public class Task {
	protected MyEnumeration positions;
	protected int mode;
	
	/**
	 * The constructor for a new task.
	 * @param positions the list of positions to walk
	 * @param mode the mode of this task
	 */
	public Task(Vector positions, int mode) {
		this.positions = new MyEnumeration(positions);
		this.mode = mode;
	}
	
	/**
	 * Returns true if there are more positions or not 
	 * @return
	 */
	public boolean hasNextPosition() {
		return positions.hasMoreElements();
	}
	
	/**
	 * Returns the next position to go to and removes it from the list
	 * @return the next position to go to.
	 */
	public Position nextPosition() {
		return (Position) positions.nextElement();
	}
	
	/**
	 * Returns true if this task can be broken up.
	 * @return true if the mode is DISCOVER_MODE or SEARCH_MODE
	 * /
	public boolean isBreakable() {
		return (   mode == AI.DISCOVER_MODE
			    || mode == AI.SEARCH_MODE);
	}*/
}

/*
 * $Log: Task.java,v $
 * Revision 1.3  2009/05/06 20:05:10  mahanja
 * Commented out some unused methods
 *
 * Revision 1.2  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 * Revision 1.1  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 */