/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.init.MobEffects
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.common.ForgeHooks
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.entity;

import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.JumpEvent;
import net.ccbluex.liquidbounce.event.StrafeEvent;
import net.ccbluex.liquidbounce.features.module.modules.movement.LiquidWalk;
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.injection.forge.mixins.entity.MixinEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase
extends MixinEntity {
    @Shadow
    private int field_70773_bE;
    @Shadow
    protected boolean field_70703_bu;
    @Shadow
    public int field_184628_bn;

    @Shadow
    public abstract ItemStack func_184607_cu();

    @Shadow
    public abstract float func_110143_aJ();

    @Shadow
    public abstract boolean func_184587_cr();

    @Inject(method={"moveRelative"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleRotations(float f, float f2, float f3, float f4, CallbackInfo callbackInfo) {
        if (this != Minecraft.func_71410_x().field_71439_g) {
            return;
        }
        StrafeEvent strafeEvent = new StrafeEvent(f, f3, f4);
        LiquidBounce.eventManager.callEvent(strafeEvent);
        if (strafeEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Shadow
    public abstract int func_184605_cv();

    @Shadow
    public abstract ItemStack func_184586_b(EnumHand var1);

    @Shadow
    public abstract boolean func_70644_a(Potion var1);

    @Inject(method={"onLivingUpdate"}, at={@At(value="HEAD")})
    private void headLiving(CallbackInfo callbackInfo) {
        if (Objects.requireNonNull(LiquidBounce.moduleManager.getModule(NoJumpDelay.class)).getState()) {
            this.field_70773_bE = 0;
        }
    }

    @Shadow
    public abstract boolean func_184613_cA();

    @Inject(method={"isPotionActive(Lnet/minecraft/potion/Potion;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void isPotionActive(Potion potion, CallbackInfoReturnable callbackInfoReturnable) {
        AntiBlind antiBlind = (AntiBlind)LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if ((potion == MobEffects.field_76431_k || potion == MobEffects.field_76440_q) && Objects.requireNonNull(antiBlind).getState() && ((Boolean)antiBlind.getConfusionEffect().get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    @Shadow
    protected abstract void func_70629_bd();

    @Shadow
    public void func_70636_d() {
    }

    @Shadow
    public abstract PotionEffect func_70660_b(Potion var1);

    @Overwrite
    private int func_82166_i() {
        int n;
        int n2 = n = LiquidBounce.moduleManager.getModule(Animations.class).getState() ? 2 + (20 - (Integer)Animations.SpeedSwing.get()) : 6;
        return this.func_70644_a(MobEffects.field_76424_c) ? n - (1 + this.func_70660_b(MobEffects.field_76424_c).func_76458_c()) : (this.func_70644_a(MobEffects.field_76421_d) ? n + (1 + this.func_70660_b(MobEffects.field_76421_d).func_76458_c()) * 2 : n);
    }

    @Overwrite
    protected void func_70664_aZ() {
        JumpEvent jumpEvent = new JumpEvent(this.func_175134_bD());
        LiquidBounce.eventManager.callEvent(jumpEvent);
        if (jumpEvent.isCancelled()) {
            return;
        }
        this.field_70181_x = jumpEvent.getMotion();
        if (this.func_70644_a(MobEffects.field_76430_j)) {
            this.field_70181_x += (double)((float)(this.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1f);
        }
        if (this.func_70051_ag()) {
            float f = this.field_70177_z * ((float)Math.PI / 180);
            this.field_70159_w -= (double)(MathHelper.func_76126_a((float)f) * 0.2f);
            this.field_70179_y += (double)(MathHelper.func_76134_b((float)f) * 0.2f);
        }
        this.field_70160_al = true;
        ForgeHooks.onLivingJump((EntityLivingBase)((EntityLivingBase)this));
    }

    @Override
    @Shadow
    protected abstract void func_184231_a(double var1, boolean var3, IBlockState var4, BlockPos var5);

    @Shadow
    protected abstract void func_70626_be();

    @Inject(method={"getLook"}, at={@At(value="HEAD")}, cancellable=true)
    private void getLook(CallbackInfoReturnable callbackInfoReturnable) {
        if ((EntityLivingBase)this instanceof EntityPlayerSP) {
            callbackInfoReturnable.setReturnValue((Object)this.func_174806_f(this.field_70125_A, this.field_70177_z));
        }
    }

    @Inject(method={"onLivingUpdate"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal=1)})
    private void onJumpSection(CallbackInfo callbackInfo) {
        LiquidWalk liquidWalk = (LiquidWalk)LiquidBounce.moduleManager.getModule(LiquidWalk.class);
        if (Objects.requireNonNull(liquidWalk).getState() && !this.field_70703_bu && !this.func_70093_af() && this.func_70090_H() && ((String)liquidWalk.getModeValue().get()).equalsIgnoreCase("Swim")) {
            this.func_70629_bd();
        }
    }

    @Shadow
    protected abstract float func_175134_bD();
}

