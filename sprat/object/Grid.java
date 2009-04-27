//$Header:
package object;


import java.util.Vector;

import def.Definitions;

public class Grid {
	Vector list = new Vector();
	Position homeBase;
	//int ySize = 1;
	//ArrayList y = new ArrayList;
	
	/**
	 * the grid should be created after the initialisation of master and slave
	 */
	public Grid(){
		Definitions def = Definitions.getInstance();
		if(def.myName.equals(Definitions.MASTER)){
			homeBase = new Position(Definitions.masterJnctOffset, 0);
		}else{
			homeBase = new Position(Definitions.slaveJunctOffset, 0);
		}
	}
	
	/**
	 * set junction adds a new junction to the memory, if it is already existent the type is overwritten
	 * @param junct
	 */
	public void setJunction(Junction junct){
		//int i = 0;
		int  junctIndex = getJunctIndex(junct.getPosition());
		
		if(junctIndex == -1){
			list.add(junct);
		}else{
			list.set(junctIndex, junct);
		}
	}
	
	public Junction getJunction(Position pos){
		Junction junctList;
		for (int i = 0; i < list.size(); i++) {
			junctList = (Junction)list.get(i);
			if(junctList.getPosition().getX() == pos.getX()&& junctList.getPosition().getY() == pos.getY()){
				return junctList;
			}
		}
		return null;
	}
	
	private int getJunctIndex(Position pos){
		Junction junctList;
		for (int i = 0; i < list.size(); i++) {
			junctList = (Junction)list.get(i);
			if(junctList.getPosition().getX() == pos.getX()&& junctList.getPosition().getY() == pos.getY()){
				return i;
			}
		}
		return -1;
	}

}
/*
 * $Log:
 */