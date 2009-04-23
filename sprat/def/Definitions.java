// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Definitions.java,v 1.1 2009/04/23 12:08:34 mahanja Exp $

package def;

import sun.security.jca.GetInstance.Instance;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class Definitions {
	public static final String MASTER = "master";
	public static final String SLAVE = "slave";
	
	public String myName = "UNDEFINED"; // Must be defined before starting the AI!
	
	private static Definitions instance = null;
	
	public static Definitions getInstance() {
		return instance;
	}
	
	public static Definitions initInstance(String name) {
		return instance = new Definitions(name); 
	}
	
	public Definitions(String name) {
		myName = name;
		
		// TODO: Das vo Floh!!!
	}
	
}

/*
 * $Log: Definitions.java,v $
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
