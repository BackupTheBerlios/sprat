// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/com/Communicator.java,v 1.8 2009/05/10 05:21:36 mahanja Exp $

package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Vector;

import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;

import ai.AI;

import com.msg.*;

import def.Definitions;

import object.Direction;
import object.Grid;
import object.Junction;
import object.Position;
import object.RemoteMove;
import object.Robot;
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
		"E: BT off";
	public static final String ERR_OTHER_ROBOT_NOT_IN_RANGE = 
		"E: Other not in range";
	
	public static final String ERROR_UNABLE_TO_OPEN_OUTPUT_STREAM =
		"E: o stream";

	public static final String ERROR_UNABLE_TO_OPEN_INPUT_STREAM =
		"E: i stream";
	
	public static final String UNSUPORTED_PARAM =
		"E: Uns. param value";
	
	/* Public constants definition */
	public static final boolean ACK = true;
	public static final boolean NACK = false;

	
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
	private boolean acknowledgeAnswer = false,
					acknowledgeArrived = false;
					/*,
	                readyToHelp = false,
	                askedForHelp = false,
					helpFinished = false;
	private Position needHelpPosition = null;
	private Direction needHelpOrientation = null;*/ 
	private AI ai;
	

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
	public static Communicator getInstance(AI ai) throws Exception {
		String myName, othersName; 
		if (instance == null) {
			myName = def.Definitions.getInstance().myName;
			if (myName.equals(def.Definitions.MASTER))
				othersName = def.Definitions.SLAVE;
			else 
				othersName = def.Definitions.MASTER;
			instance = new Communicator(ai, myName, othersName);
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
	protected Communicator(AI ai, String myName, String othersName) throws Exception {
		this.ai = ai;
		
		initDevice(myName);

		if (myName.equals(def.Definitions.MASTER)) {
			initMaster(othersName);
		} else {
			initSlave();
		}
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
		Console.println("Establishing conn...");
		
		// connect to the other robot
		RemoteDevice other = null;
		while ((other = Bluetooth.getKnownDevice(othersName)) == null) // busy waiting
			;//Console.print(".");
		
		if (other != null) {
			conn = Bluetooth.connect(other);
			conn.setIOMode(NXTConnection.PACKET);
		} else {
	    	Console.println("E: conn");
			throw new Exception(ERR_OTHER_ROBOT_NOT_IN_RANGE);
		}
		
		// open an input and an output stream to the other robot
		dos = conn.openDataOutputStream();
	    if (dos == null) {
	    	Console.println("E: dos conn");
			throw new Exception(ERROR_UNABLE_TO_OPEN_OUTPUT_STREAM);
	    }

		dis = conn.openDataInputStream();
	    if (dis == null) {
	    	Console.println("E: dis conn");
			throw new Exception(ERROR_UNABLE_TO_OPEN_INPUT_STREAM);
	    }
		
		// start listening
		this.start();
	}
	
	
	/**
	 * Catches the connection from the master
	 * and starts listener thread
	 * @throws Exception
	 */
	private void initSlave() throws Exception {
		// wait for the connection from the master
		Console.println("wait for conn..");
		BTConnection connection = Bluetooth.waitForConnection();
		//Console.print(".ok");

		// open streams
		dis = connection.openDataInputStream();
	    if (dis == null) {
	    	Console.println("E: dis conn");
			throw new Exception(ERROR_UNABLE_TO_OPEN_INPUT_STREAM);
	    }

		dos = connection.openDataOutputStream();
	    if (dos == null) {
	    	Console.println("E: dos conn");
			throw new Exception(ERROR_UNABLE_TO_OPEN_OUTPUT_STREAM);
	    }

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
		sendMessage(new NextPositionMessage(pos));
	}
	
	/**
	 * Sends a message to indicate the that this robot is ready 
	 * to help the other one as a slave.
	 * @throws Exception 
	 * /
	public void sendReadyToHelp() throws Exception {
		sendMessage(new ReadyToHelpMessage());
	}*/
	
	/**
	 * Sends the information about a new discovered junction point.
	 * @param junct the junction just discovered.
	 * @throws Exception 
	 */
	public void sendDiscoveredJunction(Junction junct) throws Exception {
		sendMessage(new DiscoveredJunctionMessage(junct));
	}
	
	/**
	 * Sends an acknowledge to the other robot.
	 * @param AcknowledgeMessage.OK or AcknowledgeMessage.NOT_OK
	 * @throws Exception 
	 */
	 public void sendAcknowledge(int ok) throws Exception {
		 if (ok != AcknowledgeMessage.OK &&
		     ok != AcknowledgeMessage.NOT_OK)
			 throw new Exception(UNSUPORTED_PARAM);

		sendMessage(new AcknowledgeMessage(ok));
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
	 * @param pos the position where this one needs the other to help
	 * @param direction the direction the other robot needs to help
	 * @throws Exception 
	 * /
	public void sendDemandHelp(Position pos, Direction direction) throws Exception {
		sendMessage(new NeedHelpMessage(pos, direction));
	}*/
	
	/**
	 * Sends command for remote control. This is used when two robots are collaborating.
	 * The moves are one of object.RemoteMove
	 * @see object.RemoteMove
	 * @param the direction where to move
	 * @throws Exception 
	 * /
	public void sendRemoteMove(int type) throws Exception {
		sendMessage(new RemoteMessage(type));
	}*/
	
	/**
	 * The msg must end with a "newline" character as it will be understood by the receiver!
	 * @param msg
	 * @throws Exception
	 */
	private void sendMessage(String msg) throws Exception {
		//Console.println("Sending...");
		dos.writeChars(msg);
    	dos.flush();
    	//Console.println(msg +" send");
	}
	
	private void sendMessage(Message msg) throws Exception {
		//Console.println("Send: "+msg.getMessageString());
		
		sendMessage(msg.getMessageString() + ENDL);
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
	
	private void processMessage(String text) {
		//Console.println("r: "+ text);
		
		// data gets information...
		Message m = null;
		int type = Integer.parseInt(text.substring(0,1)); // ever only first char
		int[] values = parseMessageBody(text.substring(2, text.length()));
		
		if (type == Message.MSGTYPE_ACK) { 
			m = new AcknowledgeMessage(values[0]);
			processAcknowledgeMessage(m);
		} else if (type == Message.MSGTYPE_NEXTPOS) { 
			m = new NextPositionMessage(new Position(values[0], values[1]));
			processNextPositionMessage(m);
		} else if (type == Message.MSGTYPE_JUNCTION) {
			m = new DiscoveredJunctionMessage(
				new Junction(new Position(values[1],values[2]),values[0]));
			processDiscoveredJunctionMessage(m);
		}/* else if (type == Message.MSGTYPE_NEEDHELP) {
			m = new NeedHelpMessage(new Position(values[0], values[1]), new Direction(values[2])); 
			processNeedHelpMessage(m);
		} else if (type == Message.MSGTYPE_READYTOHELP) {
			m = new ReadyToHelpMessage(); 
			processReadyToHelpMessage(m);
		} else if (type == Message.MSGTYPE_REMOTEMOVE) {
			m = new RemoteMessage(values[0]);
			processRemoteMessage(m);
		}*/ else { 
			Console.println("E: msg="+text);
		}
	}
	
	private int[] parseMessageBody(String body) {
		String tmp = null;
		Vector v = new Vector();
		int idx;
		
		while(body.length() > 0) { // there is min 1 times ";" in body
			idx = body.indexOf(";");
			if (idx < 0) // the last number
				idx = body.length();

			v.addElement(new Integer(Integer.parseInt(body.substring(0, idx))));

			if (idx < body.length()) {
				tmp = body.substring(idx+1, body.length());
			} else { 
				tmp = ""; // the last one
			}
			body = tmp;
		}

		int array[] = new int[v.size()];
		for (int i = 0; i < v.size(); i++)
			array[i] = ((Integer)(v.elementAt(i))).intValue();

		return array;
	}
	
	private void processAcknowledgeMessage(Message m) {
		AcknowledgeMessage ack = (AcknowledgeMessage) m;
		
		acknowledgeAnswer = (ack.getOK() == AcknowledgeMessage.OK);
		
		acknowledgeArrived = true;
		//Console.println("ACK:"+ack.getOK());
	}
	
	public boolean acknowledgeArrived() {
		if (acknowledgeArrived) {
			acknowledgeArrived = false;
			return true;
		}
		return false;
	}
	
	public void resetAchnowledge() {
		acknowledgeAnswer = false;
	}
	
	public boolean getAcknowledge() {
		return acknowledgeAnswer;
	}
	
	private void processNextPositionMessage(Message m) {
		//int[] values = parseMessageBody(m.getMessageString());
		Position p = ((NextPositionMessage)m).getPosition();
		
		if ((ai.getRobot().getMyActualPosition().getX() == p.getX() &&
		     ai.getRobot().getMyActualPosition().getY() == p.getY()) |
			(ai.getRobot().getMyNextPosition().getX() == p.getX() &&
			 ai.getRobot().getMyNextPosition().getY() == p.getY())) {
			try {
				sendAcknowledge(false);
			} catch (Exception e) {
				Console.println("E: procNP f");
			}
		} else {
			try {
				sendAcknowledge(true);
			} catch (Exception e) {
				Console.println("E: procNP t");
			}
		}
	}
	
	private void processDiscoveredJunctionMessage(Message m) {
		ai.getGrid().setJunction(((DiscoveredJunctionMessage)m).getJunction(), false);
	}
	
	/*private void processNeedHelpMessage(Message m) {
		needHelpPosition = ((NeedHelpMessage)m).getPosition();
		needHelpOrientation = ((NeedHelpMessage)m).getDirection();
		askedForHelp = true;
	}
	
	public Direction getNeedHelpOrientation() {
		return needHelpOrientation;
	}
	public Position getNeedHelpPosotion() {
		return needHelpPosition;
	}
	public boolean askedForHelp() {
		if (askedForHelp) {
			askedForHelp = false;
			return true;
		}
		return false;
	}
	private void processReadyToHelpMessage(Message m) {
		readyToHelp = true;
	}
	
	public boolean getReadyToHelp() {
		if (readyToHelp) {
			readyToHelp = false;
			return true;
		}
		return false;
	}
	private void processRemoteMessage(Message m) {
		RemoteMessage rm = (RemoteMessage)m;
		
		if (rm.getType() == RemoteMove.FREE) {
			helpFinished = true;
			return;
		}
		
		ai.getMotion().performRemoteMove(rm.getType());
	}

	public boolean helpFinished() {
		if (helpFinished) {
			helpFinished = false;
			return true;
		}
		return false;
	}*/

	
	///////////////////////////////////////////////////////
	//                                                   //
	//                     TESTING                       //
	//                                                   //
	///////////////////////////////////////////////////////
	
	/*public static void main(String args[]) {
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
			Console.println("E: send failed");
			try {
				Button.ESCAPE.waitForPressAndRelease();
			} catch (InterruptedException ie) {}
			System.exit(1);
		}
/*
		// taking remote control over the others port A motor
		Console.println("REMOTE as "+Definitions.getInstance().myName);
		
		if (Definitions.getInstance().myName.equals(Definitions.MASTER)) {
			RemoteNXT remote = null;
			try {
				remote= com.takeControll(Definitions.getInstance().othersName);
			} catch (Exception e) {
				Console.println("E: remote failed");
				try {
					Button.ESCAPE.waitForPressAndRelease();
				} catch (InterruptedException ie) {}
				System.exit(1);
			}

			Console.println("controlling");
			
			while(!Button.ESCAPE.isPressed()) {
				remote.A.rotate(Motor.A.getTachoCount());
			}
		} else {
			Console.println("being controlled");
			try {
				Button.ESCAPE.waitForPressAndRelease();
			} catch (InterruptedException ie) {}
			System.exit(1);
		}
* /
		
		// exiting
		Console.println("EXIT");
		try {
			Button.ESCAPE.waitForPressAndRelease();
		} catch (InterruptedException ie) {}
		System.exit(1);
	}*/
}

/*
 * $Log: Communicator.java,v $
 * Revision 1.8  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.7  2009/05/06 19:51:03  mahanja
 * It loads an obj very well. but somewhere before unloading is a bug inside.
 *
 * Revision 1.6  2009/04/27 20:19:56  mahanja
 * implemented processDiscoveredJunctionMessage and fixed a very stupid bug!
 *
 * Revision 1.5  2009/04/27 20:03:52  mahanja
 * Parsing message such as number higher than 9 are supported
 *
 * Revision 1.4  2009/04/27 08:48:14  mahanja
 * Remote controll should work. All messages are parsed at the receiver (supports yet just a 10x10 grid)
 *
 * Revision 1.3  2009/04/23 21:25:25  mahanja
 * Connection bug fixed (don't know exactly what it was)
 *
 * Revision 1.2  2009/04/23 19:05:21  mahanja
 * Functional and tested!
 * To use the main method you have to give a name  "Definitions.initInstance(Definitions.CONSTANT_TO_BE_SET);" ).
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
