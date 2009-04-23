// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/DiscoveredJunctionMessage.java,v 1.1 2009/04/23 12:08:34 mahanja Exp $

package com.msg;

import object.Position;
import com.Communicator;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class DiscoveredJunctionMessage {

		private Position pos;
		private int type;
		
		public DiscoveredJunctionMessage(int type, Position pos) {
			this.type = type;
			this.pos = pos;
		}
		
		public void setPosition(Position pos) {
			this.pos = pos;
		}
		
		public Position getPosition() {
			return pos;
		}

		public void setType(int tpye) {
			this.type = type;
		}
		
		public int getType() {
			return type;
		}
		
		/**
		 * The format is "1:x;y", where x and y will be replaced by their appropriate values.
		 * @return the String to be send over bluetooth
		 */
		public String getMessageString() {
			return Communicator.MSGTYPE_NEXTPOS + ":" + pos.getX() + ";" + pos.getY();
		}
}

/*
 * $Log: DiscoveredJunctionMessage.java,v $
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
