//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/BrightnessRange.java,v 1.4 2009/04/27 09:56:02 stollf06 Exp $
package def;

public class BrightnessRange {

	public int min = 0;
	public int max = 0;
	
	public BrightnessRange(){
		
	}
	public BrightnessRange(int min, int max){
		this.min = min;
		this.max = max;
	}
}

/**
 * $Log: BrightnessRange.java,v $
 * Revision 1.4  2009/04/27 09:56:02  stollf06
 * added default initialisation
 *
 * Revision 1.3  2009/04/23 14:39:41  stollf06
 * first upload of calibration and motion classes
 *
 * Revision 1.2  2009/04/23 13:39:22  stollf06
 * variables and constants for motion added
 *
 * Revision 1.1  2009/04/23 13:33:27  stollf06
 * no message
 */
