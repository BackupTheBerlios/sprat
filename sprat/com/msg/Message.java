// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/Message.java,v 1.3 2009/04/27 08:48:14 mahanja Exp $

package com.msg;

public interface Message {

	public static final int MSGTYPE_ACK = 0;
	public static final int MSGTYPE_NEXTPOS = 1;
	public static final int MSGTYPE_JUNCTION = 2;
	public static final int MSGTYPE_NEEDHELP = 3;
	public static final int MSGTYPE_READYTOHELP = 4;
	public static final int MSGTYPE_REMOTEMOVE = 5;
	
	
	/**
	 * The format is "type:value[;value]", where the values will be replaced by 
	 * their appropriate values.
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString();
}
