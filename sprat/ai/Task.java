//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/ai/Task.java,v 1.1 2009/04/29 19:01:23 mahanja Exp $

package ai;

import java.util.Vector;

import object.Position;

public class Task {
	protected Vector positions;
	protected int mode;
	
	/**
	 * The constructor for a new task.
	 * @param positions the list of positions to walk
	 * @param mode the mode of this task
	 */
	public Task(Vector positions, int mode) {
		this.positions = positions;
		this.mode = mode;
	}
	
	/**
	 * Returns true if there are more positions or not 
	 * @return
	 */
	public boolean hasNextPosition() {
		return positions.size() != 0;
	}
	
	/**
	 * Returns the next position to go to and removes it from the list
	 * @return the next position to go to.
	 */
	public Position nextPosition() {
		return (Position) positions.remove(0);
	}
	
	/**
	 * Returns true if this task can be broken up.
	 * @return true if the mode is DISCOVER_MODE or SEARCH_MODE
	 */
	public boolean isBreakable() {
		return (   mode == AI.DISCOVER_MODE
			    || mode == AI.SEARCH_MODE);
	}
}

/*
 * $Log: Task.java,v $
 * Revision 1.1  2009/04/29 19:01:23  mahanja
 * The AI is near to be complete. Never tested yet!
 *
 */