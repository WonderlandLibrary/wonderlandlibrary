// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import org.apache.logging.log4j.LogManager;
import net.minecraft.scoreboard.Team;
import java.util.Iterator;
import net.optifine.shaders.Shaders;
import net.minecraft.src.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.optifine.EmissiveTextures;
import net.optifine.entity.model.CustomEntityModels;
import ru.tuskevich.modules.Module;
import ru.tuskevich.modules.impl.RENDER.Glass;
import ru.tuskevich.Minced;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.optifine.reflect.Reflector;
import net.minecraft.client.model.ModelSpider;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import java.util.List;
import java.nio.FloatBuffer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.EntityLivingBase;

public abstract class RenderLivingBase<T extends EntityLivingBase> extends Render<T>
{
    private static final Logger LOGGER;
    private static final DynamicTexture TEXTURE_BRIGHTNESS;
    public ModelBase mainModel;
    protected FloatBuffer brightnessBuffer;
    protected List<LayerRenderer<T>> layerRenderers;
    protected boolean renderMarker;
    public static float NAME_TAG_RANGE;
    public static float NAME_TAG_RANGE_SNEAK;
    public EntityLivingBase renderEntity;
    public float renderLimbSwing;
    public float renderLimbSwingAmount;
    public float renderAgeInTicks;
    public float renderHeadYaw;
    public float renderHeadPitch;
    public float renderScaleFactor;
    public float renderPartialTicks;
    private boolean renderModelPushMatrix;
    private boolean renderLayersPushMatrix;
    public static final boolean animateModelLiving;
    
    public RenderLivingBase(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
        this.brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
        this.layerRenderers = (List<LayerRenderer<T>>)Lists.newArrayList();
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
        this.renderModelPushMatrix = (this.mainModel instanceof ModelSpider);
    }
    
    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(final U layer) {
        return this.layerRenderers.add((LayerRenderer<T>)layer);
    }
    
    public ModelBase getMainModel() {
        return this.mainModel;
    }
    
