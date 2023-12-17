// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.init.Blocks;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.tileentity.TileEntityPiston;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer<TileEntityPiston>
{
    private final BlockRendererDispatcher blockRenderer;
    
    public TileEntityPistonRenderer() {
        this.blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
    }
    
    @Override
    public void render(final TileEntityPiston te, final double x, final double y, final double z, final float partialTicks, final int destroyStage, final float alpha) {
        final BlockPos blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        final Block block = iblockstate.getBlock();
        if (iblockstate.getMaterial() != Material.AIR && te.getProgress(partialTicks) < 1.0f) {
            final Tessellator tessellator = Tessellator.getInstance();
            final BufferBuilder bufferbuilder = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            }
            else {
                GlStateManager.shadeModel(7424);
            }
            bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
            bufferbuilder.setTranslation(x - blockpos.getX() + te.getOffsetX(partialTicks), y - blockpos.getY() + te.getOffsetY(partialTicks), z - blockpos.getZ() + te.getOffsetZ(partialTicks));
            final World world = this.getWorld();
            if (block == Blocks.PISTON_HEAD && te.getProgress(partialTicks) <= 0.25f) {
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, true);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending()) {
                final BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = (block == Blocks.STICKY_PISTON) ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate2 = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty((IProperty<Comparable>)BlockPistonExtension.FACING, (Comparable)iblockstate.getValue((IProperty<V>)BlockPistonBase.FACING));
                iblockstate2 = iblockstate2.withProperty((IProperty<Comparable>)BlockPistonExtension.SHORT, te.getProgress(partialTicks) >= 0.5f);
                this.renderStateModel(blockpos, iblockstate2, bufferbuilder, world, true);
                bufferbuilder.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
                iblockstate = iblockstate.withProperty((IProperty<Comparable>)BlockPistonBase.EXTENDED, true);
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, true);
            }
            else {
                this.renderStateModel(blockpos, iblockstate, bufferbuilder, world, false);
            }
            bufferbuilder.setTranslation(0.0, 0.0, 0.0);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    private boolean renderStateModel(final BlockPos pos, final IBlockState state, final BufferBuilder buffer, final World p_188186_4_, final boolean checkSides) {
        return this.blockRenderer.getBlockModelRenderer().renderModel(p_188186_4_, this.blockRenderer.getModelForState(state), state, pos, buffer, checkSides);
    }
}
