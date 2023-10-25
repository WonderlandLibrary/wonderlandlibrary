/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.entity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class RenderFallingBlock
extends Render<EntityFallingBlock> {
    @Override
    protected ResourceLocation getEntityTexture(EntityFallingBlock entityFallingBlock) {
        return TextureMap.locationBlocksTexture;
    }

    public RenderFallingBlock(RenderManager renderManager) {
        super(renderManager);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityFallingBlock entityFallingBlock, double d, double d2, double d3, float f, float f2) {
        if (entityFallingBlock.getBlock() != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            IBlockState iBlockState = entityFallingBlock.getBlock();
            Block block = iBlockState.getBlock();
            BlockPos blockPos = new BlockPos(entityFallingBlock);
            World world = entityFallingBlock.getWorldObj();
            if (iBlockState != world.getBlockState(blockPos) && block.getRenderType() != -1 && block.getRenderType() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)d, (float)d2, (float)d3);
                GlStateManager.disableLighting();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldRenderer = tessellator.getWorldRenderer();
                worldRenderer.begin(7, DefaultVertexFormats.BLOCK);
                int n = blockPos.getX();
                int n2 = blockPos.getY();
                int n3 = blockPos.getZ();
                worldRenderer.setTranslation((float)(-n) - 0.5f, -n2, (float)(-n3) - 0.5f);
                BlockRendererDispatcher blockRendererDispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                IBakedModel iBakedModel = blockRendererDispatcher.getModelFromBlockState(iBlockState, world, null);
                blockRendererDispatcher.getBlockModelRenderer().renderModel(world, iBakedModel, iBlockState, blockPos, worldRenderer, false);
                worldRenderer.setTranslation(0.0, 0.0, 0.0);
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                super.doRender(entityFallingBlock, d, d2, d3, f, f2);
            }
        }
    }
}

