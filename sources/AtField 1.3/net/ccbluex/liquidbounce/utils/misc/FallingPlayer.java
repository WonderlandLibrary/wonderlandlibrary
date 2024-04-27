/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public class FallingPlayer
extends MinecraftInstance {
    private final float yaw;
    private float forward;
    private double z;
    private double motionZ;
    private double motionY;
    private double motionX;
    private double x;
    private float strafe;
    private double y;

    public FallingPlayer(double d, double d2, double d3, double d4, double d5, double d6, float f, float f2, float f3) {
        this.x = d;
        this.y = d2;
        this.z = d3;
        this.motionX = d4;
        this.motionY = d5;
        this.motionZ = d6;
        this.yaw = f;
        this.strafe = f2;
        this.forward = f3;
    }

    private void calculateForTick() {
        this.strafe *= 0.98f;
        this.forward *= 0.98f;
        float f = this.strafe * this.strafe + this.forward * this.forward;
        if (f >= 1.0E-4f) {
            if ((f = (float)Math.sqrt(f)) < 1.0f) {
                f = 1.0f;
            }
            f = mc.getThePlayer().getJumpMovementFactor() / f;
            this.strafe *= f;
            this.forward *= f;
            float f2 = (float)Math.sin(this.yaw * (float)Math.PI / 180.0f);
            float f3 = (float)Math.cos(this.yaw * (float)Math.PI / 180.0f);
            this.motionX += (double)(this.strafe * f3 - this.forward * f2);
            this.motionZ += (double)(this.forward * f3 + this.strafe * f2);
        }
        this.motionY -= 0.08;
        this.motionX *= 0.91;
        this.motionY *= (double)0.98f;
        this.motionY *= 0.91;
        this.motionZ *= 0.91;
        this.x += this.motionX;
        this.y += this.motionY;
        this.z += this.motionZ;
    }

    private WBlockPos rayTrace(WVec3 wVec3, WVec3 wVec32) {
        IMovingObjectPosition iMovingObjectPosition = mc.getTheWorld().rayTraceBlocks(wVec3, wVec32, true);
        if (iMovingObjectPosition != null && iMovingObjectPosition.getTypeOfHit() == IMovingObjectPosition.WMovingObjectType.BLOCK && iMovingObjectPosition.getSideHit().isUp()) {
            return iMovingObjectPosition.getBlockPos();
        }
        return null;
    }

    public CollisionResult findCollision(int n) {
        for (int i = 0; i < n; ++i) {
            WVec3 wVec3 = new WVec3(this.x, this.y, this.z);
            this.calculateForTick();
            WVec3 wVec32 = new WVec3(this.x, this.y, this.z);
            float f = mc.getThePlayer().getWidth() / 2.0f;
            WBlockPos wBlockPos = this.rayTrace(wVec3, wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(f, 0.0, f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(-f, 0.0, f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(f, 0.0, -f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(-f, 0.0, -f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(f, 0.0, f / 2.0f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(-f, 0.0, f / 2.0f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(f / 2.0f, 0.0, f), wVec32);
            if (wBlockPos != null) {
                return new CollisionResult(wBlockPos, i);
            }
            wBlockPos = this.rayTrace(wVec3.addVector(f / 2.0f, 0.0, -f), wVec32);
            if (wBlockPos == null) continue;
            return new CollisionResult(wBlockPos, i);
        }
        return null;
    }

    public static class CollisionResult {
        private final int tick;
        private final WBlockPos pos;

        public int getTick() {
            return this.tick;
        }

        public CollisionResult(WBlockPos wBlockPos, int n) {
            this.pos = wBlockPos;
            this.tick = n;
        }

        public WBlockPos getPos() {
            return this.pos;
        }
    }
}

