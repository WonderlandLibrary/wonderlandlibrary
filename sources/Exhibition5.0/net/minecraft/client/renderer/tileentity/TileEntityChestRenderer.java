// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntityChest;
import java.util.Calendar;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer
{
    private static final ResourceLocation textureTrappedDouble;
    private static final ResourceLocation textureChristmasDouble;
    private static final ResourceLocation textureNormalDouble;
    private static final ResourceLocation textureTrapped;
    private static final ResourceLocation textureChristmas;
    private static final ResourceLocation textureNormal;
    private ModelChest simpleChest;
    private ModelChest largeChest;
    private boolean isChristams;
    private static final String __OBFID = "CL_00000965";
    
    public TileEntityChestRenderer() {
        this.simpleChest = new ModelChest();
        this.largeChest = new ModelLargeChest();
        final Calendar var1 = Calendar.getInstance();
        if (var1.get(2) + 1 == 12 && var1.get(5) >= 24 && var1.get(5) <= 26) {
            this.isChristams = true;
        }
    }
    
    public void func_180538_a(final TileEntityChest p_180538_1_, final double p_180538_2_, final double p_180538_4_, final double p_180538_6_, final float p_180538_8_, final int p_180538_9_) {
        int var10;
        if (!p_180538_1_.hasWorldObj()) {
            var10 = 0;
        }
        else {
            final Block var11 = p_180538_1_.getBlockType();
            var10 = p_180538_1_.getBlockMetadata();
            if (var11 instanceof BlockChest && var10 == 0) {
                ((BlockChest)var11).checkForSurroundingChests(p_180538_1_.getWorld(), p_180538_1_.getPos(), p_180538_1_.getWorld().getBlockState(p_180538_1_.getPos()));
                var10 = p_180538_1_.getBlockMetadata();
            }
            p_180538_1_.checkForAdjacentChests();
        }
        if (p_180538_1_.adjacentChestZNeg == null && p_180538_1_.adjacentChestXNeg == null) {
            ModelChest var12;
            if (p_180538_1_.adjacentChestXPos == null && p_180538_1_.adjacentChestZPos == null) {
                var12 = this.simpleChest;
                if (p_180538_9_ >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[p_180538_9_]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (p_180538_1_.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrapped);
                }
                else if (this.isChristams) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmas);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormal);
                }
            }
            else {
                var12 = this.largeChest;
                if (p_180538_9_ >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[p_180538_9_]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (p_180538_1_.getChestType() == 1) {
                    this.bindTexture(TileEntityChestRenderer.textureTrappedDouble);
                }
                else if (this.isChristams) {
                    this.bindTexture(TileEntityChestRenderer.textureChristmasDouble);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.textureNormalDouble);
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (p_180538_9_ < 0) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            }
            GlStateManager.translate((float)p_180538_2_, (float)p_180538_4_ + 1.0f, (float)p_180538_6_ + 1.0f);
            GlStateManager.scale(1.0f, -1.0f, -1.0f);
            GlStateManager.translate(0.5f, 0.5f, 0.5f);
            short var13 = 0;
            if (var10 == 2) {
                var13 = 180;
            }
            if (var10 == 3) {
                var13 = 0;
            }
            if (var10 == 4) {
                var13 = 90;
            }
            if (var10 == 5) {
                var13 = -90;
            }
            if (var10 == 2 && p_180538_1_.adjacentChestXPos != null) {
                GlStateManager.translate(1.0f, 0.0f, 0.0f);
            }
            if (var10 == 5 && p_180538_1_.adjacentChestZPos != null) {
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
            }
            GlStateManager.rotate(var13, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            float var14 = p_180538_1_.prevLidAngle + (p_180538_1_.lidAngle - p_180538_1_.prevLidAngle) * p_180538_8_;
            if (p_180538_1_.adjacentChestZNeg != null) {
                final float var15 = p_180538_1_.adjacentChestZNeg.prevLidAngle + (p_180538_1_.adjacentChestZNeg.lidAngle - p_180538_1_.adjacentChestZNeg.prevLidAngle) * p_180538_8_;
                if (var15 > var14) {
                    var14 = var15;
                }
            }
            if (p_180538_1_.adjacentChestXNeg != null) {
                final float var15 = p_180538_1_.adjacentChestXNeg.prevLidAngle + (p_180538_1_.adjacentChestXNeg.lidAngle - p_180538_1_.adjacentChestXNeg.prevLidAngle) * p_180538_8_;
                if (var15 > var14) {
                    var14 = var15;
                }
            }
            var14 = 1.0f - var14;
            var14 = 1.0f - var14 * var14 * var14;
            var12.chestLid.rotateAngleX = -(var14 * 3.1415927f / 2.0f);
            var12.renderAll();
            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (p_180538_9_ >= 0) {
                GlStateManager.matrixMode(5890);
                GlStateManager.popMatrix();
                GlStateManager.matrixMode(5888);
            }
        }
    }
    
    @Override
    public void renderTileEntityAt(final TileEntity p_180535_1_, final double p_180535_2_, final double p_180535_4_, final double p_180535_6_, final float p_180535_8_, final int p_180535_9_) {
        this.func_180538_a((TileEntityChest)p_180535_1_, p_180535_2_, p_180535_4_, p_180535_6_, p_180535_8_, p_180535_9_);
    }
    
    static {
        textureTrappedDouble = new ResourceLocation("textures/entity/chest/trapped_double.png");
        textureChristmasDouble = new ResourceLocation("textures/entity/chest/christmas_double.png");
        textureNormalDouble = new ResourceLocation("textures/entity/chest/normal_double.png");
        textureTrapped = new ResourceLocation("textures/entity/chest/trapped.png");
        textureChristmas = new ResourceLocation("textures/entity/chest/christmas.png");
        textureNormal = new ResourceLocation("textures/entity/chest/normal.png");
    }
}