    protected float interpolateRotation(final float prevYawOffset, final float yawOffset, final float partialTicks) {
        float f;
        for (f = yawOffset - prevYawOffset; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return prevYawOffset + partialTicks * f;
    }
    
    public void transformHeldFull3DItemLayer() {
    }
    
    public void doRender2(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, partialTicks, x, y, z)) {
            if (RenderLivingBase.animateModelLiving) {
                entity.limbSwingAmount = 1.0f;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            this.mainModel.isRiding = entity.isRiding();
            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = (entity.isRiding() && entity.getRidingEntity() != null && Reflector.callBoolean(entity.getRidingEntity(), Reflector.ForgeEntity_shouldRiderSit, new Object[0]));
            }
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (this.mainModel.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                float f5;
                if (entity == this.renderManager.renderViewEntity) {
                    f5 = entity.prevRotationPitchHead + (entity.rotationPitchHead - entity.prevRotationPitchHead) * partialTicks;
                }
                else {
                    f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                }
                this.renderLivingAt(entity, x, y, z);
                final float f6 = this.handleRotationFloat(entity, partialTicks);
                this.applyRotations(entity, f6, f, partialTicks);
                final float f7 = this.prepareScale(entity, partialTicks);
                float f8 = 0.0f;
                float f9 = 0.0f;
                if (!entity.isRiding()) {
                    f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                    if (entity.isChild()) {
                        f9 *= 3.0f;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f9, f8, partialTicks);
                this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, f7, entity);
                this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                GlStateManager.depthMask(true);
                if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                    if (entity instanceof EntityPlayerSP) {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    else {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception exception1) {
                RenderLivingBase.LOGGER.error("Couldn't render entity", (Throwable)exception1);
            }
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, partialTicks, x, y, z);
            }
        }
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (Minced.getInstance().manager.getModule(Glass.class).state && entity instanceof EntityPlayer && !((Glass)Minced.getInstance().manager.getModule(Glass.class)).mode.get(5) && !(entity instanceof EntityPlayerSP)) {
            if (!this.renderOutlines) {
                this.renderName(entity, x, y, z);
            }
            return;
        }
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, partialTicks, x, y, z)) {
            if (RenderLivingBase.animateModelLiving) {
                entity.limbSwingAmount = 1.0f;
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            this.mainModel.isRiding = entity.isRiding();
            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = (entity.isRiding() && entity.getRidingEntity() != null && Reflector.callBoolean(entity.getRidingEntity(), Reflector.ForgeEntity_shouldRiderSit, new Object[0]));
            }
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (this.mainModel.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                float f5;
                if (entity == this.renderManager.renderViewEntity) {
                    f5 = entity.prevRotationPitchHead + (entity.rotationPitchHead - entity.prevRotationPitchHead) * partialTicks;
                }
                else {
                    f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                }
                this.renderLivingAt(entity, x, y, z);
                final float f6 = this.handleRotationFloat(entity, partialTicks);
                this.applyRotations(entity, f6, f, partialTicks);
                final float f7 = this.prepareScale(entity, partialTicks);
                float f8 = 0.0f;
                float f9 = 0.0f;
                if (!entity.isRiding()) {
                    f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                    if (entity.isChild()) {
                        f9 *= 3.0f;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f9, f8, partialTicks);
                this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, f7, entity);
                if (CustomEntityModels.isActive()) {
                    this.renderEntity = entity;
                    this.renderLimbSwing = f9;
                    this.renderLimbSwingAmount = f8;
                    this.renderAgeInTicks = f6;
                    this.renderHeadYaw = f3;
                    this.renderHeadPitch = f5;
                    this.renderScaleFactor = f7;
                    this.renderPartialTicks = partialTicks;
                }
                if (this.renderOutlines) {
                    final boolean flag1 = this.setScoreTeamColor(entity);
                    GlStateManager.enableColorMaterial();
                    GlStateManager.enableOutlineMode(this.getTeamColor(entity));
                    if (!this.renderMarker) {
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                    }
                    GlStateManager.disableOutlineMode();
                    GlStateManager.disableColorMaterial();
                    if (flag1) {
                        this.unsetScoreTeamColor();
                    }
                }
                else {
                    boolean flag2 = false;
                    flag2 = ((!Minced.getInstance().manager.getModule(Glass.class).state || !(entity instanceof EntityPlayer)) && this.setDoRenderBrightness(entity, partialTicks));
                    if (EmissiveTextures.isActive()) {
                        EmissiveTextures.beginRender();
                    }
                    if (this.renderModelPushMatrix) {
                        GlStateManager.pushMatrix();
                    }
                    if (entity instanceof EntityPlayerSP) {
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                    }
                    else {
                        this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                    }
                    if (this.renderModelPushMatrix) {
                        GlStateManager.popMatrix();
                    }
                    if (EmissiveTextures.isActive()) {
                        if (EmissiveTextures.hasEmissive()) {
                            this.renderModelPushMatrix = true;
                            EmissiveTextures.beginRenderEmissive();
                            GlStateManager.pushMatrix();
                            this.renderModel(entity, f9, f8, f6, f3, f5, f7);
                            GlStateManager.popMatrix();
                            EmissiveTextures.endRenderEmissive();
                        }
                        EmissiveTextures.endRender();
                    }
                    if (flag2) {
                        if (entity instanceof EntityPlayerSP) {
                            this.unsetBrightness();
                        }
                        else {
                            this.unsetBrightness();
                        }
                    }
                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        if (entity instanceof EntityPlayerSP) {
                            this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                        }
                        else {
                            this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, f7);
                        }
                    }
                }
                if (CustomEntityModels.isActive()) {
                    this.renderEntity = null;
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception exception1) {
                RenderLivingBase.LOGGER.error("Couldn't render entity", (Throwable)exception1);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, partialTicks, x, y, z);
            }
        }
    }
    
    public void doRenderT(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, partialTicks, x, y, z)) {
            if (RenderLivingBase.animateModelLiving) {
                entity.limbSwingAmount = 1.0f;
            }
            GlStateManager.pushMatrix();
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (this.mainModel.isRiding && entity.getRidingEntity() instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.getRidingEntity();
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapDegrees(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                    f3 = f2 - f;
                }
                float f5;
                if (entity == this.renderManager.renderViewEntity) {
                    f5 = entity.prevRotationPitchHead + (entity.rotationPitchHead - entity.prevRotationPitchHead) * partialTicks;
                }
                else {
                    f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                }
                this.renderLivingAt(entity, x, y, z);
                final float f6 = this.handleRotationFloat(entity, partialTicks);
                this.applyRotations(entity, f6, f, partialTicks);
                final float f7 = this.prepareScale(entity, partialTicks);
                float f8 = 0.0f;
                float f9 = 0.0f;
                if (!entity.isRiding()) {
                    f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                    f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                    if (entity.isChild()) {
                        f9 *= 3.0f;
                    }
                    if (f8 > 1.0f) {
                        f8 = 1.0f;
                    }
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f9, f8, partialTicks);
                this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, f7, entity);
                this.renderModel(entity, f9, f8, f6, entityYaw, f5, f7);
            }
            catch (Exception exception1) {
                RenderLivingBase.LOGGER.error("Couldn't render entity", (Throwable)exception1);
            }
            GlStateManager.enableTexture2D();
            GlStateManager.popMatrix();
            if (Reflector.RenderLivingEvent_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, partialTicks, x, y, z);
            }
        }
    }
    
    public float prepareScale(final T entitylivingbaseIn, final float partialTicks) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.preRenderCallback(entitylivingbaseIn, partialTicks);
        final float f = 0.0625f;
        GlStateManager.translate(0.0f, -1.501f, 0.0f);
        return 0.0625f;
    }
    
    protected boolean setScoreTeamColor(final T entityLivingBaseIn) {
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected boolean func_193115_c(final T p_193115_1_) {
        return !p_193115_1_.isInvisible() || this.renderOutlines;
    }
    
    protected void rotateCorpse(final T entityLiving, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        if (entityLiving.deathTime > 0) {
            float f = (entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f = MathHelper.sqrt(f);
            if (f > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
        }
        else {
            final String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
            if (s != null && ("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, entityLiving.height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    protected void renderModel(final T entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor) {
        final boolean flag = this.func_193115_c(entitylivingbaseIn);
        boolean b = false;
        Label_0031: {
            if (!flag) {
                Minecraft.getMinecraft();
                if (!entitylivingbaseIn.isInvisibleToPlayer(Minecraft.player)) {
                    b = true;
                    break Label_0031;
                }
            }
            b = false;
        }
        final boolean flag2 = b;
        if (flag || flag2) {
            if (!this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }
            if (flag2) {
                GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
            this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
            if (flag2) {
                GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
            }
        }
    }
    
    protected boolean isVisible(final T p_193115_1_) {
        return !p_193115_1_.isInvisible() || this.renderOutlines;
    }
    
    protected boolean setDoRenderBrightness(final T entityLivingBaseIn, final float partialTicks) {
        return this.setBrightness(entityLivingBaseIn, partialTicks, true);
    }
    
    protected boolean setBrightness(final T entitylivingbaseIn, final float partialTicks, final boolean combineTextures) {
        final float f = entitylivingbaseIn.getBrightness();
        final int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        final boolean flag = (i >> 24 & 0xFF) > 0;
        final boolean flag2 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;
        if (!flag && !flag2) {
            return false;
        }
        if (!flag && !combineTextures) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        this.brightnessBuffer.position(0);
        if (flag2) {
            this.brightnessBuffer.put(1.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.3f);
            if (Config.isShaders()) {
                Shaders.setEntityColor(1.0f, 0.0f, 0.0f, 0.3f);
            }
        }
        else {
            final float f2 = (i >> 24 & 0xFF) / 255.0f;
            final float f3 = (i >> 16 & 0xFF) / 255.0f;
            final float f4 = (i >> 8 & 0xFF) / 255.0f;
            final float f5 = (i & 0xFF) / 255.0f;
            this.brightnessBuffer.put(f3);
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(f5);
            this.brightnessBuffer.put(1.0f - f2);
            if (Config.isShaders()) {
                Shaders.setEntityColor(f3, f4, f5, 1.0f - f2);
            }
        }
        this.brightnessBuffer.flip();
        GlStateManager.glTexEnv(8960, 8705, this.brightnessBuffer);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(RenderLivingBase.TEXTURE_BRIGHTNESS.getGlTextureId());
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetBrightness() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        GlStateManager.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
    
    protected void renderLivingAt(final T entityLivingBaseIn, final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }
    
    protected void applyRotations(final T entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        GlStateManager.rotate(180.0f - rotationYaw, 0.0f, 1.0f, 0.0f);
        if (entityLiving.deathTime > 0) {
            float f = (entityLiving.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f = MathHelper.sqrt(f);
            if (f > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate(f * this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
        }
        else {
            final String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s)) && (!(entityLiving instanceof EntityPlayer) || ((EntityPlayer)entityLiving).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, entityLiving.height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    protected float getSwingProgress(final T livingBase, final float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }
    
    protected float handleRotationFloat(final T livingBase, final float partialTicks) {
        return livingBase.ticksExisted + partialTicks;
    }
    
    protected void renderLayers(final T entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleIn) {
        for (final LayerRenderer<T> layerrenderer : this.layerRenderers) {
            final boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
            if (EmissiveTextures.isActive()) {
                EmissiveTextures.beginRender();
            }
            if (this.renderLayersPushMatrix) {
                GlStateManager.pushMatrix();
            }
            layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
            if (this.renderLayersPushMatrix) {
                GlStateManager.popMatrix();
            }
            if (EmissiveTextures.isActive()) {
                if (EmissiveTextures.hasEmissive()) {
                    this.renderLayersPushMatrix = true;
                    EmissiveTextures.beginRenderEmissive();
                    GlStateManager.pushMatrix();
                    layerrenderer.doRenderLayer(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scaleIn);
                    GlStateManager.popMatrix();
                    EmissiveTextures.endRenderEmissive();
                }
                EmissiveTextures.endRender();
            }
            if (flag) {
                this.unsetBrightness();
            }
        }
    }
    
    protected float getDeathMaxRotation(final T entityLivingBaseIn) {
        return 90.0f;
    }
    
    protected int getColorMultiplier(final T entitylivingbaseIn, final float lightBrightness, final float partialTickTime) {
        return 0;
    }
    
    protected void preRenderCallback(final T entitylivingbaseIn, final float partialTickTime) {
    }
    
    public void renderName(final T entity, final double x, final double y, final double z) {
        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, entity, this, x, y, z)) {
            if (this.canRenderName(entity)) {
                final double d0 = entity.getDistanceSq(this.renderManager.renderViewEntity);
                final float f = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
                if (d0 < f * f) {
                    final String s = entity.getDisplayName().getFormattedText();
                    GlStateManager.alphaFunc(516, 0.1f);
                    this.renderEntityName(entity, x, y, z, s, d0);
                }
            }
            if (Reflector.RenderLivingEvent_Specials_Post_Constructor.exists()) {
                Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, entity, this, x, y, z);
            }
        }
    }
    
    @Override
    protected boolean canRenderName(final T entity) {
        Minecraft.getMinecraft();
        final EntityPlayerSP entityplayersp = Minecraft.player;
        final boolean flag = !entity.isInvisibleToPlayer(entityplayersp);
        if (entity != entityplayersp) {
            final Team team = entity.getTeam();
            final Team team2 = entityplayersp.getTeam();
            if (team != null) {
                final Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
                switch (team$enumvisible) {
                    case ALWAYS: {
                        return flag;
                    }
                    case NEVER: {
                        return false;
                    }
                    case HIDE_FOR_OTHER_TEAMS: {
                        return (team2 == null) ? flag : (team.isSameTeam(team2) && (team.getSeeFriendlyInvisiblesEnabled() || flag));
                    }
                    case HIDE_FOR_OWN_TEAM: {
                        return (team2 == null) ? flag : (!team.isSameTeam(team2) && flag);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != this.renderManager.renderViewEntity && flag && !entity.isBeingRidden();
    }
    
    public List<LayerRenderer<T>> getLayerRenderers() {
        return this.layerRenderers;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        TEXTURE_BRIGHTNESS = new DynamicTexture(16, 16);
        RenderLivingBase.NAME_TAG_RANGE = 64.0f;
        RenderLivingBase.NAME_TAG_RANGE_SNEAK = 32.0f;
        animateModelLiving = Boolean.getBoolean("animate.model.living");
        final int[] aint = RenderLivingBase.TEXTURE_BRIGHTNESS.getTextureData();
        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }
        RenderLivingBase.TEXTURE_BRIGHTNESS.updateDynamicTexture();
    }
}
