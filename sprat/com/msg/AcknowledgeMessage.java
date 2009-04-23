// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/AcknowledgeMessage.java,v 1.1 2009/04/23 19:01:27 mahanja Exp $

package com.msg;

import com.Communicator;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class AcknowledgeMessage {
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
		return Communicator.MSGTYPE_ACK + ":" + ok;
	}
}

/*
 * $Log: AcknowledgeMessage.java,v $
 * Revision 1.1  2009/04/23 19:01:27  mahanja
 * A new message type
 *
 */
