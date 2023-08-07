# ms-transaction

## APIS:

### <b>Account transfer is a 2 step process: </b></br>
#### <i><u>1. Initiate transaction: </u></i></br>
    - find sender account by cif, iban and currency
    - check if account is blocked for debit
    - check if account has sufficient balance
    - create draft payment in db
#### <i><u>2. Execute transaction:</u></i></br>
    - Send transaction to queue for processing
    - Update receiver account balance considering current rate
    - Update sender account balance
    - Update transaction status to SUCCESS or REJECT


## HOW TO:

### To test apis:

- Use docker-compose file to start <code>Postgresql</code> and <code>RabbitMQ</code>
  - <code>docker-compose -f default-compose.yaml up</code>
- Run Application