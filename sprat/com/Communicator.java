// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/Communicator.java,v 1.2 2009/04/23 19:05:21 mahanja Exp $

package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import com.msg.AcknowledgeMessage;
import com.msg.DiscoveredJunctionMessage;
import com.msg.NeedHelpMessage;
import com.msg.NextPositionMessage;

import def.Definitions;

import object.Direction;
import object.Junction;
import object.Position;
import tool.Console;

import lejos.nxt.Button;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;


/**
 * This class implements a bluetooth interface for sending and receiving
 * messages to and from the other robot.
 * 
 * Because of the implementation of the Lego NXT stack one of the robots
 * must be the master and the others are slaves. The master initializes
 * the connection. The slaves are just waiting to be connected. After 
 * this initialization both robots are able to send and receive messages. 
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
	
	public static final String UNSUPORTED_PARAM =
		"Error: Unsuported parameter value";
	
	/* Public constants definition */
	public static final boolean ACK = true;
	public static final boolean NACK = false;

	public static final int MSGTYPE_ACK = 0;
	public static final int MSGTYPE_NEXTPOS = 1;
	public static final int MSGTYPE_JUNCTION = 2;
	public static final int MSGTYPE_NEEDHELP = 3;
	public static final int MSGTYPE_READYTOHELP = 4;
	
	public static final int POLLING_FREQUENCY = 10; // of the listener in milliseconds
	
	/* Private constants definition */
	private static final int VISIBLE = 1;
	private static final String ENDL = ""+'\n';
	
	/* Global variable definition */
	private static Communicator instance = null; // singleton instance 
	
	private LocalDevice device;
	private BTConnection conn = null;
	private DataOutputStream dos;
	private DataInputStream dis;
	

	///////////////////////////////////////////////////////
	//                                                   //
	//                  INITIALIZATION                   //
	//                                                   //
	///////////////////////////////////////////////////////
	
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
	 * The protected constructor (this is a singleton class)
	 * @param myName The name of this robot used for the bluetooth connection 
	 * @param othersName The name of the other robot used for the bluetoot connection.
	 * If this is a slave, this parameter is not used.
	 * @throws Exception Thrown on connection problems
	 */
	protected Communicator(String myName, String othersName) throws Exception {
		//Console.println("iDev");
		initDevice(myName);
		//Console.print("as."+myName +".ok");

		if (myName.equals(def.Definitions.MASTER)) {
			//Console.println("iMas");
			initMaster(othersName);
			//Console.print(".ok");
		} else {
			initSlave();
		}
		//Console.println("iDone");
		Console.println("Comm online");
	}

	// initializes the local bluetooth device
	private void initDevice(String myName) throws Exception {
		if (!LocalDevice.isPowerOn()) 
			throw new Exception(ERR_BLUETOOTH_OFF);

		device = LocalDevice.getLocalDevice();
		device.setDiscoverable(VISIBLE);
		device.setFriendlyName(myName);
	}
	
	/**
	 * Gets ready the device and initializes the connection to the slave
	 * @throws Exception
	 */
	private void initMaster(String othersName) throws Exception {
		// connect to the other robot
		RemoteDevice other = null;
		while ((other = Bluetooth.getKnownDevice(othersName)) == null)
			//Console.print(".");
		
		if (other != null) {
			conn = Bluetooth.connect(other);
			conn.setIOMode(NXTConnection.PACKET);
		} else {
			throw new Exception(ERR_OTHER_ROBOT_NOT_IN_RANGE);
		}
		
		// open an input and an output stream to the other robot
		dos = null;
		while (dos ==null) {
			try {
				dos = conn.openDataOutputStream();
			} catch(Exception e) {
				Console.println("E:dos conn");
				Button.ESCAPE.waitForPressAndRelease();
			}
		}
		
		dis = null;
		while (dis ==null) {
			try {
				dis = conn.openDataInputStream();
			} catch(Exception e) {
				Console.println("E:dis conn");
				Button.ESCAPE.waitForPressAndRelease();
			}
		}
		
		// start listening
		this.start();
	}
	
	
	// catches the connection from the master
	// and starts listener thread
	private void initSlave() throws Exception {
		// wait for the connection from the master
		//Console.println("waiting for conn..");
		BTConnection connection = Bluetooth.waitForConnection();
		//Console.print(".ok");

		// open streams
		dis = connection.openDataInputStream();
	    if (dis == null) {
	    	System.exit(1);
	    }

		dos = connection.openDataOutputStream();
	    if (dos == null) {
	    	System.exit(1);
	    }
	    //Console.println("S ready, start");

	    // start listening
		this.start();
	}

	///////////////////////////////////////////////////////
	//                                                   //
	//                      SENDER                       //
	//                                                   //
	///////////////////////////////////////////////////////
	/**
	 * Sends a message to indicate the next position to the other robot.
	 * @param the next position to this robot wants to go to.
	 * @throws Exception 
	 */
	public void sendNextPosition(Position pos) throws Exception {
		sendMessage(new NextPositionMessage(pos).getMessageString());
	}
	
	/**
	 * Sends the information about a new discovered junction point.
	 * @param junct the junction just discovered.
	 * @throws Exception 
	 */
	public void sendDiscoveredJunction(Junction junct) throws Exception {
		sendMessage(new DiscoveredJunctionMessage(junct).getMessageString());
	}
	
	/**
	 * Sends an acknowledge to the other robot.
	 * @param AcknowledgeMessage.OK or AcknowledgeMessage.NOT_OK
	 * @throws Exception 
	 */
	 public void sendAcknowledge(int ok) throws Exception {
		 if (ok != AcknowledgeMessage.OK ||
		     ok != AcknowledgeMessage.NOT_OK)
			 throw new Exception(UNSUPORTED_PARAM);
		 
		sendMessage(new AcknowledgeMessage(ok).getMessageString());
	}
	
	/**
	 * Sends an acknowledge to the other robot.
	 * @param ok (true) or not ok (false)
	 * @throws Exception 
	 */
	public void sendAcknowledge(boolean ok) throws Exception {
		sendAcknowledge(
			ok ? AcknowledgeMessage.OK : 
				 AcknowledgeMessage.NOT_OK);
	}
	
	/**
	 * Sends a demand for help with the information the other robot needs to come and help
	 * @param pos
	 * @param direction
	 * @throws Exception 
	 */
	public void sendDemandHelp(Position pos, Direction direction) throws Exception {
		sendMessage(new NeedHelpMessage(pos, direction).getMessageString());
	}
	
	private void sendMessage(String msg) throws Exception {
		//Console.println("Sending...");
		dos.writeChars(msg + ENDL);
    	dos.flush();
    	//Console.println(msg +" send");
	}

	///////////////////////////////////////////////////////
	//                                                   //
	//                     RECEIVER                      //
	//                                                   //
	///////////////////////////////////////////////////////
	
	/**
	 * This starts the listener thread.
	 */
	public void run() {
		if (dis == null) {
			Console.println("E: dis=null");
			System.exit(1);
		}
			
	    while (true) {
	    	try {
	    		processMessage(dis.readLine());
	    	} catch (Exception e) {
	    		// this error occurs every time when the input queue is empty.
	    		// This is a kind of busy waiting but I don't know how to do it better.
	    		// If you have any ideas...
	    		// PS. DataInputStream.available() is not implemented :( 
	    		try {
					sleep(POLLING_FREQUENCY);
				} catch (InterruptedException e1) { }
	    	}
	    }
	}
	
	private void processMessage(String msg) {
		Console.println("r: "+ msg);
	}
	
	///////////////////////////////////////////////////////
	//                                                   //
	//                     TESTING                       //
	//                                                   //
	///////////////////////////////////////////////////////
	
	public static void main(String args[]) {
		Definitions.initInstance(Definitions.MASTER);
		//Definitions.initInstance(Definitions.SLAVE);
		
		Communicator com = null;
		
		// init and connect
		try {
			com = Communicator.getInstance();
		} catch(Exception e) {
			Console.println("E: init com failed");
			try {
				Button.ESCAPE.waitForPressAndRelease();
			} catch (InterruptedException ie) {}
			System.exit(1);
		}
		
		// send something
		try {
			com.sendMessage("HALLO "+def.Definitions.getInstance().othersName);
		} catch (Exception e) {
			Console.println("E: send com failed");
			try {
				Button.ESCAPE.waitForPressAndRelease();
			} catch (InterruptedException ie) {}
			System.exit(1);
		}
		try {
			Button.ESCAPE.waitForPressAndRelease();
		} catch (InterruptedException ie) {}
		System.exit(1);
	}
}

/*
 * $Log: Communicator.java,v $
 * Revision 1.2  2009/04/23 19:05:21  mahanja
 * Functional and tested!
 * To use the main method you have to give a name  "Definitions.initInstance(Definitions.CONSTANT_TO_BE_SET);" ).
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
