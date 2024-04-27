package me.aquavit.liquidsense.utils.data;

public class Ranges {

    public static float coerceIn(float now, float min, float max) {

        if (now < min) return min;
        if (now > max) return max;
        return now;
    }

}
