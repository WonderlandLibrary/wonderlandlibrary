/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.MovingObjectPosition$MovingObjectType
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.dev.important.utils.misc;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.dev.important.utils.MinecraftInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0006\n\u0002\b\u0006\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u000f\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004BW\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006\u0012\u0006\u0010\n\u001a\u00020\u0006\u0012\u0006\u0010\u000b\u001a\u00020\u0006\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\r\u0012\u0006\u0010\u000f\u001a\u00020\r\u0012\b\b\u0002\u0010\u0010\u001a\u00020\r\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0013H\u0002J\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0016\u001a\u00020\u0017J\u001a\u0010\u0018\u001a\u0004\u0018\u00010\u00152\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001aH\u0002R\u000e\u0010\u000f\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lnet/dev/important/utils/misc/NewFallingPlayer;", "Lnet/dev/important/utils/MinecraftInstance;", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "(Lnet/minecraft/entity/player/EntityPlayer;)V", "x", "", "y", "z", "motionX", "motionY", "motionZ", "yaw", "", "strafe", "forward", "jumpMovementFactor", "(DDDDDDFFFF)V", "calculateForTick", "", "findCollision", "Lnet/minecraft/util/BlockPos;", "ticks", "", "rayTrace", "start", "Lnet/minecraft/util/Vec3;", "end", "LiquidBounce"})
public final class NewFallingPlayer
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
    private final float jumpMovementFactor;

    public NewFallingPlayer(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float strafe, float forward, float jumpMovementFactor) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        this.yaw = yaw;
        this.strafe = strafe;
        this.forward = forward;
        this.jumpMovementFactor = jumpMovementFactor;
    }

    public /* synthetic */ NewFallingPlayer(double d, double d2, double d3, double d4, double d5, double d6, float f, float f2, float f3, float f4, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 0x200) != 0) {
            f4 = 0.02f;
        }
        this(d, d2, d3, d4, d5, d6, f, f2, f3, f4);
    }

    public NewFallingPlayer(@NotNull EntityPlayer player) {
        Intrinsics.checkNotNullParameter(player, "player");
        this(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70159_w, player.field_70181_x, player.field_70179_y, player.field_70177_z, player.field_70702_br, player.field_70701_bs, player.field_70747_aH);
    }

    private final void calculateForTick() {
        this.strafe *= 0.98f;
        this.forward *= 0.98f;
        float v = this.strafe * this.strafe + this.forward * this.forward;
        if (v >= 1.0E-4f) {
            if ((v = MathHelper.func_76129_c((float)v)) < 1.0f) {
                v = 1.0f;
            }
            float fixedJumpFactor = this.jumpMovementFactor;
            if (MinecraftInstance.mc.field_71439_g.func_70051_ag()) {
                fixedJumpFactor = (float)((double)fixedJumpFactor * 1.3);
            }
            v = fixedJumpFactor / v;
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
        this.motionZ *= 0.91;
        this.x += this.motionX;
        this.y += this.motionY;
        this.z += this.motionZ;
    }

    @Nullable
    public final BlockPos findCollision(int ticks) {
        int n = 0;
        while (n < ticks) {
            BlockPos blockPos;
            int i = n++;
            Vec3 start = new Vec3(this.x, this.y, this.z);
            this.calculateForTick();
            Vec3 end = new Vec3(this.x, this.y, this.z);
            BlockPos raytracedBlock = null;
            float w = MinecraftInstance.mc.field_71439_g.field_70130_N / 2.0f;
            BlockPos it = blockPos = this.rayTrace(start, end);
            boolean bl = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c((double)w, 0.0, (double)w);
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(w.toDouble(), 0.0, w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl2 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c(-((double)w), 0.0, (double)w);
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(-w.toDouble(), 0.0, w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl3 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c((double)w, 0.0, -((double)w));
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(w.toDouble(), 0.0, -w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl4 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c(-((double)w), 0.0, -((double)w));
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(-w.toDouble(), 0.0, -w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl5 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c((double)w, 0.0, (double)(w / 2.0f));
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(w.toDoub\u20260.0, (w / 2f).toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl6 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c(-((double)w), 0.0, (double)(w / 2.0f));
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector(-w.toDou\u20260.0, (w / 2f).toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl7 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c((double)(w / 2.0f), 0.0, (double)w);
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector((w / 2f)\u2026ble(), 0.0, w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl8 = false;
            raytracedBlock = it;
            if (blockPos != null) {
                return raytracedBlock;
            }
            blockPos = start.func_72441_c((double)(w / 2.0f), 0.0, -((double)w));
            Intrinsics.checkNotNullExpressionValue(blockPos, "start.addVector((w / 2f)\u2026le(), 0.0, -w.toDouble())");
            it = blockPos = this.rayTrace((Vec3)blockPos, end);
            boolean bl9 = false;
            raytracedBlock = it;
            if (blockPos == null) continue;
            return raytracedBlock;
        }
        return null;
    }

    private final BlockPos rayTrace(Vec3 start, Vec3 end) {
        MovingObjectPosition result = MinecraftInstance.mc.field_71441_e.func_72901_a(start, end, true);
        return result != null && result.field_72313_a == MovingObjectPosition.MovingObjectType.BLOCK && result.field_178784_b == EnumFacing.UP ? result.func_178782_a() : null;
    }
}

