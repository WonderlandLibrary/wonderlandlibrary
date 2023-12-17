// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import java.util.Random;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.EntityLivingBase;

public class LayerArrow implements LayerRenderer<EntityLivingBase>
{
    private final RenderLivingBase<?> renderer;
    
    public LayerArrow(final RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
    }
    
    @Override
    public void doRenderLayer(final EntityLivingBase entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
        final int i = entitylivingbaseIn.getArrowCountInEntity();
        if (i > 0) {
            final Entity entity = new EntityTippedArrow(entitylivingbaseIn.world, entitylivingbaseIn.posX, entitylivingbaseIn.posY, entitylivingbaseIn.posZ);
            final Random random = new Random(entitylivingbaseIn.getEntityId());
            RenderHelper.disableStandardItemLighting();
            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                final ModelRenderer modelrenderer = this.renderer.getMainModel().getRandomModelBox(random);
                final ModelBox modelbox = modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625f);
                float f = random.nextFloat();
                float f2 = random.nextFloat();
                float f3 = random.nextFloat();
                final float f4 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16.0f;
                final float f5 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f2) / 16.0f;
                final float f6 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f3) / 16.0f;
                GlStateManager.translate(f4, f5, f6);
                f = f * 2.0f - 1.0f;
                f2 = f2 * 2.0f - 1.0f;
                f3 = f3 * 2.0f - 1.0f;
                f *= -1.0f;
                f2 *= -1.0f;
                f3 *= -1.0f;
                final float f7 = MathHelper.sqrt(f * f + f3 * f3);
                entity.rotationYaw = (float)(Math.atan2(f, f3) * 57.29577951308232);
                entity.rotationPitch = (float)(Math.atan2(f2, f7) * 57.29577951308232);
                entity.prevRotationYaw = entity.rotationYaw;
                entity.prevRotationPitch = entity.rotationPitch;
                final double d0 = 0.0;
                final double d2 = 0.0;
                final double d3 = 0.0;
                this.renderer.getRenderManager().renderEntity(entity, 0.0, 0.0, 0.0, 0.0f, partialTicks, false);
                GlStateManager.popMatrix();
            }
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
