package org.design.atm

import spock.lang.Specification

class ATMFacadeAuthenticationSpec extends Specification {

    def "ATM Facade Service - User is not authorised and retries exhausted"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("11111 12 11111 13 11111 14".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.beginProcess()

        then:
        buffer.toString() == consoleMessage

        where:
        userAccount || consoleMessage
        11111       || "\n" +
                "Please enter your bank account number: \n" +
                "Please enter your PIN: \n" +
                "Invalid account number or PIN code Entered. Please do try again.\n" +
                "Please enter your bank account number: \n" +
                "Please enter your PIN: \n" +
                "Invalid account number or PIN code Entered. Please do try again.\n" +
                "Please enter your bank account number: \n" +
                "Please enter your PIN: \n" +
                "Invalid account number or PIN code Entered. Please do try again.\n" +
                "You have reached max number of tries for PIN.\n" +
                "Thank You For Transaction! Goodbye!\n"
    }

    def "ATM Facade Service - User is authorised and Check Balance"() {
        given:
        InputStream sysInBackup = System.in // backup System.in to restore it later
        ByteArrayInputStream inn = new ByteArrayInputStream("11111 54321 1 4".getBytes())
        System.setIn(inn)
        def atmFacade = ATMFacade.withDefaults();

        when:
        def buffer = new ByteArrayOutputStream()
        System.out = new PrintStream(buffer)

        and:
        atmFacade.beginProcess()

        then:
        buffer.toString() == consoleMessage

        where:
        userAccount || consoleMessage
        11111       || "\n" +
                "Please enter your bank account number: \n" +
                "Please enter your PIN: \n" +
                "Main Menu:\n" +
                "1)  Check my balance\n" +
                "2)  Withdraw money\n" +
                "3)  Deposit money \n" +
                "4)  Exit\n" +
                "\n" +
                "\n" +
                "Balance Information:\n" +
                " - Available balance: \$10,000.00\n" +
                " - Total balance:\$12,000.00\n" +
                "\n" +
                "Main Menu:\n" +
                "1)  Check my balance\n" +
                "2)  Withdraw money\n" +
                "3)  Deposit money \n" +
                "4)  Exit\n" +
                "\n" +
                "\n" +
                "Exiting system....\n" +
                "Thank You For Transaction! Goodbye!\n"
    }
}