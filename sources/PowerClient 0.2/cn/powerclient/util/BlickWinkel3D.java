/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.util;

import me.AveReborn.util.Location2D;
import me.AveReborn.util.Location3D;

public class BlickWinkel3D {
    public double yaw;
    public double pitch;

    public BlickWinkel3D(Location3D start, Location3D target) {
        double yawX = new Location2D(start.getX(), start.getZ()).distance(new Location2D(target.getX(), target.getZ()));
        double yawY = target.getY() - start.getY();
        double YawrunterRechnen = new Location2D(0.0, 0.0).distance(new Location2D(yawX, yawY));
        double var10000 = yawX / YawrunterRechnen;
        double yaw = - Math.toDegrees(Math.asin(yawY /= YawrunterRechnen));
        double pitchX = target.getX() - start.getX();
        double pitchY = target.getZ() - start.getZ();
        double PitchrunterRechnen = new Location2D(0.0, 0.0).distance(new Location2D(pitchX, pitchY));
        var10000 = pitchX / PitchrunterRechnen;
        double pitch = Math.toDegrees(Math.asin(pitchY /= PitchrunterRechnen));
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

