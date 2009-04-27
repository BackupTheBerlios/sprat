// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Definitions.java,v 1.5 2009/04/27 09:57:05 stollf06 Exp $

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
	public static final String MASTER = "NXT";
	public static final String SLAVE = "SLAVE";
	
	public static final LightSensor ls = new LightSensor(SensorPort.S1);
	public static final float junctionSize = 5.9f;//give value in cm
	public static final float distBtwnJunct = 14.1f;//give value in cm
	public static final float distBtwnLsWheel = 3.7f;//give the distance between the light sensor and wheel center in cm
	public static final int leftJunctAngle = -90;
	public static final int rightJunctAngle = 90;
	public static final float corrTestAngle = 20.0f; //used to test the 
	public static final float corrAngle = 2.0f; //used to correct the direction in case it goes of the line
	public static final int brightTolerance = 5; //used to give some comparitive tolerance
	public static boolean wayFinderOn = true; //TODO delete the concerning code in Motion.isThereAWay()
	
	public String myName = "UNDEFINED"; // Must be defined before starting the AI!
	public String othersName = "UNDEFINED"; // Must be defined before starting the AI!

	public static final TachoNavigator  pilot = new TachoNavigator (5.5f, 12.5f, Motor.A, Motor.B,false);//TODO
	public static BrightnessRange colWhiteGround;
	public static BrightnessRange colLine;
	public static BrightnessRange colMyObjects;
	public static BrightnessRange colOtherRobotObjects;
	public static BrightnessRange colCommonObjects;
	
	public static final int masterJnctOffset = 0;
	public static final int slaveJunctOffset = 2;//relative x-axis offset of slave compared to the master 
	
	public static final String[] calibWhitePaper = {"put the light sensor", "on the white surface", "then press enter to", "start the calibration"}; 

	public static final String[] calibLine = {"put the light sensor", "on the line", "then press enter to", "start the calibration"}; 
	
	public static final String[] calibMyObject = {"put the light sensor", "on the my object", "then press enter to", "start the calibration"}; 
	
	public static final String[] calibOtherRobotObject = {"put the light sensor", "on the other robots Object", "then press enter to", "start the calibration"}; 
	
	public static final String[] calibCommonObject = {"put the light sensor", "on the common object", "then press enter to", "start the calibration"}; 

	
	private static Definitions instance = null;
	
	public static Definitions getInstance() {
		return instance;
	}
	
	public static Definitions initInstance(String name) {
		return instance = new Definitions(name); 
	}
	
	public Definitions(String name) {
		myName = name;
		if (myName.equals(MASTER)) {
			othersName = SLAVE;
		} else {
			othersName = MASTER;
		}
	}
}

/*
 * $Log: Definitions.java,v $
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
