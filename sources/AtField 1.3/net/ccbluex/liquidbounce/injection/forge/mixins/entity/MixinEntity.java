/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.util.SoundEvent
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.HitBox;
import net.ccbluex.liquidbounce.features.module.modules.exploit.NoPitchLimit;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
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
    public World field_70170_p;
    @Shadow
    protected Random field_70146_Z;
    @Shadow
    public float field_70138_W;
    @Shadow
    public float field_70126_B;
    @Shadow
    protected boolean field_71087_bX;
    @Shadow
    public float field_82151_R;
    @Shadow
    public float field_70125_A;
    @Shadow
    public float field_70177_z;
    @Shadow
    public boolean field_70160_al;
    @Shadow
    public float field_191959_ay;
    @Shadow
    public int field_70150_b;
    @Shadow
    public float field_70130_N;
    @Shadow
    public boolean field_70134_J;
    @Shadow
    public double field_70179_y;
    @Shadow
    public int field_71088_bW;
    @Shadow
    public int field_190534_ay;
    @Shadow
    public boolean field_70145_X;
    @Shadow
    @Final
    public double[] field_191505_aI;
    @Shadow
    public double field_70165_t;
    @Shadow
    public Entity field_184239_as;
    @Shadow
    public double field_70163_u;
    @Shadow
    public long field_191506_aJ;
    @Shadow
    public boolean field_70123_F;
    @Shadow
    public double field_70181_x;
    @Shadow
    public boolean field_70124_G;
    @Shadow
    public float field_70127_C;
    @Shadow
    public boolean field_70132_H;
    @Shadow
    public double field_70159_w;
    @Shadow
    public float field_70140_Q;
    @Shadow
    public double field_70161_v;
    @Shadow
    public boolean field_70122_E;

    @Shadow
    @Nullable
    public abstract Entity func_184187_bx();

    @Shadow
    public abstract boolean func_184207_aI();

    @Shadow
    public abstract boolean func_70027_ad();

    @Shadow
    protected abstract SoundEvent func_184184_Z();

    public int getFire() {
        return this.field_190534_ay;
    }

    @Shadow
    public abstract boolean func_70051_ag();

    @Shadow
    public abstract boolean func_70026_G();

    @Shadow
    public abstract void func_174826_a(AxisAlignedBB var1);

    @Inject(method={"turn"}, at={@At(value="HEAD")}, cancellable=true)
    private void setAngles(float f, float f2, CallbackInfo callbackInfo) {
        if (Objects.requireNonNull(LiquidBounce.moduleManager.getModule(NoPitchLimit.class)).getState()) {
            callbackInfo.cancel();
            float f3 = this.field_70125_A;
            float f4 = this.field_70177_z;
            this.field_70177_z = (float)((double)this.field_70177_z + (double)f * 0.15);
            this.field_70125_A = (float)((double)this.field_70125_A - (double)f2 * 0.15);
            this.field_70127_C += this.field_70125_A - f3;
            this.field_70126_B += this.field_70177_z - f4;
        }
    }

    @Shadow
    public abstract void func_174829_m();

    @Shadow
    public abstract boolean func_70093_af();

    @Shadow
    protected abstract void func_145775_I();

    @Shadow
    protected abstract float func_191954_d(float var1);

    @Shadow
    protected abstract void func_184231_a(double var1, boolean var3, IBlockState var4, BlockPos var5);

    @Shadow
    public abstract boolean func_70055_a(Material var1);

    public void setNextStepDistance(int n) {
        this.field_70150_b = n;
    }

    @Shadow
    public abstract void func_85029_a(CrashReportCategory var1);

    @Shadow
    protected abstract boolean func_70041_e_();

    @Shadow
    public abstract AxisAlignedBB func_174813_aQ();

    @Inject(method={"getCollisionBorderSize"}, at={@At(value="HEAD")}, cancellable=true)
    private void getCollisionBorderSize(CallbackInfoReturnable callbackInfoReturnable) {
        HitBox hitBox = (HitBox)LiquidBounce.moduleManager.getModule(HitBox.class);
        if (Objects.requireNonNull(hitBox).getState()) {
            callbackInfoReturnable.setReturnValue((Object)Float.valueOf(0.1f + ((Float)hitBox.getSizeValue().get()).floatValue()));
        }
    }

    @Shadow
    public void func_70091_d(MoverType moverType, double d, double d2, double d3) {
    }

    @Shadow
    protected abstract boolean func_191957_ae();

    @Shadow
    public abstract boolean func_70090_H();

    @Shadow
    protected abstract Vec3d func_174806_f(float var1, float var2);

    @Shadow
    protected abstract void func_70081_e(int var1);

    @Shadow
    public abstract void func_70015_d(int var1);

    @Shadow
    @Nullable
    public abstract Entity func_184179_bs();

    public int getNextStepDistance() {
        return this.field_70150_b;
    }

    @Shadow
    public abstract boolean func_184218_aH();

    @Shadow
    protected abstract void func_180429_a(BlockPos var1, Block var2);

    @Shadow
    protected abstract int func_190531_bD();

    @Shadow
    public abstract UUID func_110124_au();

    @Shadow
    public abstract void func_184185_a(SoundEvent var1, float var2, float var3);
}

