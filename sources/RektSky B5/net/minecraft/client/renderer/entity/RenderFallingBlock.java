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
    public RenderFallingBlock(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }

    @Override
    public void doRender(EntityFallingBlock entity, double x2, double y2, double z2, float entityYaw, float partialTicks) {
        if (entity.getBlock() != null) {
            this.bindTexture(TextureMap.locationBlocksTexture);
            IBlockState iblockstate = entity.getBlock();
            Block block = iblockstate.getBlock();
            BlockPos blockpos = new BlockPos(entity);
            World world = entity.getWorldObj();
            if (iblockstate != world.getBlockState(blockpos) && block.getRenderType() != -1 && block.getRenderType() == 3) {
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)x2, (float)y2, (float)z2);
                GlStateManager.disableLighting();
                Tessellator tessellator = Tessellator.getInstance();
                WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                worldrenderer.begin(7, DefaultVertexFormats.BLOCK);
                int i2 = blockpos.getX();
                int j2 = blockpos.getY();
                int k2 = blockpos.getZ();
                worldrenderer.setTranslation((float)(-i2) - 0.5f, -j2, (float)(-k2) - 0.5f);
                BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                IBakedModel ibakedmodel = blockrendererdispatcher.getModelFromBlockState(iblockstate, world, null);
                blockrendererdispatcher.getBlockModelRenderer().renderModel(world, ibakedmodel, iblockstate, blockpos, worldrenderer, false);
                worldrenderer.setTranslation(0.0, 0.0, 0.0);
                tessellator.draw();
                GlStateManager.enableLighting();
                GlStateManager.popMatrix();
                super.doRender(entity, x2, y2, z2, entityYaw, partialTicks);
            }
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFallingBlock entity) {
        return TextureMap.locationBlocksTexture;
    }
}

