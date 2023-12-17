// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Calendar;
import net.minecraft.client.model.ModelLargeChest;
import net.minecraft.client.model.ModelChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.tileentity.TileEntityChest;

public class TileEntityChestRenderer extends TileEntitySpecialRenderer<TileEntityChest>
{
    private static final ResourceLocation TEXTURE_TRAPPED_DOUBLE;
    private static final ResourceLocation TEXTURE_CHRISTMAS_DOUBLE;
    private static final ResourceLocation TEXTURE_NORMAL_DOUBLE;
    private static final ResourceLocation TEXTURE_TRAPPED;
    private static final ResourceLocation TEXTURE_CHRISTMAS;
    private static final ResourceLocation TEXTURE_NORMAL;
    private final ModelChest simpleChest;
    private final ModelChest largeChest;
    private boolean isChristmas;
    
    public TileEntityChestRenderer() {
        this.simpleChest = new ModelChest();
        this.largeChest = new ModelLargeChest();
        final Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26) {
            this.isChristmas = true;
        }
    }
    
    @Override
    public void render(final TileEntityChest te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;
        if (te.hasWorld()) {
            final Block block = te.getBlockType();
            i = te.getBlockMetadata();
            if (block instanceof BlockChest && i == 0) {
                ((BlockChest)block).checkForSurroundingChests(te.getWorld(), te.getPos(), te.getWorld().getBlockState(te.getPos()));
                i = te.getBlockMetadata();
            }
            te.checkForAdjacentChests();
        }
        else {
            i = 0;
        }
        if (te.adjacentChestZNeg == null && te.adjacentChestXNeg == null) {
            ModelChest modelchest;
            if (te.adjacentChestXPos == null && te.adjacentChestZPos == null) {
                modelchest = this.simpleChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_CHRISTMAS);
                }
                else if (te.getChestType() == BlockChest.Type.TRAP) {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_TRAPPED);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_NORMAL);
                }
            }
            else {
                modelchest = this.largeChest;
                if (destroyStage >= 0) {
                    this.bindTexture(TileEntityChestRenderer.DESTROY_STAGES[destroyStage]);
                    GlStateManager.matrixMode(5890);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(8.0f, 4.0f, 1.0f);
                    GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
                    GlStateManager.matrixMode(5888);
                }
                else if (this.isChristmas) {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_CHRISTMAS_DOUBLE);
                }
                else if (te.getChestType() == BlockChest.Type.TRAP) {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_TRAPPED_DOUBLE);
                }
                else {
                    this.bindTexture(TileEntityChestRenderer.TEXTURE_NORMAL_DOUBLE);
                }
            }
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            if (destroyStage < 0) {
                GlStateManager.color(1.0f, 1.0f, 1.0f, alpha);
            }
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
            if (i == 2 && te.adjacentChestXPos != null) {
                GlStateManager.translate(1.0f, 0.0f, 0.0f);
            }
            if (i == 5 && te.adjacentChestZPos != null) {
                GlStateManager.translate(0.0f, 0.0f, -1.0f);
            }
            GlStateManager.rotate((float)j, 0.0f, 1.0f, 0.0f);
            GlStateManager.translate(-0.5f, -0.5f, -0.5f);
            float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;
            if (te.adjacentChestZNeg != null) {
                final float f2 = te.adjacentChestZNeg.prevLidAngle + (te.adjacentChestZNeg.lidAngle - te.adjacentChestZNeg.prevLidAngle) * partialTicks;
                if (f2 > f) {
                    f = f2;
                }
            }
            if (te.adjacentChestXNeg != null) {
                final float f3 = te.adjacentChestXNeg.prevLidAngle + (te.adjacentChestXNeg.lidAngle - te.adjacentChestXNeg.prevLidAngle) * partialTicks;
                if (f3 > f) {
                    f = f3;
                }
            }
            f = 1.0f - f;
            f = 1.0f - f * f * f;
            modelchest.chestLid.rotateAngleX = -(f * 1.5707964f);
            modelchest.renderAll();
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
    
    static {
        TEXTURE_TRAPPED_DOUBLE = new ResourceLocation("textures/entity/chest/trapped_double.png");
        TEXTURE_CHRISTMAS_DOUBLE = new ResourceLocation("textures/entity/chest/christmas_double.png");
        TEXTURE_NORMAL_DOUBLE = new ResourceLocation("textures/entity/chest/normal_double.png");
        TEXTURE_TRAPPED = new ResourceLocation("textures/entity/chest/trapped.png");
        TEXTURE_CHRISTMAS = new ResourceLocation("textures/entity/chest/christmas.png");
        TEXTURE_NORMAL = new ResourceLocation("textures/entity/chest/normal.png");
    }
}
