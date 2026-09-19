package com.gdb.tests;

import com.gdb.domain.*;
import com.gdb.service.TransferService;
import com.gdb.exceptions.*;

public class TestTransfer {
    public static void main(String[] args) throws Exception {

        System.out.println("=".repeat(60));
        System.out.println("  ACTIVITY 15 — TRANSFER WITH DAILY LIMITS");
        System.out.println("=".repeat(60));

        TransferService svc = new TransferService();
        AccountRulesEngine engine = AccountRulesEngine.getInstance();

        // ============================================================
        // 📝 STEP 9: Create Two Accounts And Set PIN
        // ============================================================

        Account acc1 = (Account) AccountFactory.createAccount(
                "SAVINGS",
                1001,
                "Rajesh Sharma",
                30,
                100000
        );

        Account acc2 = (Account) AccountFactory.createAccount(
                "SAVINGS",
                1002,
                "Priya Patel",
                28,
                20000
        );

        acc1.setPin(1234);

        // ============================================================
        // 📝 STEP 10: Successful Transfer
        // ============================================================

        svc.transfer(acc1, acc2, 5000, 1234);

        System.out.println(
                "Transfer Rs. 5,000: SUCCESS"
                + " | acc1 = Rs. " + acc1.getBalance()
                + " | acc2 = Rs. " + acc2.getBalance()
        );

        // ============================================================
        // 📝 STEP 11: Insufficient Balance
        // ============================================================

        try {

            svc.transfer(
                    acc1,
                    acc2,
                    1_00_000,
                    1234
            );

        } catch (InsufficientBalanceException e) {

            System.out.println(
                    "Insufficient Balance: " + e.getMessage()
            );
        }

        // ============================================================
        // 📝 STEP 12: Daily Limit Breach
        // ============================================================

        System.out.println(
                "Daily Transfer Limit: Rs. "
                + acc1.getDailyTransferLimit()
        );

        try {

            while (true) {

                svc.transfer(
                        acc1,
                        acc2,
                        20_000,
                        1234
                );

                System.out.println(
                        "Transfer Rs. 20,000: SUCCESS"
                        + " | acc1 = Rs. "
                        + acc1.getBalance()
                        + " | acc2 = Rs. "
                        + acc2.getBalance()
                );
            }

        } catch (AccountException e) {

            System.out.println(
                    "Daily Limit Breached: "
                    + e.getMessage()
            );
        }

        // ============================================================
        // 📝 STEP 13: Print Remaining Limit
        // ============================================================

        System.out.println(
                "Daily Transfer Total Used: Rs. "
                + acc1.getDailyTransferTotal()
        );

        System.out.println(
                "Remaining Daily Transfer Limit: Rs. "
                + acc1.getRemainingDailyTransferLimit()
        );
    }
}