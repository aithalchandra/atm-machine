package org.design.atm.command;

import org.design.atm.component.CashDepositSlot;
import org.design.atm.component.CashDispenser;
import org.design.atm.component.view.ATMScreen;
import org.design.atm.component.view.Keypad;
import org.design.atm.errors.NotAuthorizedException;
import org.design.atm.model.UserSession;
import org.design.atm.service.BankingService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public abstract class TransactionExecutor implements Transaction {
	private UserSession userSession;
	private ATMScreen screen;
	private BankingService bankingService;
	private CashDispenser cashDispenser;
	private CashDepositSlot depositSlot;
    
    @Override
    public void init(UserSession userSession, ATMScreen atmScreen, BankingService bankingService, CashDispenser cashDispenser, CashDepositSlot depositSlot) {
    	this.userSession = userSession;
        this.screen = atmScreen;
        this.bankingService = bankingService;
        this.cashDispenser = cashDispenser;
        this.depositSlot = depositSlot;
    }

    @Override
    public void doExecute() {
        validate();
        execute();
    }

    abstract void execute();

    private void validate() {
        if (!userSession.isUserAuthenticated()) {
            throw new NotAuthorizedException("User is not authorized");
        }
    }
	
    /**
     * Creates a Transaction.
     */
    public static <T extends Transaction> T create(Class<T> classz) {
        try {
			return classz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("Error In Initializing Transaction : {}", classz, e);
		}
        return null;
    }
    
    public int getAccountNumber() {
    	return this.userSession.getAccountNumber();
    }
    
    public Keypad getKeypad() {
    	return this.screen.getKeypad();
    }
}