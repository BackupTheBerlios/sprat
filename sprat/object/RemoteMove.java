// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/object/RemoteMove.java,v 1.4 2009/05/13 14:51:25 mahanja Exp $

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
	
	public static final int FORKLIFT_DOWN= 8; 
	public static final int FORKLIFT_UP= 9;
	
	public static final int FREE = 10;
}


/*
 * $Log: RemoteMove.java,v $
 * Revision 1.4  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.3  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.2  2009/04/27 20:06:02  mahanja
 * Added forklifter function to the vocabulary
 *
 * Revision 1.1  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 */