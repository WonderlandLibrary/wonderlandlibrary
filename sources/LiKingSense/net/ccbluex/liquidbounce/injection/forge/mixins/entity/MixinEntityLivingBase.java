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
 *  net.minecraft.util.math.Vec3d
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
import net.ccbluex.liquidbounce.features.module.modules.movement.NoJumpDelay;
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
import net.minecraft.util.math.Vec3d;
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
    public int field_184628_bn;
    @Shadow
    protected boolean field_70703_bu;
    @Shadow
    private int field_70773_bE;

    @Shadow
    public abstract boolean func_184587_cr();

    @Shadow
    public abstract ItemStack func_184607_cu();

    @Shadow
    protected abstract float func_175134_bD();

    @Shadow
    public abstract PotionEffect func_70660_b(Potion var1);

    @Shadow
    public abstract boolean func_70644_a(Potion var1);

    @Shadow
    public void func_70636_d() {
    }

    @Override
    @Shadow
    protected abstract void func_184231_a(double var1, boolean var3, IBlockState var4, BlockPos var5);

    @Shadow
    public abstract float func_110143_aJ();

    @Shadow
    public abstract ItemStack func_184586_b(EnumHand var1);

    @Shadow
    protected abstract void func_70626_be();

    @Shadow
    protected abstract void func_70629_bd();

    @Shadow
    public abstract boolean func_184613_cA();

    @Shadow
    public abstract int func_184605_cv();

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

    @Inject(method={"onLivingUpdate"}, at={@At(value="HEAD")})
    private void headLiving(CallbackInfo callbackInfo) {
        if (Objects.requireNonNull(LiquidBounce.moduleManager.getModule(NoJumpDelay.class)).getState()) {
            this.field_70773_bE = 0;
        }
    }

    @Inject(method={"onLivingUpdate"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal=1)})
    private void onJumpSection(CallbackInfo callbackInfo) {
        if (!this.field_70703_bu && !this.func_70093_af() && this.func_70090_H()) {
            this.func_70629_bd();
        }
    }

    @Inject(method={"getLook"}, at={@At(value="HEAD")}, cancellable=true)
    private void getLook(CallbackInfoReturnable<Vec3d> callbackInfoReturnable) {
        if ((EntityLivingBase)this instanceof EntityPlayerSP) {
            callbackInfoReturnable.setReturnValue((Object)this.func_174806_f(this.field_70125_A, this.field_70177_z));
        }
    }

    @Inject(method={"isPotionActive(Lnet/minecraft/potion/Potion;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void isPotionActive(Potion p_isPotionActive_1_, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        AntiBlind antiBlind = (AntiBlind)LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if ((p_isPotionActive_1_ == MobEffects.field_76431_k || p_isPotionActive_1_ == MobEffects.field_76440_q) && Objects.requireNonNull(antiBlind).getState() && ((Boolean)antiBlind.getConfusionEffect().get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue((Object)false);
        }
    }

    /*
     * Exception decompiling
     */
    @Overwrite
    private int func_82166_i() {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: While processing lbl10 : ISUB - null : Stack underflow
         *     at org.benf.cfr.reader.bytecode.analysis.stack.StackSim.getChange(StackSim.java:81)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:242)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.populateStackInfo(Op02WithProcessedDataAndRefs.java:1559)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:434)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Inject(method={"moveRelative"}, at={@At(value="HEAD")}, cancellable=true)
    private void handleRotations(float strafe, float up, float forward, float friction, CallbackInfo callbackInfo) {
        if (this != Minecraft.func_71410_x().field_71439_g) {
            return;
        }
        StrafeEvent strafeEvent = new StrafeEvent(strafe, forward, friction);
        LiquidBounce.eventManager.callEvent(strafeEvent);
        if (strafeEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }
}

