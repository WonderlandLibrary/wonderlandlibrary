/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnderChestRenderer
extends TileEntitySpecialRenderer<TileEntityEnderChest> {
    private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    private ModelChest field_147521_c = new ModelChest();

    @Override
    public void renderTileEntityAt(TileEntityEnderChest te, double x2, double y2, double z2, float partialTicks, int destroyStage) {
        int i2 = 0;
        if (te.hasWorldObj()) {
            i2 = te.getBlockMetadata();
        }
        if (destroyStage >= 0) {
            this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(ENDER_CHEST_TEXTURE);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.translate((float)x2, (float)y2 + 1.0f, (float)z2 + 1.0f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        int j2 = 0;
        if (i2 == 2) {
            j2 = 180;
        }
        if (i2 == 3) {
            j2 = 0;
        }
        if (i2 == 4) {
            j2 = 90;
        }
        if (i2 == 5) {
            j2 = -90;
        }
        GlStateManager.rotate(j2, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        float f2 = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f2 = 1.0f - f2;
        f2 = 1.0f - f2 * f2 * f2;
        this.field_147521_c.chestLid.rotateAngleX = -(f2 * (float)Math.PI / 2.0f);
        this.field_147521_c.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

