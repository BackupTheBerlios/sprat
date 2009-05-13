// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/NeedHelpMessage.java,v 1.3 2009/05/13 14:51:25 mahanja Exp $

package com.msg;

import com.Communicator;

import object.Direction;
import object.Position;

/**
 * Informs the one robot that the other one needs help.
 * The position and direction matter. 
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
	
	/**
	 * Sets the position where the help is needed
	 * @param pos the position where the help is needed
	 */
	public void setPosition(Position pos) {
		this.pos = pos;
	}

	/**
	 * Returns the position where the help is needed
	 * @return the position where the help is needed 
	 */
	public Position getPosition() {
		return pos;
	}
	/**
	 * Sets the direction the robot needs to be able to help to the other
	 * @param dir the direction the robot needs to be able to help to the other 
	 */
	public void setDirection(Direction dir) {
		this.dir = dir;
	}

	/**
	 * Returns the direction the robot needs to be able to help to the other
	 * @return the direction the robot needs to be able to help to the other
	 */
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
 * Revision 1.3  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.2  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.1  2009/04/23 19:01:27  mahanja
 * A new message type
 *
 */
