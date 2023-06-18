package com.eofitg.hardcore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.regex.Pattern;

public class MathUtil {

    // Float Number Cleaners
    public static double round_half_up(double number, int scale) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }
    public static double round_down(double number, int scale) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(scale, RoundingMode.DOWN).doubleValue();
    }

    // Digital Checkers
    public static boolean isInt(String s) {
        return s.matches("[0-9]+");
    }
    public static boolean isDouble(String s) {
        Pattern pattern = Pattern.compile("[+-]?\\d+(.\\d+)?");
        return pattern.matcher(s).matches();
    }

    // Random Number Generators
    public static int randomInt(int MIN, int MAX) {
        if(MAX <= MIN) {
            return 0;
        }
        SecureRandom r = null;
        try {
            r = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return r.nextInt(MAX - MIN + 1) + MIN;
    }

}
