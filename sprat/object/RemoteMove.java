// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/RemoteMove.java,v 1.1 2009/04/27 08:48:14 mahanja Exp $

package object;

/**
 * This class just defines the required types for remote moving.
 * 
 * These are:
 * 
 * <img src="D:\AdrianGreiler\SemesterProjekt\Documentation\Dokumentation\RemoteMoveComunication.png"/>
 * 
 * @author $Author: mahanja $
 *
 */


public class RemoteMove {
	public static final int GO_FORWARD = 0;
	public static final int TURN_RIGHT_FORWARD = 1;
	public static final int TURN_LEFT_FORWARD = 2;
	public static final int GO_BACKWARD = 3;
	public static final int TURN_RIGHT_BACKWARD = 4;
	public static final int TURN_LEFT_BACKWARD = 5;
	public static final int TURN_RIGHT = 6;
	public static final int TURN_LEFT = 7; 
}


/*
 * $Log: RemoteMove.java,v $
 * Revision 1.1  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 */