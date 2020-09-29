# ATM Machine Desing Implementation


## Maven tasks

```sh
$ ./mvn clean install
```

```sh
$ mvn test
```

## Test Setup

Spock framework(Groovy) is used for testing.


### Assumptions
* Ability to capture Card and PIN details and authenticate the user
* User should not be able to withdraw more than 40 notes at a time
* Currently supported multiple currency denomition withdrawals of 50/100/200/500/2000 notes
* Currency load from ATM machine (Admin-part) not addressed but ATM CashDispenser is able to load multiple currency notes
