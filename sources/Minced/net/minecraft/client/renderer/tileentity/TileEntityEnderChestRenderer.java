// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityEnderChest;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer<TileEntityEnderChest>
{
    private static final ResourceLocation ENDER_CHEST_TEXTURE;
    private final ModelChest modelChest;
    
    public TileEntityEnderChestRenderer() {
        this.modelChest = new ModelChest();
    }
    
    @Override
    public void render(final TileEntityEnderChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        int i = 0;
        if (te.hasWorld()) {
            i = te.getBlockMetadata();
        }
        if (destroyStage >= 0) {
            this.bindTexture(TileEntityEnderChestRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntityEnderChestRenderer.ENDER_CHEST_TEXTURE);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        GlStateManager.translate((float)x, (float)y + 1.0f, (float)z + 1.0f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        int j = 0;
        if (i == 2) {
            j = 180;
        }
        if (i == 3) {
            j = 0;
        }
        if (i == 4) {
            j = 90;
        }
        if (i == 5) {
            j = -90;
        }
        GlStateManager.rotate((float)j, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
        f = 1.0f - f;
        f = 1.0f - f * f * f;
        this.modelChest.chestLid.rotateAngleX = -(f * 1.5707964f);
        this.modelChest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    static {
        ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    }
}
