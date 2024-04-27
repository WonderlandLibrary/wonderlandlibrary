package me.aquavit.liquidsense.injection.forge.mixins.render;

import me.aquavit.liquidsense.LiquidSense;
import me.aquavit.liquidsense.module.modules.client.RenderChanger;
import me.aquavit.liquidsense.module.modules.client.Rotations;
import me.aquavit.liquidsense.module.modules.client.TrueSight;
import me.aquavit.liquidsense.module.modules.exploit.Derp;
import me.aquavit.liquidsense.module.modules.render.*;
import me.aquavit.liquidsense.utils.client.ClientUtils;
import me.aquavit.liquidsense.utils.entity.EntityUtils;
import me.aquavit.liquidsense.utils.client.RotationUtils;
import me.aquavit.liquidsense.utils.render.Colors;
import me.aquavit.liquidsense.utils.render.OutlineUtils;
import com.google.common.collect.Lists;
import me.aquavit.liquidsense.event.events.PreUpdateEvent;
import me.aquavit.liquidsense.module.modules.render.ESP;
import me.aquavit.liquidsense.utils.render.ColorUtils;
import me.aquavit.liquidsense.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

@Mixin(RendererLivingEntity.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRendererLivingEntity extends MixinRender {
    Minecraft mc = Minecraft.getMinecraft();

    @Shadow
    private static final Logger logger = LogManager.getLogger();

    @Shadow
    protected ModelBase mainModel;

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setScoreTeamColor(T p_setScoreTeamColor_1_);

    @Shadow
    protected List<LayerRenderer<?>> layerRenderers = Lists.newArrayList();

    @Shadow
    public boolean renderOutlines = false;

    @Shadow
    public abstract <T extends EntityLivingBase> float getSwingProgress(T p_getSwingProgress_1_, float p_getSwingProgress_2_);

    @Shadow
    public abstract float interpolateRotation (float p_interpolateRotation_1_, float p_interpolateRotation_2_, float p_interpolateRotation_3_);

    @Shadow
    public abstract <T extends EntityLivingBase> void renderLivingAt(T p_renderLivingAt_1_, double p_renderLivingAt_2_, double p_renderLivingAt_4_, double p_renderLivingAt_6_);

    @Shadow
    public abstract <T extends EntityLivingBase> float handleRotationFloat(T p_handleRotationFloat_1_, float p_handleRotationFloat_2_);

    @Shadow
    public abstract <T extends EntityLivingBase> void preRenderCallback(T p_preRenderCallback_1_, float p_preRenderCallback_2_);

    @Shadow
    public abstract void unsetScoreTeamColor();

    @Shadow
    public abstract <T extends EntityLivingBase> boolean setDoRenderBrightness(T p_setDoRenderBrightness_1_, float p_setDoRenderBrightness_2_);

    @Shadow
    public abstract  void unsetBrightness();

    @Shadow
    protected <T extends EntityLivingBase> float getDeathMaxRotation(T p_getDeathMaxRotation_1_) {
        return 90.0F;
    }

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setBrightness(T p_setBrightness_1_, float p_setBrightness_2_, boolean p_setBrightness_3_);


	/**
	 * @author  CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    public <T extends EntityLivingBase> void renderLayers(T p_renderLayers_1_, float p_renderLayers_2_, float p_renderLayers_3_, float p_renderLayers_4_, float p_renderLayers_5_, float p_renderLayers_6_, float p_renderLayers_7_, float p_renderLayers_8_) {
        Iterator var9 = this.layerRenderers.iterator();

        while(var9.hasNext()) {
            LayerRenderer<T> layerrenderer = (LayerRenderer)var9.next();
            boolean flag = this.setBrightness(p_renderLayers_1_, p_renderLayers_4_, layerrenderer.shouldCombineTextures());
            layerrenderer.doRenderLayer(p_renderLayers_1_, p_renderLayers_2_, p_renderLayers_3_, p_renderLayers_4_, p_renderLayers_5_, p_renderLayers_6_, p_renderLayers_7_, p_renderLayers_8_);
            if (flag) {
                this.unsetBrightness();
            }
        }

    }

    @Inject(method = "canRenderName*", at = @At("HEAD"), cancellable = true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {

        final NameTags nameTags = (NameTags) LiquidSense.moduleManager.getModule(NameTags.class);

        if (!ESP.renderNameTags || (nameTags.getState() && EntityUtils.isSelected(entity, false, false)))
            callbackInfoReturnable.setReturnValue(false);
    }

	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    protected <T extends EntityLivingBase> void rotateCorpse(T p_rotateCorpse_1_, float p_rotateCorpse_2_, float p_rotateCorpse_3_, float p_rotateCorpse_4_){
        GlStateManager.rotate(180.0F - p_rotateCorpse_3_, 0.0F, 1.0F, 0.0F);

        if (p_rotateCorpse_1_.deathTime > 0) {
            float f = ((float) p_rotateCorpse_1_.deathTime + p_rotateCorpse_4_ - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F) {
                f = 1.0F;
            }

            GlStateManager.rotate(f * getDeathMaxRotation(p_rotateCorpse_1_), 0.0F, 0.0F, 1.0F);
        } else {
            final String s = EnumChatFormatting.getTextWithoutFormattingCodes(p_rotateCorpse_1_.getName());

            final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);
            final boolean flag = rc.getState() && RenderChanger.flipEntitiesValue.get();

            if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm") || flag) && (!(p_rotateCorpse_1_ instanceof EntityPlayer)
                    || ((EntityPlayer) p_rotateCorpse_1_).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0F, flag && RenderChanger.littleEntitiesValue.get() ? 1 : (p_rotateCorpse_1_.height + 0.1F), 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    /**
     * @author CCBlueX
     * @reason CCBlueX
     */
    @Overwrite
    public <T extends EntityLivingBase> void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        final Chams chams = (Chams) LiquidSense.moduleManager.getModule(Chams.class);
        final RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

        if (chams.getState() && Chams.targetsValue.get() && EntityUtils.isSelected(entity, false,false)) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(1.0F, -1000000F);
        }

        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(entity, (RendererLivingEntity)(Object)this, x, y, z))) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            mainModel.swingProgress = getSwingProgress(entity, partialTicks);
            boolean shouldSit = entity.isRiding() && entity.ridingEntity != null && entity.ridingEntity.shouldRiderSit();
            boolean rider = entity == mc.thePlayer && rc.getState() && RenderChanger.riderValue.get();
            mainModel.isRiding = shouldSit || rider;
            mainModel.isChild = entity.isChild();

            try {
                float f = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                float f1 = interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f2 = f1 - f;
                float f3;
                if (shouldSit && entity.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
                    f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f2 = f1 - f;
                    f3 = MathHelper.wrapAngleTo180_float(f2);
                    if (f3 < -85.0F) {
                        f3 = -85.0F;
                    }

                    if (f3 >= 85.0F) {
                        f3 = 85.0F;
                    }

                    f = f1 - f3;
                    if (f3 * f3 > 2500.0F) {
                        f += f3 * 0.2F;
                    }
                }

                float f7 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                float f5 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                float f6 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);

                boolean ok = (RotationUtils.targetRotation != null || LiquidSense.moduleManager.getModule(Derp.class).getState());

                renderLivingAt(entity, x, y, z);

                if (this.mainModel.isRiding)
                    GlStateManager.translate(0.0, RenderChanger.sleeperValue.get() ? 0.05 : -0.5, 0.0);

                /* Ghost ---------------------------------------------------------------------------------------------- */
                if (LiquidSense.moduleManager.getModule(Rotations.class).getState() && Rotations.ghost.get() && ok && entity == mc.thePlayer) {

                    if (RenderChanger.littleEntitiesValue.get()) {
                        GL11.glPushMatrix();
                        GL11.glScaled(0.5, 0.5, 0.5);
                    }

                    GlStateManager.pushMatrix();
                    this.rotateCorpse(entity, f7, this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks), partialTicks);
                    GlStateManager.enableRescaleNormal();
                    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                    this.preRenderCallback(entity, partialTicks);
                    GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
                    if (entity.isChild()) {
                        f6 *= 3.0F;
                    }
                    if (f5 > 1.0F) {
                        f5 = 1.0F;
                    }
                    GlStateManager.enableAlpha();
                    this.mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                    this.mainModel.setRotationAngles(f6, f5, f7, f2, f7, 0.0625f, entity);

                    this.renderModel(entity, f6, f5, f7, f2, f7, 0.0625f);

                    if (setDoRenderBrightness(entity, partialTicks)) {
                        unsetBrightness();
                    }

                    GlStateManager.depthMask(true);

                    if (!((EntityPlayer) entity).isSpectator()) {
                        this.renderLayers(entity, f6, f5, partialTicks, f7, f2, f7, 0.0625f);
                    }
                    GlStateManager.popMatrix();


                    if (RenderChanger.littleEntitiesValue.get()) {
                        GL11.glPopMatrix();
                    }
                }
                /* ---------------------------------------------------------------------------------------------------- */

                if (entity instanceof EntityPlayerSP && LiquidSense.moduleManager.getModule(Rotations.class).getState() && ok){
                    float YAW = PreUpdateEvent.Companion.getYAW();
                    float PITCH = PreUpdateEvent.Companion.getPITCH();
                    float PREVYAW = PreUpdateEvent.Companion.getPrevYAW();
                    float PREVPITCH = PreUpdateEvent.Companion.getPrevPITCH();

                    f = this.interpolateRotation(PREVYAW, YAW, partialTicks);
                    float renderYaw = this.interpolateRotation(PREVYAW, YAW, partialTicks) - f;
                    float renderPitch = this.interpolateRotation(PREVPITCH, PITCH, partialTicks);
                    f2 = renderYaw;
                    f7 = renderPitch;
                }

                rotateCorpse(entity, f7, f, partialTicks);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                preRenderCallback(entity, partialTicks);
                GlStateManager.translate(0.0F, -1.5078125F, 0.0F);

                if (entity.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }

                if (rc.getState() && RenderChanger.littleEntitiesValue.get()) {
                    GL11.glPushMatrix();
                    GL11.glScaled(0.5, 0.5, 0.5);
                    GL11.glTranslated(0, 1.5, 0);
                }

                GlStateManager.enableAlpha();
                mainModel.setLivingAnimations(entity, f6, f5, partialTicks);
                mainModel.setRotationAngles(f6, f5, f7, f2, f7, 0.0625F, entity);

                boolean flag;
                if (renderOutlines) {
                    flag = setScoreTeamColor(entity);
                    renderModel(entity, f6, f5, f7, f2, f7, 0.0625F);
                    if (flag) {
                        unsetScoreTeamColor();
                    }
                } else {
                    flag = setDoRenderBrightness(entity, partialTicks);

                    if (LiquidSense.moduleManager.getModule(Rotations.class).getState() && Rotations.ghost.get() && ok && entity == mc.thePlayer) {
                        glPushMatrix();
                        glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                        glDisable(GL11.GL_DEPTH_TEST);
                        glDisable(GL11.GL_TEXTURE_2D);
                        glEnable(GL11.GL_BLEND);
                        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        glDisable(GL11.GL_LIGHTING);
                        glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
                        int ghost = Rotations.rainbow.get() ? ColorUtils.rainbow(400000000L, Rotations.alphaValue.get()).getRGB() :
                                new Color(Rotations.colorRedValue.get(),Rotations.colorGreenValue.get(),
                                        Rotations.colorBlueValue.get(),Rotations.alphaValue.get()).getRGB();
                        float alpha = (ghost >> 24 & 0xFF) / 255.0F;
                        float red = (ghost >> 16 & 0xFF) / 255.0F;
                        float green = (ghost >> 8 & 0xFF) / 255.0F;
                        float blue = (ghost & 0xFF) / 255.0F;
                        glColor4f(red , green , blue , alpha);
                        this.renderModel(entity, f6, f5, f7, f2, f7, 0.0625F);
                        glColor4f(1f , 1f , 1f , 1f);
                        glEnable(GL11.GL_LIGHTING);
                        glDisable(GL11.GL_BLEND);
                        glEnable(GL11.GL_TEXTURE_2D);
                        glEnable(GL11.GL_DEPTH_TEST);
                        glPopAttrib();
                        glPopMatrix();
                        if (flag) {
                            unsetBrightness();
                        }
                        GlStateManager.depthMask(true);
                    } else {
                        renderModel(entity, f6, f5, f7, f2, f7, 0.0625F);
                        if (flag) {
                            unsetBrightness();
                        }

                        GlStateManager.depthMask(true);
                        if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator()) {
                            renderLayers(entity, f6, f5, partialTicks, f7, f2, f7, 0.0625F);
                        }
                    }
                }

                if (rc.getState() && RenderChanger.littleEntitiesValue.get()) {
                    GL11.glPopMatrix();
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception var20) {
                logger.error("Couldn't render entity", var20);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!renderOutlines) {
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }

            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(entity, (RendererLivingEntity)(Object)this, x, y, z));
        }
    }


	/**
	 * @author CCBlueX
	 * @reason CCBlueX
	 */
    @Overwrite
    protected <T extends EntityLivingBase> void renderModel(T entityLivingBaseIn, float x, float y, float z, float yaw, float pitch, float scaleFactor) {
        boolean visible = !entityLivingBaseIn.isInvisible();
        final TrueSight trueSight = (TrueSight) LiquidSense.moduleManager.getModule(TrueSight.class);
        boolean semiVisible = !visible && (!entityLivingBaseIn.isInvisibleToPlayer(mc.thePlayer) || (trueSight.getState() && TrueSight.entitiesValue.get()));

        Chams chams = (Chams) LiquidSense.moduleManager.getModule(Chams.class);
        RenderChanger rc = (RenderChanger) LiquidSense.moduleManager.getModule(RenderChanger.class);

        if (entityLivingBaseIn == mc.thePlayer && rc.getState() && RenderChanger.riderValue.get()) {
            x += ((AbstractClientPlayer) entityLivingBaseIn).renderOffsetX;
            z += ((AbstractClientPlayer) entityLivingBaseIn).renderOffsetZ;
        }

        if (visible || semiVisible) {
            if(!bindEntityTexture(entityLivingBaseIn))
                return;

            if (semiVisible) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            if (rc.getState() && RenderChanger.littleEntitiesValue.get()) {
                GL11.glPushMatrix();
                GL11.glScaled(0.5, 0.5, 0.5);
                GL11.glTranslated(0, 1.5, 0);
                GL11.glPopMatrix();
            }

            final ESP esp = (ESP) LiquidSense.moduleManager.getModule(ESP.class);
            if (esp.getState() && EntityUtils.isSelected(entityLivingBaseIn, false, false)) {
                boolean fancyGraphics = mc.gameSettings.fancyGraphics;
                mc.gameSettings.fancyGraphics = false;

                float gamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 100000F;

                switch(esp.modeValue.get().toLowerCase()) {
                    case "frame":
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_LINE_SMOOTH);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        RenderUtils.glColor(esp.getColor(entityLivingBaseIn));
                        GL11.glLineWidth(esp.wireframeWidth.get());
                        mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    case "outline":
                        ClientUtils.disableFastRender();
                        GlStateManager.resetColor();

                        final Color color = esp.getColor(entityLivingBaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(esp.outlineWidth.get());
                        mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                }
                mc.gameSettings.fancyGraphics = fancyGraphics;
                mc.gameSettings.gammaSetting = gamma;
            }

            if (chams.getState() && !Chams.modeValue.get().equals("Normal") && (Chams.all.get() || EntityUtils.isSelected(entityLivingBaseIn, false,false))) {
                GL11.glPushAttrib(1048575);
                GL11.glDisable(3008);
                GL11.glDisable(3553);
                if (!Chams.modeValue.get().equals("CSGO"))
                    GL11.glDisable(2896);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glLineWidth(1.5f);
                GL11.glEnable(2960);

                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                GL11.glEnable(10754);
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
                RenderUtils.glColor(Colors.reAlpha(Chams.rainbow.get() ?
                        Colors.rainbowEffect(1,Chams.saturationValue.get(), Chams.brightnessValue.get()).getRGB() :
                        new Color(Chams.colorRed2Value.get() / 255,Chams.colorGreen2Value.get() / 255, Chams.colorBlue2Value.get() / 255).getRGB(),
                        Chams.colorA2Value.get() / 255));
                this.mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);

                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                RenderUtils.glColor(Colors.reAlpha(Chams.rainbow.get() ?
                        Colors.rainbowEffect(1, Chams.saturationValue.get(), Chams.brightnessValue.get()).getRGB() :
                        new Color(Chams.colorRedValue.get() / 255, Chams.colorGreenValue.get() / 255, Chams.colorBlueValue.get() / 255).getRGB(),
                        Chams.colorAValue.get() / 255));
                this.mainModel.render(entityLivingBaseIn, x, y, z, yaw, pitch, scaleFactor);

                GL11.glEnable(3042);
                if (!Chams.modeValue.get().equals("CSGO"))
                    GL11.glEnable(2896);
                GL11.glEnable(3553);
                GL11.glEnable(3008);
                GL11.glPopAttrib();
            } else {
                mainModel.render(entityLivingBaseIn, x, y, z,yaw,pitch, scaleFactor);
            }

            if (semiVisible) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
}