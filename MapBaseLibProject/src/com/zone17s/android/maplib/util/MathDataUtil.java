package com.zone17s.android.maplib.util;

import java.math.BigDecimal;
import java.util.Random;

public class MathDataUtil {

    /**
     * get random double value
     * 
     * @return
     */
    public static double getRandomDou(double min, double max, int digitsNumber) {
        Random r = new Random();
        double randomLimit = max - min;
        double dou = r.nextDouble() * randomLimit + min;
        BigDecimal bg = new BigDecimal(dou);
        dou = bg.setScale(digitsNumber, BigDecimal.ROUND_DOWN).doubleValue();
        return dou;
    }
}
