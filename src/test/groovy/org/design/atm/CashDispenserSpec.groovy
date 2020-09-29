package org.design.atm

import org.design.atm.component.CashDispenser
import spock.lang.Specification
import java.util.stream.IntStream;

class CashDispenserSpec extends Specification {

    def "create a Cash dispenser  with fixed/given capacity"() {
        expect:
        lot.count == initCapacity

        where:
        lot                                                                         || initCapacity
        CashDispenser.withCapacity(240, IntStream.of(40, 40, 60, 40, 60).toArray()) || 240
        CashDispenser.withDefaultCapacity()                                         || 200

    }

    def "creating a Cash dispenser with invalid capacity should fail"() {
        when:
        CashDispenser.withCapacity(notesCount, currencyNotesRequired)

        then:
        def err = thrown(expectedException)
        err.message == errorMessage

        where:
        notesCount || currencyNotesRequired                     || expectedException || errorMessage
        -1         || IntStream.of(40, 40, 0, 0, 0).toArray()   || AssertionError    || "Capacity should be at min 1 and max 1000"
        0          || IntStream.of(40, 40, 0, 0, 0).toArray()   || AssertionError    || "Capacity should be at min 1 and max 1000"
        10001      || IntStream.of(40, 40, 0, 0, 0).toArray()   || AssertionError    || "Capacity should be at min 1 and max 1000"
        240        || IntStream.of(40, 40, 40, 40, 0).toArray() || AssertionError    || "Notes count should be equal to sum of all given currency notes"
    }

    def "Cash dispenser with multiple currency notes dispensed"() {
        when:
        def cashDispenser = CashDispenser.withCapacity(200, IntStream.of(40, 40, 40, 40, 40).toArray())

        then:
        cashDispenser.isSufficientCashAvailable(amount) == isSufficientCashAvailable
        cashDispenser.checkCurrencyNotesRequired(amount) == currencyNotesRequired

        where:
        amount || isSufficientCashAvailable || currencyNotesRequired
        100000 || true                      || IntStream.of(40, 40, 0, 0, 0).toArray()
        600    || true                      || IntStream.of(0, 1, 0, 1, 0).toArray()
        13500  || true                      || IntStream.of(6, 3, 0, 0, 0).toArray()
        45100  || true                      || IntStream.of(22, 2, 0, 1, 0).toArray()
        200    || true                      || IntStream.of(0, 0, 1, 0, 0).toArray()
        18100  || true                      || IntStream.of(9, 0, 0, 1, 0).toArray()
    }

    def "Cash dispenser with  currency notes not available"() {
        when:
        def cashDispenser = CashDispenser.withCapacity(200, IntStream.of(40, 40, 40, 40, 40).toArray())
        cashDispenser.checkCurrencyNotesRequired(amount)

        then:
        def err = thrown(expectedException)
        err.message == errorMessage
        cashDispenser.isSufficientCashAvailable(amount) == isSufficientCashAvailable

        where:
        amount || isSufficientCashAvailable || expectedException || errorMessage
        60     || false                     || AssertionError    || "Currency notes denominations not available for provided amount"
        400000 || false                     || AssertionError    || "Currency notes denominations not available for provided amount"
        190    || false                     || AssertionError    || "Currency notes denominations not available for provided amount"
    }
}