//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/def/BrightnessRange.java,v 1.5 2009/05/13 14:51:25 mahanja Exp $
package def;

/**
 * Defines a range of brightness with a minimum and a maximum value.
 * @author greila06
 *
 */
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
 * Revision 1.5  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
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
