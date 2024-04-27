package me.betterfps;

import me.betterfps.MiscUtils;

public class NewMCMath {
    private static final float[] SIN_TABLE = MiscUtils.INSTANCE.make(new float[65536], e -> {
        for (int i = 0; i < ((float[])e).length; ++i) {
            e[i] = (float)Math.sin((double)i * Math.PI * 2.0 / 65536.0);
        }
    });

    public float sin(float rad) {
        return SIN_TABLE[(int)(rad * 10430.378f) & 0xFFFF];
    }

    public float cos(float rad) {
        return SIN_TABLE[(int)(rad * 10430.378f + 16384.0f) & 0xFFFF];
    }
}
