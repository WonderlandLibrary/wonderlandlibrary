// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.entity.item.EntityFallingBlock;

public class RenderFallingBlock extends Render<EntityFallingBlock>
{
    public RenderFallingBlock(final RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.5f;
    }
    
    @Override
    public void doRender(final EntityFallingBlock entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (entity.getBlock() != null) {
            final IBlockState iblockstate = entity.getBlock();
            if (iblockstate.getRenderType() == EnumBlockRenderType.MODEL) {
                final World world = entity.getWorldObj();
                if (iblockstate != world.getBlockState(new BlockPos(entity)) && iblockstate.getRenderType() != EnumBlockRenderType.INVISIBLE) {
                    this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                    GlStateManager.pushMatrix();
                    GlStateManager.disableLighting();
                    final Tessellator tessellator = Tessellator.getInstance();
                    final BufferBuilder bufferbuilder = tessellator.getBuffer();
                    if (this.renderOutlines) {
                        GlStateManager.enableColorMaterial();
                        GlStateManager.enableOutlineMode(this.getTeamColor(entity));
                    }
                    bufferbuilder.begin(7, DefaultVertexFormats.BLOCK);
                    final BlockPos blockpos = new BlockPos(entity.posX, entity.getEntityBoundingBox().maxY, entity.posZ);
                    GlStateManager.translate((float)(x - blockpos.getX() - 0.5), (float)(y - blockpos.getY()), (float)(z - blockpos.getZ() - 0.5));
                    final BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
                    blockrendererdispatcher.getBlockModelRenderer().renderModel(world, blockrendererdispatcher.getModelForState(iblockstate), iblockstate, blockpos, bufferbuilder, false, MathHelper.getPositionRandom(entity.getOrigin()));
                    tessellator.draw();
                    if (this.renderOutlines) {
                        GlStateManager.disableOutlineMode();
                        GlStateManager.disableColorMaterial();
                    }
                    GlStateManager.enableLighting();
                    GlStateManager.popMatrix();
                    super.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final EntityFallingBlock entity) {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }
}
