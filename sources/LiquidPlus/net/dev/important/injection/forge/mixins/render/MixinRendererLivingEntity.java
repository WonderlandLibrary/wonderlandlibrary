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
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package net.dev.important.injection.forge.mixins.render;

import java.awt.Color;
import net.dev.important.Client;
import net.dev.important.injection.forge.mixins.render.MixinRender;
import net.dev.important.modules.module.modules.color.ColorMixer;
import net.dev.important.modules.module.modules.render.Chams;
import net.dev.important.modules.module.modules.render.ESP;
import net.dev.important.modules.module.modules.render.ESP2D;
import net.dev.important.modules.module.modules.render.NameTags;
import net.dev.important.modules.module.modules.render.NoRender;
import net.dev.important.modules.module.modules.render.TrueSight;
import net.dev.important.utils.ClientUtils;
import net.dev.important.utils.EntityUtils;
import net.dev.important.utils.render.ColorUtils;
import net.dev.important.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

    @Inject(method={"doRender"}, at={@At(value="HEAD")}, cancellable=true)
    private <T extends EntityLivingBase> void injectChamsPre(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo callbackInfo) {
        Chams chams = (Chams)Client.moduleManager.getModule(Chams.class);
        NoRender noRender = (NoRender)Client.moduleManager.getModule(NoRender.class);
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
        Chams chams = (Chams)Client.moduleManager.getModule(Chams.class);
        NoRender noRender = (NoRender)Client.moduleManager.getModule(NoRender.class);
        if (chams.getState() && ((Boolean)chams.getTargetsValue().get()).booleanValue() && ((Boolean)chams.getLegacyMode().get()).booleanValue() && EntityUtils.isSelected(entity, false) && (!noRender.getState() || !noRender.shouldStopRender((Entity)entity))) {
            GL11.glPolygonOffset((float)1.0f, (float)1000000.0f);
            GL11.glDisable((int)32823);
        }
    }

    @Inject(method={"canRenderName"}, at={@At(value="HEAD")}, cancellable=true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        NoRender noRender = (NoRender)Client.moduleManager.getModule(NoRender.class);
        if (!ESP.renderNameTags || Client.moduleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected(entity, false) || ESP2D.shouldCancelNameTag(entity) || noRender.getState() && ((Boolean)noRender.getNameTagsValue().get()).booleanValue()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Overwrite
    protected <T extends EntityLivingBase> void func_77036_a(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean semiVisible;
        boolean visible = !entitylivingbaseIn.func_82150_aj();
        TrueSight trueSight = (TrueSight)Client.moduleManager.getModule(TrueSight.class);
        Chams chams = (Chams)Client.moduleManager.getModule(Chams.class);
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
            if ((esp = (ESP)Client.moduleManager.getModule(ESP.class)).getState() && EntityUtils.isSelected(entitylivingbaseIn, false)) {
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
                        RenderUtils.setColor(color);
                        RenderUtils.renderOne(((Float)esp.outlineWidth.get()).floatValue());
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        RenderUtils.setColor(color);
                        RenderUtils.renderTwo();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        RenderUtils.setColor(color);
                        RenderUtils.renderThree();
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        RenderUtils.setColor(color);
                        RenderUtils.renderFour(color);
                        this.field_77045_g.func_78088_a(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        RenderUtils.setColor(color);
                        RenderUtils.renderFive();
                        RenderUtils.setColor(Color.WHITE);
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
                case "Rainbow": {
                    chamsColor = new Color(RenderUtils.getRainbowOpaque((Integer)chams.getMixerSecondsValue().get(), ((Float)chams.getSaturationValue().get()).floatValue(), ((Float)chams.getBrightnessValue().get()).floatValue(), 0));
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
                case "Mixer": {
                    chamsColor = ColorMixer.getMixedColor(0, (Integer)chams.getMixerSecondsValue().get());
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

