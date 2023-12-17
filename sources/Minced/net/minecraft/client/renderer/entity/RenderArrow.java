// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityArrow;

public abstract class RenderArrow<T extends EntityArrow> extends Render<T>
{
    public RenderArrow(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.bindEntityTexture(entity);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0f, 0.0f, 1.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final int i = 0;
        final float f = 0.0f;
        final float f2 = 0.5f;
        final float f3 = 0.0f;
        final float f4 = 0.15625f;
        final float f5 = 0.0f;
        final float f6 = 0.15625f;
        final float f7 = 0.15625f;
        final float f8 = 0.3125f;
        final float f9 = 0.05625f;
        GlStateManager.enableRescaleNormal();
        final float f10 = entity.arrowShake - partialTicks;
        if (f10 > 0.0f) {
            final float f11 = -MathHelper.sin(f10 * 3.0f) * f10;
            GlStateManager.rotate(f11, 0.0f, 0.0f, 1.0f);
        }
        GlStateManager.rotate(45.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(0.05625f, 0.05625f, 0.05625f);
        GlStateManager.translate(-4.0f, 0.0f, 0.0f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        GlStateManager.glNormal3f(0.05625f, 0.0f, 0.0f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, -2.0, -2.0).tex(0.0, 0.15625).endVertex();
        bufferbuilder.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        bufferbuilder.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        bufferbuilder.pos(-7.0, 2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        GlStateManager.glNormal3f(-0.05625f, 0.0f, 0.0f);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0, 2.0, -2.0).tex(0.0, 0.15625).endVertex();
        bufferbuilder.pos(-7.0, 2.0, 2.0).tex(0.15625, 0.15625).endVertex();
        bufferbuilder.pos(-7.0, -2.0, 2.0).tex(0.15625, 0.3125).endVertex();
        bufferbuilder.pos(-7.0, -2.0, -2.0).tex(0.0, 0.3125).endVertex();
        tessellator.draw();
        for (int j = 0; j < 4; ++j) {
            GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
            GlStateManager.glNormal3f(0.0f, 0.0f, 0.05625f);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
            bufferbuilder.pos(-8.0, -2.0, 0.0).tex(0.0, 0.0).endVertex();
            bufferbuilder.pos(8.0, -2.0, 0.0).tex(0.5, 0.0).endVertex();
            bufferbuilder.pos(8.0, 2.0, 0.0).tex(0.5, 0.15625).endVertex();
            bufferbuilder.pos(-8.0, 2.0, 0.0).tex(0.0, 0.15625).endVertex();
            tessellator.draw();
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
