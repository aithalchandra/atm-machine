package org.design.atm.command;

import org.design.atm.component.CashDepositSlot;
import org.design.atm.component.CashDispenser;
import org.design.atm.component.view.ATMScreen;
import org.design.atm.model.UserSession;
import org.design.atm.service.BankingService;

public interface Transaction {

	void doExecute();
	void init(UserSession userSession, ATMScreen atmScreen, BankingService bankingService, CashDispenser cashDispenser, CashDepositSlot depositSlot);
}