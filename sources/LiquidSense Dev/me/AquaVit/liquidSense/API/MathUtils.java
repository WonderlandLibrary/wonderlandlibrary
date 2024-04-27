package me.AquaVit.liquidSense.API;

import java.util.concurrent.ThreadLocalRandom;

public class MathUtils {
    public static double randomNumber(double max, double min) {
        return Math.random() * (max - min) + min;
    }

}
