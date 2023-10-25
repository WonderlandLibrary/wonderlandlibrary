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
    private ModelChest field_147521_c = new ModelChest();
    private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");

    @Override
    public void renderTileEntityAt(TileEntityEnderChest tileEntityEnderChest, double d, double d2, double d3, float f, int n) {
        int n2 = 0;
        if (tileEntityEnderChest.hasWorldObj()) {
            n2 = tileEntityEnderChest.getBlockMetadata();
        }
        if (n >= 0) {
            this.bindTexture(DESTROY_STAGES[n]);
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
        GlStateManager.translate((float)d, (float)d2 + 1.0f, (float)d3 + 1.0f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        int n3 = 0;
        if (n2 == 2) {
            n3 = 180;
        }
        if (n2 == 3) {
            n3 = 0;
        }
        if (n2 == 4) {
            n3 = 90;
        }
        if (n2 == 5) {
            n3 = -90;
        }
        GlStateManager.rotate(n3, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        float f2 = tileEntityEnderChest.prevLidAngle + (tileEntityEnderChest.lidAngle - tileEntityEnderChest.prevLidAngle) * f;
        f2 = 1.0f - f2;
        f2 = 1.0f - f2 * f2 * f2;
        this.field_147521_c.chestLid.rotateAngleX = -(f2 * (float)Math.PI / 2.0f);
        this.field_147521_c.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (n >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

