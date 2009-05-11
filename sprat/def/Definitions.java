// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Definitions.java,v 1.12 2009/05/11 13:05:20 stollf06 Exp $

package def;


import lejos.navigation.TachoNavigator;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;

/**
 * TODO: DESCRIPTION
 * 
 * @author $Author: stollf06 $
 */
public class  Definitions {
	public static final String MASTER = "MASTER";//"NXT";
	public static final String SLAVE = "SLAVE";
	
	public static final LightSensor ls = new LightSensor(SensorPort.S1);
	public static final float junctionSize = 5.9f;//give value in cm
	public static final float distBtwnJunct = 20.7f;//give value in cm
	public static final float distBtwnLsWheel = 3.0f;//give the distance between the light sensor and wheel center in cm
	public static final int leftJunctAngle = -90; // angle for left turns
	public static final int rightJunctAngle = 90; //angle for right turns
	public static final float corrTestAngle = 25.0f; //used to test the 
	public static final float corrAngle = 3.0f; //used to correct the direction in case it goes of the line
	public static final int brightTolerance = 20; //used to give some comparitive tolerance
	public static final float objSize = 10.0f;//give the length of one side for a single object
	public static final float lsBarDist = 1.0f; //distance between middle of the sensor and the lifting bars
	//public static boolean wayFinderOn = true; //TODO delete the concerning code in Motion.isThereAWay()
	
	public String myName = "UNDEFINED"; // Must be defined before starting the AI!
	public String othersName = "UNDEFINED"; // Must be defined before starting the AI!
	public boolean isMaster = false;

	public static final TachoNavigator  pilot = new TachoNavigator (5.5f, 13.6f, Motor.A, Motor.B,false);//TODO
	public static BrightnessRange colJunction;
	public static BrightnessRange colLine;
	public static BrightnessRange colMyObjects;
	public static BrightnessRange colOtherRobotObjects;
	public static BrightnessRange colCommonObjects;
	
	public static final int masterJnctOffset = 0;//absolute x-axis offset compared to 0
	public static final int slaveJunctOffset = 2;//relative x-axis offset of slave compared to the master
	public static final int commonJunctOffset = 1;//absolute x-axis offset

	
	private static Definitions instance = null;
	
	public static Definitions getInstance() {
		return instance;
	}
	
	public static Definitions initInstance(String name) {
		if(instance == null){
			instance = new Definitions(name); 
		}
		return instance;
	}
	
	private Definitions(String name) {
		myName = name;
		
		if (myName.equals(MASTER)) {
			othersName = SLAVE;
			isMaster = true;
		} else {
			othersName = MASTER;
			isMaster = false;
		}
	}
}

/*
 * $Log: Definitions.java,v $
 * Revision 1.12  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.11  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.10  2009/05/06 17:44:37  stollf06
 * for the big grid
 *
 * Revision 1.8  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 * Revision 1.7  2009/04/29 19:08:45  stollf06
 * better handling of next junction and actualjunction
 *
 * Revision 1.6  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.5  2009/04/27 09:57:05  stollf06
 * added masterJnctOffset and slaveJunctOffset
 *
 * Revision 1.4  2009/04/23 19:00:07  mahanja
 * The names are uppercase.
 * The name of the other robot will be found out at init.
 *
 * Revision 1.3  2009/04/23 14:39:41  stollf06
 * first upload of calibration and motion classes
 *
 * Revision 1.2  2009/04/23 13:39:22  stollf06
 * variables and constants for motion added
 *
 * Revision 1.1  2009/04/23 12:08:34  mahanja
 * First check in. This code isn't tested yet!
 *
 */
