// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/ReadyToHelpMessage.java,v 1.1 2009/04/23 19:01:27 mahanja Exp $

package com.msg;

import com.Communicator;

import object.Direction;
import object.Position;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class ReadyToHelpMessage {

	public ReadyToHelpMessage () {}
	
	/**
	 * The format is "4:1".
	 * 
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString() {
		return Communicator.MSGTYPE_READYTOHELP + ":1";
	}
}

/*
 * $Log: ReadyToHelpMessage.java,v $
 * Revision 1.1  2009/04/23 19:01:27  mahanja
 * A new message type
 *
 */
