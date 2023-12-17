// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelLlamaSpit;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityLlamaSpit;

public class RenderLlamaSpit extends Render<EntityLlamaSpit>
{
    private static final ResourceLocation LLAMA_SPIT_TEXTURE;
    private final ModelLlamaSpit model;
    
    public RenderLlamaSpit(final RenderManager p_i47202_1_) {
        super(p_i47202_1_);
        this.model = new ModelLlamaSpit();
    }
    
    @Override
    public void doRender(final EntityLlamaSpit entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y + 0.15f, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0f, 0.0f, 1.0f);
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.model.render(entity, partialTicks, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLlamaSpit entity) {
        return RenderLlamaSpit.LLAMA_SPIT_TEXTURE;
    }
    
    static {
        LLAMA_SPIT_TEXTURE = new ResourceLocation("textures/entity/llama/spit.png");
    }
}
