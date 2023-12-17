// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.tileentity.TileEntityShulkerBox;

public class TileEntityShulkerBoxRenderer extends TileEntitySpecialRenderer<TileEntityShulkerBox>
{
    private final ModelShulker model;
    
    public TileEntityShulkerBoxRenderer(final ModelShulker modelIn) {
        this.model = modelIn;
    }
    
    @Override
    public void render(final TileEntityShulkerBox te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        EnumFacing enumfacing = EnumFacing.UP;
        if (te.hasWorld()) {
            final IBlockState iblockstate = this.getWorld().getBlockState(te.getPos());
            if (iblockstate.getBlock() instanceof BlockShulkerBox) {
                enumfacing = iblockstate.getValue(BlockShulkerBox.FACING);
            }
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        if (destroyStage >= 0) {
            this.bindTexture(TileEntityShulkerBoxRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[te.getColor().getMetadata()]);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        if (destroyStage < 0) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        }
        GlStateManager.translate((float)x + 0.5f, (float)y + 1.5f, (float)z + 0.5f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.0f, 1.0f, 0.0f);
        final float f = 0.9995f;
        GlStateManager.scale(0.9995f, 0.9995f, 0.9995f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        switch (enumfacing) {
            case DOWN: {
                GlStateManager.translate(0.0f, 2.0f, 0.0f);
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
                break;
            }
            case NORTH: {
                GlStateManager.translate(0.0f, 1.0f, 1.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
            case SOUTH: {
                GlStateManager.translate(0.0f, 1.0f, -1.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                break;
            }
            case WEST: {
                GlStateManager.translate(-1.0f, 1.0f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
            case EAST: {
                GlStateManager.translate(1.0f, 1.0f, 0.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(90.0f, 0.0f, 0.0f, 1.0f);
                break;
            }
        }
        this.model.base.render(0.0625f);
        GlStateManager.translate(0.0f, -te.getProgress(partialTicks) * 0.5f, 0.0f);
        GlStateManager.rotate(270.0f * te.getProgress(partialTicks), 0.0f, 1.0f, 0.0f);
        this.model.lid.render(0.0625f);
        GlStateManager.enableCull();
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
