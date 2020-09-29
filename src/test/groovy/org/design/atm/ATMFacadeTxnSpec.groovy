package org.design.atm

import org.design.atm.model.ATMOperation
import org.design.atm.model.UserSession
import spock.lang.Shared
import spock.lang.Specification

class ATMFacadeTxnSpec extends Specification {
    @Shared
    def atmFacade

    def setupSpec() {
        def InputStream sysInBackup = System.in // backup System.in to restore it later
        def ByteArrayInputStream inn = new ByteArrayInputStream("4 400".getBytes())
        System.setIn(inn)
        atmFacade = ATMFacade.withDefaults()
    }

    def "ATM Facade Service - Withdrawal/Check Funds"() {
        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(atmOperation, userSession);

        then:
        buffer.toString() == consoleMessage

        where:
        atmOperation                 || userAccount || consoleMessage
        ATMOperation.BALANCE_INQUIRY || 11111       || "\n" +
                "Balance Information:\n" +
                " - Available balance: \$10,000.00\n" +
                " - Total balance:\$12,000.00\n"
        ATMOperation.WITHDRAWAL      || 11111       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Custom Amount\n" +
                "7 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Your cash has been dispensed. Please take your cash now.\n"
        ATMOperation.BALANCE_INQUIRY || 11111       || "\n" +
                "Balance Information:\n" +
                " - Available balance: \$8,000.00\n" +
                " - Total balance:\$10,000.00\n"
    }

    def "ATM Facade Service - Deposit/Check Funds"() {
        when:
        def userSession = new UserSession(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(atmOperation, userSession);

        then:
        buffer.toString() == consoleMessage

        where:
        atmOperation                 || userAccount || consoleMessage
        ATMOperation.DEPOSIT         || 11111       || "\n" +
                "Please enter a deposit amount in RUPEES (or 0 to cancel): \n" +
                "Please insert a deposit envelope containing \$400.00.\n"
        ATMOperation.BALANCE_INQUIRY || 11111       || "\n" +
                "Balance Information:\n" +
                " - Available balance: \$8,000.00\n" +
                " - Total balance:\$10,400.00\n"
    }
}