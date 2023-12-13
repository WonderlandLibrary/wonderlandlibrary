package me.nyan.flush.utils.other;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Timer {
    public long lastMS = System.currentTimeMillis();

    public static boolean isDay() {
        return isDay(8, 18);
    }

    public static boolean isDay(int hourMin, int hourMax) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("H");
        return (Integer.parseInt(simpleDateFormat.format(date)) > hourMin) && (Integer.parseInt(simpleDateFormat.format(date)) < hourMax);
    }

    public void reset() {
        lastMS = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long delay, boolean reset) {
        if (System.currentTimeMillis() - lastMS >= delay) {
            if (reset) {
                reset();
            }
            return true;
        }

        return false;
    }
}
