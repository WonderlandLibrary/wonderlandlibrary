/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.eyes.CosmeticEyes;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.celestial.client.Celestial;
import org.celestial.client.feature.impl.visuals.Chams;
import org.celestial.client.feature.impl.visuals.CustomModel;

public class RenderPlayer
extends RenderLivingBase<AbstractClientPlayer> {
    private final boolean smallArms;

    public RenderPlayer(RenderManager renderManager) {
        this(renderManager, false);
    }

    public RenderPlayer(RenderManager renderManager, boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0f, useSmallArms), 0.5f);
        this.smallArms = useSmallArms;
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        this.addLayer(new LayerElytra(this));
        this.addLayer(new LayerEntityOnShoulder(renderManager));
        this.addLayer(new CosmeticEyes(this));
    }

    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }

    @Override
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
            double d0 = y;
            if (entity.isSneaking()) {
                d0 = y - 0.125;
            }
            this.setModelVisibilities(entity);
            if (Celestial.instance.featureManager.getFeatureByClass(Chams.class).getState() && Chams.chamsMode.currentMode.equals("Walls")) {
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, -1000000.0f);
                super.doRender(entity, x, d0, z, entityYaw, partialTicks);
                GlStateManager.disablePolygonOffset();
                GlStateManager.doPolygonOffset(1.0f, 1000000.0f);
            } else {
                super.doRender(entity, x, d0, z, entityYaw, partialTicks);
            }
        }
    }

    private void setModelVisibilities(AbstractClientPlayer clientPlayer) {
        ModelPlayer modelplayer = this.getMainModel();
        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        } else {
            ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            ItemStack itemstack1 = clientPlayer.getHeldItemOffhand();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.isSneak = clientPlayer.isSneaking();
            ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                modelbiped$armpose = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0) {
                    EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                    } else if (enumaction == EnumAction.BOW) {
                        modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }
            if (!itemstack1.isEmpty()) {
                EnumAction enumaction1;
                modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0 && (enumaction1 = itemstack1.getItemUseAction()) == EnumAction.BLOCK) {
                    modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
                }
            }
            if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose1;
            } else {
                modelplayer.rightArmPose = modelbiped$armpose1;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        }
    }

    @Override
    public ResourceLocation getEntityTexture(AbstractClientPlayer entity) {
        if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && (!CustomModel.onlyMe.getCurrentValue() || entity == Minecraft.getMinecraft().player || Celestial.instance.friendManager.isFriend(entity.getName()) && CustomModel.friends.getCurrentValue())) {
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Amogus")) {
                return new ResourceLocation("celestial/models/amogus.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Jeff Killer")) {
                return new ResourceLocation("celestial/models/jeff.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Crab")) {
                return new ResourceLocation("celestial/models/crab.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Demon")) {
                return new ResourceLocation("celestial/models/demon.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Red Panda")) {
                return new ResourceLocation("celestial/models/redpanda.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Freddy Bear")) {
                return new ResourceLocation("celestial/models/freddy.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Chinchilla")) {
                return new ResourceLocation("celestial/models/chinchilla.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("CupHead")) {
                return new ResourceLocation("celestial/models/cuphead.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Sonic")) {
                return new ResourceLocation("celestial/models/sonic.png");
            }
            if (Celestial.instance.featureManager.getFeatureByClass(CustomModel.class).getState() && CustomModel.modelMode.currentMode.equals("Crazy Rabbit")) {
                return new ResourceLocation("celestial/models/rabbit.png");
            }
        }
        return entity.getLocationSkin();
    }

    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }

    @Override
    protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime) {
        float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }

    @Override
    protected void renderEntityName(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq) {
        Scoreboard scoreboard;
        ScoreObjective scoreobjective;
        if (distanceSq < 100.0 && (scoreobjective = (scoreboard = entityIn.getWorldScoreboard()).getObjectiveInDisplaySlot(2)) != null) {
            Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
            this.renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
            y += (double)((float)this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * 0.025f);
        }
        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }

    public void renderRightArm(AbstractClientPlayer clientPlayer) {
        float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        float f1 = 0.0625f;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.swingProgress = 0.0f;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.bipedRightArm.rotateAngleX = 0.0f;
        modelplayer.bipedRightArm.render(0.0625f);
        modelplayer.bipedRightArmwear.rotateAngleX = 0.0f;
        modelplayer.bipedRightArmwear.render(0.0625f);
        GlStateManager.disableBlend();
    }

    public void renderLeftArm(AbstractClientPlayer clientPlayer) {
        float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        float f1 = 0.0625f;
        ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.isSneak = false;
        modelplayer.swingProgress = 0.0f;
        modelplayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArm.render(0.0625f);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArmwear.render(0.0625f);
        GlStateManager.disableBlend();
    }

    @Override
    protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z) {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
            super.renderLivingAt(entityLivingBaseIn, x + (double)entityLivingBaseIn.renderOffsetX, y + (double)entityLivingBaseIn.renderOffsetY, z + (double)entityLivingBaseIn.renderOffsetZ);
        } else {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }

    @Override
    protected void rotateCorpse(AbstractClientPlayer entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping()) {
            GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
        } else if (entityLiving.isElytraFlying()) {
            super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
            float f = (float)entityLiving.getTicksElytraFlying() + partialTicks;
            float f1 = MathHelper.clamp(f * f / 100.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f1 * (-90.0f - entityLiving.rotationPitch), 1.0f, 0.0f, 0.0f);
            Vec3d vec3d = entityLiving.getLook(partialTicks);
            double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
            if (d0 > 0.0 && d1 > 0.0) {
                double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d1));
                double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
                GlStateManager.rotate((float)(Math.signum(d3) * Math.acos(d2)) * 180.0f / (float)Math.PI, 0.0f, 1.0f, 0.0f);
            }
        } else {
            super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
        }
    }
}

