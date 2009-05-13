//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Calibration.java,v 1.8 2009/05/13 14:51:25 mahanja Exp $

package def;

import tool.Console;
import lejos.nxt.Button;
import lejos.nxt.LCD;

/**
 * Sets the values used to determine the type of a junction as well as the
 * BrightnessRange of the line.
 * @author greila06
 *
 */
public class Calibration {
	private  final String[] calibJunction = {"light sensor", "on junction", "then press enter to", "start the calibration"}; 

	private  final String[] calibLine = {"light sensor", "on the line", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibMyObject = {"light sensor", "on my object", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibOtherRobotObject = {"light sensor", "on the other Object", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibCommonObject = {"light sensor", "on the common object", "then press enter to", "start the calibration"}; 
	
	public int whiteGround;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	/**
	 * For testing purposes this class uses constant values for type-recognition.
	 */
	public Calibration(){
		//Definitions def = Definitions.getInstance();
		BrightnessRange objBR;
		/* TOOOO MUCH!
		setDisplay(calibJunction);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colJunction = objBR;
		
		//line
		setDisplay(calibLine);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colLine = objBR;
		
		
		//own object
		setDisplay(calibMyObject);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colMyObjects = objBR; 
		
		//other object
		setDisplay(calibOtherRobotObject);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colOtherRobotObjects = objBR; 
		
		*/
		
		
////////////////////////// for testing \\\\\\\\\\\\\\\\\\\\\\\\\\\
		// junction
		objBR = new BrightnessRange();
		objBR.max = 300;//293;
		objBR.min = 150;
		Definitions.colJunction = objBR;
		
		//line
		objBR = new BrightnessRange();
		objBR.max = 400;//368;
		objBR.min = 301;//313;
		Definitions.colLine = objBR;
		
		
		if (Definitions.MASTER.equals(new NameAsker().askName())) {
			//own object
			objBR = new BrightnessRange();
			objBR.max = 600;//565;
			objBR.min = 501;//519;
			Definitions.colMyObjects = objBR; 
			
			//other object
			objBR = new BrightnessRange();
			objBR.max = 500;//443;
			objBR.min = 401;//490;
			Definitions.colOtherRobotObjects = objBR; 
			
			Definitions.initInstance(Definitions.MASTER);
		} else {
			//own object
			objBR = new BrightnessRange();
			objBR.max = 600;//565;
			objBR.min = 501;//519;
			Definitions.colOtherRobotObjects = objBR; 
			
			//other object
			objBR = new BrightnessRange();
			objBR.max = 500;//443;
			objBR.min = 401;//490;
			Definitions.colMyObjects = objBR; 

			Definitions.initInstance(Definitions.SLAVE);
		}
////////////////////////// for testing \\\\\\\\\\\\\\\\\\\\\\\\\\\
		
		
		
		
		
		
		
		
		
		//commun object
		//setDisplay(Definitions.calibCommonObject);
		//Definitions.colCommonObjects = setBrightRange();
		
		
		/*LCD.clear();
		Console.println("I am the: "+Definitions.getInstance().myName);
		Console.println("Junction"+Definitions.colJunction.min+"/"+Definitions.colJunction.max);
		Console.println("line: "+Definitions.colLine.min+"/"+Definitions.colLine.max);
		Console.println("myObj: "+Definitions.colMyObjects.min+"/"+Definitions.colMyObjects.max);
		Console.println("otherObj: "+Definitions.colOtherRobotObjects.min+"/"+Definitions.colOtherRobotObjects.max);
		LCD.refresh();
		Button.waitForPress();
		LCD.clearDisplay();*/
		
	}
	
	public void setDisplay(String[] text){
		LCD.clear();
		for (int i = 0; i < text.length; i++) {
			LCD.drawString(text[i] , 0, i);
		}
		LCD.refresh();
		Button.waitForPress();
		LCD.clear();
	}
	
	public BrightnessRange setBrightRange(){
		
		int min=Integer.MAX_VALUE,max=0;
		int actual=0;
		
		while(!Button.ENTER.isPressed()){
			actual = Definitions.ls.readNormalizedValue();
			LCD.drawString("actual:" , 0, 0);
			LCD.drawInt(actual, 8, 0);
			if(actual<min){
				min = actual;
			}
			if(actual>max){
				max = actual;
			}
			LCD.drawString("min:" , 0, 1);
			LCD.drawInt(min, 4, 1);
			LCD.drawString("max:" , 0, 2);
			LCD.drawInt(max, 4, 2);
			LCD.refresh();
		}
		LCD.clear();
		LCD.drawString("final values" , 0, 0);
		LCD.drawString("min:" , 0, 1);
		LCD.drawInt(min, 4, 1);
		LCD.drawString("max:" , 0, 2);
		LCD.drawInt(max, 4, 2);
		LCD.drawString("press enter to" , 0, 4);
		LCD.drawString("to conitnue" , 0, 5);
		LCD.refresh();
		Button.waitForPress();
		return new BrightnessRange(min, max);
	}

}
/*
 * $Log: Calibration.java,v $
 * Revision 1.8  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.7  2009/05/11 13:05:20  stollf06
 * code cleaning
 *
 * Revision 1.6  2009/05/10 05:21:36  mahanja
 * It works all well!
 *
 * Revision 1.5  2009/05/06 19:51:03  mahanja
 * It loads an obj very well. but somewhere before unloading is a bug inside.
 *
 * Revision 1.4  2009/05/06 17:22:49  stollf06
 * full calibration now
 *
 * Revision 1.3  2009/04/27 19:53:55  stollf06
 * introduction of orientation on the grid
 *
 * Revision 1.2  2009/04/27 09:57:58  stollf06
 * took out calibration of whitePaper (unnecessary)
 *
 * Revision 1.1  2009/04/23 14:47:39  stollf06
 * first upload
 *
 */
