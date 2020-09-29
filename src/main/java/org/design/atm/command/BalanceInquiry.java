package org.design.atm.command;

import lombok.extern.slf4j.Slf4j;
import org.design.atm.component.view.ATMScreen;

@Slf4j
public class BalanceInquiry extends TransactionExecutor {
	
	//Do transaction
	@Override
	public void execute() {
		log.debug("Transaction - BalanceEnquiry initiating");

		ATMScreen screen = getScreen();
		
		double availableBalance = getBankingService().getAvailableBalance(getAccountNumber());
		
		// get total balance for the account involved
		double totalBalance = getBankingService().getTotalBalance(getAccountNumber());
		
		// Display all the information to the screen
		screen.showMessageLine("\nBalance Information:");
		screen.showMessage(" - Available balance: ");
		screen.showRupeesAmount(availableBalance);
		screen.showMessage("\n - Total balance:");
		screen.showRupeesAmount(totalBalance);
		screen.showMessageLine("");

	}
}