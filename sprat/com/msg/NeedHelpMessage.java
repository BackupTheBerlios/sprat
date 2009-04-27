// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/NeedHelpMessage.java,v 1.2 2009/04/27 08:48:14 mahanja Exp $

package com.msg;

import com.Communicator;

import object.Direction;
import object.Position;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class NeedHelpMessage implements Message {

	private Position pos;
	private Direction dir;

	public NeedHelpMessage(Position pos, Direction dir) {
		this.pos = pos;
		this.dir = dir;
	}
	
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	public Position getPosition() {
		return pos;
	}

	public void setDirection(Direction dir) {
		this.dir = dir;
	}
	
	public Direction getDirection() {
		return dir;
	}

	/**
	 * The format is "3:x;y;d", where x, y, d will be replaced by their
	 * appropriate values. 
	 * d = the direction needed. 
	 * x, y = coordinates of object.Position
	 * 
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString() {
		return MSGTYPE_NEEDHELP + ":"
			+ pos.getX() + ";"
			+ pos.getY() + ";"
			+ dir.getDirection();
	}
}

/*
 * $Log: NeedHelpMessage.java,v $
 * Revision 1.2  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.1  2009/04/23 19:01:27  mahanja
 * A new message type
 *
 */
