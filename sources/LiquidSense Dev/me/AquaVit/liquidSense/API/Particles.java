package me.AquaVit.liquidSense.API;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Particles {
    public int ticks;
    public Location location;
    public String text;

    public Particles(final Location location, final String text) {
        this.location = location;
        this.text = text;
        this.ticks = 0;
    }
    public static double roundToPlace(final float value, final int places) {
        if (places < 0) {
            return value;
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
