// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityFallingBlock;

public class RenderFallingBlock extends Render
{
    private static final String __OBFID = "CL_00000994";
    
    public RenderFallingBlock(final RenderManager p_i46177_1_) {
        super(p_i46177_1_);
        this.shadowSize = 0.5f;
    }
    
    public void doRender(final EntityFallingBlock p_180557_1_, final double p_180557_2_, final double p_180557_4_, final double p_180557_6_, final float p_180557_8_, final float p_180557_9_) {
        if (p_180557_1_.getBlock() != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            final IBlockState var10 = p_180557_1_.getBlock();
            final Block var11 = var10.getBlock();
            final BlockPos var12 = new BlockPos(p_180557_1_);
            final World var13 = p_180557_1_.getWorldObj();
            if (var10 != var13.getBlockState(var12) && var11.getRenderType() != -1 && var11.getRenderType() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)p_180557_2_, (float)p_180557_4_, (float)p_180557_6_);
                GlStateManager.disableLighting();
                final Tessellator var14 = Tessellator.getInstance();
                final WorldRenderer var15 = var14.getWorldRenderer();
                var15.startDrawingQuads();
                var15.setVertexFormat(DefaultVertexFormats.field_176600_a);
                final int var16 = var12.getX();
                final int var17 = var12.getY();
                final int var18 = var12.getZ();
                var15.setTranslation(-var16 - 0.5f, -var17, -var18 - 0.5f);
                final BlockRendererDispatcher var19 = Minecraft.getMinecraft().getBlockRendererDispatcher();
                final IBakedModel var20 = var19.getModelFromBlockState(var10, var13, null);
                var19.func_175019_b().renderBlockModel(var13, var20, var10, var12, var15, false);
                var15.setTranslation(0.0, 0.0, 0.0);
                var14.draw();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                super.doRender(p_180557_1_, p_180557_2_, p_180557_4_, p_180557_6_, p_180557_8_, p_180557_9_);
            }
        }
    }
    
    protected ResourceLocation getEntityTexture(final EntityFallingBlock p_110775_1_) {
        return TextureMap.locationBlocksTexture;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity p_110775_1_) {
        return this.getEntityTexture((EntityFallingBlock)p_110775_1_);
    }
    
    @Override
    public void doRender(final Entity p_76986_1_, final double p_76986_2_, final double p_76986_4_, final double p_76986_6_, final float p_76986_8_, final float p_76986_9_) {
        this.doRender((EntityFallingBlock)p_76986_1_, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
    }
}
