package org.design.atm;

public class CommandLine {
	public static void main(String[] args) {
		ATMFacade theATM =  ATMFacade.withDefaults();
		theATM.run();
	}
}