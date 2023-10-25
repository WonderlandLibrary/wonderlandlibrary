/*
 * Decompiled with CFR 0.152.
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
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventType;
import me.Tengoku.Terror.event.events.EventRenderEntity;
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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

public abstract class RendererLivingEntity<T extends EntityLivingBase>
extends Render<T> {
    protected ModelBase mainModel;
    protected List<LayerRenderer<T>> layerRenderers;
    private static final DynamicTexture field_177096_e;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
    private static final Logger logger;
    protected boolean renderOutlines = false;

    static {
        logger = LogManager.getLogger();
        field_177096_e = new DynamicTexture(16, 16);
        int[] nArray = field_177096_e.getTextureData();
        int n = 0;
        while (n < 256) {
            nArray[n] = -1;
            ++n;
        }
        field_177096_e.updateDynamicTexture();
    }

    public void setRenderOutlines(boolean bl) {
        this.renderOutlines = bl;
    }

    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(U u) {
        return this.layerRenderers.remove(u);
    }

    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(U u) {
        return this.layerRenderers.add(u);
    }

    protected void renderLivingAt(T t, double d, double d2, double d3) {
        GlStateManager.translate((float)d, (float)d2, (float)d3);
    }

    /*
     * Unable to fully structure code
     */
    protected void renderModel(T var1_1, float var2_2, float var3_3, float var4_4, float var5_5, float var6_6, float var7_7) {
        v0 = var8_8 = var1_1.isInvisible() == false;
        if (var8_8) ** GOTO lbl-1000
        Minecraft.getMinecraft();
        if (!var1_1.isInvisibleToPlayer(Minecraft.thePlayer)) {
            v1 = true;
        } else lbl-1000:
        // 2 sources

        {
            v1 = var9_9 = false;
        }
        if (var8_8 || var9_9) {
            if (!this.bindEntityTexture(var1_1)) {
                return;
            }
            if (var9_9) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            this.mainModel.render((Entity)var1_1, var2_2, var3_3, var4_4, var5_5, var6_6, var7_7);
            if (var9_9) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }

    protected void renderLayers(T t, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        for (LayerRenderer<T> layerRenderer : this.layerRenderers) {
            boolean bl = this.setBrightness(t, f3, layerRenderer.shouldCombineTextures());
            layerRenderer.doRenderLayer(t, f, f2, f3, f4, f5, f6, f7);
            if (!bl) continue;
            this.unsetBrightness();
        }
    }

    protected void preRenderCallback(T t, float f) {
    }

    protected boolean setDoRenderBrightness(T t, float f) {
        return this.setBrightness(t, f, true);
    }

    @Override
    protected boolean canRenderName(T t) {
        Minecraft.getMinecraft();
        EntityPlayerSP entityPlayerSP = Minecraft.thePlayer;
        if (t instanceof EntityPlayer && t != entityPlayerSP) {
            Team team = ((EntityLivingBase)t).getTeam();
            Team team2 = entityPlayerSP.getTeam();
            if (team != null) {
                Team.EnumVisible enumVisible = team.getNameTagVisibility();
                switch (enumVisible) {
                    case ALWAYS: {
                        return true;
                    }
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return team2 == null || team.isSameTeam(team2);
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return team2 == null || !team.isSameTeam(team2);
                    }
                }
                return true;
            }
        }
        return Minecraft.isGuiEnabled() && t != this.renderManager.livingPlayer && !((Entity)t).isInvisibleToPlayer(entityPlayerSP) && ((EntityLivingBase)t).riddenByEntity == null;
    }

    @Override
    public void renderName(T t, double d, double d2, double d3) {
        if (this.canRenderName(t)) {
            float f;
            double d4 = ((Entity)t).getDistanceSqToEntity(this.renderManager.livingPlayer);
            float f2 = f = ((Entity)t).isSneaking() ? 32.0f : 64.0f;
            if (d4 < (double)(f * f)) {
                String string = ((Entity)t).getDisplayName().getFormattedText();
                float f3 = 0.02666667f;
                GlStateManager.alphaFunc(516, 0.1f);
                if (((Entity)t).isSneaking()) {
                    FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)d, (float)d2 + ((EntityLivingBase)t).height + 0.5f - (((EntityLivingBase)t).isChild() ? ((EntityLivingBase)t).height / 2.0f : 0.0f), (float)d3);
                    GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
                    GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                    GlStateManager.translate(0.0f, 9.374999f, 0.0f);
                    GlStateManager.disableLighting();
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.disableTexture2D();
                    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                    int n = fontRenderer.getStringWidth(string) / 2;
                    Tessellator tessellator = Tessellator.getInstance();
                    WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                    worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldRenderer.pos(-n - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(-n - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(n + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldRenderer.pos(n + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontRenderer.drawString(string, -fontRenderer.getStringWidth(string) / 2, 0.0, 0x20FFFFFF);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                } else {
                    this.renderOffsetLivingLabel(t, d, d2 - (((EntityLivingBase)t).isChild() ? (double)(((EntityLivingBase)t).height / 2.0f) : 0.0), d3, string, 0.02666667f, d4);
                }
            }
        }
    }

    protected void rotateCorpse(T t, float f, float f2, float f3) {
        GlStateManager.rotate(180.0f - f2, 0.0f, 1.0f, 0.0f);
        if (((EntityLivingBase)t).deathTime > 0) {
            float f4 = ((float)((EntityLivingBase)t).deathTime + f3 - 1.0f) / 20.0f * 1.6f;
            if ((f4 = MathHelper.sqrt_float(f4)) > 1.0f) {
                f4 = 1.0f;
            }
            GlStateManager.rotate(f4 * this.getDeathMaxRotation(t), 0.0f, 0.0f, 1.0f);
        } else {
            String string = EnumChatFormatting.getTextWithoutFormattingCodes(((Entity)t).getName());
            if (string != null && (string.equals("Dinnerbone") || string.equals("Grumm")) && (!(t instanceof EntityPlayer) || ((EntityPlayer)t).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, ((EntityLivingBase)t).height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }

    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public RendererLivingEntity(RenderManager renderManager, ModelBase modelBase, float f) {
        super(renderManager);
        this.layerRenderers = Lists.newArrayList();
        this.mainModel = modelBase;
        this.shadowSize = f;
    }

    protected boolean setBrightness(T t, float f, boolean bl) {
        boolean bl2;
        float f2 = ((Entity)t).getBrightness(f);
        int n = this.getColorMultiplier(t, f2, f);
        boolean bl3 = (n >> 24 & 0xFF) > 0;
        boolean bl4 = bl2 = ((EntityLivingBase)t).hurtTime > 0 || ((EntityLivingBase)t).deathTime > 0;
        if (!bl3 && !bl2) {
            return false;
        }
        if (!bl3 && !bl) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)OpenGlHelper.GL_INTERPOLATE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE2_RGB, (int)OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND2_RGB, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        this.brightnessBuffer.position(0);
        if (bl2) {
            this.brightnessBuffer.put(1.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.3f);
        } else {
            float f3 = (float)(n >> 24 & 0xFF) / 255.0f;
            float f4 = (float)(n >> 16 & 0xFF) / 255.0f;
            float f5 = (float)(n >> 8 & 0xFF) / 255.0f;
            float f6 = (float)(n & 0xFF) / 255.0f;
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(f5);
            this.brightnessBuffer.put(f6);
            this.brightnessBuffer.put(1.0f - f3);
        }
        this.brightnessBuffer.flip();
        GL11.glTexEnv((int)8960, (int)8705, (FloatBuffer)this.brightnessBuffer);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(field_177096_e.getGlTextureId());
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)7681);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected int getColorMultiplier(T t, float f, float f2) {
        return 0;
    }

    protected float interpolateRotation(float f, float f2, float f3) {
        float f4 = f2 - f;
        while (f4 < -180.0f) {
            f4 += 360.0f;
        }
        while (f4 >= 180.0f) {
            f4 -= 360.0f;
        }
        return f + f3 * f4;
    }

    protected float handleRotationFloat(T t, float f) {
        return (float)((EntityLivingBase)t).ticksExisted + f;
    }

    public ModelBase getMainModel() {
        return this.mainModel;
    }

    protected boolean setScoreTeamColor(T t) {
        String string;
        ScorePlayerTeam scorePlayerTeam;
        int n = 0xFFFFFF;
        if (t instanceof EntityPlayer && (scorePlayerTeam = (ScorePlayerTeam)((EntityLivingBase)t).getTeam()) != null && (string = FontRenderer.getFormatFromString(scorePlayerTeam.getColorPrefix())).length() >= 2) {
            n = this.getFontRendererFromRenderManager().getColorCode(string.charAt(1));
        }
        float f = (float)(n >> 16 & 0xFF) / 255.0f;
        float f2 = (float)(n >> 8 & 0xFF) / 255.0f;
        float f3 = (float)(n & 0xFF) / 255.0f;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(f, f2, f3, 1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }

    protected float getSwingProgress(T t, float f) {
        return ((EntityLivingBase)t).getSwingProgress(f);
    }

    public void transformHeldFull3DItemLayer() {
    }

    protected void unsetBrightness() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_ALPHA, (int)OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_ALPHA, (int)770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)5890);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)5890);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        GL11.glTexEnvi((int)8960, (int)8704, (int)OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_RGB, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND1_RGB, (int)768);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_RGB, (int)5890);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE1_RGB, (int)OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_COMBINE_ALPHA, (int)8448);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_OPERAND0_ALPHA, (int)770);
        GL11.glTexEnvi((int)8960, (int)OpenGlHelper.GL_SOURCE0_ALPHA, (int)5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    protected float getDeathMaxRotation(T t) {
        return 90.0f;
    }

    @Override
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        EventRenderEntity eventRenderEntity = new EventRenderEntity((Entity)t);
        eventRenderEntity.call();
        Exodus.onEvent(eventRenderEntity);
        eventRenderEntity.setType(EventType.PRE);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        this.mainModel.swingProgress = this.getSwingProgress(t, f2);
        this.mainModel.isRiding = ((Entity)t).isRiding();
        this.mainModel.isChild = ((EntityLivingBase)t).isChild();
        try {
            float f3;
            float f4 = this.interpolateRotation(((EntityLivingBase)t).prevRenderYawOffset, ((EntityLivingBase)t).renderYawOffset, f2);
            float f5 = this.interpolateRotation(((EntityLivingBase)t).prevRotationYawHead, ((EntityLivingBase)t).rotationYawHead, f2);
            float f6 = f5 - f4;
            if (((Entity)t).isRiding() && ((EntityLivingBase)t).ridingEntity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase)((EntityLivingBase)t).ridingEntity;
                f4 = this.interpolateRotation(entityLivingBase.prevRenderYawOffset, entityLivingBase.renderYawOffset, f2);
                f6 = f5 - f4;
                f3 = MathHelper.wrapAngleTo180_float(f6);
                if (f3 < -85.0f) {
                    f3 = -85.0f;
                }
                if (f3 >= 85.0f) {
                    f3 = 85.0f;
                }
                f4 = f5 - f3;
                if (f3 * f3 > 2500.0f) {
                    f4 += f3 * 0.2f;
                }
            }
            float f7 = t == this.renderManager.livingPlayer ? ((EntityLivingBase)t).prevRotationPitchHead + (((EntityLivingBase)t).rotationPitchHead - ((EntityLivingBase)t).prevRotationPitchHead) * f2 : ((EntityLivingBase)t).prevRotationPitch + (((EntityLivingBase)t).rotationPitch - ((EntityLivingBase)t).prevRotationPitch) * f2;
            this.renderLivingAt(t, d, d2, d3);
            f3 = this.handleRotationFloat(t, f2);
            this.rotateCorpse(t, f3, f4, f2);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(-1.0f, -1.0f, 1.0f);
            this.preRenderCallback(t, f2);
            float f8 = 0.0625f;
            GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
            float f9 = ((EntityLivingBase)t).prevLimbSwingAmount + (((EntityLivingBase)t).limbSwingAmount - ((EntityLivingBase)t).prevLimbSwingAmount) * f2;
            float f10 = ((EntityLivingBase)t).limbSwing - ((EntityLivingBase)t).limbSwingAmount * (1.0f - f2);
            if (((EntityLivingBase)t).isChild()) {
                f10 *= 3.0f;
            }
            if (f9 > 1.0f) {
                f9 = 1.0f;
            }
            GlStateManager.enableAlpha();
            this.mainModel.setLivingAnimations((EntityLivingBase)t, f10, f9, f2);
            this.mainModel.setRotationAngles(f10, f9, f3, f6, f7, 0.0625f, (Entity)t);
            if (this.renderOutlines) {
                boolean bl = this.setScoreTeamColor(t);
                this.renderModel(t, f10, f9, f3, f6, f7, 0.0625f);
                if (bl) {
                    this.unsetScoreTeamColor();
                }
            } else {
                boolean bl = this.setDoRenderBrightness(t, f2);
                this.renderModel(t, f10, f9, f3, f6, f7, 0.0625f);
                if (bl) {
                    this.unsetBrightness();
                }
                GlStateManager.depthMask(true);
                if (!(t instanceof EntityPlayer) || !((EntityPlayer)t).isSpectator()) {
                    this.renderLayers(t, f10, f9, f2, f3, f6, f7, 0.0625f);
                }
            }
            GlStateManager.disableRescaleNormal();
        }
        catch (Exception exception) {
            logger.error("Couldn't render entity", (Throwable)exception);
        }
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        if (!this.renderOutlines) {
            super.doRender(t, d, d2, d3, f, f2);
        }
    }
}

