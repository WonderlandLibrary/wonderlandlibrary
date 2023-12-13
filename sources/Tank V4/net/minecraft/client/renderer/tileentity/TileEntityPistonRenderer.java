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
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class TileEntityPistonRenderer extends TileEntitySpecialRenderer {
   private final BlockRendererDispatcher blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();

   public void renderTileEntityAt(TileEntityPiston var1, double var2, double var4, double var6, float var8, int var9) {
      BlockPos var10 = var1.getPos();
      IBlockState var11 = var1.getPistonState();
      Block var12 = var11.getBlock();
      if (var12.getMaterial() != Material.air && var1.getProgress(var8) < 1.0F) {
         Tessellator var13 = Tessellator.getInstance();
         WorldRenderer var14 = var13.getWorldRenderer();
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

         var14.begin(7, DefaultVertexFormats.BLOCK);
         var14.setTranslation((double)((float)var2 - (float)var10.getX() + var1.getOffsetX(var8)), (double)((float)var4 - (float)var10.getY() + var1.getOffsetY(var8)), (double)((float)var6 - (float)var10.getZ() + var1.getOffsetZ(var8)));
         World var15 = this.getWorld();
         if (var12 == Blocks.piston_head && var1.getProgress(var8) < 0.5F) {
            var11 = var11.withProperty(BlockPistonExtension.SHORT, true);
            this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, true);
         } else if (var1.shouldPistonHeadBeRendered() && !var1.isExtending()) {
            BlockPistonExtension.EnumPistonType var16 = var12 == Blocks.sticky_piston ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
            IBlockState var17 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.TYPE, var16).withProperty(BlockPistonExtension.FACING, (EnumFacing)var11.getValue(BlockPistonBase.FACING));
            var17 = var17.withProperty(BlockPistonExtension.SHORT, var1.getProgress(var8) >= 0.5F);
            this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var17, var15, var10), var17, var10, var14, true);
            var14.setTranslation((double)((float)var2 - (float)var10.getX()), (double)((float)var4 - (float)var10.getY()), (double)((float)var6 - (float)var10.getZ()));
            var11.withProperty(BlockPistonBase.EXTENDED, true);
            this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, true);
         } else {
            this.blockRenderer.getBlockModelRenderer().renderModel(var15, this.blockRenderer.getModelFromBlockState(var11, var15, var10), var11, var10, var14, false);
         }

         var14.setTranslation(0.0D, 0.0D, 0.0D);
         var13.draw();
         RenderHelper.enableStandardItemLighting();
      }

   }

   public void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8, int var9) {
      this.renderTileEntityAt((TileEntityPiston)var1, var2, var4, var6, var8, var9);
   }
}
