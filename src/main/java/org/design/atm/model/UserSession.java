package org.design.atm.model;

public final class UserSession {

    private final boolean userAuthenticated; // whether user is authenticated
    private final int accountNumber; // current user's account number

    private UserSession(final int accountNumber, final boolean userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
        this.accountNumber = accountNumber;
    }

    public static UserSession init(final int accountNumber, final boolean userAuthenticated) {
        return new UserSession(accountNumber, userAuthenticated);
    }

    public boolean isUserAuthenticated() {
        return userAuthenticated;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
}
