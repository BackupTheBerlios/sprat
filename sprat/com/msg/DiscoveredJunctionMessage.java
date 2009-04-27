// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/DiscoveredJunctionMessage.java,v 1.3 2009/04/27 08:48:14 mahanja Exp $

package com.msg;

import object.Junction;
import object.Position;
import com.Communicator;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class DiscoveredJunctionMessage implements Message {

		private Junction junction;
		
		public DiscoveredJunctionMessage(Junction junction) {
			this.junction = junction;
		}
		
		public void setPosition(Junction junction) {
			this.junction = junction;
		}
		
		public Junction getJunction() {
			return junction;
		}
		
		/**
		 * The format is "2:t;x;y", where t, x and y will be replaced by their appropriate values.
		 * t = type of the junction (one of object.Junction. types)
		 * x, y = coordinates of object.Position
		 * @return the String to be send over bluetooth
		 */
		public String getMessageString() {
			return MSGTYPE_JUNCTION + ":" + 
				junction.getType() + ";" + 
				junction.getPosition().getX() + ";" + 
				junction.getPosition().getY();
		}
}

/*
 * $Log: DiscoveredJunctionMessage.java,v $
 * Revision 1.3  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.2  2009/04/23 19:03:08  mahanja
 * position and type together are a junction
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
