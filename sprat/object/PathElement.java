//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/PathElement.java,v 1.2 2009/05/10 05:21:36 mahanja Exp $

package object;

import java.util.Vector;

public class PathElement extends Position {

	private PathElement nextElt = null;
	
	public PathElement(Position p) {
		this(p.getX(), p.getY());
	}
	
	public PathElement(int x, int y) {
		super(x,y);
	}
	
	public boolean hasNextElt() {
		return nextElt != null;
	}
	
	public PathElement getNextElt() {
		return nextElt;
	}
	
	public PathElement setNextElt(PathElement ne) {
		nextElt = ne;
		return nextElt;
	}
	
    // not in class diagramm...
	public Vector toVector() {
		Vector path = new Vector();
		path.addElement(this);
		if (hasNextElt())
			return nextElt.toVector(path);
		else
			return path;
	}
	
	private Vector toVector(Vector path) {
		path.addElement(this);
		if (hasNextElt())
			return nextElt.toVector(path);
		else
			return path;
	}
	
	public int length() {
		if (hasNextElt())
			return nextElt.length() + 1;
		else
			return 1;
	}
	
	public PathElement getLast() {
		if (hasNextElt())
			return nextElt.getLast();
		else
			return this;
	}
	
    // not in class diagramm...
	public PathElement getClone() {
		PathElement myChain = this;
		PathElement cloneChain = new PathElement(x, y);
		while(myChain.hasNextElt()) {
			myChain = myChain.getNextElt();
			cloneChain.setNextElt(new PathElement(myChain.x, myChain.y));
			cloneChain = cloneChain.getNextElt();
		}
		return cloneChain;
	}
}

/*
 * $Log: PathElement.java,v $
 * Revision 1.2  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.1  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 */