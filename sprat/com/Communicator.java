// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/Communicator.java,v 1.1 2009/04/23 12:08:34 mahanja Exp $

package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import object.Direction;
import object.Junction;
import object.Position;

import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;


/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: mahanja $
 */
public class Communicator extends Thread {
	/* Exception text definition */
	public static final String ERR_BLUETOOTH_OFF = 
		"Error: Bluetooth off";
	public static final String ERR_OTHER_ROBOT_NOT_IN_RANGE = 
		"Error: Other robot not in range";
	
	public static final String ERROR_UNABLE_TO_OPEN_OUTPUT_STREAM =
		"Error: Unable to open an output stream";

	public static final String ERROR_UNABLE_TO_OPEN_INPUT_STREAM =
		"Error: Unable to open an input stream";
	
	/* Public constants definition */
	public static final boolean ACK = true;
	public static final boolean NACK = false;

	public static final int MSGTYPE_ACK = 0;
	public static final int MSGTYPE_NEXTPOS = 1;
	public static final int MSGTYPE_JUNCTION = 2;
	public static final int MSGTYPE_NEEDHELP = 3;
	
	/* Private constants definition */
	private static final int VISIBLE = 1;
	private static final String ENDL = ""+'\n';
	
	/* Global variable definition */
	private static Communicator instance = null; // singleton instance 
	
	private LocalDevice device;
	private BTConnection conn = null;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	/**
	 * Returns a singleton instance of this class
	 * @return A singleton instance of this class
	 * @throws Exception Thrown on connection problems
	 */
	public static Communicator getInstance() throws Exception {
		String myName, othersName; 

		if (instance == null) {
			myName = def.Definitions.getInstance().myName;
			if (myName.equals(def.Definitions.MASTER))
				othersName = def.Definitions.SLAVE;
			else 
				othersName = def.Definitions.MASTER;
			
			instance = new Communicator(myName, othersName);
		}
		return instance;
	}
	
	/**
	 * The protected constructor (this is a singleton class
	 * @param myName The name of this robot used for the bluetooth connection 
	 * @param othersName The name of the other robot used for the bluetoot connection
	 * @throws Exception Thrown on connection problems
	 */
	protected Communicator(String myName, String othersName) throws Exception {
		initDevice(myName);
		connectOther(othersName);
		openStreams();
	}
	
	// initializes the local bluetooth device
	private void initDevice(String myName) throws Exception {
		if (!LocalDevice.isPowerOn()) 
			throw new Exception(ERR_BLUETOOTH_OFF);

		device = LocalDevice.getLocalDevice();
		device.setDiscoverable(VISIBLE);
		device.setFriendlyName(myName);
	}
	
	// connects to the other robot
	private void connectOther(String othersName) throws Exception {
		RemoteDevice other = Bluetooth.getKnownDevice("SLAVE");
		if (other != null) {
			conn = Bluetooth.connect(other);
			conn.setIOMode(NXTConnection.PACKET);
		} else {
			throw new Exception(ERR_OTHER_ROBOT_NOT_IN_RANGE);
		}
	}
	
	// opens an input and an output stream to the other robot
	private void openStreams() throws Exception {
		dos = conn.openDataOutputStream();
	    dis = conn.openDataInputStream();
	    
	    if (dos == null)
	    	throw new Exception(ERROR_UNABLE_TO_OPEN_OUTPUT_STREAM);

	    if (dis == null)
	    	throw new Exception(ERROR_UNABLE_TO_OPEN_INPUT_STREAM);
	}
	
	private void sendMessage(String msg) throws Exception {
		if (msg.substring(msg.length()-1) != ENDL)
			msg = msg + ENDL;
		
		dos.writeChars("Gugus"+'\n');
    	dos.flush();
	}

	public void sendNextPosition(Position pos) {
		// TODO: implementiere...
	}
	
	public void sendDiscoveredJunction(Junction junct) {
		// TODO: implementiere...
	}
	
	public void sendAcknoledge(boolean ok) {
		// TODO: implementiere...
	}
	
	public void sendDemandHelp(Position pos, Direction direction) {
		// TODO: implementiere...
	}
}

/*
 * $Log: Communicator.java,v $
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
