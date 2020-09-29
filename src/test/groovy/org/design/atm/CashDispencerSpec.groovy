package org.design.atm

import spock.lang.Specification
import org.design.atm.component.CashDispenser

class CashDispencerSpec extends Specification {

    def "create a Cash dispenser  with fixed/given capacity"() {
        expect:
        lot.count == initCapacity

        where:
        lot                                 || initCapacity
        CashDispenser.withCapacity(300)     || 300
        CashDispenser.withDefaultCapacity() || 2000
    }

    def "creating a Cash dispenser with invalid capacity should fail"() {
        when:
        CashDispenser.withCapacity(-1)

        then:
        def err = thrown(expectedException)
        err.message == errorMessage

        where:
        capacity || expectedException || errorMessage
        -1       || AssertionError    || "Capacity should be at min 1 and max 10000"
        0        || AssertionError    || "Capacity should be at min 1 and max 10000"
        10001     || AssertionError    || "Capacity should be at min 1 and max 10000"
    }
}