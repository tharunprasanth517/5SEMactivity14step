package com.gdb.service;

import com.gdb.domain.Account;
import com.gdb.domain.AccountRulesEngine;
import com.gdb.exceptions.*;

public class TransferService {

    public void transfer(
            Account sender,
            Account receiver,
            double amount,
            int pin)
            throws AccountException {

        // 1. Check sender and receiver
        if (sender == null) {
            throw new AccountException(
                    "Sender account cannot be null"
            );
        }

        if (receiver == null) {
            throw new AccountException(
                    "Receiver account cannot be null"
            );
        }

        // 2. Both accounts must be active
        if (!sender.isActive()) {
            throw new InactiveAccountException(
                    "Sender account is inactive."
            );
        }

        if (!receiver.isActive()) {
            throw new InactiveAccountException(
                    "Receiver account is inactive."
            );
        }

        // 3. Validate sender PIN
        if (!sender.hasPin()) {
            throw new InvalidPinException(
                    "PIN not set for sender account"
            );
        }

        if (!sender.verifyPin(pin)) {
            throw new InvalidPinException(
                    "Incorrect PIN"
            );
        }

        // 4. Validate amount
        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Transfer amount must be positive. Provided: Rs. "
                            + amount
            );
        }

        // 5. Check available balance and minimum balance
        double minimumBalance =
                AccountRulesEngine.getInstance()
                        .getMinimumBalance(
                                sender.getAccountType(),
                                sender.getTenureYears()
                        );

        if (sender.getBalance() - amount < minimumBalance) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Minimum balance of Rs. "
                            + minimumBalance
                            + " must be maintained."
            );
        }

        // 6. Check daily transfer limit
        if (!sender.canTransfer(amount)) {

            double remaining =
                    sender.getRemainingDailyTransferLimit();

            throw new AccountException(
                    "Daily transfer limit exceeded. "
                            + "Remaining limit: Rs. "
                            + remaining
            );
        }

        // 7. Debit sender
        sender.withdraw(amount, pin);

        // 8. Credit receiver
        receiver.deposit(amount);

        // 9. Record completed transfer
        sender.updateDailyTransferTotal(amount);
    }
}
