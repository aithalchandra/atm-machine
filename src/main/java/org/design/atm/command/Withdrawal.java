package org.design.atm.command;

import lombok.extern.slf4j.Slf4j;
import org.design.atm.component.view.ATMScreen;

@Slf4j
public class Withdrawal extends TransactionExecutor {
    // constant corresponding menu option to cancel
    private final static int CANCELED = 6;

    @Override
    public void execute() {
    	log.info("Transaction - Withdrawal initiating");
        boolean cashDispensed = false;
        double availableBalance;

        //Do loop until cash dispenses or user cancels
        do {
            int amount = showMenuOfAmounts();

            // check whether user choose withdrawal amount or canceled
            if (amount != CANCELED) {
                // get available balance of account involved
                availableBalance = getBankingService().getAvailableBalance(getAccountNumber());

                if (amount <= availableBalance) {
                    if (this.getCashDispenser().isSufficientCashAvailable(amount)) {

                        getBankingService().debit(getAccountNumber(), amount);
                        this.getCashDispenser().dispenseCash(amount);
                        log.info("Cash withdrawal from account:{}, amount:{}", getAccountNumber(), amount);
                        cashDispensed = true;
                        getScreen().showMessageLine("\nYour cash has been" + " dispensed. Please take your cash now.");
                    } else {
                        getScreen().showMessageLine("\nInsufficient cash available in the ATM." + "\n\nPlease choose a smaller amount.");
                    }
                } else {
                    getScreen().showMessageLine("\nInsufficient funds in your account." + "\n\nPlease choose a smaller amount.");
                }
            } else {
                getScreen().showMessageLine("\nCancelling transaction...");
                return;
            }
        } while (!cashDispensed);
    }

    private int showMenuOfAmounts() {
        int userChoice = 0;

        ATMScreen screen = getScreen();

        int[] amounts = {0, 100, 500, 1000, 2000, 5000};

        while (userChoice == 0) {
            // display the menu
            screen.showMessageLine("\nWithdrawal menu:");
            screen.showMessageLine("1 - $100");
            screen.showMessageLine("2 - $500");
            screen.showMessageLine("3 - $1000");
            screen.showMessageLine("4 - $2000");
            screen.showMessageLine("5 - $5000");
            screen.showMessageLine("6 - Cancel transaction");
            screen.showMessage("\nChoose a withdrawal amount: ");

            int input = this.getKeypad().getInput();

            switch (input) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                    userChoice = amounts[input];
                    break;
                case CANCELED:
                    userChoice = CANCELED;
                    break;
                default:
                    screen.showMessage("\nInvalid selection. Try again. ");
            }
        }
        return userChoice;
    }
}