/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RenderShulker
extends RenderLiving<EntityShulker> {
    public static final ResourceLocation[] SHULKER_ENDERGOLEM_TEXTURE = new ResourceLocation[]{new ResourceLocation("textures/entity/shulker/shulker_white.png"), new ResourceLocation("textures/entity/shulker/shulker_orange.png"), new ResourceLocation("textures/entity/shulker/shulker_magenta.png"), new ResourceLocation("textures/entity/shulker/shulker_light_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_yellow.png"), new ResourceLocation("textures/entity/shulker/shulker_lime.png"), new ResourceLocation("textures/entity/shulker/shulker_pink.png"), new ResourceLocation("textures/entity/shulker/shulker_gray.png"), new ResourceLocation("textures/entity/shulker/shulker_silver.png"), new ResourceLocation("textures/entity/shulker/shulker_cyan.png"), new ResourceLocation("textures/entity/shulker/shulker_purple.png"), new ResourceLocation("textures/entity/shulker/shulker_blue.png"), new ResourceLocation("textures/entity/shulker/shulker_brown.png"), new ResourceLocation("textures/entity/shulker/shulker_green.png"), new ResourceLocation("textures/entity/shulker/shulker_red.png"), new ResourceLocation("textures/entity/shulker/shulker_black.png")};

    public RenderShulker(RenderManager p_i47194_1_) {
        super(p_i47194_1_, new ModelShulker(), 0.0f);
        this.addLayer(new HeadLayer());
    }

    @Override
    public ModelShulker getMainModel() {
        return (ModelShulker)super.getMainModel();
    }

    @Override
    public void doRender(EntityShulker entity, double x, double y, double z, float entityYaw, float partialTicks) {
        int i = entity.getClientTeleportInterp();
        if (i > 0 && entity.isAttachedToBlock()) {
            BlockPos blockpos = entity.getAttachmentPos();
            BlockPos blockpos1 = entity.getOldAttachPos();
            double d0 = (double)((float)i - partialTicks) / 6.0;
            d0 *= d0;
            double d1 = (double)(blockpos.getX() - blockpos1.getX()) * d0;
            double d2 = (double)(blockpos.getY() - blockpos1.getY()) * d0;
            double d3 = (double)(blockpos.getZ() - blockpos1.getZ()) * d0;
            super.doRender(entity, x - d1, y - d2, z - d3, entityYaw, partialTicks);
        } else {
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    public boolean shouldRender(EntityShulker livingEntity, ICamera camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntity, camera, camX, camY, camZ)) {
            return true;
        }
        if (livingEntity.getClientTeleportInterp() > 0 && livingEntity.isAttachedToBlock()) {
            BlockPos blockpos = livingEntity.getOldAttachPos();
            BlockPos blockpos1 = livingEntity.getAttachmentPos();
            Vec3d vec3d = new Vec3d(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());
            Vec3d vec3d1 = new Vec3d(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            if (camera.isBoundingBoxInFrustum(new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z))) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityShulker entity) {
        return SHULKER_ENDERGOLEM_TEXTURE[entity.func_190769_dn().getMetadata()];
    }

    @Override
    protected void rotateCorpse(EntityShulker entityLiving, float p_77043_2_, float p_77043_3_, float partialTicks) {
        super.rotateCorpse(entityLiving, p_77043_2_, p_77043_3_, partialTicks);
        switch (entityLiving.getAttachmentFacing()) {
            default: {
                break;
            }
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
            }
        }
    }

    @Override
    protected void preRenderCallback(EntityShulker entitylivingbaseIn, float partialTickTime) {
        float f = 0.999f;
        GlStateManager.scale(0.999f, 0.999f, 0.999f);
    }

    class HeadLayer
    implements LayerRenderer<EntityShulker> {
        private HeadLayer() {
        }

        @Override
        public void doRenderLayer(EntityShulker entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
            GlStateManager.pushMatrix();
            switch (entitylivingbaseIn.getAttachmentFacing()) {
                default: {
                    break;
                }
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
                }
            }
            ModelRenderer modelrenderer = RenderShulker.this.getMainModel().head;
            modelrenderer.rotateAngleY = netHeadYaw * ((float)Math.PI / 180);
            modelrenderer.rotateAngleX = headPitch * ((float)Math.PI / 180);
            RenderShulker.this.bindTexture(SHULKER_ENDERGOLEM_TEXTURE[entitylivingbaseIn.func_190769_dn().getMetadata()]);
            modelrenderer.render(scale);
            GlStateManager.popMatrix();
        }

        @Override
        public boolean shouldCombineTextures() {
            return false;
        }
    }
}

