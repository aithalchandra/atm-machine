package org.design.atm.component;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
public class CashDispenser {
    public final static int DEFAULT_INITIAL_COUNT = 200; // Total notes
    private int count; // number of RS 100 notes

    private static final int MIN_CAPACITY = 1;
    private static final int MAX_CAPACITY = 1000;
    private static final int CASH_NOTES_LIMIT = 40;

    private int[] currencyNotesSupported = IntStream.of(2000, 500, 200, 100, 50).toArray();
    private int[] noteCounter = new int[5];

    private CashDispenser(int count, int[] notesCounter) {
        this.count = count;
        this.noteCounter = notesCounter;
    }

    public static CashDispenser withCapacity(int notesCount, int[] notesCounter) {
        assert (notesCount >= MIN_CAPACITY && notesCount <= MAX_CAPACITY) : "Capacity should be at min 1 and max 1000";
        assert (IntStream.of(notesCounter).sum() == notesCount) : "Notes count should be equal to sum of all given currency notes";

        return new CashDispenser(notesCount, notesCounter);
    }

    public static CashDispenser withDefaultCapacity() {
        return new CashDispenser(DEFAULT_INITIAL_COUNT, IntStream.of(40, 40, 40, 40, 40).toArray());
    }

    public void dispenseCash(int amount) {
        log.info("Cash dispensed of Amount:{}", amount);

        int[] noteCurrencyRequired = checkCurrencyNotesRequired(amount);
        int notesRequired = 0;
        for (int i = 0; i < 5; i++) {
            noteCounter[i] = noteCounter[i] - noteCurrencyRequired[i];
            notesRequired += noteCurrencyRequired[i];
            log.info("Currency:" + currencyNotesSupported[i] + ",  Required Notes-" + noteCurrencyRequired[i] + ", Balance Notes:" + noteCounter[i]);
        }
        count -= notesRequired; // update the count of notes
    }

    public boolean isAllowedNotesLimit(int amount) {
        int notesRequired = amount / 100; // amount of notes required
        if (notesRequired > CASH_NOTES_LIMIT) {
            return false;
        }
        return true;
    }

    public boolean isSufficientCashAvailable(int amount) {
        try {
            checkCurrencyNotesRequired(amount);
            return true;
        } catch (AssertionError ex) {
            return false;
        }
    }

    public int[] checkCurrencyNotesRequired(int amount) {
        int[] noteRequired = new int[5];
        int requiredCount = 0;
        for (int i = 0; i < 5; i++) {
            if (amount >= currencyNotesSupported[i]) {
                requiredCount = amount / currencyNotesSupported[i];
                if (requiredCount < noteCounter[i]) {
                    noteRequired[i] = requiredCount;
                } else {
                    noteRequired[i] = noteCounter[i];
                }
                amount = amount - noteRequired[i] * currencyNotesSupported[i];
            }
        }
        assert amount == 0 : "Currency notes denominations not available for provided amount";
        return noteRequired;
    }
}