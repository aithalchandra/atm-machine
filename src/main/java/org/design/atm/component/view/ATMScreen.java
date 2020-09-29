package org.design.atm.component.view;

import lombok.Data;

@Data
public class ATMScreen {
	private Keypad keypad;
	
	private ATMScreen() {
	}

	public static ATMScreen withKeyPad(final Keypad keypad) {
		ATMScreen atmScreen = new ATMScreen();
		atmScreen.keypad = keypad;
		return atmScreen;
	}

	
	public void showMessage(String message) {
		System.out.print(message);
	}
	
	public void showMessageLine(String message) {
		System.out.println(message);
	}
	
	public void showRupeesAmount(double amount) {
		System.out.printf("$%,.2f", amount);
	}
	
	public int displayMenu() {
		showMessageLine("\nMain Menu:");
		showMessageLine("1)  Check my balance");
		showMessageLine("2)  Withdraw money");
		showMessageLine("3)  Deposit money ");
		showMessageLine("4)  Exit\n");
		return keypad.getInput();
	}
	
	public String welcomeScreen() {
		showMessageLine("\nWelcome to ATM ! Enter Your name");
		return keypad.getAny();
	}
}