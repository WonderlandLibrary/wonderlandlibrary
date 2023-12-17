// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityMinecart;

public class RenderMinecart<T extends EntityMinecart> extends Render<T>
{
    private static final ResourceLocation MINECART_TEXTURES;
    protected ModelBase modelMinecart;
    
    public RenderMinecart(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelMinecart = new ModelMinecart();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final T entity, double x, double y, double z, float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        long i = entity.getEntityId() * 493286711L;
        i = i * i * 4392167121L + i * 98761L;
        final float f = (((i >> 16 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float f2 = (((i >> 20 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        final float f3 = (((i >> 24 & 0x7L) + 0.5f) / 8.0f - 0.5f) * 0.004f;
        GlStateManager.translate(f, f2, f3);
        final double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        final double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        final double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
        final double d4 = 0.30000001192092896;
        final Vec3d vec3d = entity.getPos(d0, d2, d3);
        float f4 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
        if (vec3d != null) {
            Vec3d vec3d2 = entity.getPosOffset(d0, d2, d3, 0.30000001192092896);
            Vec3d vec3d3 = entity.getPosOffset(d0, d2, d3, -0.30000001192092896);
            if (vec3d2 == null) {
                vec3d2 = vec3d;
            }
            if (vec3d3 == null) {
                vec3d3 = vec3d;
            }
            x += vec3d.x - d0;
            y += (vec3d2.y + vec3d3.y) / 2.0 - d2;
            z += vec3d.z - d3;
            Vec3d vec3d4 = vec3d3.add(-vec3d2.x, -vec3d2.y, -vec3d2.z);
            if (vec3d4.length() != 0.0) {
                vec3d4 = vec3d4.normalize();
                entityYaw = (float)(Math.atan2(vec3d4.z, vec3d4.x) * 180.0 / 3.141592653589793);
                f4 = (float)(Math.atan(vec3d4.y) * 73.0);
            }
        }
        GlStateManager.translate((float)x, (float)y + 0.375f, (float)z);
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-f4, 0.0f, 0.0f, 1.0f);
        final float f5 = entity.getRollingAmplitude() - partialTicks;
        float f6 = entity.getDamage() - partialTicks;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f5 > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f5) * f5 * f6 / 10.0f * entity.getRollingDirection(), 1.0f, 0.0f, 0.0f);
        }
        final int j = entity.getDisplayTileOffset();
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        final IBlockState iblockstate = entity.getDisplayTile();
        if (iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            GlStateManager.pushMatrix();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            final float f7 = 0.75f;
            GlStateManager.scale(0.75f, 0.75f, 0.75f);
            GlStateManager.translate(-0.5f, (j - 8) / 16.0f, 0.5f);
            this.renderCartContents(entity, partialTicks, iblockstate);
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.bindEntityTexture(entity);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        this.modelMinecart.render(entity, 0.0f, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final T entity) {
        return RenderMinecart.MINECART_TEXTURES;
    }
    
    protected void renderCartContents(final T p_188319_1_, final float partialTicks, final IBlockState p_188319_3_) {
        GlStateManager.pushMatrix();
        Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(p_188319_3_, p_188319_1_.getBrightness());
        GlStateManager.popMatrix();
    }
    
    static {
        MINECART_TEXTURES = new ResourceLocation("textures/entity/minecart.png");
    }
}
