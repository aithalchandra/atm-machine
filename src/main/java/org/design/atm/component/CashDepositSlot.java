package org.design.atm.component;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CashDepositSlot {
    private List<Envelop> envelops;

    private CashDepositSlot() {
    }

    public static CashDepositSlot init() {
        CashDepositSlot cashDepositSlot = new CashDepositSlot();
        cashDepositSlot.envelops = new ArrayList<CashDepositSlot.Envelop>();
        return cashDepositSlot;
    }

    public void depositEnvelop(Envelop envelop) {
        log.info("Envelope added for userAccount:{}, amount: {}" + envelop.accountNumber, envelop.amount);
        this.envelops.add(envelop);
    }

    public static class Envelop {
        private final int accountNumber;
        private final double amount;

        private Envelop(int accountNumber, double amount) {
            super();
            this.accountNumber = accountNumber;
            this.amount = amount;
        }

        public static Envelop init(int accountNumber, double amount) {
            return new Envelop(accountNumber, amount);
        }
    }
}