/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.GL11
 */
package net.minecraft.client.renderer.entity;

import com.google.common.collect.Lists;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import optifine.Config;
import optifine.Reflector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import shadersmod.client.Shaders;

public abstract class RendererLivingEntity
extends Render {
    private static final Logger logger = LogManager.getLogger();
    private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
    protected ModelBase mainModel;
    protected FloatBuffer field_177095_g = GLAllocation.createDirectFloatBuffer(4);
    protected List field_177097_h = Lists.newArrayList();
    protected boolean field_177098_i = false;
    private static final String __OBFID = "CL_00001012";
    public static float NAME_TAG_RANGE = 64.0f;
    public static float NAME_TAG_RANGE_SNEAK = 32.0f;

    static {
        int[] var0 = field_177096_e.getTextureData();
        for (int var1 = 0; var1 < 256; ++var1) {
            var0[var1] = -1;
        }
        field_177096_e.updateDynamicTexture();
    }

    public RendererLivingEntity(RenderManager p_i46156_1_, ModelBase p_i46156_2_, float p_i46156_3_) {
        super(p_i46156_1_);
        this.mainModel = p_i46156_2_;
        this.shadowSize = p_i46156_3_;
    }

    public boolean addLayer(LayerRenderer p_177094_1_) {
        return this.field_177097_h.add(p_177094_1_);
    }

    protected boolean func_177089_b(LayerRenderer p_177089_1_) {
        return this.field_177097_h.remove(p_177089_1_);
    }

    public ModelBase getMainModel() {
        return this.mainModel;
    }

    protected float interpolateRotation(float p_77034_1_, float p_77034_2_, float p_77034_3_) {
        float var4;
        for (var4 = p_77034_2_ - p_77034_1_; var4 < -180.0f; var4 += 360.0f) {
        }
        while (var4 >= 180.0f) {
            var4 -= 360.0f;
        }
        return p_77034_1_ + p_77034_3_ * var4;
    }

    public void func_82422_c() {
    }

    public void doRender(EntityLivingBase p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        block19: {
            block18: {
                if (!Reflector.RenderLivingEvent_Pre_Constructor.exists()) break block18;
                if (Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, p_76986_1_, this, p_76986_2_, p_76986_4_, p_76986_6_)) break block19;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(p_76986_1_, p_76986_9_);
            this.mainModel.isRiding = p_76986_1_.isRiding();
            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = p_76986_1_.isRiding() && p_76986_1_.ridingEntity != null && Reflector.callBoolean(p_76986_1_.ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0]);
            }
            this.mainModel.isChild = p_76986_1_.isChild();
            try {
                float var14;
                float var19 = this.interpolateRotation(p_76986_1_.prevRenderYawOffset, p_76986_1_.renderYawOffset, p_76986_9_);
                float var11 = this.interpolateRotation(p_76986_1_.prevRotationYawHead, p_76986_1_.rotationYawHead, p_76986_9_);
                float var12 = var11 - var19;
                if (this.mainModel.isRiding && p_76986_1_.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase var20 = (EntityLivingBase)p_76986_1_.ridingEntity;
                    var19 = this.interpolateRotation(var20.prevRenderYawOffset, var20.renderYawOffset, p_76986_9_);
                    var12 = var11 - var19;
                    var14 = MathHelper.wrapAngleTo180_float(var12);
                    if (var14 < -85.0f) {
                        var14 = -85.0f;
                    }
                    if (var14 >= 85.0f) {
                        var14 = 85.0f;
                    }
                    var19 = var11 - var14;
                    if (var14 * var14 > 2500.0f) {
                        var19 += var14 * 0.2f;
                    }
                }
                float var201 = p_76986_1_.prevRotationPitch + (p_76986_1_.rotationPitch - p_76986_1_.prevRotationPitch) * p_76986_9_;
                this.renderLivingAt(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_);
                var14 = this.handleRotationFloat(p_76986_1_, p_76986_9_);
                this.rotateCorpse(p_76986_1_, var14, var19, p_76986_9_);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0f, -1.0f, 1.0f);
                this.preRenderCallback(p_76986_1_, p_76986_9_);
                float var15 = 0.0625f;
                GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
                float var16 = p_76986_1_.prevLimbSwingAmount + (p_76986_1_.limbSwingAmount - p_76986_1_.prevLimbSwingAmount) * p_76986_9_;
                float var17 = p_76986_1_.limbSwing - p_76986_1_.limbSwingAmount * (1.0f - p_76986_9_);
                if (p_76986_1_.isChild()) {
                    var17 *= 3.0f;
                }
                if (var16 > 1.0f) {
                    var16 = 1.0f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(p_76986_1_, var17, var16, p_76986_9_);
                this.mainModel.setRotationAngles(var17, var16, var14, var12, var201, 0.0625f, p_76986_1_);
                if (this.field_177098_i) {
                    boolean var18 = this.func_177088_c(p_76986_1_);
                    this.renderModel(p_76986_1_, var17, var16, var14, var12, var201, 0.0625f);
                    if (var18) {
                        this.func_180565_e();
                    }
                } else {
                    boolean var18 = this.func_177090_c(p_76986_1_, p_76986_9_);
                    this.renderModel(p_76986_1_, var17, var16, var14, var12, var201, 0.0625f);
                    if (var18) {
                        this.func_177091_f();
                    }
                    GlStateManager.depthMask(true);
                    if (!(p_76986_1_ instanceof EntityPlayer) || !((EntityPlayer)p_76986_1_).func_175149_v()) {
                        this.func_177093_a(p_76986_1_, var17, var16, p_76986_9_, var14, var12, var201, 0.0625f);
                    }
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception var191) {
                logger.error("Couldn't render entity", (Throwable)var191);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.func_179098_w();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!this.field_177098_i) {
                super.doRender(p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
            }
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                if (!Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, p_76986_1_, this, p_76986_2_, p_76986_4_, p_76986_6_)) {
                    // empty if block
                }
            }
        }
    }

    protected boolean func_177088_c(EntityLivingBase p_177088_1_) {
        String var7;
        ScorePlayerTeam var6;
        int var2 = 0xFFFFFF;
        if (p_177088_1_ instanceof EntityPlayer && (var6 = (ScorePlayerTeam)p_177088_1_.getTeam()) != null && (var7 = FontRenderer.getFormatFromString(var6.getColorPrefix())).length() >= 2) {
            var2 = this.getFontRendererFromRenderManager().func_175064_b(var7.charAt(1));
        }
        float var61 = (float)(var2 >> 16 & 0xFF) / 255.0f;
        float var71 = (float)(var2 >> 8 & 0xFF) / 255.0f;
        float var5 = (float)(var2 & 0xFF) / 255.0f;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(var61, var71, var5, 1.0f);
        GlStateManager.func_179090_x();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.func_179090_x();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void func_180565_e() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.func_179098_w();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.func_179098_w();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected void renderModel(EntityLivingBase p_77036_1_, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_) {
        boolean var9;
        boolean var8 = !p_77036_1_.isInvisible();
        boolean bl = var9 = !var8 && !p_77036_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
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

    protected boolean func_177090_c(EntityLivingBase p_177090_1_, float p_177090_2_) {
        return this.func_177092_a(p_177090_1_, p_177090_2_, true);
    }

    protected boolean func_177092_a(EntityLivingBase p_177092_1_, float p_177092_2_, boolean p_177092_3_) {
        boolean var7;
        float var4 = p_177092_1_.getBrightness(p_177092_2_);
        int var5 = this.getColorMultiplier(p_177092_1_, var4, p_177092_2_);
        boolean var6 = (var5 >> 24 & 0xFF) > 0;
        boolean bl = var7 = p_177092_1_.hurtTime > 0 || p_177092_1_.deathTime > 0;
        if (!var6 && !var7) {
            return false;
        }
        if (!var6 && !p_177092_3_) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.func_179098_w();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.field_176093_u);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.func_179098_w();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)OpenGlHelper.field_176094_t);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)OpenGlHelper.field_176092_v);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176080_A, (int)OpenGlHelper.field_176092_v);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176076_D, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        this.field_177095_g.position(0);
        if (var7) {
            this.field_177095_g.put(1.0f);
            this.field_177095_g.put(0.0f);
            this.field_177095_g.put(0.0f);
            this.field_177095_g.put(0.3f);
            if (Config.isShaders()) {
                Shaders.setEntityColor(1.0f, 0.0f, 0.0f, 0.3f);
            }
        } else {
            float var8 = (float)(var5 >> 24 & 0xFF) / 255.0f;
            float var9 = (float)(var5 >> 16 & 0xFF) / 255.0f;
            float var10 = (float)(var5 >> 8 & 0xFF) / 255.0f;
            float var11 = (float)(var5 & 0xFF) / 255.0f;
            this.field_177095_g.put(var9);
            this.field_177095_g.put(var10);
            this.field_177095_g.put(var11);
            this.field_177095_g.put(1.0f - var8);
            if (Config.isShaders()) {
                Shaders.setEntityColor(var9, var10, var11, 1.0f - var8);
            }
        }
        this.field_177095_g.flip();
        GL11.glTexEnv((int)8960, (int)8705, (FloatBuffer)this.field_177095_g);
        GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
        GlStateManager.func_179098_w();
        GlStateManager.func_179144_i(field_177096_e.getGlTextureId());
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected void func_177091_f() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.func_179098_w();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.field_176093_u);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176079_G, (int)OpenGlHelper.field_176093_u);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176086_J, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)5890);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)5890);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.field_176096_r);
        GlStateManager.func_179090_x();
        GlStateManager.func_179144_i(0);
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.field_176095_s);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176099_x, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176081_B, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176082_C, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176098_y, (int)5890);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176097_z, (int)OpenGlHelper.field_176091_w);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176077_E, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176085_I, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.field_176078_F, (int)5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }

    protected void renderLivingAt(EntityLivingBase p_77039_1_, double p_77039_2_, double p_77039_4_, double p_77039_6_) {
        GlStateManager.translate((float)p_77039_2_, (float)p_77039_4_, (float)p_77039_6_);
    }

    protected void rotateCorpse(EntityLivingBase p_77043_1_, float p_77043_2_, float p_77043_3_, float p_77043_4_) {
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        if (p_77043_1_.deathTime > 0) {
            float var6 = ((float)p_77043_1_.deathTime + p_77043_4_ - 1.0f) / 20.0f * 1.6f;
            if ((var6 = MathHelper.sqrt_float(var6)) > 1.0f) {
                var6 = 1.0f;
            }
            GlStateManager.rotate(var6 * this.getDeathMaxRotation(p_77043_1_), 0.0f, 0.0f, 1.0f);
        } else {
            String var61 = EnumChatFormatting.getTextWithoutFormattingCodes(p_77043_1_.getName());
            if (var61 != null && (var61.equals("Dinnerbone") || var61.equals("Grumm")) && (!(p_77043_1_ instanceof EntityPlayer) || ((EntityPlayer)p_77043_1_).func_175148_a(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, p_77043_1_.height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }

    protected float getSwingProgress(EntityLivingBase p_77040_1_, float p_77040_2_) {
        return p_77040_1_.getSwingProgress(p_77040_2_);
    }

    protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_) {
        return (float)p_77044_1_.ticksExisted + p_77044_2_;
    }

    protected void func_177093_a(EntityLivingBase p_177093_1_, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_) {
        for (LayerRenderer var10 : this.field_177097_h) {
            boolean var11 = this.func_177092_a(p_177093_1_, p_177093_4_, var10.shouldCombineTextures());
            var10.doRenderLayer(p_177093_1_, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
            if (!var11) continue;
            this.func_177091_f();
        }
    }

    protected float getDeathMaxRotation(EntityLivingBase p_77037_1_) {
        return 90.0f;
    }

    protected int getColorMultiplier(EntityLivingBase p_77030_1_, float p_77030_2_, float p_77030_3_) {
        return 0;
    }

    protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_) {
    }

    public void passSpecialRender(EntityLivingBase p_77033_1_, double p_77033_2_, double p_77033_4_, double p_77033_6_) {
        block10: {
            block9: {
                if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists()) break block9;
                if (Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_)) break block10;
            }
            if (this.canRenderName(p_77033_1_)) {
                float var10;
                double var8 = p_77033_1_.getDistanceSqToEntity(this.renderManager.livingPlayer);
                float f = var10 = p_77033_1_.isSneaking() ? NAME_TAG_RANGE_SNEAK : NAME_TAG_RANGE;
                if (var8 < (double)(var10 * var10)) {
                    String var11 = p_77033_1_.getDisplayName().getFormattedText();
                    float var12 = 0.02666667f;
                    GlStateManager.alphaFunc(516, 0.1f);
                    if (p_77033_1_.isSneaking()) {
                        FontRenderer var13 = this.getFontRendererFromRenderManager();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float)p_77033_2_, (float)p_77033_4_ + p_77033_1_.height + 0.5f - (p_77033_1_.isChild() ? p_77033_1_.height / 2.0f : 0.0f), (float)p_77033_6_);
                        GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                        GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                        GlStateManager.translate(0.0f, 9.374999f, 0.0f);
                        GlStateManager.disableLighting();
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.func_179090_x();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        Tessellator var14 = Tessellator.getInstance();
                        WorldRenderer var15 = var14.getWorldRenderer();
                        var15.startDrawingQuads();
                        int var16 = var13.getStringWidth(var11) / 2;
                        var15.func_178960_a(0.0f, 0.0f, 0.0f, 0.25f);
                        var15.addVertex(-var16 - 1, -1.0, 0.0);
                        var15.addVertex(-var16 - 1, 8.0, 0.0);
                        var15.addVertex(var16 + 1, 8.0, 0.0);
                        var15.addVertex(var16 + 1, -1.0, 0.0);
                        var14.draw();
                        GlStateManager.func_179098_w();
                        GlStateManager.depthMask(true);
                        var13.drawString(var11, -var13.getStringWidth(var11) / 2, 0.0, 0x20FFFFFF);
                        GlStateManager.enableLighting();
                        GlStateManager.disableBlend();
                        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                        GlStateManager.popMatrix();
                    } else {
                        this.func_177069_a(p_77033_1_, p_77033_2_, p_77033_4_ - (p_77033_1_.isChild() ? (double)(p_77033_1_.height / 2.0f) : 0.0), p_77033_6_, var11, 0.02666667f, var8);
                    }
                }
            }
            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
                if (!Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, p_77033_1_, this, p_77033_2_, p_77033_4_, p_77033_6_)) {
                    // empty if block
                }
            }
        }
    }

    protected boolean canRenderName(EntityLivingBase targetEntity) {
        EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;
        if (targetEntity instanceof EntityPlayer && targetEntity != var2) {
            Team var3 = targetEntity.getTeam();
            Team var4 = var2.getTeam();
            if (var3 != null) {
                Team.EnumVisible var5 = var3.func_178770_i();
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
                }
                return true;
            }
        }
        return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(var2) && targetEntity.riddenByEntity == null;
    }

    public void func_177086_a(boolean p_177086_1_) {
        this.field_177098_i = p_177086_1_;
    }

    @Override
    protected boolean func_177070_b(Entity p_177070_1_) {
        return this.canRenderName((EntityLivingBase)p_177070_1_);
    }

    @Override
    public void func_177067_a(Entity p_177067_1_, double p_177067_2_, double p_177067_4_, double p_177067_6_) {
        this.passSpecialRender((EntityLivingBase)p_177067_1_, p_177067_2_, p_177067_4_, p_177067_6_);
    }

    @Override
    public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_) {
        this.doRender((EntityLivingBase)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }

    static final class SwitchEnumVisible {
        static final int[] field_178679_a = new int[Team.EnumVisible.values().length];

        static {
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
            try {
                SwitchEnumVisible.field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError) {
                // empty catch block
            }
        }

        SwitchEnumVisible() {
        }
    }
}

