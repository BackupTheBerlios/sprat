//$Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/action/Eye.java,v 1.3 2009/05/11 13:04:52 stollf06 Exp $

package action;

import def.Definitions;
import object.Junction;
import tool.Console;

public class Eye {
	
	public Eye() {
		
	}
	
	/**
	 * returns a junction type according to the read value of the lightsensor
	 * @return
	 */
	public static int getType() {
		int lsRead = Definitions.ls.readNormalizedValue();
		Console.println("col:"+lsRead);
		if (Definitions.colMyObjects.min <= lsRead && lsRead <= Definitions.colMyObjects.max) {
			if(Definitions.getInstance().isMaster){
				return Junction.MASTER_OBJ;
			}else{
				return Junction.SLAVE_OBJ;
			}
		}else if(Definitions.colOtherRobotObjects.min <= lsRead && lsRead <= Definitions.colOtherRobotObjects.max){
			if(Definitions.getInstance().isMaster){
				return Junction.SLAVE_OBJ;
			}else{
				return Junction.MASTER_OBJ;
			}
		}
		return Junction.EMPTY;
	}
	
	
}

/*
 * $Log: Eye.java,v $
 * Revision 1.3  2009/05/11 13:04:52  stollf06
 * code cleaning
 *
 * Revision 1.2  2009/05/06 17:17:50  stollf06
 * eye fonctional, little updates for the forklift
 *
 * Revision 1.1  2009/05/04 15:15:17  mahanja
 * Ai is mostly implemented but is still throwing errors everywhere!
 *
 */
