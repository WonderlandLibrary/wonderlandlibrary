// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.client.model.ModelBed;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityBed;

public class TileEntityBedRenderer extends TileEntitySpecialRenderer<TileEntityBed>
{
    private static final ResourceLocation[] TEXTURES;
    private ModelBed model;
    private int version;
    
    public TileEntityBedRenderer() {
        this.model = new ModelBed();
        this.version = this.model.getModelVersion();
    }
    
    @Override
    public void render(final TileEntityBed te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        if (this.version != this.model.getModelVersion()) {
            this.model = new ModelBed();
            this.version = this.model.getModelVersion();
        }
        final boolean flag = te.getWorld() != null;
        final boolean flag2 = !flag || te.isHeadPiece();
        final EnumDyeColor enumdyecolor = (te != null) ? te.getColor() : EnumDyeColor.RED;
        final int i = flag ? (te.getBlockMetadata() & 0x3) : 0;
        if (destroyStage >= 0) {
            this.bindTexture(TileEntityBedRenderer.DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        }
        else {
            final ResourceLocation resourcelocation = TileEntityBedRenderer.TEXTURES[enumdyecolor.getMetadata()];
            if (resourcelocation != null) {
                this.bindTexture(resourcelocation);
            }
        }
        if (flag) {
            this.renderPiece(flag2, x, y, z, i, alpha);
        }
        else {
            GlStateManager.pushMatrix();
            this.renderPiece(true, x, y, z, i, alpha);
            this.renderPiece(false, x, y, z - 1.0, i, alpha);
            GlStateManager.popMatrix();
        }
        if (destroyStage >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
    
    private void renderPiece(final boolean p_193847_1_, final double x, final double y, final double z, final int p_193847_8_, final float alpha) {
        this.model.preparePiece(p_193847_1_);
        GlStateManager.pushMatrix();
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        if (p_193847_8_ == EnumFacing.NORTH.getHorizontalIndex()) {
            f = 0.0f;
        }
        else if (p_193847_8_ == EnumFacing.SOUTH.getHorizontalIndex()) {
            f = 180.0f;
            f2 = 1.0f;
            f3 = 1.0f;
        }
        else if (p_193847_8_ == EnumFacing.WEST.getHorizontalIndex()) {
            f = -90.0f;
            f3 = 1.0f;
        }
        else if (p_193847_8_ == EnumFacing.EAST.getHorizontalIndex()) {
            f = 90.0f;
            f2 = 1.0f;
        }
        GlStateManager.translate((float)x + f2, (float)y + 0.5625f, (float)z + f3);
        GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.rotate(f, 0.0f, 0.0f, 1.0f);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        this.model.render();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
        GlStateManager.popMatrix();
    }
    
    static {
        final EnumDyeColor[] aenumdyecolor = EnumDyeColor.values();
        TEXTURES = new ResourceLocation[aenumdyecolor.length];
        for (final EnumDyeColor enumdyecolor : aenumdyecolor) {
            TileEntityBedRenderer.TEXTURES[enumdyecolor.getMetadata()] = new ResourceLocation("textures/entity/bed/" + enumdyecolor.getDyeColorName() + ".png");
        }
    }
}
