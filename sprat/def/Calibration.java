//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Calibration.java,v 1.4 2009/05/06 17:22:49 stollf06 Exp $

package def;

import tool.Console;
import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Calibration {
	private  final String[] calibJunction = {"put the light sensor", "on the junction", "then press enter to", "start the calibration"}; 

	private  final String[] calibLine = {"put the light sensor", "on the line", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibMyObject = {"put the light sensor", "on the my object", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibOtherRobotObject = {"put the light sensor", "on the other robots Object", "then press enter to", "start the calibration"}; 
	
	private  final String[] calibCommonObject = {"put the light sensor", "on the common object", "then press enter to", "start the calibration"}; 
	
	public int whiteGround;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	public Calibration(){
		//Definitions def = Definitions.getInstance();
		BrightnessRange objBR;
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
		
		//commun object
		//setDisplay(Definitions.calibCommonObject);
		//Definitions.colCommonObjects = setBrightRange();
		
		
		LCD.clear();
		Console.println("Junction"+Definitions.colJunction.min+"/"+Definitions.colJunction.max);
		Console.println("line: "+Definitions.colLine.min+"/"+Definitions.colLine.max);
		Console.println("myObj: "+Definitions.colMyObjects.min+"/"+Definitions.colMyObjects.max);
		Console.println("otherObj: "+Definitions.colOtherRobotObjects.min+"/"+Definitions.colOtherRobotObjects.max);
		LCD.refresh();
		Button.waitForPress();
		LCD.clearDisplay();
		
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
