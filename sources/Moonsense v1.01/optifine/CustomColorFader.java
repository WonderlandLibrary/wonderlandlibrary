// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

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
        final long timeNow = System.currentTimeMillis();
        final long timeDiff = timeNow - this.timeUpdate;
        if (timeDiff == 0L) {
            return this.color;
        }
        this.timeUpdate = timeNow;
        if (Math.abs(x - this.color.xCoord) < 0.004 && Math.abs(y - this.color.yCoord) < 0.004 && Math.abs(z - this.color.zCoord) < 0.004) {
            return this.color;
        }
        double k = timeDiff * 0.001;
        k = Config.limit(k, 0.0, 1.0);
        final double dx = x - this.color.xCoord;
        final double dy = y - this.color.yCoord;
        final double dz = z - this.color.zCoord;
        final double xn = this.color.xCoord + dx * k;
        final double yn = this.color.yCoord + dy * k;
        final double zn = this.color.zCoord + dz * k;
        return this.color = new Vec3(xn, yn, zn);
    }
}
