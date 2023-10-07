/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.model.ModelBase
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.RendererLivingEntity
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EnumPlayerModelParts
 *  net.minecraft.util.EnumChatFormatting
 *  net.minecraft.util.MathHelper
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import co.uk.hexeption.utils.OutlineUtils;
import java.awt.Color;
import me.report.liquidware.modules.render.Animations;
import me.report.liquidware.modules.render.NoRender;
import me.report.liquidware.modules.render.PlayerEdit;
import me.report.liquidware.modules.render.SilentView;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.render.Chams;
import net.ccbluex.liquidbounce.features.module.modules.render.ESP;
import net.ccbluex.liquidbounce.features.module.modules.render.NameTags;
import net.ccbluex.liquidbounce.features.module.modules.render.TrueSight;
import net.ccbluex.liquidbounce.injection.forge.mixins.render.MixinRender;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.EntityUtils;
import net.ccbluex.liquidbounce.utils.RotationUtils;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SideOnly(value=Side.CLIENT)
@Mixin(value={RendererLivingEntity.class})
public abstract class MixinRendererLivingEntity
extends MixinRender {
    @Shadow
    protected ModelBase field_77045_g;
    @Shadow
    protected boolean field_177098_i = false;

    @Shadow
    protected abstract <T extends EntityLivingBase> float func_77040_d(T var1, float var2);

    @Shadow
    protected abstract float func_77034_a(float var1, float var2, float var3);

    @Shadow
    protected abstract void func_177091_f();

    @Shadow
    protected abstract void func_180565_e();

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean func_177088_c(T var1);

    @Shadow
    protected abstract <T extends EntityLivingBase> void func_77039_a(T var1, double var2, double var4, double var6);

    @Shadow
    protected abstract <T extends EntityLivingBase> void func_77041_b(T var1, float var2);

    @Shadow
    protected abstract <T extends EntityLivingBase> float func_77044_a(T var1, float var2);

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean func_177090_c(T var1, float var2);

    @Shadow
    protected abstract <T extends EntityLivingBase> void func_177093_a(T var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8);

    @Shadow
    protected <T extends EntityLivingBase> float func_77037_a(T p_getDeathMaxRotation_1_) {
        return 90.0f;
    }

    @Overwrite
    protected <T extends EntityLivingBase> void func_77043_a(T p_rotateCorpse_1_, float p_rotateCorpse_2_, float p_rotateCorpse_3_, float p_rotateCorpse_4_) {
        PlayerEdit playerEdit = (PlayerEdit)LiquidBounce.moduleManager.getModule(PlayerEdit.class);
        GlStateManager.func_179114_b((float)(180.0f - p_rotateCorpse_3_), (float)0.0f, (float)1.0f, (float)0.0f);
        if (p_rotateCorpse_1_.field_70725_aQ > 0) {
            float f = ((float)p_rotateCorpse_1_.field_70725_aQ + p_rotateCorpse_4_ - 1.0f) / 20.0f * 1.6f;
            if ((f = MathHelper.func_76129_c((float)f)) > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.func_179114_b((float)(f * this.func_77037_a(p_rotateCorpse_1_)), (float)0.0f, (float)0.0f, (float)1.0f);
        } else {
            String s = EnumChatFormatting.func_110646_a((String)p_rotateCorpse_1_.func_70005_c_());
            if (s != null && ((Boolean)Animations.rotatePlayer.get()).booleanValue() && p_rotateCorpse_1_.equals((Object)Minecraft.func_71410_x().field_71439_g) && LiquidBounce.moduleManager.get(PlayerEdit.class).getState() && (!(p_rotateCorpse_1_ instanceof EntityPlayer) || ((EntityPlayer)p_rotateCorpse_1_).func_175148_a(EnumPlayerModelParts.CAPE))) {
                GlStateManager.func_179109_b((float)0.0f, (float)(p_rotateCorpse_1_.field_70131_O + 0.1f), (float)0.0f);
                GlStateManager.func_179114_b((float)180.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            }
        }
    }

    @Inject(method={"doRender"}, at={@At(value="HEAD")}, cancellable=true)
    private <T extends EntityLivingBase> void injectChamsPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        NoRender noRender = (NoRender)LiquidBounce.moduleManager.getModule(NoRender.class);
        if (noRender.getState() && noRender.shouldStopRender((Entity)entity)) {
            callbackInfo.cancel();
            return;
        }
        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && ((Boolean)chams.getLegacyMode().get()).booleanValue() && EntityUtils.isSelected(entity, false)) {
            GL11.glEnable((int)32823);
            GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
        }
    }

    @Inject(method={"doRender"}, at={@At(value="RETURN")})
    private <T extends EntityLivingBase> void injectChamsPost(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        NoRender noRender = (NoRender)LiquidBounce.moduleManager.getModule(NoRender.class);
        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && ((Boolean)chams.getLegacyMode().get()).booleanValue() && EntityUtils.isSelected(entity, false) && (!noRender.getState() || !noRender.shouldStopRender((Entity)entity))) {
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
        }
    }

    @Inject(method={"canRenderName"}, at={@At(value="HEAD")}, cancellable=true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || !LiquidBounce.moduleManager.getModule(NameTags.class).getState() || EntityUtils.isSelected(entity, false)) {
            // empty if block
        }
        callbackInfoReturnable.setReturnValue(false);
    }

    @Inject(method={"doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"}, at={@At(value="HEAD")})
    private <T extends EntityLivingBase> void injectFakeBody(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179129_p();
        this.field_77045_g.field_78095_p = this.func_77040_d(entity, partialTicks);
        this.field_77045_g.field_78093_q = entity.func_70115_ae();
        this.field_77045_g.field_78091_s = entity.func_70631_g_();
        try {
            float renderyaw;
            float renderpitch;
            float f = this.func_77034_a(entity.field_70760_ar, entity.field_70761_aq, partialTicks);
            float f1 = this.func_77034_a(entity.field_70758_at, entity.field_70759_as, partialTicks);
            float f2 = f1 - f;
            if (entity.func_70115_ae() && entity.field_70154_o instanceof EntityLivingBase) {
                EntityLivingBase entitylivingbase = (EntityLivingBase)entity.field_70154_o;
                f = this.func_77034_a(entitylivingbase.field_70760_ar, entitylivingbase.field_70761_aq, partialTicks);
                f2 = f1 - f;
                float f3 = MathHelper.func_76142_g((float)f2);
                if (f3 < -85.0f) {
                    f3 = -85.0f;
                }
                if (f3 >= 85.0f) {
                    f3 = 85.0f;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0f) {
                    f += f3 * 0.2f;
                }
            }
            float f7 = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks;
            this.func_77039_a(entity, x, y, z);
            float f8 = this.func_77044_a(entity, partialTicks);
            this.func_77043_a(entity, f8, f, partialTicks);
            GlStateManager.func_179091_B();
            GlStateManager.func_179152_a((float)-1.0f, (float)-1.0f, (float)1.0f);
            this.func_77041_b(entity, partialTicks);
            float f4 = 0.0625f;
            GlStateManager.func_179109_b((float)0.0f, (float)-1.5078125f, (float)0.0f);
            float f5 = entity.field_70722_aY + (entity.field_70721_aZ - entity.field_70722_aY) * partialTicks;
            float f6 = entity.field_70754_ba - entity.field_70721_aZ * (1.0f - partialTicks);
            if (entity.func_70631_g_()) {
                f6 *= 3.0f;
            }
            if (f5 > 1.0f) {
                f5 = 1.0f;
            }
            GlStateManager.func_179141_d();
            this.field_77045_g.func_78086_a(entity, f6, f5, partialTicks);
            this.field_77045_g.func_78087_a(f6, f5, f8, f2, f7, 0.0625f, entity);
            if (this.field_177098_i) {
                boolean flag1 = this.func_177088_c(entity);
                this.renderModels(entity, f6, f5, f8, f2, f7, 0.0625f);
                if (flag1) {
                    this.func_180565_e();
                }
            } else {
                boolean flag = this.func_177090_c(entity, partialTicks);
                this.renderModels(entity, f6, f5, f8, f2, f7, 0.0625f);
                if (flag) {
                    this.func_177091_f();
                }
                GlStateManager.func_179132_a((boolean)true);
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).func_175149_v()) {
                    this.func_177093_a(entity, f6, f5, partialTicks, f8, f2, f7, 0.0625f);
                }
            }
            SilentView rotations = (SilentView)LiquidBounce.moduleManager.getModule(SilentView.class);
            int blend = 3042;
            int depth = 2929;
            int srcAlpha = 770;
            int srcAlphaPlus1 = 771;
            int polygonOffsetLine = 10754;
            int texture2D = 3553;
            int lighting = 2896;
            float f3 = Minecraft.func_71410_x().field_71474_y.field_74320_O != 0 && rotations.getState() && ((String)rotations.getMode().get()).equals("CSGO") && entity == Minecraft.func_71410_x().field_71439_g ? entity.field_70127_C + ((RotationUtils.serverRotation.getPitch() != 0.0f ? RotationUtils.serverRotation.getPitch() : entity.field_70125_A) - entity.field_70127_C) : (renderpitch = entity.field_70127_C + (entity.field_70125_A - entity.field_70127_C) * partialTicks);
            float f9 = Minecraft.func_71410_x().field_71474_y.field_74320_O != 0 && rotations.getState() && ((String)rotations.getMode().get()).equals("CSGO") && entity == Minecraft.func_71410_x().field_71439_g ? entity.field_70126_B + ((RotationUtils.serverRotation.getYaw() != 0.0f ? RotationUtils.serverRotation.getYaw() : entity.field_70177_z) - entity.field_70126_B) : (renderyaw = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * partialTicks);
            if (entity == Minecraft.func_71410_x().field_71439_g && rotations.getState() && ((String)rotations.getMode().get()).equals("CSGO") && rotations.shouldRotate()) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179147_l();
                GlStateManager.func_179112_b((int)770, (int)771);
                GlStateManager.func_179131_c((float)0.98180395f, (float)0.98180395f, (float)0.98180395f, (float)0.018196078f);
                GlStateManager.func_179121_F();
            }
            if (rotations.getState() && ((String)rotations.getMode().get()).equals("CSGO") && entity.equals((Object)Minecraft.func_71410_x().field_71439_g) && rotations.shouldRotate()) {
                GL11.glPushMatrix();
                GL11.glPushAttrib((int)1048575);
                GL11.glDisable((int)2929);
                GL11.glDisable((int)3553);
                GL11.glDisable((int)3553);
                GL11.glEnable((int)3042);
                GL11.glBlendFunc((int)770, (int)771);
                GL11.glDisable((int)2896);
                GL11.glPolygonMode((int)1032, (int)6914);
                GL11.glColor4f((float)(((Float)rotations.getR().get()).floatValue() / 255.0f), (float)(((Float)rotations.getG().get()).floatValue() / 255.0f), (float)((Float)rotations.getB().get()).floatValue(), (float)(((Float)rotations.getAlpha().get()).floatValue() / 255.0f));
                GL11.glRotatef((float)(renderyaw - f), (float)0.0f, (float)0.001f, (float)0.0f);
                this.field_77045_g.func_78088_a((Entity)Minecraft.func_71410_x().field_71439_g, f6, f5, renderpitch, f2, renderpitch, 0.0625f);
                GL11.glEnable((int)2896);
                GL11.glDisable((int)3042);
                GL11.glEnable((int)3553);
                GL11.glEnable((int)2929);
                GL11.glColor3d((double)1.0, (double)1.0, (double)1.0);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }
            GlStateManager.func_179101_C();
        }
        catch (Exception exception) {
            // empty catch block
        }
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77476_b);
        GlStateManager.func_179098_w();
        GlStateManager.func_179138_g((int)OpenGlHelper.field_77478_a);
        GlStateManager.func_179089_o();
        GlStateManager.func_179121_F();
    }

    protected <T extends EntityLivingBase> void renderModels(T p_renderModel_1_, float p_renderModel_2_, float p_renderModel_3_, float p_renderModel_4_, float p_renderModel_5_, float p_renderModel_6_, float p_renderModel_7_) {
        boolean semiVisible;
        boolean visible = !p_renderModel_1_.func_82150_aj();
        TrueSight trueSight = (TrueSight)LiquidBounce.moduleManager.getModule(TrueSight.class);
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        boolean chamsFlag = chams.getState() && (Boolean)chams.getTargetsValue().get() != false && (Boolean)chams.getLegacyMode().get() == false && ((Boolean)chams.getLocalPlayerValue().get() != false && p_renderModel_1_ == Minecraft.func_71410_x().field_71439_g || EntityUtils.isSelected(p_renderModel_1_, false));
        boolean bl = semiVisible = !visible && (!p_renderModel_1_.func_98034_c((EntityPlayer)Minecraft.func_71410_x().field_71439_g) || trueSight.getState() && (Boolean)trueSight.getEntitiesValue().get() != false);
        if (visible || semiVisible) {
            ESP esp;
            if (!this.func_180548_c(p_renderModel_1_)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.15f);
                GlStateManager.func_179132_a((boolean)false);
                GlStateManager.func_179147_l();
                GlStateManager.func_179112_b((int)770, (int)771);
                GlStateManager.func_179092_a((int)516, (float)0.003921569f);
            }
            if ((esp = (ESP)LiquidBounce.moduleManager.getModule(ESP.class)).getState() && EntityUtils.isSelected(p_renderModel_1_, false)) {
                Minecraft mc = Minecraft.func_71410_x();
                boolean fancyGraphics = mc.field_71474_y.field_74347_j;
                mc.field_71474_y.field_74347_j = false;
                float gamma = mc.field_71474_y.field_74333_Y;
                mc.field_71474_y.field_74333_Y = 100000.0f;
                switch (((String)esp.modeValue.get()).toLowerCase()) {
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        RenderUtils.glColor(esp.getColor((Entity)p_renderModel_1_));
                        GL11.glLineWidth((float)((Float)esp.wireframeWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                    case "outline": {
                        ClientUtils.disableFastRender();
                        GlStateManager.func_179117_G();
                        Color color = esp.getColor((Entity)p_renderModel_1_);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(((Float)esp.outlineWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                    }
                }
                mc.field_71474_y.field_74347_j = fancyGraphics;
                mc.field_71474_y.field_74333_Y = gamma;
            }
            int blend = 3042;
            int depth = 2929;
            int srcAlpha = 770;
            int srcAlphaPlus1 = 771;
            int polygonOffsetLine = 10754;
            int texture2D = 3553;
            int lighting = 2896;
            boolean textured = (Boolean)chams.getTexturedValue().get();
            Color chamsColor = new Color(0);
            switch ((String)chams.getColorModeValue().get()) {
                case "Custom": {
                    chamsColor = new Color((Integer)chams.getRedValue().get(), (Integer)chams.getGreenValue().get(), (Integer)chams.getBlueValue().get());
                    break;
                }
                case "Sky": {
                    chamsColor = RenderUtils.skyRainbow(0, ((Float)chams.getSaturationValue().get()).floatValue(), ((Float)chams.getBrightnessValue().get()).floatValue());
                    break;
                }
                case "LiquidSlowly": {
                    chamsColor = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)chams.getSaturationValue().get()).floatValue(), ((Float)chams.getBrightnessValue().get()).floatValue());
                    break;
                }
                case "Fade": {
                    chamsColor = ColorUtils.fade(new Color((Integer)chams.getRedValue().get(), (Integer)chams.getGreenValue().get(), (Integer)chams.getBlueValue().get(), (Integer)chams.getAlphaValue().get()), 0, 100);
                }
            }
            chamsColor = ColorUtils.reAlpha(chamsColor, (Integer)chams.getAlphaValue().get());
            if (chamsFlag) {
                Color chamsColor2 = new Color(0);
                switch ((String)chams.getBehindColorModeValue().get()) {
                    case "Same": {
                        chamsColor2 = chamsColor;
                        break;
                    }
                    case "Opposite": {
                        chamsColor2 = ColorUtils.getOppositeColor(chamsColor);
                        break;
                    }
                    case "Red": {
                        chamsColor2 = new Color(-1104346);
                    }
                }
                GL11.glPushMatrix();
                GL11.glEnable((int)10754);
                GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
                OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
                if (!textured) {
                    GL11.glEnable((int)3042);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2896);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glColor4f((float)((float)chamsColor2.getRed() / 255.0f), (float)((float)chamsColor2.getGreen() / 255.0f), (float)((float)chamsColor2.getBlue() / 255.0f), (float)((float)chamsColor2.getAlpha() / 255.0f));
                }
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
            }
            this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
            if (chamsFlag) {
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                if (!textured) {
                    GL11.glColor4f((float)((float)chamsColor.getRed() / 255.0f), (float)((float)chamsColor.getGreen() / 255.0f), (float)((float)chamsColor.getBlue() / 255.0f), (float)((float)chamsColor.getAlpha() / 255.0f));
                }
                this.field_77045_g.func_78088_a(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
                if (!textured) {
                    GL11.glEnable((int)3553);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    GL11.glDisable((int)3042);
                    GL11.glEnable((int)2896);
                }
                GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
                GL11.glDisable((int)10754);
                GL11.glPopMatrix();
            }
            if (semiVisible) {
                GlStateManager.func_179084_k();
                GlStateManager.func_179092_a((int)516, (float)0.1f);
                GlStateManager.func_179121_F();
                GlStateManager.func_179132_a((boolean)true);
            }
        }
    }

    @Overwrite
    protected <T extends EntityLivingBase> void func_77036_a(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean semiVisible;
        boolean visible = !entitylivingbaseIn.func_82150_aj();
        TrueSight trueSight = (TrueSight)LiquidBounce.moduleManager.getModule(TrueSight.class);
        Chams chams = (Chams)LiquidBounce.moduleManager.getModule(Chams.class);
        boolean chamsFlag = chams.getState() && (Boolean)chams.getTargetsValue().get() != false && (Boolean)chams.getLegacyMode().get() == false && EntityUtils.isSelected(entitylivingbaseIn, false);
        boolean bl = semiVisible = !visible && (!entitylivingbaseIn.func_98034_c((EntityPlayer)Minecraft.func_71410_x().field_71439_g) || trueSight.getState() && (Boolean)trueSight.getEntitiesValue().get() != false);
        if (visible || semiVisible) {
            ESP esp;
            if (!this.func_180548_c(entitylivingbaseIn)) {
                return;
            }
            if (semiVisible) {
                GlStateManager.func_179094_E();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)0.15f);
                GlStateManager.func_179132_a((boolean)false);
                GlStateManager.func_179147_l();
                GlStateManager.func_179112_b((int)770, (int)771);
                GlStateManager.func_179092_a((int)516, (float)0.003921569f);
            }
            if ((esp = (ESP)LiquidBounce.moduleManager.getModule(ESP.class)).getState() && EntityUtils.isSelected(entitylivingbaseIn, false)) {
                Minecraft mc = Minecraft.func_71410_x();
                boolean fancyGraphics = mc.field_71474_y.field_74347_j;
                mc.field_71474_y.field_74347_j = false;
                float gamma = mc.field_71474_y.field_74333_Y;
                mc.field_71474_y.field_74333_Y = 100000.0f;
                switch (((String)esp.modeValue.get()).toLowerCase()) {
                    case "wireframe": {
                        GL11.glPushMatrix();
                        GL11.glPushAttrib((int)1048575);
                        GL11.glPolygonMode((int)1032, (int)6913);
                        GL11.glDisable((int)3553);
                        GL11.glDisable((int)2896);
                        GL11.glDisable((int)2929);
                        GL11.glEnable((int)2848);
                        GL11.glEnable((int)3042);
                        GL11.glBlendFunc((int)770, (int)771);
                        RenderUtils.glColor(esp.getColor((Entity)entitylivingbaseIn));
                        GL11.glLineWidth((float)((Float)esp.wireframeWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    }
                    case "outline": {
                        ClientUtils.disableFastRender();
                        GlStateManager.func_179117_G();
                        Color color = esp.getColor((Entity)entitylivingbaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(((Float)esp.outlineWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                    }
                }
                mc.field_71474_y.field_74347_j = fancyGraphics;
                mc.field_71474_y.field_74333_Y = gamma;
            }
            int blend = 3042;
            int depth = 2929;
            int srcAlpha = 770;
            int srcAlphaPlus1 = 771;
            int polygonOffsetLine = 10754;
            int texture2D = 3553;
            int lighting = 2896;
            boolean textured = (Boolean)chams.getTexturedValue().get();
            Color chamsColor = new Color(0);
            switch ((String)chams.getColorModeValue().get()) {
                case "Custom": {
                    chamsColor = new Color((Integer)chams.getRedValue().get(), (Integer)chams.getGreenValue().get(), (Integer)chams.getBlueValue().get());
                    break;
                }
                case "LiquidSlowly": {
                    chamsColor = ColorUtils.LiquidSlowly(System.nanoTime(), 0, ((Float)chams.getSaturationValue().get()).floatValue(), ((Float)chams.getBrightnessValue().get()).floatValue());
                }
            }
            chamsColor = ColorUtils.reAlpha(chamsColor, (Integer)chams.getAlphaValue().get());
            if (chamsFlag) {
                Color chamsColor2 = new Color(0);
                switch ((String)chams.getBehindColorModeValue().get()) {
                    case "Same": {
                        chamsColor2 = chamsColor;
                        break;
                    }
                    case "Opposite": {
                        chamsColor2 = ColorUtils.getOppositeColor(chamsColor);
                        break;
                    }
                    case "Red": {
                        chamsColor2 = new Color(-1104346);
                    }
                }
                GL11.glPushMatrix();
                GL11.glEnable((int)10754);
                GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
                OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)240.0f, (float)240.0f);
                if (!textured) {
                    GL11.glEnable((int)3042);
                    GL11.glDisable((int)3553);
                    GL11.glDisable((int)2896);
                    GL11.glBlendFunc((int)770, (int)771);
                    GL11.glColor4f((float)((float)chamsColor2.getRed() / 255.0f), (float)((float)chamsColor2.getGreen() / 255.0f), (float)((float)chamsColor2.getBlue() / 255.0f), (float)((float)chamsColor2.getAlpha() / 255.0f));
                }
                GL11.glDisable((int)2929);
                GL11.glDepthMask((boolean)false);
            }
            this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
            if (chamsFlag) {
                GL11.glEnable((int)2929);
                GL11.glDepthMask((boolean)true);
                if (!textured) {
                    GL11.glColor4f((float)((float)chamsColor.getRed() / 255.0f), (float)((float)chamsColor.getGreen() / 255.0f), (float)((float)chamsColor.getBlue() / 255.0f), (float)((float)chamsColor.getAlpha() / 255.0f));
                }
                this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                if (!textured) {
                    GL11.glEnable((int)3553);
                    GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                    GL11.glDisable((int)3042);
                    GL11.glEnable((int)2896);
                }
                GL11.glPolygonOffset((float)1.0f, (float)-1000000.0f);
                GL11.glDisable((int)10754);
                GL11.glPopMatrix();
            }
            if (semiVisible) {
                GlStateManager.func_179084_k();
                GlStateManager.func_179092_a((int)516, (float)0.1f);
                GlStateManager.func_179121_F();
                GlStateManager.func_179132_a((boolean)true);
            }
        }
    }
}

