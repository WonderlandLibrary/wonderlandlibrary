// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.CrashReport;
import net.minecraft.world.WorldType;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class BlockRendererDispatcher implements IResourceManagerReloadListener
{
    private final BlockModelShapes blockModelShapes;
    private final BlockModelRenderer blockModelRenderer;
    private final ChestRenderer chestRenderer;
    private final BlockFluidRenderer fluidRenderer;
    
    public BlockRendererDispatcher(final BlockModelShapes p_i46577_1_, final BlockColors p_i46577_2_) {
        this.chestRenderer = new ChestRenderer();
        this.blockModelShapes = p_i46577_1_;
        this.blockModelRenderer = new BlockModelRenderer(p_i46577_2_);
        this.fluidRenderer = new BlockFluidRenderer(p_i46577_2_);
    }
    
    public BlockModelShapes getBlockModelShapes() {
        return this.blockModelShapes;
    }
    
    public void renderBlockDamage(IBlockState state, final BlockPos pos, final TextureAtlasSprite texture, final IBlockAccess blockAccess) {
        if (state.getRenderType() == EnumBlockRenderType.MODEL) {
            state = state.getActualState(blockAccess, pos);
            final IBakedModel ibakedmodel = this.blockModelShapes.getModelForState(state);
            final IBakedModel ibakedmodel2 = new SimpleBakedModel.Builder(state, ibakedmodel, texture, pos).makeBakedModel();
            this.blockModelRenderer.renderModel(blockAccess, ibakedmodel2, state, pos, Tessellator.getInstance().getBuffer(), true);
        }
    }
    
    public boolean renderBlock(IBlockState state, final BlockPos pos, final IBlockAccess blockAccess, final BufferBuilder bufferBuilderIn) {
        try {
            final EnumBlockRenderType enumblockrendertype = state.getRenderType();
            if (enumblockrendertype == EnumBlockRenderType.INVISIBLE) {
                return false;
            }
            if (blockAccess.getWorldType() != WorldType.DEBUG_ALL_BLOCK_STATES) {
                try {
                    state = state.getActualState(blockAccess, pos);
                }
                catch (Exception ex) {}
            }
            switch (enumblockrendertype) {
                case MODEL: {
                    return this.blockModelRenderer.renderModel(blockAccess, this.getModelForState(state), state, pos, bufferBuilderIn, true);
                }
                case ENTITYBLOCK_ANIMATED: {
                    return false;
                }
                case LIQUID: {
                    return this.fluidRenderer.renderFluid(blockAccess, state, pos, bufferBuilderIn);
                }
                default: {
                    return false;
                }
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, state.getBlock(), state.getBlock().getMetaFromState(state));
            throw new ReportedException(crashreport);
        }
    }
    
    public BlockModelRenderer getBlockModelRenderer() {
        return this.blockModelRenderer;
    }
    
    public IBakedModel getModelForState(final IBlockState state) {
        return this.blockModelShapes.getModelForState(state);
    }
    
    public void renderBlockBrightness(final IBlockState state, final float brightness) {
        final EnumBlockRenderType enumblockrendertype = state.getRenderType();
        if (enumblockrendertype != EnumBlockRenderType.INVISIBLE) {
            switch (enumblockrendertype) {
                case MODEL: {
                    final IBakedModel ibakedmodel = this.getModelForState(state);
                    this.blockModelRenderer.renderModelBrightness(ibakedmodel, state, brightness, true);
                    break;
                }
                case ENTITYBLOCK_ANIMATED: {
                    this.chestRenderer.renderChestBrightness(state.getBlock(), brightness);
                    break;
                }
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.fluidRenderer.initAtlasSprites();
    }
}
