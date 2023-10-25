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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMinecart<T extends EntityMinecart>
extends Render<T> {
    private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
    protected ModelBase modelMinecart = new ModelMinecart();

    public RenderMinecart(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture((Entity)entity);
        long i = (long)entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        float f = (((float)(i >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f1 = (((float)(i >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f2 = (((float)(i >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate((float)f, (float)f1, (float)f2);
        double d0 = ((EntityMinecart)entity).lastTickPosX + (((EntityMinecart)entity).posX - ((EntityMinecart)entity).lastTickPosX) * (double)partialTicks;
        double d1 = ((EntityMinecart)entity).lastTickPosY + (((EntityMinecart)entity).posY - ((EntityMinecart)entity).lastTickPosY) * (double)partialTicks;
        double d2 = ((EntityMinecart)entity).lastTickPosZ + (((EntityMinecart)entity).posZ - ((EntityMinecart)entity).lastTickPosZ) * (double)partialTicks;
        double d3 = 0.3f;
        Vec3 vec3 = entity.func_70489_a(d0, d1, d2);
        float f3 = ((EntityMinecart)entity).prevRotationPitch + (((EntityMinecart)entity).rotationPitch - ((EntityMinecart)entity).prevRotationPitch) * partialTicks;
        if (vec3 != null) {
            Vec3 vec31 = entity.func_70495_a(d0, d1, d2, d3);
            Vec3 vec32 = entity.func_70495_a(d0, d1, d2, -d3);
            if (vec31 == null) {
                vec31 = vec3;
            }
            if (vec32 == null) {
                vec32 = vec3;
            }
            x += vec3.xCoord - d0;
            y += (vec31.yCoord + vec32.yCoord) / 2.0 - d1;
            z += vec3.zCoord - d2;
            Vec3 vec33 = vec32.addVector(-vec31.xCoord, -vec31.yCoord, -vec31.zCoord);
            if (vec33.lengthVector() != 0.0) {
                vec33 = vec33.normalize();
                entityYaw = (float)(Math.atan2((double)vec33.zCoord, (double)vec33.xCoord) * 180.0 / Math.PI);
                f3 = (float)(Math.atan((double)vec33.yCoord) * 73.0);
            }
        }
        GlStateManager.translate((float)((float)x), (float)((float)y + 0.375f), (float)((float)z));
        GlStateManager.rotate((float)(180.0f - entityYaw), (float)0.0f, (float)1.0f, (float)0.0f);
        GlStateManager.rotate((float)(-f3), (float)0.0f, (float)0.0f, (float)1.0f);
        float f5 = (float)entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 0.0f) {
            GlStateManager.rotate((float)(MathHelper.sin((float)f5) * f5 * f6 / 10.0f * (float)entity.getRollingDirection()), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        int j = entity.getDisplayTileOffset();
        IBlockState iblockstate = entity.getDisplayTile();
        if (iblockstate.getBlock().getRenderType() != -1) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f4 = 0.75f;
            GlStateManager.scale((float)f4, (float)f4, (float)f4);
            GlStateManager.translate((float)-0.5f, (float)((float)(j - 8) / 16.0f), (float)0.5f);
            this.func_180560_a(entity, partialTicks, iblockstate);
            GlStateManager.popMatrix();
            GlStateManager.color((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            this.bindEntityTexture((Entity)entity);
        }
        GlStateManager.scale((float)-1.0f, (float)-1.0f, (float)1.0f);
        this.modelMinecart.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    protected ResourceLocation getEntityTexture(T entity) {
        return minecartTextures;
    }

    protected void func_180560_a(T minecart, float partialTicks, IBlockState state) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(state, minecart.getBrightness(partialTicks));
        GlStateManager.popMatrix();
    }
}
