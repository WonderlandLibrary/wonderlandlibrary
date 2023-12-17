// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelLeashKnot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.EntityLeashKnot;

public class RenderLeashKnot extends Render<EntityLeashKnot>
{
    private static final ResourceLocation LEASH_KNOT_TEXTURES;
    private final ModelLeashKnot leashKnotModel;
    
    public RenderLeashKnot(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.leashKnotModel = new ModelLeashKnot();
    }
    
    @Override
    public void doRender(final EntityLeashKnot entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.translate((float)x, (float)y, (float)z);
        final float f = 0.0625f;
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
        GlStateManager.enableAlpha();
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.leashKnotModel.render(entity, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityLeashKnot entity) {
        return RenderLeashKnot.LEASH_KNOT_TEXTURES;
    }
    
    static {
        LEASH_KNOT_TEXTURES = new ResourceLocation("textures/entity/lead_knot.png");
    }
}
