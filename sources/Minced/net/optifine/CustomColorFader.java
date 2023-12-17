// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import net.minecraft.util.math.Vec3d;

public class CustomColorFader
{
    private Vec3d color;
    private long timeUpdate;
    
    public CustomColorFader() {
        this.color = null;
        this.timeUpdate = System.currentTimeMillis();
    }
    
    public Vec3d getColor(final double x, final double y, final double z) {
        if (this.color == null) {
            return this.color = new Vec3d(x, y, z);
        }
        final long i = System.currentTimeMillis();
        final long j = i - this.timeUpdate;
        if (j == 0L) {
            return this.color;
        }
        this.timeUpdate = i;
        if (Math.abs(x - this.color.x) < 0.004 && Math.abs(y - this.color.y) < 0.004 && Math.abs(z - this.color.z) < 0.004) {
            return this.color;
        }
        double d0 = j * 0.001;
        d0 = Config.limit(d0, 0.0, 1.0);
        final double d2 = x - this.color.x;
        final double d3 = y - this.color.y;
        final double d4 = z - this.color.z;
        final double d5 = this.color.x + d2 * d0;
        final double d6 = this.color.y + d3 * d0;
        final double d7 = this.color.z + d4 * d0;
        return this.color = new Vec3d(d5, d6, d7);
    }
}
