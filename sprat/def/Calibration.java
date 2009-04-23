//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/Calibration.java,v 1.1 2009/04/23 14:47:39 stollf06 Exp $

package def;

import lejos.nxt.Button;
import lejos.nxt.LCD;

public class Calibration {
	
	public int whiteGround;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	}
	
	public Calibration(){
		Definitions def = Definitions.getInstance();
		BrightnessRange objBR;
		//white paper
		setDisplay(def.calibWhitePaper);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colWhiteGround = objBR;
		
		//line
		setDisplay(def.calibLine);
		objBR = setBrightRange();
		objBR.max += Definitions.brightTolerance;
		objBR.min -= Definitions.brightTolerance;
		Definitions.colLine = objBR;
		
		
		//own object
		//setDisplay(Definitions.calibMyObject);
		//Definitions.colMyObjects = setBrightRange();
		
		//other object
		//setDisplay(Definitions.calibOtherRobotObject);
		//Definitions.colOtherRobotObjects = setBrightRange();
		
		//commun object
		//setDisplay(Definitions.calibCommonObject);
		//Definitions.colCommonObjects = setBrightRange();
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
 * Revision 1.1  2009/04/23 14:47:39  stollf06
 * first upload
 *
 */
