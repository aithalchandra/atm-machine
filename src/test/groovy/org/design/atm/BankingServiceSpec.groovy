package org.design.atm

import org.design.atm.component.CashDispenser
import spock.lang.Specification
import org.design.atm.service.BankingService

class BankingServiceSpec extends Specification {

    def "Banking service authentication-service"() {
        given:
        def bankingService = BankingService.withDefaults();

        expect:
        bankingService.authenticateUser(userAccountNumber, pin) == isAuthenticated

        where:
        userAccountNumber || pin   || isAuthenticated
        11111             || 54321 || true
        22222             || 321   || false
        33333             || 21000 || true
    }

    def "Banking service Account balance check"() {
        given:
        def bankingService = BankingService.withDefaults();

        expect:
        bankingService.getTotalBalance(userAccountNumber) == totalBalance
        bankingService.getAvailableBalance(userAccountNumber) == availableBalance

        where:
        userAccountNumber || availableBalance || totalBalance
        11111             || 10000.0           || 12000.0
        22222             || 1200.0            || 1200.0
        33333             || 5000.0            || 5000.0
    }

    def "Banking service Account - Credit fund"() {
        given: 'Account 1111 has total balance as $10000.0'
        def bankingService = BankingService.withDefaults();
        def accountNo = 11111

        when:
        bankingService.credit(accountNo, 500.0)

        then:
        bankingService.getTotalBalance(accountNo) == 12500.00
        bankingService.getAvailableBalance(accountNo) == 10000.00
    }

    def "Banking service Account - Debit fund"() {
        given: 'Account 11111 has available balance as $10000.0'
        def bankingService = BankingService.withDefaults();
        def accountNo = 11111

        when:
        bankingService.debit(accountNo, 1000.0)

        then:
        bankingService.getAvailableBalance(accountNo) == 9000.00
    }
}