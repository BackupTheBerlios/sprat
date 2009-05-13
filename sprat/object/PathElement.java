//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/PathElement.java,v 1.3 2009/05/13 14:51:25 mahanja Exp $

package object;

import java.util.Vector;

/**
 * Defines a chained list of position, so the robot can follow 
 * this path to reach a target position
 * @author greila06
 *
 */
public class PathElement extends Position {

	private PathElement nextElt = null;
	
	public PathElement(Position p) {
		this(p.getX(), p.getY());
	}
	
	public PathElement(int x, int y) {
		super(x,y);
	}

	/**
	 * Returns true if there is a following element
	 * @return true if there is a following element
	 */
	public boolean hasNextElt() {
		return nextElt != null;
	}
	
	/**
	 * Returns the following element
	 * @return the following element
	 */
	public PathElement getNextElt() {
		return nextElt;
	}
	
	/**
	 * Sets the following element
	 * @param the following element
	 * @return the given following element
	 */
	public PathElement setNextElt(PathElement ne) {
		nextElt = ne;
		return nextElt;
	}
	
    /**
     * Returns the length of this chain of PahtElements
     * @return the length of this chain of PahtElements
     */
	public int length() {
		if (hasNextElt())
			return nextElt.length() + 1;
		else
			return 1;
	}
	
	/**
	 * Returns the last element of this chain of PathElements
	 * @return the last element of this chain of PathElements
	 */
	public PathElement getLast() {
		if (hasNextElt())
			return nextElt.getLast();
		else
			return this;
	}
}

/*
 * $Log: PathElement.java,v $
 * Revision 1.3  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.2  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.1  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 */