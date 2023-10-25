/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.util.EnumFacing;

public class TileEntityShulkerBoxRenderer
extends TileEntitySpecialRenderer<TileEntityShulkerBox> {
    private final ModelShulker field_191285_a;

    public TileEntityShulkerBoxRenderer(ModelShulker p_i47216_1_) {
        this.field_191285_a = p_i47216_1_;
    }

    @Override
    public void func_192841_a(TileEntityShulkerBox p_192841_1_, double p_192841_2_, double p_192841_4_, double p_192841_6_, float p_192841_8_, int p_192841_9_, float p_192841_10_) {
        IBlockState iblockstate;
        EnumFacing enumfacing = EnumFacing.UP;
        if (p_192841_1_.hasWorldObj() && (iblockstate = this.getWorld().getBlockState(p_192841_1_.getPos())).getBlock() instanceof BlockShulkerBox) {
            enumfacing = iblockstate.getValue(BlockShulkerBox.field_190957_a);
        }
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        if (p_192841_9_ >= 0) {
            this.bindTexture(DESTROY_STAGES[p_192841_9_]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0f, 4.0f, 1.0f);
            GlStateManager.translate(0.0625f, 0.0625f, 0.0625f);
            GlStateManager.matrixMode(5888);
        } else {
            this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[p_192841_1_.func_190592_s().getMetadata()]);
        }
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        if (p_192841_9_ < 0) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, p_192841_10_);
        }
        GlStateManager.translate((float)p_192841_2_ + 0.5f, (float)p_192841_4_ + 1.5f, (float)p_192841_6_ + 0.5f);
        GlStateManager.scale(1.0f, -1.0f, -1.0f);
        GlStateManager.translate(0.0f, 1.0f, 0.0f);
        float f = 0.9995f;
        GlStateManager.scale(0.9995f, 0.9995f, 0.9995f);
        GlStateManager.translate(0.0f, -1.0f, 0.0f);
        switch (enumfacing) {
            case DOWN: {
                GlStateManager.translate(0.0f, 2.0f, 0.0f);
                GlStateManager.rotate(180.0f, 1.0f, 0.0f, 0.0f);
            }
            default: {
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
            }
        }
        this.field_191285_a.base.render(0.0625f);
        GlStateManager.translate(0.0f, -p_192841_1_.func_190585_a(p_192841_8_) * 0.5f, 0.0f);
        GlStateManager.rotate(270.0f * p_192841_1_.func_190585_a(p_192841_8_), 0.0f, 1.0f, 0.0f);
        this.field_191285_a.lid.render(0.0625f);
        GlStateManager.enableCull();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        if (p_192841_9_ >= 0) {
            GlStateManager.matrixMode(5890);
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
        }
    }
}

