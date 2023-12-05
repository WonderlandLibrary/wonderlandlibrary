package koks.api.util;

import java.util.concurrent.ThreadLocalRandom;

public class NumbersUtil {

    /* thx ShiraDev ^^ */

    public double smooth(double max, double min, double time, boolean randomizing, double randomStrength) {
        min += 1;
        double radians = Math.toRadians((System.currentTimeMillis() * time % 360) - 180);
        double base = (Math.tanh(radians) + 1) / 2;
        double delta = max - min;
        delta *= base;
        double value = min + delta;
        if(randomizing)value *= ThreadLocalRandom.current().nextDouble(randomStrength,1);
        return Math.ceil(value *1000) / 1000;
    }
}
