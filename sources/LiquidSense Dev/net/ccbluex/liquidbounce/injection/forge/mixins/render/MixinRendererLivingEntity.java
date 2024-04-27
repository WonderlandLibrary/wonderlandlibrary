/*
 * LiquidBounce Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge.
 * https://github.com/CCBlueX/LiquidBounce/
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.render;

import co.uk.hexeption.utils.OutlineUtils;
import com.google.common.collect.Lists;
import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.event.SommtheEvent;
import net.ccbluex.liquidbounce.features.module.modules.combat.Aura;
import net.ccbluex.liquidbounce.features.module.modules.render.*;
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold;
import net.ccbluex.liquidbounce.utils.*;
import net.ccbluex.liquidbounce.utils.render.ColorUtils;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

@Mixin(RendererLivingEntity.class)
@SideOnly(Side.CLIENT)
public abstract class MixinRendererLivingEntity<T extends EntityLivingBase> extends MixinRender {

    @Shadow
    private static final Logger logger = LogManager.getLogger();

    @Shadow
    protected ModelBase mainModel;

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setScoreTeamColor(T p_setScoreTeamColor_1_);

    @Shadow
   // protected List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();
    public List<LayerRenderer<T>> layerRenderers = Lists.newArrayList();

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
    public abstract <T extends EntityLivingBase> void rotateCorpse(T p_rotateCorpse_1_, float p_rotateCorpse_2_, float p_rotateCorpse_3_, float p_rotateCorpse_4_);

    @Shadow
    public abstract <T extends EntityLivingBase> void preRenderCallback(T p_preRenderCallback_1_, float p_preRenderCallback_2_);

    @Shadow
    public abstract void unsetScoreTeamColor();

    @Shadow
    public abstract <T extends EntityLivingBase> boolean setDoRenderBrightness(T p_setDoRenderBrightness_1_, float p_setDoRenderBrightness_2_);

    @Shadow
    public abstract  void unsetBrightness();

    @Shadow
    protected abstract <T extends EntityLivingBase> boolean setBrightness(T p_setBrightness_1_, float p_setBrightness_2_, boolean p_setBrightness_3_);




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
/*
    @Shadow
    public abstract <T extends EntityLivingBase> void renderLayers(T p_renderLayers_1_, float p_renderLayers_2_, float p_renderLayers_3_, float p_renderLayers_4_, float p_renderLayers_5_, float p_renderLayers_6_, float p_renderLayers_7_, float p_renderLayers_8_);


 */

    @Inject(method = "canRenderName", at = @At("HEAD"), cancellable = true)
    private <T extends EntityLivingBase> void canRenderName(T entity, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (!ESP.renderNameTags || (LiquidBounce.moduleManager.getModule(NameTags.class).getState() && EntityUtils.isSelected(entity, false)))
            callbackInfoReturnable.setReturnValue(false);
    }

    @Overwrite
    public <T extends EntityLivingBase> void doRender(T p_doRender_1_, double p_doRender_2_, double p_doRender_4_, double p_doRender_6_, float p_doRender_8_, float p_doRender_9_) {
        final Chams chams = (Chams) LiquidBounce.moduleManager.getModule(Chams.class);
        if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(p_doRender_1_, false)) {
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glPolygonOffset(0.75F, -1000000F);
        }

        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(p_doRender_1_, (RendererLivingEntity)(Object)this, p_doRender_2_, p_doRender_4_, p_doRender_6_))) {

            final Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
            final Scaffold scaffold = (Scaffold) LiquidBounce.moduleManager.getModule(Scaffold.class);
            final Rotations rotations = (Rotations) LiquidBounce.moduleManager.getModule(Rotations.class);


            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(p_doRender_1_, p_doRender_9_);
            boolean shouldSit = p_doRender_1_.isRiding() && p_doRender_1_.ridingEntity != null && p_doRender_1_.ridingEntity.shouldRiderSit();
            this.mainModel.isRiding = shouldSit;
            this.mainModel.isChild = p_doRender_1_.isChild();

            if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(p_doRender_1_, false)) {
                GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
                GL11.glPolygonOffset(1.0F, -1000000F);
            }

            try {
                float f = this.interpolateRotation(p_doRender_1_.prevRenderYawOffset, p_doRender_1_.renderYawOffset, p_doRender_9_);
                float f1 = this.interpolateRotation(p_doRender_1_.prevRotationYawHead, p_doRender_1_.rotationYawHead, p_doRender_9_);
                float f2 = f1 - f;
                float f8;
                if (shouldSit && p_doRender_1_.ridingEntity instanceof EntityLivingBase) {
                    EntityLivingBase entitylivingbase = (EntityLivingBase)p_doRender_1_.ridingEntity;
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, p_doRender_9_);
                    f2 = f1 - f;
                    f8 = MathHelper.wrapAngleTo180_float(f2);
                    if (f8 < -85.0F) {
                        f8 = -85.0F;
                    }

                    if (f8 >= 85.0F) {
                        f8 = 85.0F;
                    }

                    f = f1 - f8;
                    if (f8 * f8 > 2500.0F) {
                        f += f8 * 0.2F;
                    }
                }
                Minecraft mc = Minecraft.getMinecraft();
                float f7 = p_doRender_1_.prevRotationPitch + (p_doRender_1_.rotationPitch - p_doRender_1_.prevRotationPitch) * p_doRender_9_;
                this.renderLivingAt(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_);
                f8 = this.handleRotationFloat(p_doRender_1_, p_doRender_9_);
                if( p_doRender_1_ instanceof EntityPlayerSP && rotations.getState() && rotations.getModeValue().equals("Other")){
                    float YAW = SommtheEvent.YAW;
                    float PITCH = SommtheEvent.PITCH;
                    float PREVYAW = SommtheEvent.PREVYAW;
                    float PREVPITCH = SommtheEvent.PREVPITCH;
                    boolean sneaking = SommtheEvent.SNEAKING;
                    if(killAura.getTarget() !=null || scaffold.getState()) {
                        f = this.interpolateRotation(PREVYAW, YAW, p_doRender_9_);

                        float renderYaw = this.interpolateRotation(PREVYAW, YAW, p_doRender_9_) - f;
                        float renderPitch = this.interpolateRotation(PREVPITCH, PITCH, p_doRender_9_);
                        f2 = renderYaw;
                        f7 = renderPitch;
                    }

                }
                this.rotateCorpse(p_doRender_1_, f8, f, p_doRender_9_);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0F, -1.0F, 1.0F);
                this.preRenderCallback(p_doRender_1_, p_doRender_9_);
                float f4 = 0.0625F;
                GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
                float f5 = p_doRender_1_.prevLimbSwingAmount + (p_doRender_1_.limbSwingAmount - p_doRender_1_.prevLimbSwingAmount) * p_doRender_9_;
                float f6 = p_doRender_1_.limbSwing - p_doRender_1_.limbSwingAmount * (1.0F - p_doRender_9_);
                if (p_doRender_1_.isChild()) {
                    f6 *= 3.0F;
                }

                if (f5 > 1.0F) {
                    f5 = 1.0F;
                }

                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(p_doRender_1_, f6, f5, p_doRender_9_);
                this.mainModel.setRotationAngles(f6, f5, f8, f2, f7, 0.0625F, p_doRender_1_);
                boolean flag;
                if (this.renderOutlines) {
                    flag = this.setScoreTeamColor(p_doRender_1_);
                    this.renderModel(p_doRender_1_, f6, f5, f8, f2, f7, 0.0625F);
                    if (flag) {
                        this.unsetScoreTeamColor();
                    }
                } else {
                    flag = this.setDoRenderBrightness(p_doRender_1_, p_doRender_9_);
                    this.renderModel(p_doRender_1_, f6, f5, f8, f2, f7, 0.0625F);
                    if (flag) {
                        this.unsetBrightness();
                    }

                    GlStateManager.depthMask(true);
                    if (!(p_doRender_1_ instanceof EntityPlayer) || !((EntityPlayer)p_doRender_1_).isSpectator()) {
                        this.renderLayers(p_doRender_1_, f6, f5, p_doRender_9_, f8, f2, f7, 0.0625F);
                    }
                }

                GlStateManager.disableRescaleNormal();
            } catch (Exception var20) {
                logger.error("Couldn't render entity", var20);
            }

            if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(p_doRender_1_, false)) {
                GL11.glPolygonOffset(1.0F, 1000000F);
                GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            }

            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!this.renderOutlines) {
                super.doRender(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_, p_doRender_8_, p_doRender_9_);
            }



            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_doRender_1_, (RendererLivingEntity)(Object)this, p_doRender_2_, p_doRender_4_, p_doRender_6_));
        }

        if (chams.getState() && chams.getTargetsValue().get() && EntityUtils.isSelected(p_doRender_1_, false)) {
            GL11.glPolygonOffset(1.0F, 1000000F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
        }
    }

    /**
     * @author CCBlueX
     */
    @Overwrite
    protected <T extends EntityLivingBase> void renderModel(T entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float scaleFactor) {
        boolean visible = !entitylivingbaseIn.isInvisible();
        final TrueSight trueSight = (TrueSight) LiquidBounce.moduleManager.getModule(TrueSight.class);
        boolean semiVisible = !visible && (!entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) || (trueSight.getState() && trueSight.getEntitiesValue().get()));
        Chams chams = (Chams) LiquidBounce.moduleManager.getModule(Chams.class);
        Rotations ra = (Rotations) LiquidBounce.moduleManager.getModule(Rotations.class);
        Aura killAura = (Aura) LiquidBounce.moduleManager.getModule(Aura.class);
        final Color sb = chams.getRainbow().get() ? ColorUtils.rainbow() : new Color(!Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entitylivingbaseIn) ? ChamsColor.red2 : ChamsColor.red,!Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entitylivingbaseIn) ? ChamsColor.green2 : ChamsColor.green,!Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entitylivingbaseIn) ? ChamsColor.blue2 : ChamsColor.blue,!Minecraft.getMinecraft().thePlayer.canEntityBeSeen(entitylivingbaseIn) ? ChamsColor.Apl2 : ChamsColor.Apl);
        final Color cnm = ra.getRainbow().get() ? ColorUtils.rainbow() : new Color(ra.getColorRedValue().get(),ra.getColorGreenValue().get(),ra.getColorBlueValue().get(),ra.getAlphaValue().get());

        if(visible || semiVisible) {
            if(!this.bindEntityTexture(entitylivingbaseIn))
                return;

            if(semiVisible) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569F);
            }

            final ESP esp = (ESP) LiquidBounce.moduleManager.getModule(ESP.class);
            if(esp.getState() && EntityUtils.isSelected(entitylivingbaseIn, false)) {
                Minecraft mc = Minecraft.getMinecraft();
                boolean fancyGraphics = mc.gameSettings.fancyGraphics;
                mc.gameSettings.fancyGraphics = false;

                float gamma = mc.gameSettings.gammaSetting;
                mc.gameSettings.gammaSetting = 100000F;

                switch(esp.modeValue.get().toLowerCase()) {
                    case "wireframe":
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
                        GL11.glDisable(GL11.GL_TEXTURE_2D);
                        GL11.glDisable(GL11.GL_LIGHTING);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        GL11.glEnable(GL11.GL_LINE_SMOOTH);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        RenderUtils.glColor(esp.getColor(entitylivingbaseIn));
                        GL11.glLineWidth(esp.wireframeWidth.get());
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        break;
                    case "outline":
                        ClientUtils.disableFastRender();
                        GlStateManager.resetColor();

                        final Color color = esp.getColor(entitylivingbaseIn);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderOne(esp.outlineWidth.get());
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderTwo();
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderThree();
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFour(color);
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        OutlineUtils.setColor(color);
                        OutlineUtils.renderFive();
                        OutlineUtils.setColor(Color.WHITE);
                }
                mc.gameSettings.fancyGraphics = fancyGraphics;
                mc.gameSettings.gammaSetting = gamma;
            }

            if(ra.getState() && ra.getModeValue().get().equalsIgnoreCase("Ghost")){
                if (entitylivingbaseIn.equals(Minecraft.getMinecraft().thePlayer)){
                    if(killAura.getTarget() !=null && RotationUtils.serverRotation != null) {
                        //Ghost
                        GL11.glPushMatrix();
                        GL11.glPushAttrib(1048575);
                        GL11.glDisable(2929);
                        GL11.glDisable(3553);
                        GL11.glEnable(3042);
                        GL11.glBlendFunc(770, 771);
                        GL11.glDisable(2896);
                        GL11.glPolygonMode(1032, 6914);
                        RenderUtils.chamsColor(cnm);
                        this.mainModel.render(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().thePlayer.prevRenderYawOffset, Minecraft.getMinecraft().thePlayer.renderYawOffset, p_77036_4_, RotationUtils.serverRotation.getYaw(), RotationUtils.serverRotation.getPitch(), scaleFactor);
                        GL11.glEnable(2896);
                        GL11.glDisable(3042);
                        GL11.glEnable(3553);
                        GL11.glEnable(2929);
                        GL11.glColor3d(1.0D, 1.0D, 1.0D);
                        GL11.glPopAttrib();
                        GL11.glPopMatrix();
                        //开始画本体
                        GlStateManager.pushMatrix();
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 0.35F);
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.blendFunc(770, 771);
                        GlStateManager.alphaFunc(516, 0.003921569F);
                        this.mainModel.render(Minecraft.getMinecraft().thePlayer,  p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                        GlStateManager.disableBlend();
                        GlStateManager.alphaFunc(516, 0.1F);
                        GlStateManager.popMatrix();
                        GlStateManager.depthMask(true);

                    } else {
                        this.mainModel.render(Minecraft.getMinecraft().thePlayer,  p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                }
            }

            if(chams.getState() && (chams.getBoolValue().get() || EntityUtils.isSelected(entitylivingbaseIn, false))) {
                GL11.glPushMatrix();
                GL11.glPushAttrib(1048575);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(2896);
                GL11.glPolygonMode(1032, 6914);

                RenderUtils.chamsColor(sb);
                this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                GL11.glEnable(2896);
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glColor3d(1.0D, 1.0D, 1.0D);
                GL11.glPopAttrib();
                GL11.glPopMatrix();
            }else{
                if(ra.getState() && ra.getModeValue().get().equalsIgnoreCase("Ghost")){
                    if(!entitylivingbaseIn.equals(Minecraft.getMinecraft().thePlayer)){
                        this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);
                    }
                } else {
                    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, scaleFactor);

                }
            }

            if(semiVisible) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
}