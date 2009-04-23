// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/Message.java,v 1.2 2009/04/23 19:02:10 mahanja Exp $

package com.msg;

public interface Message {
	
	/**
	 * The format is "type:value[;value]", where the values will be replaced by 
	 * their appropriate values.
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString();
}
