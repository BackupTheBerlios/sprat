// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/RemoteMessage.java,v 1.1 2009/04/27 08:48:14 mahanja Exp $

package com.msg;

import com.Communicator;

/**
 * This class defines the eight ways a robot must be able to move
 * to help the other robot. These are:
 * 0:	go forward
 * 1: 	turn right forward (the other robot is the center)
 * 2:	turn left forward (the other robot is the center)
 * 3:	go backward
 * 4:	turn right backward (the other robot is the center)
 * 5:	turn left backward (the other robot is the center)
 * 6: 	turn right (this robot is the center)
 * 7: 	turn left (this robot is the center)
 * 
 * @see com.msg.RemoteMove
 * @author $Author: mahanja $
 */
public class RemoteMessage implements Message {

	private int type;
	
	public RemoteMessage(int type) {
		this.type = type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

	
	/**
	 * The format is "5:m", where m is the number of the move to be performed.
	 * m is one of object.RemoteMove.\<TYPE\>
	 * 
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString() {
		return MSGTYPE_REMOTEMOVE + ":" + type;
	}
}

/*
 * $Log: RemoteMessage.java,v $
 * Revision 1.1  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 */
