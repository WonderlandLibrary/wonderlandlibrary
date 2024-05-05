package com.initial.utils.astolfo;

import java.awt.*;

public class AstolfoUtils
{
    public static int rainbow(final int count, final float bright, final float st) {
        double v1 = Math.ceil((double)(System.currentTimeMillis() + count * 109)) / 5.0;
        return Color.getHSBColor(((float)((v1 %= 360.0) / 360.0) < 0.5) ? (-(float)(v1 / 360.0)) : ((float)(v1 / 360.0)), st, bright).getRGB();
    }
}
