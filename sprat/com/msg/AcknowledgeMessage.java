// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/AcknowledgeMessage.java,v 1.3 2009/05/11 13:05:20 stollf06 Exp $

package com.msg;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: stollf06 $
 */
public class AcknowledgeMessage implements Message {
	public static int NOT_OK = 0;
	public static int OK = 1; 
	
	private int ok;

	public AcknowledgeMessage(int ok) {
		this.ok = ok;
	}
	
	public void setOK(int ok) {
		this.ok = ok;
	}

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
