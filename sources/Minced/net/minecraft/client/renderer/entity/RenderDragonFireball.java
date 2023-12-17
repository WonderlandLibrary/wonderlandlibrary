// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityDragonFireball;

public class RenderDragonFireball extends Render<EntityDragonFireball>
{
    private static final ResourceLocation DRAGON_FIREBALL_TEXTURE;
    
    public RenderDragonFireball(final RenderManager renderManagerIn) {
        super(renderManagerIn);
    }
    
    @Override
    public void doRender(final EntityDragonFireball entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.bindEntityTexture(entity);
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        final float f = 1.0f;
        final float f2 = 0.5f;
        final float f3 = 0.25f;
        GlStateManager.rotate(180.0f - this.renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(((this.renderManager.options.thirdPersonView == 2) ? -1 : 1) * -this.renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        bufferbuilder.pos(-0.5, -0.25, 0.0).tex(0.0, 1.0).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(0.5, -0.25, 0.0).tex(1.0, 1.0).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(0.5, 0.75, 0.0).tex(1.0, 0.0).normal(0.0f, 1.0f, 0.0f).endVertex();
        bufferbuilder.pos(-0.5, 0.75, 0.0).tex(0.0, 0.0).normal(0.0f, 1.0f, 0.0f).endVertex();
        tessellator.draw();
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityDragonFireball entity) {
        return RenderDragonFireball.DRAGON_FIREBALL_TEXTURE;
    }
    
    static {
        DRAGON_FIREBALL_TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");
    }
}
