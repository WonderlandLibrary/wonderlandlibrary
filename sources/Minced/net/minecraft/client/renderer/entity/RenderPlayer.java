// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.item.EnumAction;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class RenderPlayer extends RenderLivingBase<AbstractClientPlayer>
{
    private final boolean smallArms;
    
    public RenderPlayer(final RenderManager renderManager) {
        this(renderManager, false);
    }
    
    public RenderPlayer(final RenderManager renderManager, final boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0f, useSmallArms), 0.5f);
        this.smallArms = useSmallArms;
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerArrow(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerDeadmau5Head(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCape(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerElytra(this));
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new LayerEntityOnShoulder(renderManager));
    }
    
    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }
    
    @Override
    public void doRender(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!entity.isUser() || this.renderManager.renderViewEntity == entity) {
            double d0 = y;
            if (entity.isSneaking()) {
                d0 = y - 0.125;
            }
            this.setModelVisibilities(entity);
            GlStateManager.enableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
            GlStateManager.disableBlendProfile(GlStateManager.Profile.PLAYER_SKIN);
        }
    }
    
    private void setModelVisibilities(final AbstractClientPlayer clientPlayer) {
        final ModelPlayer modelplayer = this.getMainModel();
        if (clientPlayer.isSpectator()) {
            modelplayer.setVisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else {
            final ItemStack itemstack = clientPlayer.getHeldItemMainhand();
            final ItemStack itemstack2 = clientPlayer.getHeldItemOffhand();
            modelplayer.setVisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.isSneak = clientPlayer.isSneaking();
            ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
            ModelBiped.ArmPose modelbiped$armpose2 = ModelBiped.ArmPose.EMPTY;
            if (!itemstack.isEmpty()) {
                modelbiped$armpose = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
                    }
                    else if (enumaction == EnumAction.BOW) {
                        modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
                    }
                }
            }
            if (!itemstack2.isEmpty()) {
                modelbiped$armpose2 = ModelBiped.ArmPose.ITEM;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction2 = itemstack2.getItemUseAction();
                    if (enumaction2 == EnumAction.BLOCK) {
                        modelbiped$armpose2 = ModelBiped.ArmPose.BLOCK;
                    }
                }
            }
            if (clientPlayer.getPrimaryHand() == EnumHandSide.RIGHT) {
                modelplayer.rightArmPose = modelbiped$armpose;
                modelplayer.leftArmPose = modelbiped$armpose2;
            }
            else {
                modelplayer.rightArmPose = modelbiped$armpose2;
                modelplayer.leftArmPose = modelbiped$armpose;
            }
        }
    }
    
    public ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        return entity.getLocationSkin();
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    @Override
    protected void preRenderCallback(final AbstractClientPlayer entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(0.9375f, 0.9375f, 0.9375f);
    }
    
    @Override
    protected void renderEntityName(final AbstractClientPlayer entityIn, final double x, double y, final double z, final String name, final double distanceSq) {
        if (distanceSq < 100.0) {
            final Scoreboard scoreboard = entityIn.getWorldScoreboard();
            final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
            if (scoreobjective != null) {
                final Score score = scoreboard.getOrCreateScore(entityIn.getName(), scoreobjective);
                this.renderLivingLabel(entityIn, score.getScorePoints() + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                y += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * 0.025f;
            }
        }
        super.renderEntityName(entityIn, x, y, z, name, distanceSq);
    }
    
    public void renderRightArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final float f2 = 0.0625f;
        final ModelPlayer modelplayer = this.getMainModel();
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
    
    public void renderLeftArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        final float f2 = 0.0625f;
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        GlStateManager.enableBlend();
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(modelplayer.swingProgress = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.bipedLeftArm.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArm.render(0.0625f);
        modelplayer.bipedLeftArmwear.rotateAngleX = 0.0f;
        modelplayer.bipedLeftArmwear.render(0.0625f);
        GlStateManager.disableBlend();
    }
    
    @Override
    protected void renderLivingAt(final AbstractClientPlayer entityLivingBaseIn, final double x, final double y, final double z) {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
            super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
        }
        else {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }
    
    @Override
    protected void applyRotations(final AbstractClientPlayer entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping()) {
            GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
        }
        else if (entityLiving.isElytraFlying()) {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
            final float f = entityLiving.getTicksElytraFlying() + partialTicks;
            final float f2 = MathHelper.clamp(f * f / 100.0f, 0.0f, 1.0f);
            GlStateManager.rotate(f2 * (-90.0f - entityLiving.rotationPitch), 1.0f, 0.0f, 0.0f);
            final Vec3d vec3d = entityLiving.getLook(partialTicks);
            final double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
            final double d2 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;
            if (d0 > 0.0 && d2 > 0.0) {
                final double d3 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z) / (Math.sqrt(d0) * Math.sqrt(d2));
                final double d4 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
                GlStateManager.rotate((float)(Math.signum(d4) * Math.acos(d3)) * 180.0f / 3.1415927f, 0.0f, 1.0f, 0.0f);
            }
        }
        else {
            super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        }
    }
}
