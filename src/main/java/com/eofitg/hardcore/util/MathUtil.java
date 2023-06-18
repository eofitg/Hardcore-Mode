package com.eofitg.hardcore.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class MathUtil {

    public static double round_half_up(double number, int scale) {
        BigDecimal bd = new BigDecimal(number);
        return bd.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static boolean isDouble(String s) {
        Pattern pattern = Pattern.compile("[+-]?\\d+(.\\d+)?");
        return pattern.matcher(s).matches();
    }

}
