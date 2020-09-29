package org.design.atm.service;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class BankingService {
    private Account[] accounts; // AccountList

    private BankingService() {
    }

    public static BankingService withDefaults() {
        BankingService bankingService = new BankingService();
        bankingService.accounts = new Account[5]; // test accounts
        bankingService.accounts[0] = new Account(11111, 54321, 10000.0, 12000.0);
        bankingService.accounts[1] = new Account(22222, 12345, 1200.0, 1200.0);
        bankingService.accounts[2] = new Account(33333, 21000, 5000.0, 5000.0);
        bankingService.accounts[3] = new Account(44444, 31000, 5000.0, 5000.0);
        bankingService.accounts[4] = new Account(55555, 31000, 5000.0, 5000.0);
        return bankingService;
    }

    // Get account object with given account number
    private Optional<Account> getAccount(final int accountNumber) {
        return Arrays.stream(accounts).filter(account -> account.getAccountNumber() == accountNumber).findFirst();
    }

    // Authenticate User If account-number matches with provided PIN
    public boolean authenticateUser(final int userAccountNumber, final int userPin) {
        log.info("User authentication with accountNumber:{}", userAccountNumber);
        return getAccount(userAccountNumber).map(account -> account.validatePIN(userPin)).orElse(false);
    }

    // Get available balance from account with given account number
    public double getAvailableBalance(int userAccountNumber) {
        return getAccount(userAccountNumber).map(Account::getAvailableBalance).orElse(0.0);
    }

    // Get total balance from the account with specified account number
    public double getTotalBalance(int userAccountNumber) {
        return getAccount(userAccountNumber).map(Account::getTotalBalance).orElse(0.0);
    }

    // DO Credit an amount into the account with given account number
    public void credit(int userAccountNumber, double amount) {
        log.info("Credit into account:{}, amount:{}", userAccountNumber, amount);
        getAccount(userAccountNumber).ifPresent(account -> account.credit(amount));
    }

    // Do debit an amount from account with given account number
    public void debit(int userAccountNumber, double amount) {
        log.info("Debit from account:{}, amount:{}", userAccountNumber, amount);
        getAccount(userAccountNumber).ifPresent(account -> account.debit(amount));
    }
}
