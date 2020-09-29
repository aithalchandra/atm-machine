package org.design.atm.model;

import org.design.atm.command.BalanceInquiry;
import org.design.atm.command.Deposit;
import org.design.atm.command.Transaction;
import org.design.atm.command.Withdrawal;

public enum ATMTransactionManager {
    BALANCE_INQUIRY(BalanceInquiry.class), WITHDRAWAL(Withdrawal.class), DEPOSIT(Deposit.class);

    ATMTransactionManager(Class<? extends Transaction> type) {
        this.type = type;
    }

    public Class<? extends Transaction> getType() {
        return type;
    }

    private Class<? extends Transaction> type;
}