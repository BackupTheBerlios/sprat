// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/NextPositionMessage.java,v 1.3 2009/05/11 13:05:20 stollf06 Exp $

package com.msg;

import object.Position;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: stollf06 $
 */
public class NextPositionMessage implements Message{
	private Position pos;
	
	public NextPositionMessage(Position pos) {
		this.pos = pos;
	}
	
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	public Position getPosition() {
		return pos;
	}
	
	/**
	 * The format is "1:x;y", where x and y will be replaced by their appropriate values.
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString() {
		return MSGTYPE_NEXTPOS + ":" + pos.getX() + ";" + pos.getY();
	}
}

/*
 * $Log: NextPositionMessage.java,v $
 * Revision 1.3  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.2  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
