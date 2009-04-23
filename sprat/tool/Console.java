// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/tool/Console.java,v 1.1 2009/04/23 18:56:49 mahanja Exp $
package tool;

import lejos.nxt.LCD;

public class Console {
	private static final int SIZE = 7;
	private static final int LAST = SIZE -1;
	private static String[] output = new String[]{"","","","","","",""};
	
	public static void println(String text) {
		shiftAndAdd(text);
		show();
	}
	
	public static void print(String text) {
		output[LAST] = output[LAST] + text;
		show();
	}
	
	private static void show() {
		LCD.clear();
		for (int i = 0; i < SIZE; i++) {
			LCD.drawString(output[i], 0, i);
		}
	}
	
	private static void shiftAndAdd(String text) {
		for (int i = 0; i < LAST; i++) {
			output[i] = output[i+1];
		}
		output[LAST] = text;
	}
}


/*
 * $Log: Console.java,v $
 * Revision 1.1  2009/04/23 18:56:49  mahanja
 * A console to write out strings in a console like matter
 *
 */