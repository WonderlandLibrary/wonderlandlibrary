/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.client.Minecraft
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Random;
import java.util.UUID;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.fun.NoPitchLimit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={Entity.class})
public abstract class MixinEntity {
    @Shadow
    public double field_70165_t;
    @Shadow
    public double field_70163_u;
    @Shadow
    public double field_70161_v;
    @Shadow
    public float field_70125_A;
    @Shadow
    public float field_70177_z;
    @Shadow
    public Entity field_70154_o;
    @Shadow
    public double field_70159_w;
    @Shadow
    public double field_70181_x;
    @Shadow
    public double field_70179_y;
    @Shadow
    public boolean field_70122_E;
    @Shadow
    public boolean field_70160_al;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    public World field_70170_p;
    @Shadow
    public boolean field_70134_J;
    @Shadow
    public float field_70138_W;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public float field_82151_R;
    @Shadow
    protected Random field_70146_Z;
    @Shadow
    public int field_70174_ab;
    @Shadow
    protected boolean field_71087_bX;
    @Shadow
    public int field_71088_bW;
    @Shadow
    public float field_70130_N;
    @Shadow
    private int field_70150_b;
    @Shadow
    private int field_70151_c;
    @Shadow
    public float field_70127_C;
    @Shadow
    public float field_70126_B;

    @Shadow
    public abstract boolean func_70051_ag();

    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();

    @Shadow
    public void func_70091_d(double x, double y, double z) {
    }

    @Shadow
    public abstract boolean func_70090_H();

    @Shadow
    public abstract boolean func_70115_ae();

    @Shadow
    public abstract void func_70015_d(int var1);

    @Shadow
    protected abstract void func_70081_e(int var1);

    @Shadow
    public abstract boolean func_70026_G();

    @Shadow
    public abstract void func_85029_a(CrashReportCategory var1);

    @Shadow
    protected abstract void func_145775_I();

    @Shadow
    protected abstract void func_180429_a(BlockPos var1, Block var2);

    @Shadow
    public abstract void func_174826_a(AxisAlignedBB var1);

    @Shadow
    protected abstract Vec3 func_174806_f(float var1, float var2);

    @Shadow
    public abstract UUID func_110124_au();

    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    public abstract boolean func_70055_a(Material var1);

    public int getNextStepDistance() {
        return this.field_70150_b;
    }

    public void setNextStepDistance(int nextStepDistance) {
        this.field_70150_b = nextStepDistance;
    }

    public int getFire() {
        return this.field_70151_c;
    }

    @Inject(method={"getCollisionBorderSize"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCollisionBorderSize(CallbackInfoReturnable<Float> callbackInfoReturnable) {
        HitBox hitBox = (HitBox)LiquidBounce.moduleManager.getModule(HitBox.class);
        if (hitBox.getState()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf(0.1f + ((Float)hitBox.getSizeValue().get()).floatValue()));
        }
    }

    @Inject(method={"setAngles"}, at={@At(value="HEAD")}, cancellable=true)
    private void setAngles(float yaw, float pitch, CallbackInfo callbackInfo) {
        if (LiquidBounce.moduleManager.getModule(NoPitchLimit.class).getState()) {
            callbackInfo.cancel();
            float f = this.field_70125_A;
            float f1 = this.field_70177_z;
            this.field_70177_z = (float)((double)this.field_70177_z + (double)yaw * 0.15);
            this.field_70125_A = (float)((double)this.field_70125_A - (double)pitch * 0.15);
            this.field_70127_C += this.field_70125_A - f;
            this.field_70126_B += this.field_70177_z - f1;
        }
    }

    @Inject(method={"moveFlying"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleRotations(float strafe, float forward, float friction, CallbackInfo callbackInfo) {
        if ((Entity)this != Minecraft.func_71410_x().field_71439_g) {
            return;
        }
        StrafeEvent strafeEvent = new StrafeEvent(strafe, forward, friction);
        LiquidBounce.eventManager.callEvent(strafeEvent);
        if (strafeEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}

