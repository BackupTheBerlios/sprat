// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/tool/MyEnumeration.java,v 1.1 2009/05/04 15:15:17 mahanja Exp $

package tool;

import java.util.Enumeration;
import java.util.Vector;

public class MyEnumeration implements Enumeration {
	private Vector elts;
	private int currP;
	
	public MyEnumeration() {
		elts = new Vector();
		currP = 0;
	}
	public MyEnumeration(Vector v) {
		elts = v;
		currP = 0;
	}
	public void add(Object elt) {
		elts.addElement(elt);
	}
	public boolean hasMoreElements() {
		try {
			if (elts == null)
				Console.println("elts == null");
			else 
				Console.println("elts != null");
				
			if (elts.elementAt(currP) == null) 
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public Object nextElement() {
		return elts.elementAt(currP++);
	}
}

/*
 * $Log: MyEnumeration.java,v $
 * Revision 1.1  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 */