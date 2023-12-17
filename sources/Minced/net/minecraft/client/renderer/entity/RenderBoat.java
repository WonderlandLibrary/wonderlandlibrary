// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.model.IMultipassModel;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelBoat;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityBoat;

public class RenderBoat extends Render<EntityBoat>
{
    private static final ResourceLocation[] BOAT_TEXTURES;
    protected ModelBase modelBoat;
    
    public RenderBoat(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelBoat = new ModelBoat();
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityBoat entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.setupTranslation(x, y, z);
        this.setupRotation(entity, entityYaw, partialTicks);
        this.bindEntityTexture(entity);
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        this.modelBoat.render(entity, partialTicks, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    public void setupRotation(final EntityBoat entityIn, final float entityYaw, final float partialTicks) {
        GlStateManager.rotate(180.0f - entityYaw, 0.0f, 1.0f, 0.0f);
        final float f = entityIn.getTimeSinceHit() - partialTicks;
        float f2 = entityIn.getDamageTaken() - partialTicks;
        if (f2 < 0.0f) {
            f2 = 0.0f;
        }
        if (f > 0.0f) {
            GlStateManager.rotate(MathHelper.sin(f) * f * f2 / 10.0f * entityIn.getForwardDirection(), 1.0f, 0.0f, 0.0f);
        }
        GlStateManager.scale(-1.0f, -1.0f, 1.0f);
    }
    
    public void setupTranslation(final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y + 0.375f, (float)z);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityBoat entity) {
        return RenderBoat.BOAT_TEXTURES[entity.getBoatType().ordinal()];
    }
    
    @Override
    public boolean isMultipass() {
        return true;
    }
    
    @Override
    public void renderMultipass(final EntityBoat entityIn, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        this.setupTranslation(x, y, z);
        this.setupRotation(entityIn, entityYaw, partialTicks);
        this.bindEntityTexture(entityIn);
        ((IMultipassModel)this.modelBoat).renderMultipass(entityIn, partialTicks, 0.0f, -0.1f, 0.0f, 0.0f, 0.0625f);
        GlStateManager.popMatrix();
    }
    
    static {
        BOAT_TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/entity/boat/boat_oak.png"), new ResourceLocation("textures/entity/boat/boat_spruce.png"), new ResourceLocation("textures/entity/boat/boat_birch.png"), new ResourceLocation("textures/entity/boat/boat_jungle.png"), new ResourceLocation("textures/entity/boat/boat_acacia.png"), new ResourceLocation("textures/entity/boat/boat_darkoak.png") };
    }
}
