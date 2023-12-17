// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.monster.EntityShulker;

public class RenderShulker extends RenderLiving<EntityShulker>
{
    public static final ResourceLocation[] SHULKER_ENDERGOLEM_TEXTURE;
    
    public RenderShulker(final RenderManager p_i47194_1_) {
        super(p_i47194_1_, new ModelShulker(), 0.0f);
        ((RenderLivingBase<EntityLivingBase>)this).addLayer(new HeadLayer());
    }
    
    @Override
    public ModelShulker getMainModel() {
        return (ModelShulker)super.getMainModel();
    }
    
    @Override
    public void doRender(final EntityShulker entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final int i = entity.getClientTeleportInterp();
        if (i > 0 && entity.isAttachedToBlock()) {
            final BlockPos blockpos = entity.getAttachmentPos();
            final BlockPos blockpos2 = entity.getOldAttachPos();
            double d0 = (i - partialTicks) / 6.0;
            d0 *= d0;
            final double d2 = (blockpos.getX() - blockpos2.getX()) * d0;
            final double d3 = (blockpos.getY() - blockpos2.getY()) * d0;
            final double d4 = (blockpos.getZ() - blockpos2.getZ()) * d0;
            super.doRender(entity, x - d2, y - d3, z - d4, entityYaw, partialTicks);
        }
        else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    public boolean shouldRender(final EntityShulker livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.getClientTeleportInterp() > 0 && livingEntity.isAttachedToBlock()) {
            final BlockPos blockpos = livingEntity.getOldAttachPos();
            final BlockPos blockpos2 = livingEntity.getAttachmentPos();
            final Vec3d vec3d = new Vec3d(blockpos2.getX(), blockpos2.getY(), blockpos2.getZ());
            final Vec3d vec3d2 = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d2.x, vec3d2.y, vec3d2.z, vec3d.x, vec3d.y, vec3d.z))) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityShulker entity) {
        return RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[entity.getColor().getMetadata()];
    }
    
    @Override
    protected void applyRotations(final EntityShulker entityLiving, final float ageInTicks, final float rotationYaw, final float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        switch (entityLiving.getAttachmentFacing()) {
            case EAST: {
                GlStateManager.translate(0.5f, 0.5f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
            case WEST: {
                GlStateManager.translate(-0.5f, 0.5f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
            case NORTH: {
                GlStateManager.translate(0.0f, 0.5f, -0.5f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                break;
            }
            case SOUTH: {
                GlStateManager.translate(0.0f, 0.5f, 0.5f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
            case UP: {
                GlStateManager.translate(0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                break;
            }
        }
    }
    
    @Override
    protected void preRenderCallback(final EntityShulker entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
    }
    
    static {
        SHULKER_ENDERGOLEM_TEXTURE = new ResourceLocation[] { new ResourceLocation("textures/entity/shulker/shulker_white.png"), new ResourceLocation("textures/entity/shulker/shulker_orange.png"), new ResourceLocation("textures/entity/shulker/shulker_magenta.png"), new ResourceLocation("textures/entity/shulker/shulker_light_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_yellow.png"), new ResourceLocation("textures/entity/shulker/shulker_lime.png"), new ResourceLocation("textures/entity/shulker/shulker_pink.png"), new ResourceLocation("textures/entity/shulker/shulker_gray.png"), new ResourceLocation("textures/entity/shulker/shulker_silver.png"), new ResourceLocation("textures/entity/shulker/shulker_cyan.png"), new ResourceLocation("textures/entity/shulker/shulker_purple.png"), new ResourceLocation("textures/entity/shulker/shulker_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_brown.png"), new ResourceLocation("textures/entity/shulker/shulker_green.png"), new ResourceLocation("textures/entity/shulker/shulker_red.png"), new ResourceLocation("textures/entity/shulker/shulker_black.png") };
    }
    
    class HeadLayer implements LayerRenderer<EntityShulker>
    {
        private HeadLayer() {
        }
        
        @Override
        public void doRenderLayer(final EntityShulker entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
            GlStateManager.pushMatrix();
            switch (entitylivingbaseIn.getAttachmentFacing()) {
                case EAST: {
                    GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.translate(1.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case WEST: {
                    GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.translate(-1.0f, -1.0f, 0.0f);
                    GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    break;
                }
                case NORTH: {
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.translate(0.0f, -1.0f, -1.0f);
                    break;
                }
                case SOUTH: {
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.translate(0.0f, -1.0f, 1.0f);
                    break;
                }
                case UP: {
                    GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                    GlStateManager.translate(0.0f, -2.0f, 0.0f);
                    break;
                }
            }
            final ModelRenderer modelrenderer = RenderShulker.this.getMainModel().head;
            modelrenderer.rotateAngleY = netHeadYaw * 0.017453292f;
            modelrenderer.rotateAngleX = headPitch * 0.017453292f;
            RenderShulker.this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[entitylivingbaseIn.getColor().getMetadata()]);
            modelrenderer.render(scale);
            GlStateManager.popMatrix();
        }
        
        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}
