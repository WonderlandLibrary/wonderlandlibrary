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
import net.ccbluex.liquidbounce.features.module.modules.hyt.Animations;
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
    float delay = 0.0f;
    MSTimer rotateTimer = new MSTimer();
    @Shadow
    private ItemStack field_187467_d;
    @Shadow
    private ItemStack field_187468_e;
    @Shadow
    private float field_187469_f;
    @Shadow
    private float field_187470_g;
    @Shadow
    private float field_187471_h;
    @Shadow
    private float field_187472_i;
    @Shadow
    @Final
    private Minecraft field_78455_a;

    private static void jello(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var13 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var14 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var13 * -35.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var14 * 0.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var14 * 20.0f), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static void sigmaold(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -15.0f), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)(var4 * -10.0f), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)(var4 * -30.0f), (float)1.3f, (float)0.1f, (float)0.2f);
    }

    private static void WindMill(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -50.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private static void Push(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -10.0)), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static void Flux(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -30.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -15.0)), (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -15.0)), (float)1.0f, (float)1.0f, (float)1.0f);
    }

    private static void test(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)-90.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179114_b((float)-10.0f, (float)1.0f, (float)0.0f, (float)-1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -40.0)), (float)1.0f, (float)-0.0f, (float)1.0f);
    }

    private static void transformSideFirstPersonBlock(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -80.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    private static void SmoothBlock(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        int side = p_187459_1_ == EnumHandSide.RIGHT ? 1 : -1;
        GlStateManager.func_179137_b((double)((double)side * 0.56), (double)(-0.52 + (double)equippedProg * -0.6), (double)-0.72);
        GlStateManager.func_179137_b((double)((double)side * -0.1414214), (double)0.08, (double)0.1414214);
        GlStateManager.func_179114_b((float)-102.25f, (float)1.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 13.365f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)side * 78.05f), (float)0.0f, (float)0.0f, (float)1.0f);
        double f = Math.sin((double)(swingProgress * swingProgress) * Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * Math.PI);
        GlStateManager.func_179114_b((float)((float)(f * -20.0)), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -20.0)), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * -30.0)), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    @Shadow
    protected abstract void func_187463_a(float var1, float var2, float var3);

    @Shadow
    protected abstract void func_187453_a(EnumHandSide var1, float var2);

    @Shadow
    protected abstract void func_187454_a(float var1, EnumHandSide var2, ItemStack var3);

    @Shadow
    protected abstract void func_187456_a(float var1, float var2, EnumHandSide var3);

    @Shadow
    protected abstract void func_187465_a(float var1, EnumHandSide var2, float var3, ItemStack var4);

    @Shadow
    protected abstract void func_187459_b(EnumHandSide var1, float var2);

    @Shadow
    public abstract void func_187462_a(EntityLivingBase var1, ItemStack var2, ItemCameraTransforms.TransformType var3, boolean var4);

    private void doItemRenderGLTranslate() {
        GlStateManager.func_179109_b((float)((Float)Animations.xhValue.get()).floatValue(), (float)((Float)Animations.yhValue.get()).floatValue(), (float)((Float)Animations.zhValue.get()).floatValue());
    }

    @Overwrite
    public void func_187457_a(AbstractClientPlayer player, float p_187457_2_, float p_187457_3_, EnumHand hand, float p_187457_5_, ItemStack stack, float p_187457_7_) {
        boolean flag = hand == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? player.func_184591_cq() : player.func_184591_cq().func_188468_a();
        GlStateManager.func_179094_E();
        if (stack.func_190926_b()) {
            if (flag && !player.func_82150_aj()) {
                this.func_187456_a(p_187457_7_, p_187457_5_, enumhandside);
            }
        } else if (stack.func_77973_b() instanceof ItemMap) {
            if (flag && this.field_187468_e.func_190926_b()) {
                this.func_187463_a(p_187457_3_, p_187457_7_, p_187457_5_);
            } else {
                this.func_187465_a(p_187457_7_, enumhandside, p_187457_5_, stack);
            }
        } else if (!(stack.func_77973_b() instanceof ItemShield)) {
            boolean flag1;
            KillAura killAura = (KillAura)LiquidBounce.moduleManager.getModule(KillAura.class);
            Animations anim = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
            boolean bl = flag1 = enumhandside == EnumHandSide.RIGHT;
            if (player.func_184587_cr() && player.func_184605_cv() > 0 && player.func_184600_cs() == hand) {
                int j = flag1 ? 1 : -1;
                EnumAction enumaction = killAura.getBlockingStatus() ? EnumAction.BLOCK : stack.func_77975_n();
                switch (enumaction) {
                    case NONE: {
                        this.func_187459_b(enumhandside, p_187457_7_);
                        break;
                    }
                    case BLOCK: {
                        MixinItemRenderer.transformSideFirstPersonBlock(enumhandside, p_187457_7_, p_187457_5_);
                        break;
                    }
                    case EAT: 
                    case DRINK: {
                        this.func_187454_a(p_187457_2_, enumhandside, stack);
                        this.func_187459_b(enumhandside, p_187457_7_);
                        break;
                    }
                    case BOW: {
                        this.func_187459_b(enumhandside, p_187457_7_);
                        GlStateManager.func_179109_b((float)((float)j * -0.2785682f), (float)0.18344387f, (float)0.15731531f);
                        GlStateManager.func_179114_b((float)-13.935f, (float)1.0f, (float)0.0f, (float)0.0f);
                        GlStateManager.func_179114_b((float)((float)j * 35.3f), (float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.func_179114_b((float)((float)j * -9.785f), (float)0.0f, (float)0.0f, (float)1.0f);
                        float f5 = (float)stack.func_77988_m() - ((float)this.field_78455_a.field_71439_g.func_184605_cv() - p_187457_2_ + 1.0f);
                        float f6 = f5 / 20.0f;
                        f6 = (f6 * f6 + f6 * 2.0f) / 3.0f;
                        if (f6 > 1.0f) {
                            f6 = 1.0f;
                        }
                        if (f6 > 0.1f) {
                            float f7 = MathHelper.func_76126_a((float)((f5 - 0.1f) * 1.3f));
                            float f3 = f6 - 0.1f;
                            float f4 = f7 * f3;
                            GlStateManager.func_179109_b((float)(f4 * 0.0f), (float)(f4 * 0.004f), (float)(f4 * 0.0f));
                        }
                        GlStateManager.func_179109_b((float)(f6 * 0.0f), (float)(f6 * 0.0f), (float)(f6 * 0.04f));
                        GlStateManager.func_179152_a((float)1.0f, (float)1.0f, (float)(1.0f + f6 * 0.2f));
                        GlStateManager.func_179114_b((float)((float)j * 45.0f), (float)0.0f, (float)-1.0f, (float)0.0f);
                    }
                }
            } else if (this.field_78455_a.field_71439_g.func_184614_ca().func_77973_b() != null && this.field_78455_a.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword && (killAura.getTarget() != null && killAura.getBlockingStatus() || this.field_78455_a.field_71474_y.field_74313_G.field_74513_e) && anim.getState()) {
                float SP;
                GlStateManager.func_179109_b((float)((Float)Animations.xValue.get()).floatValue(), (float)((Float)Animations.yValue.get()).floatValue(), (float)((Float)Animations.zValue.get()).floatValue());
                float f = SP = (Boolean)Animations.SPValue.get() != false ? p_187457_7_ : 0.0f;
                if (((String)Animations.Sword.get()).equals("1.7")) {
                    MixinItemRenderer.transformSideFirstPersonBlock(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Old")) {
                    MixinItemRenderer.transformSideFirstPersonBlock(enumhandside, -0.1f + SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Push")) {
                    MixinItemRenderer.Push(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("WindMill")) {
                    MixinItemRenderer.WindMill(enumhandside, -0.2f + SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Smooth")) {
                    MixinItemRenderer.SmoothBlock(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Flux")) {
                    MixinItemRenderer.Flux(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("test")) {
                    MixinItemRenderer.test(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("BigGod")) {
                    this.ETB(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("avatar")) {
                    this.avatar(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("SigmaOld")) {
                    MixinItemRenderer.sigmaold(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Tap")) {
                    this.tap(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Zoom")) {
                    this.Zoom(enumhandside, SP, p_187457_5_);
                }
                if (((String)Animations.Sword.get()).equals("Jello")) {
                    MixinItemRenderer.jello(enumhandside, SP, p_187457_5_);
                }
                GlStateManager.func_179152_a((float)((Float)Animations.scaleValue.get()).floatValue(), (float)((Float)Animations.scaleValue.get()).floatValue(), (float)((Float)Animations.scaleValue.get()).floatValue());
            } else {
                if (((Boolean)Animations.heldValue.get()).booleanValue()) {
                    GlStateManager.func_179109_b((float)((Float)Animations.xhValue.get()).floatValue(), (float)((Float)Animations.yhValue.get()).floatValue(), (float)((Float)Animations.zhValue.get()).floatValue());
                }
                float f = -0.4f * MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_187457_5_) * (float)Math.PI));
                float f1 = 0.2f * MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_187457_5_) * ((float)Math.PI * 2)));
                float f2 = -0.2f * MathHelper.func_76126_a((float)(p_187457_5_ * (float)Math.PI));
                int i = flag1 ? 1 : -1;
                GlStateManager.func_179109_b((float)((float)i * f), (float)f1, (float)f2);
                this.func_187459_b(enumhandside, p_187457_7_);
                this.func_187453_a(enumhandside, p_187457_5_);
                this.rotateItemAnim();
                if (((Boolean)Animations.heldValue.get()).booleanValue()) {
                    GlStateManager.func_179152_a((float)((Float)Animations.scalehValue.get()).floatValue(), (float)((Float)Animations.scalehValue.get()).floatValue(), (float)((Float)Animations.scalehValue.get()).floatValue());
                }
            }
            this.func_187462_a((EntityLivingBase)player, stack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }
        GlStateManager.func_179121_F();
    }

    private void Zoom(float p_178096_1_, float p_178096_2_) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(p_178096_1_ * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(p_178096_2_ * p_178096_2_ * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)p_178096_2_) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
    }

    private void tap(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        float smooth = swingProgress * 0.8f - swingProgress * swingProgress * 0.8f;
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equippedProg * -0.15f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(smooth * -90.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179152_a((float)0.37f, (float)0.37f, (float)0.37f);
    }

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

    private void Zoom(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equippedProg * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float var3 = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float var4 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(var3 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(var4 * -20.0f), (float)0.0f, (float)0.0f, (float)0.0f);
    }

    private void ETB(EnumHandSide p_187459_1_, float equipProgress, float swingProgress) {
        GlStateManager.func_179109_b((float)0.56f, (float)-0.52f, (float)-0.71999997f);
        GlStateManager.func_179109_b((float)0.0f, (float)(equipProgress * -0.6f), (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        double f = Math.sin(swingProgress * swingProgress * (float)Math.PI);
        double f1 = Math.sin(Math.sqrt(swingProgress) * 3.1415927410125732);
        GlStateManager.func_179114_b((float)((float)(f * -34.0)), (float)0.0f, (float)1.0f, (float)0.2f);
        GlStateManager.func_179114_b((float)((float)(f1 * (double)-20.7f)), (float)0.2f, (float)0.1f, (float)1.0f);
        GlStateManager.func_179114_b((float)((float)(f1 * (double)-68.6f)), (float)1.3f, (float)0.1f, (float)0.2f);
    }

    private void avatar(EnumHandSide p_187459_1_, float equippedProg, float swingProgress) {
        GlStateManager.func_179109_b((float)0.0f, (float)0.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)45.0f, (float)0.0f, (float)1.0f, (float)0.0f);
        float f = MathHelper.func_76126_a((float)(swingProgress * swingProgress * (float)Math.PI));
        float f2 = MathHelper.func_76126_a((float)(MathHelper.func_76129_c((float)swingProgress) * (float)Math.PI));
        GlStateManager.func_179114_b((float)(f * -20.0f), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.func_179114_b((float)(f2 * -20.0f), (float)0.0f, (float)0.0f, (float)1.0f);
        GlStateManager.func_179114_b((float)(f2 * -40.0f), (float)1.0f, (float)0.0f, (float)0.0f);
    }

    @Inject(method={"renderFireInFirstPerson"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderFireInFirstPerson(CallbackInfo callbackInfo) {
        AntiBlind antiBlind = (AntiBlind)LiquidBounce.moduleManager.getModule(AntiBlind.class);
        if (antiBlind.getState() && ((Boolean)antiBlind.getFireEffect().get()).booleanValue()) {
            callbackInfo.cancel();
        }
    }

    @Overwrite
    public void func_78441_a() {
        Animations oldhiting = (Animations)LiquidBounce.moduleManager.getModule(Animations.class);
        this.field_187470_g = this.field_187469_f;
        this.field_187472_i = this.field_187471_h;
        EntityPlayerSP entityplayersp = this.field_78455_a.field_71439_g;
        ItemStack itemstack = entityplayersp.func_184614_ca();
        ItemStack itemstack1 = entityplayersp.func_184592_cb();
        if (entityplayersp.func_184838_M()) {
            this.field_187469_f = MathHelper.func_76131_a((float)(this.field_187469_f - 0.4f), (float)0.0f, (float)1.0f);
            this.field_187471_h = MathHelper.func_76131_a((float)(this.field_187471_h - 0.4f), (float)0.0f, (float)1.0f);
        } else {
            float f = entityplayersp.func_184825_o(1.0f);
            boolean requipM = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.field_187467_d, (ItemStack)itemstack, (int)entityplayersp.field_71071_by.field_70461_c);
            boolean requipO = ForgeHooksClient.shouldCauseReequipAnimation((ItemStack)this.field_187468_e, (ItemStack)itemstack1, (int)-1);
            if (!requipM && !Objects.equals(this.field_187467_d, itemstack)) {
                this.field_187467_d = itemstack;
            }
            if (!requipM && !Objects.equals(this.field_187468_e, itemstack1)) {
                this.field_187468_e = itemstack1;
            }
            float number = (Boolean)Animations.oldSPValue.get() != false ? 1.0f : f * f * f;
            this.field_187469_f += MathHelper.func_76131_a((float)((!requipM ? number : 0.0f) - this.field_187469_f), (float)-0.4f, (float)0.4f);
            this.field_187471_h += MathHelper.func_76131_a((float)((float)(!requipO ? 1 : 0) - this.field_187471_h), (float)-0.4f, (float)0.4f);
        }
        if (this.field_187469_f < 0.1f) {
            this.field_187467_d = itemstack;
        }
        if (this.field_187471_h < 0.1f) {
            this.field_187468_e = itemstack1;
        }
    }
}

