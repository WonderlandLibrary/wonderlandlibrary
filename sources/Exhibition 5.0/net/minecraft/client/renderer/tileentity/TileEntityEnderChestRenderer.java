// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityEnderChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation field_147520_b;
    private ModelChest field_147521_c;
    private static final String __OBFID = "CL_00000967";
    
    public TileEntityEnderChestRenderer() {
        this.field_147521_c = new ModelChest();
    }
    
    public void func_180540_a(final TileEntityEnderChest p_180540_1_, final double p_180540_2_, final double p_180540_4_, final double p_180540_6_, final float p_180540_8_, final int p_180540_9_) {
        int var10 = 0;
        if (p_180540_1_.hasWorldObj()) {
            var10 = p_180540_1_.getBlockMetadata();
        }
        if (p_180540_9_ >= 0) {
            this.bindTexture(TileEntityEnderChestRenderer.DESTROY_STAGES[p_180540_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(TileEntityEnderChestRenderer.field_147520_b);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.translate((float)p_180540_2_, (float)p_180540_4_ + 1.0f, (float)p_180540_6_ + 1.0f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.5f, 0.5f, 0.5f);
        short var11 = 0;
        if (var10 == 2) {
            var11 = 180;
        }
        if (var10 == 3) {
            var11 = 0;
        }
        if (var10 == 4) {
            var11 = 90;
        }
        if (var10 == 5) {
            var11 = -90;
        }
        GlStateManager.rotate(var11, 0.0f, 1.0f, 0.0f);
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        float var12 = p_180540_1_.prevLidAngle + (p_180540_1_.field_145972_a - p_180540_1_.prevLidAngle) * p_180540_8_;
        var12 = 1.0f - var12;
        var12 = 1.0f - var12 * var12 * var12;
        this.field_147521_c.chestLid.rotateAngleX = -(var12 * 3.1415927f / 2.0f);
        this.field_147521_c.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (p_180540_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180540_a((TileEntityEnderChest)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        field_147520_b = new ResourceLocation("textures/entity/chest/ender.png");
    }
}
