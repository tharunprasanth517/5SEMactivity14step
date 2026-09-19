package com.gdb.domain;

import java.util.Properties;

public class AccountRulesEngine {

    private static final Properties SAVINGS_RULES =
            AccountRulesPropertiesLoader.loadProperties("savings");

    public static String getSavingsBucket(int tenureYears) {

        if (tenureYears >= 5) {
            return "privilege";
        }

        if (tenureYears >= 3) {
            return "premium";
        }

        if (tenureYears >= 1) {
            return "standard";
        }

        return "new";
    }

    public static double getSavingsMinBalance(int tenureYears) {

        String bucket = getSavingsBucket(tenureYears);

        String key = "min.balance." + bucket;

        return AccountRulesPropertiesLoader.getDouble(
                SAVINGS_RULES,
                key,
                10000.0
        );
    }

    public static double getSavingsInterestRate(int tenureYears) {

        String bucket = getSavingsBucket(tenureYears);

        String key = "interest.rate." + bucket;

        return AccountRulesPropertiesLoader.getDouble(
                SAVINGS_RULES,
                key,
                2.70
        );
    }
}