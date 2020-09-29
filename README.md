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
* Currenyly supported single denomition of 100$ notes
