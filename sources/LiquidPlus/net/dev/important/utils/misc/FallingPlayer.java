/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.misc;

import net.dev.important.utils.MinecraftInstance;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
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
            if ((v = MathHelper.func_76129_c((float)v)) < 1.0f) {
                v = 1.0f;
            }
            v = FallingPlayer.mc.field_71439_g.field_70747_aH / v;
            this.strafe *= v;
            this.forward *= v;
            float f1 = MathHelper.func_76126_a((float)(this.yaw * (float)Math.PI / 180.0f));
            float f2 = MathHelper.func_76134_b((float)(this.yaw * (float)Math.PI / 180.0f));
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
            Vec3 start = new Vec3(this.x, this.y, this.z);
            this.calculateForTick();
            Vec3 end = new Vec3(this.x, this.y, this.z);
            float w = FallingPlayer.mc.field_71439_g.field_70130_N / 2.0f;
            BlockPos raytracedBlock = this.rayTrace(start, end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)w, 0.0, (double)w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)(-w), 0.0, (double)w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)w, 0.0, (double)(-w)), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)(-w), 0.0, (double)(-w)), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)w, 0.0, (double)(w / 2.0f)), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)(-w), 0.0, (double)(w / 2.0f)), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)(w / 2.0f), 0.0, (double)w), end);
            if (raytracedBlock != null) {
                return new CollisionResult(raytracedBlock, i);
            }
            raytracedBlock = this.rayTrace(start.func_72441_c((double)(w / 2.0f), 0.0, (double)(-w)), end);
            if (raytracedBlock == null) continue;
            return new CollisionResult(raytracedBlock, i);
        }
        return null;
    }

    @Nullable
    private BlockPos rayTrace(Vec3 start, Vec3 end) {
        MovingObjectPosition result = FallingPlayer.mc.field_71441_e.func_72901_a(start, end, true);
        if (result != null && result.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && result.field_178784_b == EnumFacing.UP) {
            return result.func_178782_a();
        }
        return null;
    }

    public static class CollisionResult {
        private final BlockPos pos;
        private final int tick;

        public CollisionResult(BlockPos pos, int tick) {
            this.pos = pos;
            this.tick = tick;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public int getTick() {
            return this.tick;
        }
    }
}

