// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.item.EntityEnderCrystal;

public class RenderEnderCrystal extends Render<EntityEnderCrystal>
{
    private static final ResourceLocation ENDER_CRYSTAL_TEXTURES;
    private final ModelBase modelEnderCrystal;
    private final ModelBase modelEnderCrystalNoBase;
    
    public RenderEnderCrystal(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.modelEnderCrystal = new ModelEnderCrystal(0.0f, true);
        this.modelEnderCrystalNoBase = new ModelEnderCrystal(0.0f, false);
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityEnderCrystal entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final float f = entity.innerRotation + partialTicks;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.bindTexture(RenderEnderCrystal.ENDER_CRYSTAL_TEXTURES);
        float f2 = MathHelper.sin(f * 0.2f) / 2.0f + 0.5f;
        f2 += f2 * f2;
        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        if (entity.shouldShowBottom()) {
            this.modelEnderCrystal.render(entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
        }
        else {
            this.modelEnderCrystalNoBase.render(entity, 0.0f, f * 3.0f, f2 * 0.2f, 0.0f, 0.0f, 0.0625f);
        }
        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }
        GlStateManager.popMatrix();
        final BlockPos blockpos = entity.getBeamTarget();
        if (blockpos != null) {
            this.bindTexture(RenderDragon.ENDERCRYSTAL_BEAM_TEXTURES);
            final float f3 = blockpos.getX() + 0.5f;
            final float f4 = blockpos.getY() + 0.5f;
            final float f5 = blockpos.getZ() + 0.5f;
            final double d0 = f3 - entity.posX;
            final double d2 = f4 - entity.posY;
            final double d3 = f5 - entity.posZ;
            RenderDragon.renderCrystalBeams(x + d0, y - 0.3 + f2 * 0.4f + d2, z + d3, partialTicks, f3, f4, f5, entity.innerRotation, entity.posX, entity.posY, entity.posZ);
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityEnderCrystal entity) {
        return RenderEnderCrystal.ENDER_CRYSTAL_TEXTURES;
    }
    
    @Override
    public boolean shouldRender(final EntityEnderCrystal livingEntity, final ICamera camera, final double camX, final double camY, final double camZ) {
        return super.shouldRender(livingEntity, camera, camX, camY, camZ) || livingEntity.getBeamTarget() != null;
    }
    
    static {
        ENDER_CRYSTAL_TEXTURES = new ResourceLocation("textures/entity/endercrystal/endercrystal.png");
    }
}
