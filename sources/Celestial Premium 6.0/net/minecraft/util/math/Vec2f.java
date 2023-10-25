/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.util.math;

public class Vec2f {
    public static final Vec2f ZERO = new Vec2f(0.0f, 0.0f);
    public static final Vec2f ONE = new Vec2f(1.0f, 1.0f);
    public static final Vec2f UNIT_X = new Vec2f(1.0f, 0.0f);
    public static final Vec2f NEGATIVE_UNIT_X = new Vec2f(-1.0f, 0.0f);
    public static final Vec2f UNIT_Y = new Vec2f(0.0f, 1.0f);
    public static final Vec2f NEGATIVE_UNIT_Y = new Vec2f(0.0f, -1.0f);
    public static final Vec2f MAX = new Vec2f(Float.MAX_VALUE, Float.MAX_VALUE);
    public static final Vec2f MIN = new Vec2f(Float.MIN_VALUE, Float.MIN_VALUE);
    public final float x;
    public final float y;

    public Vec2f(float xIn, float yIn) {
        this.x = xIn;
        this.y = yIn;
    }
}

