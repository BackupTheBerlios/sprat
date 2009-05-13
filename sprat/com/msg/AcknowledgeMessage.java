// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/AcknowledgeMessage.java,v 1.4 2009/05/13 14:51:25 mahanja Exp $

package com.msg;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class AcknowledgeMessage implements Message {
	public static int NOT_OK = 0;
	public static int OK = 1; 
	
	private int ok;

	public AcknowledgeMessage(int ok) {
		this.ok = ok;
	}
	
	/**
	 * Sets if the acknowledgment will be ACK or NACK
	 * if ok == 1 => OK <br>
	 * else if ok == 0 => NOK <br>
	 * 
	 * The method does not test the integrity of the given parameter
	 * @param ok 0 for NOT_OK or 1 for OK
	 */
	public void setOK(int ok) {
		this.ok = ok;
	}

	/**
	 * Returns eigher 1 => OK or 0 => NOT_OK
	 * @return either 1 => OK or 0 => NOT_OK
	 */
	public int getOK() {
		return ok;
	}
	
	/**
	 * The format is "0:o", where o is the indicator for OK = 1 and not OK = 0;
	 * 
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString() {
		return MSGTYPE_ACK + ":" + ok;
	}
}

/*
 * $Log: AcknowledgeMessage.java,v $
 * Revision 1.4  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.3  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.2  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.1  2009/04/23 19:01:27  mahanja
 * A new message type
 *
 */
