package org.design.atm.command;

import lombok.extern.slf4j.Slf4j;
import org.design.atm.component.CashDepositSlot.Envelop;
import org.design.atm.component.view.ATMScreen;

@Slf4j
public class Deposit extends TransactionExecutor {
	private final static int CANCELED = 0;

	// execute transaction
	@Override
	public void execute() {
		log.info("Transaction - Withdrawal initiating");
		double amount = promptForAmount();

		// check whether user entered deposit amount or canceled
		if (amount != CANCELED) {
			// request deposit envelope
			this.getScreen().showMessage("\nPlease insert a deposit envelope containing ");
			this.getScreen().showRupeesAmount(amount);
			this.getScreen().showMessageLine(".");

			this.getDepositSlot().depositEnvelop(Envelop.init(getAccountNumber(), amount));
			this.getBankingService().credit(getAccountNumber(), amount);
			log.info("Cash deposited of amount:{} at Account: {}", getAccountNumber(), amount);
		} else {
			this.getScreen().showMessageLine("\nCanceling transaction...");
		}
	}

	private double promptForAmount() {
		ATMScreen screen = getScreen();

		screen.showMessage("\nPlease enter a deposit amount in " + "RUPEES (or 0 to cancel): ");
		int input = this.getKeypad().getInput();

		// check if canceled
		if (input == CANCELED)
			return CANCELED;
		else {
			return (double) input;
		}
	}
}
