package org.design.atm.component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CashDispenser {
    /*
     * Assuming Cash-dispenser may take only RS-100 fixed notes and no-refiling supported as of now.
     *
     * If needs to support multiple denominations then needs to have map of <CURRENCY_NOTE, COUNT>
     */
    public final static int DEFAULT_INITIAL_COUNT = 2000; // Total notes
    private int count; // number of RS 100 notes

    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 10000;

    private CashDispenser(int count) {
        this.count = count;
    }

    public static CashDispenser withCapacity(int capacity) {
        assert (capacity >= MIN_CAPACITY && capacity <= MAX_CAPACITY) : "Capacity should be at min 1 and max 10000";
        return new CashDispenser(capacity);
    }

    public static CashDispenser withDefaultCapacity() {
        return new CashDispenser(DEFAULT_INITIAL_COUNT);
    }

    public void dispenseCash(int amount) {
        log.info("Cash dispensed of Amount:{}", amount);
        int notesRequired = amount / 100; // amount of notes required
        count -= notesRequired; // update the count of notes
    }

    public boolean isSufficientCashAvailable(int amount) {
        int billsRequired = amount / 100;

        if (count >= billsRequired)
            return true;
        else
            return false;
    }
}