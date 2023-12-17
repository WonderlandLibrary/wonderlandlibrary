// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHandSide;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityFishHook;

public class RenderFish extends Render<EntityFishHook>
{
    private static final ResourceLocation FISH_PARTICLES;
    
    public RenderFish(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityFishHook entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final EntityPlayer entityplayer = entity.getAngler();
        if (entityplayer != null && !this.renderOutlines) {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y, (float)z);
            GlStateManager.enableRescaleNormal();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            this.bindEntityTexture(entity);
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            final int i = 1;
            final int j = 2;
            final float f = 0.0625f;
            final float f2 = 0.125f;
            final float f3 = 0.125f;
            final float f4 = 0.1875f;
            final float f5 = 1.0f;
            final float f6 = 0.5f;
            final float f7 = 0.5f;
            GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
            if (this.renderOutlines) {
                GlStateManager.enableColorMaterial();
                GlStateManager.enableOutlineMode(this.getTeamColor(entity));
            }
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
            bufferbuilder.pos(-0.5, -0.5, 0.0).tex(0.0625, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, -0.5, 0.0).tex(0.125, 0.1875).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(0.5, 0.5, 0.0).tex(0.125, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            bufferbuilder.pos(-0.5, 0.5, 0.0).tex(0.0625, 0.125).normal(0.0f, 1.0f, 0.0f).endVertex();
            tessellator.draw();
            if (this.renderOutlines) {
                GlStateManager.disableOutlineMode();
                GlStateManager.disableColorMaterial();
            }
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            int k = (entityplayer.getPrimaryHand() == EnumHandSide.RIGHT) ? 1 : -1;
            final ItemStack itemstack = entityplayer.getHeldItemMainhand();
            if (itemstack.getItem() != Items.FISHING_ROD) {
                k = -k;
            }
            final float f8 = entityplayer.getSwingProgress(partialTicks);
            final float f9 = MathHelper.sin(MathHelper.sqrt(f8) * 3.1415927f);
            final float f10 = (entityplayer.prevRenderYawOffset + (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292f;
            final double d0 = MathHelper.sin(f10);
            final double d2 = MathHelper.cos(f10);
            final double d3 = k * 0.35;
            final double d4 = 0.8;
            double d5 = 0.0;
            double d6 = 0.0;
            double d7 = 0.0;
            double d8 = 0.0;
            Label_0828: {
                if (this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) {
                    final EntityPlayer entityPlayer = entityplayer;
                    Minecraft.getMinecraft();
                    if (entityPlayer == Minecraft.player) {
                        float f11 = this.renderManager.options.fovSetting;
                        f11 /= 100.0f;
                        Vec3d vec3d = new Vec3d(k * -0.36 * f11, -0.045 * f11, 0.4);
                        vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch + (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292f);
                        vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw + (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292f);
                        vec3d = vec3d.rotateYaw(f9 * 0.5f);
                        vec3d = vec3d.rotatePitch(-f9 * 0.7f);
                        d5 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * partialTicks + vec3d.x;
                        d6 = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * partialTicks + vec3d.y;
                        d7 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * partialTicks + vec3d.z;
                        d8 = entityplayer.getEyeHeight();
                        break Label_0828;
                    }
                }
                d5 = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * partialTicks - d2 * d3 - d0 * 0.8;
                d6 = entityplayer.prevPosY + entityplayer.getEyeHeight() + (entityplayer.posY - entityplayer.prevPosY) * partialTicks - 0.45;
                d7 = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * partialTicks - d0 * d3 + d2 * 0.8;
                d8 = (entityplayer.isSneaking() ? -0.1875 : 0.0);
            }
            final double d9 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            final double d10 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25;
            final double d11 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            final double d12 = (float)(d5 - d9);
            final double d13 = (float)(d6 - d10) + d8;
            final double d14 = (float)(d7 - d11);
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            final int l = 16;
            for (int i2 = 0; i2 <= 16; ++i2) {
                final float f12 = i2 / 16.0f;
                bufferbuilder.pos(x + d12 * f12, y + d13 * (f12 * f12 + f12) * 0.5 + 0.25, z + d14 * f12).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityFishHook entity) {
        return RenderFish.FISH_PARTICLES;
    }
    
    static {
        FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");
    }
}
