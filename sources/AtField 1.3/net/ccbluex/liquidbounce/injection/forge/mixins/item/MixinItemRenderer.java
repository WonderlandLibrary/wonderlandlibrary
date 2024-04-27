/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.ItemRenderer
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.EnumAction
 *  net.minecraft.item.ItemMap
 *  net.minecraft.item.ItemShield
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.ItemSword
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.EnumHandSide
 *  net.minecraft.util.math.MathHelper
 *  net.minecraftforge.client.ForgeHooksClient
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.item;

import java.util.Objects;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura;
import net.ccbluex.liquidbounce.features.module.modules.render.Animations;
import net.ccbluex.liquidbounce.features.module.modules.render.AntiBlind;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={ItemRenderer.class})
public abstract class MixinItemRenderer {
    @Shadow
    private float field_187469_f;
    @Shadow
    private float field_187472_i;
    @Shadow
    private ItemStack field_187468_e;
    @Shadow
    private float field_187470_g;
    float delay = 0.0f;
    @Shadow
    private ItemStack field_187467_d;
    @Shadow
    private float field_187471_h;
    MSTimer rotateTimer = new MSTimer();
    @Shadow
    @Final
    private Minecraft field_78455_a;

    private static void transformSideFirstPersonBlock(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private void Zoom(EnumHandSide enumHandSide, float f, float f2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(f * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
    }

    private static void jello(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -35.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * 20.0f), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private void doItemRenderGLTranslate() {
        GlStateManager.func_179109_b((float)((Float)Animations.xhValue.get()).floatValue(), (float)((Float)Animations.yhValue.get()).floatValue(), (float)((Float)Animations.zhValue.get()).floatValue());
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFireInFirstPerson(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getFireEffect().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    private static void Push(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    @Shadow
    protected abstract void func_187459_b(EnumHandSide var1, float var2);

    private void rotateItemAnim() {
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("RotateY")) {
            GlStateManager.func_179114_b((float)this.delay, (float)0.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("RotateXY")) {
            GlStateManager.func_179114_b((float)this.delay, (float)1.0f, (float)1.0f, (float)0.0f);
        }
        if (((String)Animations.transformFirstPersonRotate.get()).equalsIgnoreCase("Custom")) {
            GlStateManager.func_179114_b((float)this.delay, (float)((Float)Animations.customRotate1.get()).floatValue(), (float)((Float)Animations.customRotate2.get()).floatValue(), (float)((Float)Animations.customRotate3.get()).floatValue());
        }
        if (this.rotateTimer.hasTimePassed(1L)) {
            this.delay += 1.0f;
            this.delay += ((Float)Animations.SpeedRotate.get()).floatValue();
            this.rotateTimer.reset();
        }
        if (this.delay > 360.0f) {
            this.delay = 0.0f;
        }
    }

    private static void WindMill(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -50.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private static void sigmaold(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(f * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -15.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(f4 * -10.0f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f4 * -30.0f), (float)1.3f, (float)0.1f, (float)0.2f);
    }

    @Shadow
    protected abstract void func_187454_a(float var1, EnumHandSide var2, ItemStack var3);

    private void Zoom(float f, float f2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(f * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
    }

    private void avatar(EnumHandSide enumHandSide, float f, float f2) {
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f3 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f4 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f4 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    @Shadow
    protected abstract void func_187463_a(float var1, float var2, float var3);

    @Shadow
    public abstract void func_187462_a(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, boolean var4);

    @Overwrite
    public void func_187457_a(AbstractClientPlayer abstractClientPlayer, float f, float f2, EnumHand enumHand, float f3, ItemStack itemStack, float f4) {
        boolean bl = enumHand == EnumHand.MAIN_HAND;
        EnumHandSide enumHandSide = bl ? abstractClientPlayer.func_184591_cq() : abstractClientPlayer.func_184591_cq().func_188468_a();
        GlStateManager.func_179094_E();
        if (itemStack.func_190926_b()) {
            if (bl && !abstractClientPlayer.func_82150_aj()) {
                this.func_187456_a(f4, f3, enumHandSide);
            }
        } else if (itemStack.func_77973_b() instanceof ItemMap) {
            if (bl && this.field_187468_e.func_190926_b()) {
                this.func_187463_a(f2, f4, f3);
            } else {
                this.func_187465_a(f4, enumHandSide, f3, itemStack);
            }
        } else if (!(itemStack.func_77973_b() instanceof ItemShield)) {
            boolean bl2;
            KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
            Animations animations = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
            boolean bl3 = bl2 = enumHandSide == EnumHandSide.RIGHT;
            if (abstractClientPlayer.func_184587_cr() && abstractClientPlayer.func_184605_cv() > 0 && abstractClientPlayer.func_184600_cs() == enumHand) {
                int n = bl2 ? 1 : -1;
                EnumAction enumAction = killAura.getBlockingStatus() ? EnumAction.BLOCK : itemStack.func_77975_n();
                switch (1.$SwitchMap$net$minecraft$item$EnumAction[enumAction.ordinal()]) {
                    case 1: {
                        this.func_187459_b(enumHandSide, f4);
                        break;
                    }
                    case 2: {
                        MixinItemRenderer.transformSideFirstPersonBlock(enumHandSide, f4, f3);
                        break;
                    }
                    case 3: 
                    case 4: {
                        this.func_187454_a(f, enumHandSide, itemStack);
                        this.func_187459_b(enumHandSide, f4);
                        break;
                    }
                    case 5: {
                        this.func_187459_b(enumHandSide, f4);
                        GlStateManager.func_179109_b((float)((float)n * -0.2785682f), (float)0.18344387f, (float)0.15731531f);
                        GlStateManager.func_179114_b((float)-13.935f, (float)1.0f, (float)0.0f, (float)0.0f);
                        GlStateManager.func_179114_b((float)((float)n * 35.3f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.func_179114_b((float)((float)n * -9.785f), (float)0.0f, (float)0.0f, (float)1.0f);
                        float f5 = (float)itemStack.func_77988_m() - ((float)this.field_78455_a.field_71439_g.func_184605_cv() - f + 1.0f);
                        float f6 = f5 / 20.0f;
                        f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                        if (f6 > 1.0f) {
                            f6 = 1.0f;
                        }
                        if (f6 > 0.1f) {
                            float f7 = MathHelper.func_76126_a((float)((f5 - 0.1f) * 1.3f));
                            float f8 = f6 - 0.1f;
                            float f9 = f7 * f8;
                            GlStateManager.func_179109_b((float)(f9 * 0.0f), (float)(f9 * 0.004f), (float)(f9 * 0.0f));
                        }
                        GlStateManager.func_179109_b((float)(f6 * 0.0f), (float)(f6 * 0.0f), (float)(f6 * 0.04f));
                        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)(1.0f + f6 * 0.2f));
                        GlStateManager.func_179114_b((float)((float)n * 45.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    }
                }
            } else if (this.field_78455_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78455_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78455_a.field_71474_y.field_74313_G.field_74513_e) && animations.getState()) {
                float f10;
                GlStateManager.func_179109_b((float)((Float)Animations.xValue.get()).floatValue(), (float)((Float)Animations.yValue.get()).floatValue(), (float)((Float)Animations.zValue.get()).floatValue());
                float f11 = f10 = (Boolean)Animations.SPValue.get() != false ? f4 : 0.0f;
                if (((String)Animations.Sword.get()).equals("1.7")) {
                    MixinItemRenderer.transformSideFirstPersonBlock(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Old")) {
                    MixinItemRenderer.transformSideFirstPersonBlock(enumHandSide, -0.1f + f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Push")) {
                    MixinItemRenderer.Push(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("WindMill")) {
                    MixinItemRenderer.WindMill(enumHandSide, -0.2f + f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Smooth")) {
                    MixinItemRenderer.SmoothBlock(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Flux")) {
                    MixinItemRenderer.Flux(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("test")) {
                    MixinItemRenderer.test(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("BigGod")) {
                    this.ETB(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("avatar")) {
                    this.avatar(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("SigmaOld")) {
                    MixinItemRenderer.sigmaold(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Tap")) {
                    this.tap(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Zoom")) {
                    this.Zoom(enumHandSide, f10, f3);
                }
                if (((String)Animations.Sword.get()).equals("Jello")) {
                    MixinItemRenderer.jello(enumHandSide, f10, f3);
                }
                GlStateManager.func_179152_a((float)((Float)Animations.scaleValue.get()).floatValue(), (float)((Float)Animations.scaleValue.get()).floatValue(), (float)((Float)Animations.scaleValue.get()).floatValue());
            } else {
                if (((Boolean)Animations.heldValue.get()).booleanValue()) {
                    GlStateManager.func_179109_b((float)((Float)Animations.xhValue.get()).floatValue(), (float)((Float)Animations.yhValue.get()).floatValue(), (float)((Float)Animations.zhValue.get()).floatValue());
                }
                float f12 = -0.4f * MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f3) * (float)Math.PI));
                float f13 = 0.2f * MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f3) * ((float)Math.PI * 2)));
                float f14 = -0.2f * MathHelper.func_76126_a((float)(f3 * (float)Math.PI));
                int n = bl2 ? 1 : -1;
                GlStateManager.func_179109_b((float)((float)n * f12), (float)f13, (float)f14);
                this.func_187459_b(enumHandSide, f4);
                this.func_187453_a(enumHandSide, f3);
                this.rotateItemAnim();
                if (((Boolean)Animations.heldValue.get()).booleanValue()) {
                    GlStateManager.func_179152_a((float)((Float)Animations.scalehValue.get()).floatValue(), (float)((Float)Animations.scalehValue.get()).floatValue(), (float)((Float)Animations.scalehValue.get()).floatValue());
                }
            }
            this.func_187462_a((EntityLivingBase)abstractClientPlayer, itemStack, bl2 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !bl2);
        }
        GlStateManager.func_179121_F();
    }

    @Overwrite
    public void func_78441_a() {
        Animations animations = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        this.field_187470_g = this.field_187469_f;
        this.field_187472_i = this.field_187471_h;
        EntityPlayerSP entityPlayerSP = this.field_78455_a.field_71439_g;
        ItemStack itemStack = entityPlayerSP.func_184614_ca();
        ItemStack itemStack2 = entityPlayerSP.func_184592_cb();
        if (entityPlayerSP.func_184838_M()) {
            this.field_187469_f = MathHelper.func_76131_a((float)(this.field_187469_f - 0.4f), (float)0.0f, (float)1.0f);
            this.field_187471_h = MathHelper.func_76131_a((float)(this.field_187471_h - 0.4f), (float)0.0f, (float)1.0f);
        } else {
            float f = entityPlayerSP.func_184825_o(1.0f);
            boolean bl = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.field_187467_d, (ItemStack)itemStack, (int)entityPlayerSP.field_71071_by.field_70461_c);
            boolean bl2 = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.field_187468_e, (ItemStack)itemStack2, (int)-1);
            if (!bl && !Objects.equals(this.field_187467_d, itemStack)) {
                this.field_187467_d = itemStack;
            }
            if (!bl && !Objects.equals(this.field_187468_e, itemStack2)) {
                this.field_187468_e = itemStack2;
            }
            float f2 = (Boolean)Animations.oldSPValue.get() != false ? 1.0f : f * f * f;
            this.field_187469_f += MathHelper.func_76131_a((float)((!bl ? f2 : 0.0f) - this.field_187469_f), (float)-0.4f, (float)0.4f);
            this.field_187471_h += MathHelper.func_76131_a((float)((float)(!bl2 ? 1 : 0) - this.field_187471_h), (float)-0.4f, (float)0.4f);
        }
        if (this.field_187469_f < 0.1f) {
            this.field_187467_d = itemStack;
        }
        if (this.field_187471_h < 0.1f) {
            this.field_187468_e = itemStack2;
        }
    }

    @Shadow
    protected abstract void func_187453_a(EnumHandSide var1, float var2);

    @Shadow
    protected abstract void func_187456_a(float var1, float var2, EnumHandSide var3);

    private static void Flux(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -30.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -15.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -15.0)), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static void test(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)-90.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179114_b((float)-10.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -40.0)), (float)1.0f, (float)-0.0f, (float)1.0f);
    }

    private static void SmoothBlock(EnumHandSide enumHandSide, float f, float f2) {
        int n = enumHandSide == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)n * 0.56), (double)(-0.52 + (double)f * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)n * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)n * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double d = Math.sin((double)(f2 * f2) * Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(d * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * -30.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private void ETB(EnumHandSide enumHandSide, float f, float f2) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(f * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        double d = Math.sin(f2 * f2 * (float)Math.PI);
        double d2 = Math.sin(Math.sqrt(f2) * 3.1415927410125732);
        GlStateManager.func_179114_b((float)((float)(d * -34.0)), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)((float)(d2 * (double)-20.7f)), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(d2 * (double)-68.6f)), (float)1.3f, (float)0.1f, (float)0.2f);
    }

    private void tap(EnumHandSide enumHandSide, float f, float f2) {
        float f3 = f2 * 0.8f - f2 * f2 * 0.8f;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(f * -0.15f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f4 = MathHelper.func_76126_a((float)(f2 * f2 * (float)Math.PI));
        float f5 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)f2) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f3 * -90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.37f, (float)0.37f, (float)0.37f);
    }

    @Shadow
    protected abstract void func_187465_a(float var1, EnumHandSide var2, float var3, ItemStack var4);
}

