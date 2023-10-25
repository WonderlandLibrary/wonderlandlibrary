/*
 * Decompiled with CFR 0.152.
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
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

public class RenderMinecart<T extends EntityMinecart>
extends Render<T> {
    private static final ResourceLocation minecartTextures = new ResourceLocation("textures/entity/minecart.png");
    protected ModelBase modelMinecart = new ModelMinecart();

    public RenderMinecart(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5f;
    }

    @Override
    protected ResourceLocation getEntityTexture(T t) {
        return minecartTextures;
    }

    @Override
    public void doRender(T t, double d, double d2, double d3, float f, float f2) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(t);
        long l = (long)((Entity)t).getEntityId() * 493286711L;
        l = l * l * 4392167121L + l * 98761L;
        float f3 = (((float)(l >> 16 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f4 = (((float)(l >> 20 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        float f5 = (((float)(l >> 24 & 7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate(f3, f4, f5);
        double d4 = ((EntityMinecart)t).lastTickPosX + (((EntityMinecart)t).posX - ((EntityMinecart)t).lastTickPosX) * (double)f2;
        double d5 = ((EntityMinecart)t).lastTickPosY + (((EntityMinecart)t).posY - ((EntityMinecart)t).lastTickPosY) * (double)f2;
        double d6 = ((EntityMinecart)t).lastTickPosZ + (((EntityMinecart)t).posZ - ((EntityMinecart)t).lastTickPosZ) * (double)f2;
        double d7 = 0.3f;
        Vec3 vec3 = ((EntityMinecart)t).func_70489_a(d4, d5, d6);
        float f6 = ((EntityMinecart)t).prevRotationPitch + (((EntityMinecart)t).rotationPitch - ((EntityMinecart)t).prevRotationPitch) * f2;
        if (vec3 != null) {
            Vec3 vec32 = ((EntityMinecart)t).func_70495_a(d4, d5, d6, d7);
            Vec3 vec33 = ((EntityMinecart)t).func_70495_a(d4, d5, d6, -d7);
            if (vec32 == null) {
                vec32 = vec3;
            }
            if (vec33 == null) {
                vec33 = vec3;
            }
            d += vec3.xCoord - d4;
            d2 += (vec32.yCoord + vec33.yCoord) / 2.0 - d5;
            d3 += vec3.zCoord - d6;
            Vec3 vec34 = vec33.addVector(-vec32.xCoord, -vec32.yCoord, -vec32.zCoord);
            if (vec34.lengthVector() != 0.0) {
                vec34 = vec34.normalize();
                f = (float)(Math.atan2(vec34.zCoord, vec34.xCoord) * 180.0 / Math.PI);
                f6 = (float)(Math.atan(vec34.yCoord) * 73.0);
            }
        }
        GlStateManager.translate((float)d, (float)d2 + 0.375f, (float)d3);
        GlStateManager.rotate(180.0f - f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f6, 0.0f, 0.0f, 1.0f);
        float f7 = (float)((EntityMinecart)t).getRollingAmplitude() - f2;
        float f8 = ((EntityMinecart)t).getDamage() - f2;
        if (f8 < 0.0f) {
            f8 = 0.0f;
        }
        if (f7 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f7) * f7 * f8 / 10.0f * (float)((EntityMinecart)t).getRollingDirection(), 1.0f, 0.0f, 0.0f);
        }
        int n = ((EntityMinecart)t).getDisplayTileOffset();
        IBlockState iBlockState = ((EntityMinecart)t).getDisplayTile();
        if (iBlockState.getBlock().getRenderType() != -1) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.locationBlocksTexture);
            float f9 = 0.75f;
            GlStateManager.scale(f9, f9, f9);
            GlStateManager.translate(-0.5f, (float)(n - 8) / 16.0f, 0.5f);
            this.func_180560_a(t, f2, iBlockState);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindEntityTexture(t);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render((Entity)t, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        super.doRender(t, d, d2, d3, f, f2);
    }

    protected void func_180560_a(T t, float f, IBlockState iBlockState) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(iBlockState, ((Entity)t).getBrightness(f));
        GlStateManager.popMatrix();
    }
}

