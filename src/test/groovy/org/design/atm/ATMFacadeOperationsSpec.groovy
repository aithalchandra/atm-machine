package org.design.atm

import org.design.atm.ATMFacade
import org.design.atm.component.CashDispenser
import org.design.atm.errors.NotAuthorizedException
import org.design.atm.model.ATMOperation
import org.design.atm.model.UserSession
import spock.lang.Specification

class ATMFacadeOperationsSpec extends Specification {

    def "ATM Facade Service - Check Balance"() {
        given:
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.BALANCE_INQUIRY, userSession);

        then:
        buffer.toString() == consoleMessage

        where:
        userAccount || consoleMessage
        11111       || "\n" +
                "Balance Information:\n" +
                " - Available balance: \$10,000.00\n" +
                " - Total balance:\$12,000.00\n"
        22222       || "\n" +
                "Balance Information:\n" +
                " - Available balance: \$1,200.00\n" +
                " - Total balance:\$1,200.00\n"
    }

    def "ATM Facade Service - Withdrawal Funds"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("2".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.WITHDRAWAL, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        11111       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n" +
                "\n" +
                "Choose a withdrawal amount: \n" +
                "Your cash has been dispensed. Please take your cash now.\n"
    }

    def "ATM Facade Service - Deposit Funds"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("400".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.DEPOSIT, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        11111       || "\n" +
                "Please enter a deposit amount in RUPEES (or 0 to cancel): \n" +
                "Please insert a deposit envelope containing \$400.00.\n"
    }

    def "ATM Facade Service - ATM Operations and User not Authorised"() {
        given:
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, false)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(atmOperation, userSession);

        then:
        def err = thrown(expectedException)
        err.message == errorMessage

        where:
        userAccount || atmOperation                 || expectedException      || errorMessage
        11111       || ATMOperation.BALANCE_INQUIRY || NotAuthorizedException || "User is not authorized"
        11111       || ATMOperation.DEPOSIT         || NotAuthorizedException || "User is not authorized"
        11111       || ATMOperation.WITHDRAWAL      || NotAuthorizedException || "User is not authorized"
    }

    def "ATM Facade Service - Deposit Funds/Cancel"() {
        given: 'User presses 0 input as deposit-amount for cancelling deposit'
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("0".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.DEPOSIT, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        11111       || "\n" +
                "Please enter a deposit amount in RUPEES (or 0 to cancel): \n" +
                "Canceling transaction...\n"
    }

    def "ATM Facade Service - Withdrawal Funds/Cancelling"() {
        given: "User press 6 for cancellation"
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("6".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.WITHDRAWAL, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        22222       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Cancelling transaction...\n"
    }

    def "ATM Facade Service - Withdrawal Funds-Incorrect Input"() {
        given: "User first press 9 as incorrect input then correct to 1"
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("9 1".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.WITHDRAWAL, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        22222       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Invalid selection. Try again. \n" +
                "Withdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Your cash has been dispensed. Please take your cash now.\n"
    }

    def "ATM Facade Service - Withdrawal Funds- Insufficient Balance in Account"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("5 1".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.WITHDRAWAL, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        22222       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Insufficient funds in your account.\n\n" +
                "Please choose a smaller amount.\n\n" +
                "Withdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Your cash has been dispensed. Please take your cash now.\n"
    }

    def "ATM Facade Service - Withdrawal Funds- ATM Out of Funds"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("5 1".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withCashDispenser(CashDispenser.withCapacity(2));

        when:
        def userSession = UserSession.init(userAccount, true)
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.doTransaction(ATMOperation.WITHDRAWAL, userSession);

        then:
        buffer.toString() == consoleMessage

        cleanup:
        System.setIn(sysInBackup);

        where:
        userAccount || consoleMessage
        11111       || "\nWithdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Insufficient cash available in the ATM.\n\n" +
                "Please choose a smaller amount.\n\n" +
                "Withdrawal menu:\n" +
                "1 - \$100\n" +
                "2 - \$500\n" +
                "3 - \$1000\n" +
                "4 - \$2000\n" +
                "5 - \$5000\n" +
                "6 - Cancel transaction\n\n" +
                "Choose a withdrawal amount: \n" +
                "Your cash has been dispensed. Please take your cash now.\n"
    }
}