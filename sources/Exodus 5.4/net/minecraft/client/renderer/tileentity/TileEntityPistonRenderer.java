/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class TileEntityPistonRenderer
extends TileEntitySpecialRenderer<TileEntityPiston> {
    private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

    @Override
    public void renderTileEntityAt(TileEntityPiston tileEntityPiston, double d, double d2, double d3, float f, int n) {
        BlockPos blockPos = tileEntityPiston.getPos();
        IBlockState iBlockState = tileEntityPiston.getPistonState();
        Block block = iBlockState.getBlock();
        if (block.getMaterial() != Material.air && tileEntityPiston.getProgress(f) < 1.0f) {
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            this.bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(770, 771);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            } else {
                GlStateManager.shadeModel(7424);
            }
            worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
            worldRenderer.setTranslation((float)d - (float)blockPos.getX() + tileEntityPiston.getOffsetX(f), (float)d2 - (float)blockPos.getY() + tileEntityPiston.getOffsetY(f), (float)d3 - (float)blockPos.getZ() + tileEntityPiston.getOffsetZ(f));
            World world = this.getWorld();
            if (block == Blocks.piston_head && tileEntityPiston.getProgress(f) < 0.5f) {
                iBlockState = iBlockState.withProperty(BlockPistonExtension.SHORT, true);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iBlockState, world, blockPos), iBlockState, blockPos, worldRenderer, true);
            } else if (tileEntityPiston.shouldPistonHeadBeRendered() && !tileEntityPiston.isExtending()) {
                BlockPistonExtension.EnumPistonType enumPistonType = block == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iBlockState2 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, enumPistonType).withProperty(BlockPistonExtension.FACING, iBlockState.getValue(BlockPistonBase.FACING));
                iBlockState2 = iBlockState2.withProperty(BlockPistonExtension.SHORT, tileEntityPiston.getProgress(f) >= 0.5f);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iBlockState2, world, blockPos), iBlockState2, blockPos, worldRenderer, true);
                worldRenderer.setTranslation((float)d - (float)blockPos.getX(), (float)d2 - (float)blockPos.getY(), (float)d3 - (float)blockPos.getZ());
                iBlockState.withProperty(BlockPistonBase.EXTENDED, true);
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iBlockState, world, blockPos), iBlockState, blockPos, worldRenderer, true);
            } else {
                this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelFromBlockState(iBlockState, world, blockPos), iBlockState, blockPos, worldRenderer, false);
            }
            worldRenderer.setTranslation(0.0, 0.0, 0.0);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
}

