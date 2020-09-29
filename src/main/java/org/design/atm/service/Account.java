package org.design.atm.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {
	private int accountNumber;
	private int pin;
	private double availableBalance;
	/*
	 * AvailableBalance + PendingDeposit
	 */
	private double totalBalance;
	
	// validate users pin
	public boolean validatePIN (int userPIN) {
		if (pin == userPIN)
			return true;
		else
			return false;
	}
	
	public void credit(double amount) {
		totalBalance += amount;
	}
	
	public void debit(double amount) {
		availableBalance -= amount;
		totalBalance -= amount;
	}
}