package org.design.atm.component.view;

/*
	ATM Keypad
 */
import java.util.Scanner;

public class Keypad {
	private Scanner input; // reads data from the command line
	
	private Keypad() {
	}

	public static Keypad withDefault() {
		Keypad keypad = new Keypad();
		keypad.input = new Scanner(System.in);
		return keypad;
	}
	
	public int getInput() {
		return input.nextInt();
	}
	
	public String getAny() {
		return input.next();
	}
}