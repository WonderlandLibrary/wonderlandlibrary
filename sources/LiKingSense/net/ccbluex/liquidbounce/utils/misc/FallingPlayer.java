/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.utils.misc;

import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import org.jetbrains.annotations.Nullable;

public class FallingPlayer
extends MinecraftInstance {
    private double x;
    private double y;
    private double z;
    private double motionX;
    private double motionY;
    private double motionZ;
    private final float yaw;
    private float strafe;
    private float forward;

    public FallingPlayer(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float strafe, float forward) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.yaw = yaw;
        this.strafe = strafe;
        this.forward = forward;
    }

    private void calculateForTick() {
        this.strafe *= 0.98f;
        this.forward *= 0.98f;
        float v = this.strafe * this.strafe + this.forward * this.forward;
        if (v >= 1.0E-4f) {
            if ((v = (float)Math.sqrt(v)) < 1.0f) {
                v = 1.0f;
            }
            v = mc.getThePlayer().getJumpMovementFactor() / v;
            this.strafe *= v;
            this.forward *= v;
            float f1 = (float)Math.sin(this.yaw * (float)Math.PI / 180.0f);
            float f2 = (float)Math.cos(this.yaw * (float)Math.PI / 180.0f);
            this.motionX += (double)(this.strafe * f2 - this.forward * f1);
            this.motionZ += (double)(this.forward * f2 + this.strafe * f1);
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

    public CollisionResult findCollision(int ticks) {
        for (int i = 0; i < ticks; ++i) {
            WVec3 start = new WVec3(this.x, this.y, this.z);
            this.calculateForTick();
            WVec3 end = new WVec3(this.x, this.y, this.z);
            float w = mc.getThePlayer().getWidth() / 2.0f;
            WBlockPos raytracedBlock = this.rayTrace(start, end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(w, 0.0, w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(-w, 0.0, w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(w, 0.0, -w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(-w, 0.0, -w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(w, 0.0, w / 2.0f), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(-w, 0.0, w / 2.0f), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(w / 2.0f, 0.0, w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.addVector(w / 2.0f, 0.0, -w), end);
            if (raytracedBlock == null) continue;
            return new CollisionResult(raytracedBlock, i);
        }
        return null;
    }

    @Nullable
    private WBlockPos rayTrace(WVec3 start, WVec3 end) {
        IMovingObjectPosition result = mc.getTheWorld().rayTraceBlocks(start, end, true);
        if (result != null && result.getTypeOfHit() == IMovingObjectPosition.WMovingObjectType.BLOCK && result.getSideHit().isUp()) {
            return result.getBlockPos();
        }
        return null;
    }

    public static class CollisionResult {
        private final WBlockPos pos;
        private final int tick;

        public CollisionResult(WBlockPos pos, int tick) {
            this.pos = pos;
            this.tick = tick;
        }

        public WBlockPos getPos() {
            return this.pos;
        }

        public int getTick() {
            return this.tick;
        }
    }
}

