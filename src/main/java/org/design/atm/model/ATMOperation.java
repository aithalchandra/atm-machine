package org.design.atm.model;

public enum ATMOperation {
	BALANCE_INQUIRY(1), WITHDRAWAL(2), DEPOSIT(3), EXIT(4);
	private int operation;
	
	private ATMOperation(int operation) {
		this.operation = operation;
	}
	
    public static ATMOperation from(int operation) {
        for (ATMOperation atmOperation : ATMOperation.values()) {
            if (atmOperation.operation == operation) {
                return atmOperation;
            }
        }
        return null;
    }
}