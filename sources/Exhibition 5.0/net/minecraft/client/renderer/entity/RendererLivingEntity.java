// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.scoreboard.Team;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.WorldRenderer;
import exhibition.event.Event;
import net.minecraft.client.renderer.Tessellator;
import exhibition.event.EventSystem;
import exhibition.event.impl.EventNametagRender;
import java.util.Iterator;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import exhibition.util.TeamUtils;
import exhibition.module.impl.render.ESP2D;
import java.awt.Color;
import exhibition.util.RenderingUtil;
import exhibition.management.ColorManager;
import exhibition.management.friend.FriendManager;
import org.lwjgl.opengl.GL11;
import exhibition.util.render.Stencil;
import net.minecraft.entity.player.EntityPlayer;
import exhibition.module.impl.render.Outline;
import exhibition.module.impl.msgo.AntiAim;
import exhibition.Client;
import exhibition.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GLAllocation;
import java.util.List;
import java.nio.FloatBuffer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.logging.log4j.Logger;

public abstract class RendererLivingEntity extends Render
{
    private static final Logger logger;
    private static final DynamicTexture field_177096_e;
    protected ModelBase mainModel;
    protected FloatBuffer field_177095_g;
    protected List field_177097_h;
    public static boolean field_177098_i;
    private static final String __OBFID = "CL_00001012";
    
    public RendererLivingEntity(final RenderManager p_i46156_1_, final ModelBase p_i46156_2_, final float p_i46156_3_) {
        super(p_i46156_1_);
        this.field_177095_g = GLAllocation.createDirectFloatBuffer(4);
        this.field_177097_h = Lists.newArrayList();
        this.mainModel = p_i46156_2_;
        this.shadowSize = p_i46156_3_;
    }
    
    protected boolean addLayer(final LayerRenderer p_177094_1_) {
        return this.field_177097_h.add(p_177094_1_);
    }
    
    protected boolean func_177089_b(final LayerRenderer p_177089_1_) {
        return this.field_177097_h.remove(p_177089_1_);
    }
    
    public ModelBase getMainModel() {
        return this.mainModel;
    }
    
    protected float interpolateRotation(final float p_77034_1_, final float p_77034_2_, final float p_77034_3_) {
        float var4;
        for (var4 = p_77034_2_ - p_77034_1_; var4 < -180.0f; var4 += 360.0f) {}
        while (var4 >= 180.0f) {
            var4 -= 360.0f;
        }
        return p_77034_1_ + p_77034_3_ * var4;
    }
    
    public void func_82422_c() {
    }
    
