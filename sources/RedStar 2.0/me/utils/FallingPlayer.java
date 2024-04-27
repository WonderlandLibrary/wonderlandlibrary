package me.utils;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.ranges.RangesKt;
import net.ccbluex.liquidbounce.api.minecraft.client.entity.IEntityPlayerSP;
import net.ccbluex.liquidbounce.api.minecraft.client.multiplayer.IWorldClient;
import net.ccbluex.liquidbounce.api.minecraft.util.IMovingObjectPosition;
import net.ccbluex.liquidbounce.api.minecraft.util.WBlockPos;
import net.ccbluex.liquidbounce.api.minecraft.util.WVec3;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000>\n\n\n\u0000\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\u000020B\b0¢BU00\b0\t0\n00\f0\r0\r0\r0\r¢J\b0HJ020J02020HR0\rX¢\n\u0000R0\rX¢\n\u0000R\t0X¢\n\u0000R\n0X¢\n\u0000R0X¢\n\u0000R0\rX¢\n\u0000R0X¢\n\u0000R0X¢\n\u0000R\f0\rX¢\n\u0000R\b0X¢\n\u0000¨"}, d2={"Lme/utils/FallingPlayer;", "Lnet/ccbluex/liquidbounce/utils/MinecraftInstance;", "player", "Lnet/minecraft/entity/player/EntityPlayer;", "(Lnet/minecraft/entity/player/EntityPlayer;)V", "x", "", "y", "z", "motionX", "motionY", "motionZ", "yaw", "", "strafe", "forward", "jumpMovementFactor", "(DDDDDDFFFF)V", "calculateForTick", "", "findCollision", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WBlockPos;", "ticks", "", "rayTrace", "start", "Lnet/ccbluex/liquidbounce/api/minecraft/util/WVec3;", "end", "Pride"})
public final class FallingPlayer
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

    private final void calculateForTick() {
        this.strafe *= 0.98f;
        this.forward *= 0.98f;
        float v = this.strafe * this.strafe + this.forward * this.forward;
        if (v >= 1.0E-4f) {
            if ((v = (float)Math.sqrt(v)) < 1.0f) {
                v = 1.0f;
            }
            float fixedJumpFactor = this.jumpMovementFactor;
            EntityPlayerSP entityPlayerSP = MinecraftInstance.mc2.player;
            Intrinsics.checkExpressionValueIsNotNull(entityPlayerSP, "mc2.player");
            if (entityPlayerSP.func_70051_ag()) {
                fixedJumpFactor = (float)((double)fixedJumpFactor * 1.3);
            }
            v = fixedJumpFactor / v;
            this.strafe *= v;
            this.forward *= v;
            float f1 = MathHelper.sin((float)(this.yaw * (float)Math.PI / 180.0f));
            float f2 = MathHelper.cos((float)(this.yaw * (float)Math.PI / 180.0f));
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

    /*
     * WARNING - void declaration
     */
    @Nullable
    public final WBlockPos findCollision(int ticks) {
        int n = 0;
        int n2 = ticks;
        while (n < n2) {
            void i;
            double z$iv;
            void y$iv;
            void x$iv322;
            Object this_$iv;
            WVec3 start = new WVec3(this.x, this.y, this.z);
            this.calculateForTick();
            WVec3 end = new WVec3(this.x, this.y, this.z);
            Ref.ObjectRef raytracedBlock = new Ref.ObjectRef();
            IEntityPlayerSP iEntityPlayerSP = MinecraftInstance.mc.getThePlayer();
            if (iEntityPlayerSP == null) {
                Intrinsics.throwNpe();
            }
            float w = iEntityPlayerSP.getWidth() / 2.0f;
            Object object = this.rayTrace(start, end);
            boolean bl = false;
            boolean bl2 = false;
            Object it102 = object;
            boolean bl3 = false;
            raytracedBlock.element = it102;
            if (object != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            object = start;
            double d = w;
            double it102 = 0.0;
            double d2 = w;
            FallingPlayer fallingPlayer = this;
            boolean $i$f$addVector = false;
            WVec3 wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv322, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv322 = false;
            bl2 = false;
            WBlockPos it32 = this_$iv;
            boolean bl4 = false;
            raytracedBlock.element = it32;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv322 = -((double)w);
            double it32 = 0.0;
            z$iv = w;
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv322, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv22 = false;
            bl2 = false;
            Object it42 = this_$iv;
            boolean bl5 = false;
            raytracedBlock.element = it42;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv22 = w;
            double it42 = 0.0;
            z$iv = -((double)w);
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv22, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv42 = false;
            bl2 = false;
            Object it52 = this_$iv;
            boolean bl6 = false;
            raytracedBlock.element = it52;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv42 = -((double)w);
            double it52 = 0.0;
            z$iv = -((double)w);
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv42, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv52 = false;
            bl2 = false;
            Object it62 = this_$iv;
            boolean bl7 = false;
            raytracedBlock.element = it62;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv52 = w;
            double it62 = 0.0;
            z$iv = w / 2.0f;
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv52, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv62 = false;
            bl2 = false;
            Object it72 = this_$iv;
            boolean bl8 = false;
            raytracedBlock.element = it72;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv62 = -((double)w);
            double it72 = 0.0;
            z$iv = w / 2.0f;
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv62, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv72 = false;
            bl2 = false;
            Object it82 = this_$iv;
            boolean bl9 = false;
            raytracedBlock.element = it82;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv72 = w / 2.0f;
            double it82 = 0.0;
            z$iv = w;
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv72, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            this_$iv = fallingPlayer.rayTrace(wVec3, end);
            boolean x$iv82 = false;
            bl2 = false;
            Object it92 = this_$iv;
            boolean bl10 = false;
            raytracedBlock.element = it92;
            if (this_$iv != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            this_$iv = start;
            double x$iv82 = w / 2.0f;
            double it92 = 0.0;
            z$iv = -((double)w);
            fallingPlayer = this;
            $i$f$addVector = false;
            wVec3 = new WVec3(((WVec3)this_$iv).getXCoord() + x$iv82, ((WVec3)this_$iv).getYCoord() + y$iv, ((WVec3)this_$iv).getZCoord() + z$iv);
            object = fallingPlayer.rayTrace(wVec3, end);
            bl = false;
            bl2 = false;
            it102 = object;
            boolean bl11 = false;
            raytracedBlock.element = it102;
            if (object != null) {
                return (WBlockPos)raytracedBlock.element;
            }
            ++i;
        }
        return null;
    }

    private final WBlockPos rayTrace(WVec3 start, WVec3 end) {
        IMovingObjectPosition result;
        IWorldClient iWorldClient = MinecraftInstance.mc.getTheWorld();
        if (iWorldClient == null) {
            Intrinsics.throwNpe();
        }
        return (result = iWorldClient.rayTraceBlocks(start, end, true)) != null && result.getTypeOfHit() == RangesKt.rangeTo((Comparable)((Object)IMovingObjectPosition.WMovingObjectType.MISS), (Comparable)((Object)IMovingObjectPosition.WMovingObjectType.BLOCK)) && result.getSideHit() == EnumFacing.UP ? result.getBlockPos() : null;
    }

    public FallingPlayer(double x, double y, double z, double motionX, double motionY, double motionZ, float yaw, float strafe, float forward, float jumpMovementFactor) {
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

    public FallingPlayer(@NotNull EntityPlayer player) {
        Intrinsics.checkParameterIsNotNull(player, "player");
        this(player.field_70165_t, player.field_70163_u, player.field_70161_v, player.field_70159_w, player.field_70181_x, player.field_70179_y, player.field_70177_z, player.field_70702_br, player.field_191988_bg, player.field_70747_aH);
    }
}
