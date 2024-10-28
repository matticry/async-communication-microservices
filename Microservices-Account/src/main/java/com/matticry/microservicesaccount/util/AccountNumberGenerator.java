package com.matticry.microservicesaccount.util;

import java.util.Random;

public class AccountNumberGenerator {

    public static String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
