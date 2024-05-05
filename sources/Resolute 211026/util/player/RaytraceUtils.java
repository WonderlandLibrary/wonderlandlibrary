// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.util.player;

import vip.Resolute.util.world.BlockUtils2D;
import vip.Resolute.util.world.BlockUtils;

public class RaytraceUtils
{
    public double yaw;
    public double pitch;
    
    public RaytraceUtils(final BlockUtils start, final BlockUtils target) {
        final double yawX = new BlockUtils2D(start.getX(), start.getZ()).distance(new BlockUtils2D(target.getX(), target.getZ()));
        double yawY = target.getY() - start.getY();
        final double YawrunterRechnen = new BlockUtils2D(0.0, 0.0).distance(new BlockUtils2D(yawX, yawY));
        double var10000 = yawX / YawrunterRechnen;
        yawY /= YawrunterRechnen;
        final double yaw = -Math.toDegrees(Math.asin(yawY));
        final double pitchX = target.getX() - start.getX();
        double pitchY = target.getZ() - start.getZ();
        final double PitchrunterRechnen = new BlockUtils2D(0.0, 0.0).distance(new BlockUtils2D(pitchX, pitchY));
        var10000 = pitchX / PitchrunterRechnen;
        pitchY /= PitchrunterRechnen;
        double pitch = Math.toDegrees(Math.asin(pitchY));
        pitch -= 90.0;
        if (start.getX() > target.getX()) {
            pitch *= -1.0;
        }
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public double getPitch() {
        return this.yaw;
    }
    
    public double getYaw() {
        return this.pitch;
    }
}
