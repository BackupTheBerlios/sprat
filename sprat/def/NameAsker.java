package def;

import tool.Console;
import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;

/**
 * Displays a menu on which the type (MASTER/SLAVE) can be chosen.
 * @author greila06
 *
 */
public class NameAsker implements ButtonListener {
	private String current = Definitions.MASTER;
	private boolean enterPressed = false;
	
	public NameAsker() {
		Button.LEFT.addButtonListener(this);
		Button.RIGHT.addButtonListener(this);
		Button.ENTER.addButtonListener(this);
		Button.ESCAPE.addButtonListener(this);
	}
	
	/**
	 * Returns the chosen name of this robot
	 * @return the chosen name of this robot
	 */
	public String askName() {
		display(current);
		while(!enterPressed);	// busy waiting

		return current;
	}
	
	private void display(String who) {
		LCD.clear();
		LCD.drawString("Who am I?", 0, 1);
		if (who.equals(Definitions.MASTER)) {
			LCD.drawString("> Master", 0, 3);
			LCD.drawString("  Slave", 0, 4);
		} else {
			LCD.drawString("  Master", 0, 3);
			LCD.drawString("> Slave", 0, 4);
		}
		LCD.refresh();
	}

	/**
	 * ButtonListener implementation. Does nothing on buttonPressed
	 * @see buttonReleased(Button pressedButton)
	 */
	public void buttonPressed(Button arg0) {
		// Do the change on buttonReleased...
		
	}

	/**
	 * If right of left button were pressed and released change the chosen name.
	 * If enter were pressed, confirm choice. If the escape button were pressed, this
	 * end the program. This listeners keep ever on the buttons. Like this the
	 * application can ever be closed by pressing and releasing the escape buttion.
	 */
	public void buttonReleased(Button pressedButton) {
		if (pressedButton.equals(Button.LEFT) || pressedButton.equals(Button.RIGHT)) {
			if (current.equals(Definitions.MASTER))
				current = Definitions.SLAVE;
			else
				current = Definitions.MASTER;
			
			display(current);
		} else if (pressedButton.equals(Button.ENTER)) {
			enterPressed = true;
		} else if (pressedButton.equals(Button.ESCAPE)) {
			System.exit(0);
		} else {
			LCD.clear();
			LCD.drawString("???", 7, 0);
			LCD.refresh();
		}
	}
	
	
	
}
