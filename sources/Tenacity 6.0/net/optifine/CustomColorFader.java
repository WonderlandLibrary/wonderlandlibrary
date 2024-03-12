// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.minecraft.src.Config;
import net.minecraft.util.Vec3;

public class CustomColorFader
{
    private Vec3 color;
    private long timeUpdate;
    
    public CustomColorFader() {
        this.color = null;
        this.timeUpdate = System.currentTimeMillis();
    }
    
    public Vec3 getColor(final double x, final double y, final double z) {
        if (this.color == null) {
            return this.color = new Vec3(x, y, z);
        }
        final long i = System.currentTimeMillis();
        final long j = i - this.timeUpdate;
        if (j == 0L) {
            return this.color;
        }
        this.timeUpdate = i;
        if (Math.abs(x - this.color.xCoord) < 0.004 && Math.abs(y - this.color.yCoord) < 0.004 && Math.abs(z - this.color.zCoord) < 0.004) {
            return this.color;
        }
        double d0 = j * 0.001;
        d0 = Config.limit(d0, 0.0, 1.0);
        final double d2 = x - this.color.xCoord;
        final double d3 = y - this.color.yCoord;
        final double d4 = z - this.color.zCoord;
        final double d5 = this.color.xCoord + d2 * d0;
        final double d6 = this.color.yCoord + d3 * d0;
        final double d7 = this.color.zCoord + d4 * d0;
        return this.color = new Vec3(d5, d6, d7);
    }
}
