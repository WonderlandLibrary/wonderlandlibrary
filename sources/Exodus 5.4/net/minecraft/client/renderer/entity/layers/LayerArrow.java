/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity.layers;

import java.util.Random;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.MathHelper;

public class LayerArrow
implements LayerRenderer<EntityLivingBase> {
    private final RendererLivingEntity field_177168_a;

    public LayerArrow(RendererLivingEntity rendererLivingEntity) {
        this.field_177168_a = rendererLivingEntity;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entityLivingBase, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        int n = entityLivingBase.getArrowCountInEntity();
        if (n > 0) {
            EntityArrow entityArrow = new EntityArrow(entityLivingBase.worldObj, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ);
            Random random = new Random(entityLivingBase.getEntityId());
            RenderHelper.disableStandardItemLighting();
            int n2 = 0;
            while (n2 < n) {
                GlStateManager.pushMatrix();
                ModelRenderer modelRenderer = this.field_177168_a.getMainModel().getRandomModelBox(random);
                ModelBox modelBox = modelRenderer.cubeList.get(random.nextInt(modelRenderer.cubeList.size()));
                modelRenderer.postRender(0.0625f);
                float f8 = random.nextFloat();
                float f9 = random.nextFloat();
                float f10 = random.nextFloat();
                float f11 = (modelBox.posX1 + (modelBox.posX2 - modelBox.posX1) * f8) / 16.0f;
                float f12 = (modelBox.posY1 + (modelBox.posY2 - modelBox.posY1) * f9) / 16.0f;
                float f13 = (modelBox.posZ1 + (modelBox.posZ2 - modelBox.posZ1) * f10) / 16.0f;
                GlStateManager.translate(f11, f12, f13);
                f8 = f8 * 2.0f - 1.0f;
                f9 = f9 * 2.0f - 1.0f;
                f10 = f10 * 2.0f - 1.0f;
                float f14 = MathHelper.sqrt_float((f8 *= -1.0f) * f8 + (f10 *= -1.0f) * f10);
                entityArrow.prevRotationYaw = entityArrow.rotationYaw = (float)(Math.atan2(f8, f10) * 180.0 / Math.PI);
                entityArrow.prevRotationPitch = entityArrow.rotationPitch = (float)(Math.atan2(f9 *= -1.0f, f14) * 180.0 / Math.PI);
                double d = 0.0;
                double d2 = 0.0;
                double d3 = 0.0;
                this.field_177168_a.getRenderManager().renderEntityWithPosYaw(entityArrow, d, d2, d3, 0.0f, f3);
                GlStateManager.popMatrix();
                ++n2;
            }
            RenderHelper.enableStandardItemLighting();
        }
    }
}

