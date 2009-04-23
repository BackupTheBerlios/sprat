// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/msg/Message.java,v 1.1 2009/04/23 12:08:34 mahanja Exp $

package com.msg;

public interface Message {
	
	/**
	 * The format is "type:value[;value]", where the values will be replaced by 
	 * their appropriate values. Values with subvalues are enclosed by brackets 
	 * ( "(" and ")" ).
	 * @return the String to be send over bluetooth
	 */
	public String getMessageString();
}