    public void doRender(final EntityLivingBase p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(p_76986_1_, p_76986_9_);
        this.mainModel.isRiding = p_76986_1_.isRiding();
        this.mainModel.isChild = p_76986_1_.isChild();
        try {
            float var10 = this.interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
            final float var11 = this.interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
            float var12 = var11 - var10;
            if (p_76986_1_.isRiding() && p_76986_1_.ridingEntity instanceof EntityLivingBase) {
                final EntityLivingBase var13 = (EntityLivingBase)p_76986_1_.ridingEntity;
                var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, p_76986_9_);
                var12 = var11 - var10;
                float var14 = MathHelper.wrapAngleTo180_float(var12);
                if (var14 < -85.0f) {
                    var14 = -85.0f;
                }
                if (var14 >= 85.0f) {
                    var14 = 85.0f;
                }
                var10 = var11 - var14;
                if (var14 * var14 > 2500.0f) {
                    var10 += var14 * 0.2f;
                }
            }
            final float var15 = (Minecraft.getMinecraft().gameSettings.thirdPersonView != 0 && Client.getModuleManager().get(AntiAim.class).isEnabled() && p_76986_1_ == Minecraft.getMinecraft().thePlayer) ? (p_76986_1_.prevRotationPitch + (((AntiAim.rotationPitch != 0.0f) ? AntiAim.rotationPitch : p_76986_1_.rotationPitch) - p_76986_1_.prevRotationPitch)) : (p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_);
            this.renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
            float var14 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
            this.rotateCorpse(p_76986_1_, var14, var10, p_76986_9_);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(p_76986_1_, p_76986_9_);
            final float var16 = 0.0625f;
            GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
            float var17 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
            float var18 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0f - p_76986_9_);
            if (Client.getModuleManager().get(Outline.class).isEnabled() && p_76986_1_ instanceof EntityPlayer) {
                Stencil.checkSetupFBO();
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, var16);
                GL11.glBlendFunc(770, 771);
                Outline.renderOne();
                GL11.glLineWidth(3.0f);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glHint(3154, 4354);
                GL11.glEnable(3024);
                GlStateManager.enableAlpha();
                if (FriendManager.isFriend(p_76986_1_.getName())) {
                    if (p_76986_1_ == Minecraft.getMinecraft().thePlayer) {
                        GL11.glColor4f(200.0f, 200.0f, 200.0f, 255.0f);
                    }
                    else {
                        RenderingUtil.glColor(ColorManager.fVis.getColorInt());
                    }
                }
                else {
                    float health = p_76986_1_.getHealth();
                    if (health > 20.0f) {
                        health = 20.0f;
                    }
                    final float[] fractions = { 0.0f, 0.5f, 1.0f };
                    final Color[] colors = { Color.RED, Color.YELLOW, Color.GREEN };
                    final float progress = health * 5.0f * 0.01f;
                    final Color customColor = ESP2D.blendColors(fractions, colors, progress).brighter();
                    RenderingUtil.glColor(customColor.getRGB());
                    if (Outline.teamCheck) {
                        if (TeamUtils.isTeam(Minecraft.getMinecraft().thePlayer, (EntityPlayer)p_76986_1_)) {
                            final ColorManager cm = Client.cm;
                            RenderingUtil.glColor(ColorManager.fTeam.getColorInt());
                        }
                        else {
                            final ColorManager cm2 = Client.cm;
                            RenderingUtil.glColor(ColorManager.eTeam.getColorInt());
                        }
                    }
                }
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, var16);
                Outline.renderTwo();
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, var16);
                Outline.renderThree();
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, var16);
                Outline.renderFour();
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, var16);
                Outline.renderFive();
                GL11.glDisable(3024);
            }
            if (p_76986_1_.isChild()) {
                var18 *= 3.0f;
            }
            if (var17 > 1.0f) {
                var17 = 1.0f;
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations(p_76986_1_, var18, var17, p_76986_9_);
            this.mainModel.setRotationAngles(var18, var17, var14, var12, var15, 0.0625f, p_76986_1_);
            if (RendererLivingEntity.field_177098_i) {
                final boolean var19 = this.func_177088_c(p_76986_1_);
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, 0.0625f);
                if (var19) {
                    this.func_180565_e();
                }
            }
            else {
                final boolean var19 = this.func_177090_c(p_76986_1_, p_76986_9_);
                this.renderModel(p_76986_1_, var18, var17, var14, var12, var15, 0.0625f);
                if (var19) {
                    this.func_177091_f();
                }
                GlStateManager.depthMask(true);
                if (!(p_76986_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76986_1_).func_175149_v()) {
                    this.func_177093_a(p_76986_1_, var18, var17, p_76986_9_, var14, var12, var15, 0.0625f);
                }
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception var20) {
            RendererLivingEntity.logger.error("Couldn't render entity", (Throwable)var20);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!RendererLivingEntity.field_177098_i) {
            super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
        }
    }
    
    protected boolean func_177088_c(final EntityLivingBase p_177088_1_) {
        int var2 = 16777215;
        if (p_177088_1_ instanceof EntityPlayer) {
            final ScorePlayerTeam var3 = (ScorePlayerTeam)p_177088_1_.getTeam();
            if (var3 != null) {
                final String var4 = FontRenderer.getFormatFromString(var3.getColorPrefix());
                if (var4.length() >= 2) {
                    var2 = this.getFontRendererFromRenderManager().func_175064_b(var4.charAt(1));
                }
            }
        }
        final float var5 = (var2 >> 16 & 0xFF) / 255.0f;
        final float var6 = (var2 >> 8 & 0xFF) / 255.0f;
        final float var7 = (var2 & 0xFF) / 255.0f;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(var5, var6, var7, 1.0f);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void func_180565_e() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTextures();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected void renderModel(final EntityLivingBase p_77036_1_, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        final boolean var8 = !p_77036_1_.isInvisible();
        final boolean var9 = !var8 && !p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
        if (var8 || var9) {
            if (!this.bindEntityTexture(p_77036_1_)) {
                return;
            }
            if (var9) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            this.mainModel.render(p_77036_1_, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            if (var9) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
    
    protected boolean func_177090_c(final EntityLivingBase p_177090_1_, final float p_177090_2_) {
        return this.func_177092_a(p_177090_1_, p_177090_2_, true);
    }
    
    protected boolean func_177092_a(final EntityLivingBase p_177092_1_, final float p_177092_2_, final boolean p_177092_3_) {
        final float var4 = p_177092_1_.getBrightness(p_177092_2_);
        final int var5 = this.getColorMultiplier(p_177092_1_, var4, p_177092_2_);
        final boolean var6 = (var5 >> 24 & 0xFF) > 0;
        final boolean var7 = p_177092_1_.hurtTime > 0 || p_177092_1_.deathTime > 0;
        if (!var6 && !var7) {
            return false;
        }
        if (!var6 && !p_177092_3_) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTextures();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTextures();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, OpenGlHelper.field_176094_t);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_176092_v);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176080_A, OpenGlHelper.field_176092_v);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176076_D, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        this.field_177095_g.position(0);
        if (var7) {
            this.field_177095_g.put(1.0f);
            this.field_177095_g.put(0.0f);
            this.field_177095_g.put(0.0f);
            this.field_177095_g.put(0.3f);
        }
        else {
            final float var8 = (var5 >> 24 & 0xFF) / 255.0f;
            final float var9 = (var5 >> 16 & 0xFF) / 255.0f;
            final float var10 = (var5 >> 8 & 0xFF) / 255.0f;
            final float var11 = (var5 & 0xFF) / 255.0f;
            this.field_177095_g.put(var9);
            this.field_177095_g.put(var10);
            this.field_177095_g.put(var11);
            this.field_177095_g.put(1.0f - var8);
        }
        this.field_177095_g.flip();
        GL11.glTexEnv(8960, 8705, this.field_177095_g);
        GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
        GlStateManager.enableTextures();
        GlStateManager.func_179144_i(RendererLivingEntity.field_177096_e.getGlTextureId());
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void func_177091_f() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTextures();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176093_u);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176079_G, OpenGlHelper.field_176093_u);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176086_J, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, 5890);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
        GlStateManager.disableTextures();
        GlStateManager.func_179144_i(0);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.field_176095_s);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176099_x, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176081_B, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176082_C, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176098_y, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176097_z, OpenGlHelper.field_176091_w);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176077_E, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176085_I, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.field_176078_F, 5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected void renderLivingAt(final EntityLivingBase p_77039_1_, final double p_77039_2_, final double p_77039_4_, final double p_77039_6_) {
        GlStateManager.translate((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
    }
    
    protected void rotateCorpse(final EntityLivingBase p_77043_1_, final float p_77043_2_, final float p_77043_3_, final float p_77043_4_) {
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        if (p_77043_1_.deathTime > 0) {
            float var5 = (p_77043_1_.deathTime + p_77043_4_ - 1.0f) / 20.0f * 1.6f;
            var5 = MathHelper.sqrt_float(var5);
            if (var5 > 1.0f) {
                var5 = 1.0f;
            }
            GlStateManager.rotate(var5 * this.getDeathMaxRotation(p_77043_1_), 0.0f, 0.0f, 1.0f);
        }
        else {
            final String var6 = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getName());
            if (var6 != null && (var6.equals("Dinnerbone") || var6.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || ((EntityPlayer)p_77043_1_).func_175148_a(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, p_77043_1_.height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    protected float getSwingProgress(final EntityLivingBase p_77040_1_, final float p_77040_2_) {
        return p_77040_1_.getSwingProgress(p_77040_2_);
    }
    
    protected float handleRotationFloat(final EntityLivingBase p_77044_1_, final float p_77044_2_) {
        return p_77044_1_.ticksExisted + p_77044_2_;
    }
    
    protected void func_177093_a(final EntityLivingBase p_177093_1_, final float p_177093_2_, final float p_177093_3_, final float p_177093_4_, final float p_177093_5_, final float p_177093_6_, final float p_177093_7_, final float p_177093_8_) {
        for (final LayerRenderer var10 : this.field_177097_h) {
            final boolean var11 = this.func_177092_a(p_177093_1_, p_177093_4_, var10.shouldCombineTextures());
            var10.doRenderLayer(p_177093_1_, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
            if (var11) {
                this.func_177091_f();
            }
        }
    }
    
    protected float getDeathMaxRotation(final EntityLivingBase p_77037_1_) {
        return 90.0f;
    }
    
    protected int getColorMultiplier(final EntityLivingBase p_77030_1_, final float p_77030_2_, final float p_77030_3_) {
        return 0;
    }
    
    protected void preRenderCallback(final EntityLivingBase p_77041_1_, final float p_77041_2_) {
    }
    
    public void passSpecialRender(final EntityLivingBase p_77033_1_, final double p_77033_2_, final double p_77033_4_, final double p_77033_6_) {
        if (this.canRenderName(p_77033_1_)) {
            final double var8 = p_77033_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
            final float var9 = p_77033_1_.isSneaking() ? 32.0f : 64.0f;
            if (var8 < var9 * var9) {
                final String var10 = p_77033_1_.getDisplayName().getFormattedText();
                final float var11 = 0.02666667f;
                GlStateManager.alphaFunc(516, 0.1f);
                if (p_77033_1_.isSneaking()) {
                    final Event event = EventSystem.getInstance(EventNametagRender.class);
                    event.fire();
                    if (event.isCancelled()) {
                        return;
                    }
                    final FontRenderer var12 = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)p_77033_2_, (float)p_77033_4_ + p_77033_1_.height + 0.5f - (p_77033_1_.isChild() ? (p_77033_1_.height / 2.0f) : 0.0f), (float)p_77033_6_);
                    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                    final RenderManager renderManager = this.renderManager;
                    GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    final RenderManager renderManager2 = this.renderManager;
                    GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                    GlStateManager.translate(0.0f, 9.374999f, 0.0f);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTextures();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    final Tessellator var13 = Tessellator.getInstance();
                    final WorldRenderer var14 = var13.getWorldRenderer();
                    var14.startDrawingQuads();
                    final int var15 = var12.getStringWidth(var10) / 2;
                    var14.setColorRGBA(0.0f, 0.0f, 0.0f, 0.25f);
                    var14.addVertex(-var15 - 1, -1.0, 0.0);
                    var14.addVertex(-var15 - 1, 8.0, 0.0);
                    var14.addVertex(var15 + 1, 8.0, 0.0);
                    var14.addVertex(var15 + 1, -1.0, 0.0);
                    var13.draw();
                    GlStateManager.enableTextures();
                    GlStateManager.depthMask(true);
                    var12.drawString(var10, -var12.getStringWidth(var10) / 2, 0.0f, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }
                else {
                    this.func_177069_a(p_77033_1_, p_77033_2_, p_77033_4_ - (p_77033_1_.isChild() ? (p_77033_1_.height / 2.0f) : 0.0), p_77033_6_, var10, 0.02666667f, var8);
                }
            }
        }
    }
    
    protected boolean canRenderName(final EntityLivingBase targetEntity) {
        final EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;
        if (targetEntity instanceof EntityPlayer && targetEntity != var2) {
            final Team var3 = targetEntity.getTeam();
            final Team var4 = var2.getTeam();
            if (var3 != null) {
                final Team.EnumVisible var5 = var3.func_178770_i();
                switch (SwitchEnumVisible.field_178679_a[var5.ordinal()]) {
                    case 1: {
                        return true;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        return var4 == null || var3.isSameTeam(var4);
                    }
                    case 4: {
                        return var4 == null || !var3.isSameTeam(var4);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(var2) && targetEntity.riddenByEntity == null;
    }
    
    public void func_177086_a(final boolean p_177086_1_) {
        RendererLivingEntity.field_177098_i = p_177086_1_;
    }
    
    @Override
    protected boolean func_177070_b(final Entity p_177070_1_) {
        return this.canRenderName((EntityLivingBase)p_177070_1_);
    }
    
    public void func_177067_a(final Entity p_177067_1_, final double p_177067_2_, final double p_177067_4_, final double p_177067_6_) {
        this.passSpecialRender((EntityLivingBase)p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
    
    static {
        logger = LogManager.getLogger();
        field_177096_e = new DynamicTexture(16, 16);
        RendererLivingEntity.field_177098_i = false;
        final int[] var0 = RendererLivingEntity.field_177096_e.getTextureData();
        for (int var2 = 0; var2 < 256; ++var2) {
            var0[var2] = -1;
        }
        RendererLivingEntity.field_177096_e.updateDynamicTexture();
    }
    
    static final class SwitchEnumVisible
    {
        static final int[] field_178679_a;
        private static final String __OBFID = "CL_00002435";
        
        static {
            field_178679_a = new int[Team.EnumVisible.values().length];
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
