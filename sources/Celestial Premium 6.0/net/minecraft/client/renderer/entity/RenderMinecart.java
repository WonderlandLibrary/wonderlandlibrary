/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RenderMinecart<T extends EntityMinecart>
extends Render<T> {
    private static final ResourceLocation MINECART_TEXTURES = new ResourceLocation("textures/entity/minecart.png");
    protected ModelBase modelMinecart = new ModelMinecart();

    public RenderMinecart(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        IBlockState iblockstate;
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        long i = (long)((Entity)entity).getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f1 = (((float)(i >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f2 = (((float)(i >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate(f, f1, f2);
        double d0 = ((EntityMinecart)entity).lastTickPosX + (((EntityMinecart)entity).posX - ((EntityMinecart)entity).lastTickPosX) * (double)partialTicks;
        double d1 = ((EntityMinecart)entity).lastTickPosY + (((EntityMinecart)entity).posY - ((EntityMinecart)entity).lastTickPosY) * (double)partialTicks;
        double d2 = ((EntityMinecart)entity).lastTickPosZ + (((EntityMinecart)entity).posZ - ((EntityMinecart)entity).lastTickPosZ) * (double)partialTicks;
        double d3 = 0.3f;
        Vec3d vec3d = ((EntityMinecart)entity).getPos(d0, d1, d2);
        float f3 = ((EntityMinecart)entity).prevRotationPitch + (((EntityMinecart)entity).rotationPitch - ((EntityMinecart)entity).prevRotationPitch) * partialTicks;
        if (vec3d != null) {
            Vec3d vec3d1 = ((EntityMinecart)entity).getPosOffset(d0, d1, d2, 0.3f);
            Vec3d vec3d2 = ((EntityMinecart)entity).getPosOffset(d0, d1, d2, -0.3f);
            if (vec3d1 == null) {
                vec3d1 = vec3d;
            }
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }
            x += vec3d.x - d0;
            y += (vec3d1.y + vec3d2.y) / 2.0 - d1;
            z += vec3d.z - d2;
            Vec3d vec3d3 = vec3d2.add(-vec3d1.x, -vec3d1.y, -vec3d1.z);
            if (vec3d3.lengthVector() != 0.0) {
                vec3d3 = vec3d3.normalize();
                entityYaw = (float)(Math.atan2(vec3d3.z, vec3d3.x) * 180.0 / Math.PI);
                f3 = (float)(Math.atan(vec3d3.y) * 73.0);
            }
        }
        GlStateManager.translate((float)x, (float)y + 0.375f, (float)z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f3, 0.0f, 0.0f, 1.0f);
        float f5 = (float)((EntityMinecart)entity).getRollingAmplitude() - partialTicks;
        float f6 = ((EntityMinecart)entity).getDamage() - partialTicks;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0f * (float)((EntityMinecart)entity).getRollingDirection(), 1.0f, 0.0f, 0.0f);
        }
        int j = ((EntityMinecart)entity).getDisplayTileOffset();
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        if ((iblockstate = ((EntityMinecart)entity).getDisplayTile()).getRenderType() != EnumBlockRenderType.INVISIBLE) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            float f4 = 0.75f;
            GlStateManager.scale(0.75f, 0.75f, 0.75f);
            GlStateManager.translate(-0.5f, (float)(j - 8) / 16.0f, 0.5f);
            this.renderCartContents(entity, partialTicks, iblockstate);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindEntityTexture(entity);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render((Entity)entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(T entity) {
        return MINECART_TEXTURES;
    }

    protected void renderCartContents(T p_188319_1_, float partialTicks, IBlockState p_188319_3_) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(p_188319_3_, ((Entity)p_188319_1_).getBrightness());
        GlStateManager.popMatrix();
    }
}

