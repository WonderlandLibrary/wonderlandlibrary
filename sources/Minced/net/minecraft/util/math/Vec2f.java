// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util.math;

public class Vec2f
{
    public static final Vec2f ZERO;
    public static final Vec2f ONE;
    public static final Vec2f UNIT_X;
    public static final Vec2f NEGATIVE_UNIT_X;
    public static final Vec2f UNIT_Y;
    public static final Vec2f NEGATIVE_UNIT_Y;
    public static final Vec2f MAX;
    public static final Vec2f MIN;
    public final float x;
    public final float y;
    
    public Vec2f(final float xIn, final float yIn) {
        this.x = xIn;
        this.y = yIn;
    }
    
    static {
        ZERO = new Vec2f(0.0f, 0.0f);
        ONE = new Vec2f(1.0f, 1.0f);
        UNIT_X = new Vec2f(1.0f, 0.0f);
        NEGATIVE_UNIT_X = new Vec2f(-1.0f, 0.0f);
        UNIT_Y = new Vec2f(0.0f, 1.0f);
        NEGATIVE_UNIT_Y = new Vec2f(0.0f, -1.0f);
        MAX = new Vec2f(Float.MAX_VALUE, Float.MAX_VALUE);
        MIN = new Vec2f(Float.MIN_VALUE, Float.MIN_VALUE);
    }
}
