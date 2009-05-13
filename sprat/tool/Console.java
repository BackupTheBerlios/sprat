// $Header: /home/xubuntu/berlios_backup/github/tmp-cvs/sprat/Repository/sprat/tool/Console.java,v 1.3 2009/05/13 14:51:25 mahanja Exp $
package tool;

import lejos.nxt.LCD;

/**
 * This class is primarily used to print debug and state information to the screen of the NXT
 * @author greila06
 *
 */
public class Console {
	private static final int SIZE = 8;
	private static final int LAST = SIZE -1;
	private static String[] output = new String[]{"","","","","","","",""};
	
	/**
	 * Prints the text on a new line of the console
	 * @param text to be printet
	 */
	public static void println(String text) {
		shiftAndAdd(text);
		show();
	}
	
	/**
	 * Prints the given text on the newest line of the console
	 * @param text to be printed
	 */
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
 * Revision 1.3  2009/05/13 14:51:25  mahanja
 * Last commit befor we finaly stoped the development on this project.
 * mahanja and stollf06 say GOOD BYE!
 *
 * Revision 1.2  2009/05/06 17:17:54  mahanja
 * The Ai is written completely new. Objects were not yet gathered. Only the way to a unknown or my-obj will be found.
 *
 * Revision 1.1  2009/04/23 18:56:49  mahanja
 * A console to write out strings in a console like matter
 *
 */