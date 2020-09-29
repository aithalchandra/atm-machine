package org.design.atm;

import org.design.atm.command.Transaction;
import org.design.atm.command.TransactionExecutor;
import org.design.atm.component.CashDepositSlot;
import org.design.atm.component.CashDispenser;
import org.design.atm.component.view.ATMScreen;
import org.design.atm.component.view.Keypad;
import org.design.atm.errors.NotAuthorizedException;
import org.design.atm.errors.RetryExhaustedException;
import org.design.atm.model.ATMOperation;
import org.design.atm.model.ATMTransactionManager;
import org.design.atm.model.UserSession;
import org.design.atm.service.BankingService;

import lombok.extern.slf4j.Slf4j;

/*
 ATM Facade to initiates ATM transactions
 */
@Slf4j
public class ATMFacade {
    private ATMScreen screen; // ATM's screen
    private CashDispenser cashDispenser; // ATM's money dispenser
    private CashDepositSlot depositSlot; // ATM's cash deposit slot
    private BankingService bankingService; // banking-service

    private ATMFacade() {
    }

    public static ATMFacade withDefaults() {
        ATMFacade atmFacade = new ATMFacade();
        log.info("ATM Initialise");
        atmFacade.screen = ATMScreen.withKeyPad(Keypad.withDefault());
        atmFacade.cashDispenser = CashDispenser.withDefaultCapacity();
        atmFacade.depositSlot = CashDepositSlot.init();
        atmFacade.bankingService = BankingService.withDefaults();
        return atmFacade;
    }

    public static ATMFacade withCashDispenser(CashDispenser cashDispenser) {
        log.info("ATM Initialise with Cash Dispenser");
        ATMFacade atmFacade = new ATMFacade();
        atmFacade.screen = ATMScreen.withKeyPad(Keypad.withDefault());
        atmFacade.cashDispenser = cashDispenser;
        atmFacade.depositSlot = CashDepositSlot.init();
        atmFacade.bankingService = BankingService.withDefaults();
        return atmFacade;
    }

    public void run() {
        // welcome and authenticate user and perform transaction
        while (true) {
            screen.welcomeScreen();
            beginProcess();
        }
    }

    public void beginProcess() {
        try {
            UserSession userSession = validateCustomer();
            showTransactionsMenu(userSession);
        } catch (NotAuthorizedException ex) {
            this.screen.showMessage("\nUser Session Timeout/Not Authorized. Please do try again");
        } catch (RetryExhaustedException ex) {
            this.screen.showMessage("\nYou have reached max number of tries for PIN.");
        }
        screen.showMessageLine("\nThank You For Transaction! Goodbye!");
    }

    private UserSession validateCustomer() {
        int counter = 3;
        while (counter > 0) {
            screen.showMessage("\nPlease enter your bank account number: ");
            int accountNumber = screen.getKeypad().getInput();
            screen.showMessage("\nPlease enter your PIN: ");
            int pin = screen.getKeypad().getInput();

            // set userAuthenticated to boolean value set by database
            boolean userAuthenticated = bankingService.authenticateUser(accountNumber, pin);
            counter--;
            // check whether authentication succeed
            if (userAuthenticated) {
                UserSession userSession = UserSession.init(accountNumber, true);
                return userSession;
            } else {
                screen.showMessage("\nInvalid account number or PIN code Entered. Please do try again.");
            }
        }
        throw new RetryExhaustedException();
    }

    private void showTransactionsMenu(UserSession userSession) {

        boolean userExited = false; // user has not chosen exit

        while (!userExited) {
            int mainMenuSelection = screen.displayMenu();

            ATMOperation atmOperation = ATMOperation.from(mainMenuSelection);
            switch (atmOperation) {
                case BALANCE_INQUIRY:
                case WITHDRAWAL:
                case DEPOSIT:
                    doTransaction(atmOperation, userSession);
                    break;
                case EXIT:
                    screen.showMessage("\nExiting system....");
                    userExited = true;
                    break;
                default:
                    screen.showMessage("\n You have not entered a valid selection. Try again.");
                    break;
            }
        }
    }

    protected void doTransaction(ATMOperation atmOperation, UserSession userSession) {
        log.info("ATM Transaction initiated for user:{}, operation: {}", userSession.getAccountNumber(), atmOperation);
        ATMTransactionManager transactionManager = ATMTransactionManager.valueOf(atmOperation.name());
        Transaction transaction = TransactionExecutor.create(transactionManager.getType());
        transaction.init(userSession, screen, bankingService, cashDispenser, depositSlot);
        transaction.doExecute();
    }
}