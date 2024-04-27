/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package net.dev.important.injection.forge.mixins.entity;

import net.dev.important.Client;
import net.dev.important.event.JumpEvent;
import net.dev.important.injection.forge.mixins.entity.MixinEntity;
import net.dev.important.modules.module.modules.movement.AirJump;
import net.dev.important.modules.module.modules.movement.LiquidWalk;
import net.dev.important.modules.module.modules.movement.NoJumpDelay;
import net.dev.important.modules.module.modules.render.Animations;
import net.dev.important.modules.module.modules.render.AntiBlind;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
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
    public int field_110158_av;
    @Shadow
    public boolean field_82175_bq;
    @Shadow
    public float field_70733_aJ;

    @Shadow
    protected abstract float func_175134_bD();

    @Shadow
    public abstract PotionEffect func_70660_b(Potion var1);

    @Shadow
    public abstract boolean func_70644_a(Potion var1);

    @Shadow
    public void func_70636_d() {
    }

    @Shadow
    protected abstract void func_180433_a(double var1, boolean var3, Block var4, BlockPos var5);

    @Shadow
    public abstract float func_110143_aJ();

    @Shadow
    public abstract ItemStack func_70694_bm();

    @Shadow
    protected abstract void func_70629_bd();

    @Overwrite
    protected void func_70664_aZ() {
        JumpEvent jumpEvent = new JumpEvent(this.func_175134_bD());
        Client.eventManager.callEvent(jumpEvent);
        if (jumpEvent.isCancelled()) {
            return;
        }
        this.field_70181_x = jumpEvent.getMotion();
        if (this.func_70644_a(Potion.field_76430_j)) {
            this.field_70181_x += (double)((float)(this.func_70660_b(Potion.field_76430_j).func_76458_c() + 1) * 0.1f);
        }
        if (this.func_70051_ag()) {
            float f = this.field_70177_z * ((float)Math.PI / 180);
            this.field_70159_w -= (double)(MathHelper.func_76126_a((float)f) * 0.2f);
            this.field_70179_y += (double)(MathHelper.func_76134_b((float)f) * 0.2f);
        }
        this.field_70160_al = true;
    }

    @Inject(method={"onLivingUpdate"}, at={@At(value="HEAD")})
    private void headLiving(CallbackInfo callbackInfo) {
        if (Client.moduleManager.getModule(NoJumpDelay.class).getState()) {
            this.field_70773_bE = 0;
        }
    }

    @Inject(method={"onLivingUpdate"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/EntityLivingBase;isJumping:Z", ordinal=1)})
    private void onJumpSection(CallbackInfo callbackInfo) {
        LiquidWalk liquidWalk;
        if (Client.moduleManager.getModule(AirJump.class).getState() && this.field_70703_bu && this.field_70773_bE == 0) {
            this.func_70664_aZ();
            this.field_70773_bE = 10;
        }
        if ((liquidWalk = (LiquidWalk)Client.moduleManager.getModule(LiquidWalk.class)).getState() && !this.field_70703_bu && !this.func_70093_af() && this.func_70090_H() && ((String)liquidWalk.modeValue.get()).equalsIgnoreCase("Swim")) {
            this.func_70629_bd();
        }
    }

    @Inject(method={"getLook"}, at={@At(value="HEAD")}, cancellable=true)
    private void getLook(CallbackInfoReturnable<Vec3> callbackInfoReturnable) {
        if ((EntityLivingBase)this instanceof EntityPlayerSP) {
            callbackInfoReturnable.setReturnValue(this.func_174806_f(this.field_70125_A, this.field_70177_z));
        }
    }

    @Inject(method={"isPotionActive(Lnet/minecraft/potion/Potion;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void isPotionActive(Potion p_isPotionActive_1_, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        AntiBlind antiBlind = (AntiBlind)Client.moduleManager.getModule(AntiBlind.class);
        if ((p_isPotionActive_1_ == Potion.field_76431_k || p_isPotionActive_1_ == Potion.field_76440_q) && antiBlind.getState() && ((Boolean)antiBlind.getConfusionEffect().get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Overwrite
    private int func_82166_i() {
        int speed;
        int n = speed = Client.moduleManager.getModule(Animations.class).getState() ? 2 + (20 - (Integer)Animations.SpeedSwing.get()) : 6;
        return this.func_70644_a(Potion.field_76422_e) ? speed - (1 + this.func_70660_b(Potion.field_76422_e).func_76458_c()) * 1 : (this.func_70644_a(Potion.field_76419_f) ? speed + (1 + this.func_70660_b(Potion.field_76419_f).func_76458_c()) * 2 : speed);
    }
}

