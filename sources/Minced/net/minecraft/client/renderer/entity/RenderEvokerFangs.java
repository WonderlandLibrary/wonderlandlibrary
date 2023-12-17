// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelEvokerFangs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.projectile.EntityEvokerFangs;

public class RenderEvokerFangs extends Render<EntityEvokerFangs>
{
    private static final ResourceLocation EVOKER_ILLAGER_FANGS;
    private final ModelEvokerFangs model;
    
    public RenderEvokerFangs(final RenderManager p_i47208_1_) {
        super(p_i47208_1_);
        this.model = new ModelEvokerFangs();
    }
    
    @Override
    public void doRender(final EntityEvokerFangs entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final float f = entity.getAnimationProgress(partialTicks);
        if (f != 0.0f) {
            float f2 = 2.0f;
            if (f > 0.9f) {
                f2 *= (float)((1.0 - f) / 0.10000000149011612);
            }
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.enableAlpha();
            this.bindEntityTexture(entity);
            GlStateManager.translate((float)x, (float)y, (float)z);
            GlStateManager.rotate(90.0f - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
            GlStateManager.scale(-f2, -f2, f2);
            final float f3 = 0.03125f;
            GlStateManager.translate(0.0f, -0.626f, 0.0f);
            this.model.render(entity, f, 0.0f, 0.0f, entity.rotationYaw, entity.rotationPitch, 0.03125f);
            GlStateManager.popMatrix();
            GlStateManager.enableCull();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityEvokerFangs entity) {
        return RenderEvokerFangs.EVOKER_ILLAGER_FANGS;
    }
    
    static {
        EVOKER_ILLAGER_FANGS = new ResourceLocation("textures/entity/illager/fangs.png");
    }
}
