/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.ChestRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;

public class BlockRendererDispatcher
implements IResourceManagerReloadListener {
    private final BlockFluidRenderer fluidRenderer;
    private final BlockModelRenderer blockModelRenderer = new BlockModelRenderer();
    private final ChestRenderer chestRenderer = new ChestRenderer();
    private final GameSettings gameSettings;
    private BlockModelShapes blockModelShapes;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean renderBlock(IBlockState iBlockState, BlockPos blockPos, IBlockAccess iBlockAccess, WorldRenderer worldRenderer) {
        try {
            int n = iBlockState.getBlock().getRenderType();
            if (n == -1) {
                return false;
            }
            switch (n) {
                case 1: {
                    return this.fluidRenderer.renderFluid(iBlockAccess, iBlockState, blockPos, worldRenderer);
                }
                case 2: {
                    return false;
                }
                case 3: {
                    IBakedModel iBakedModel = this.getModelFromBlockState(iBlockState, iBlockAccess, blockPos);
                    return this.blockModelRenderer.renderModel(iBlockAccess, iBakedModel, iBlockState, blockPos, worldRenderer);
                }
            }
            return false;
        }
        catch (Throwable throwable) {
            CrashReport crashReport = CrashReport.makeCrashReport(throwable, "Tesselating block in world");
            CrashReportCategory crashReportCategory = crashReport.makeCategory("Block being tesselated");
            CrashReportCategory.addBlockInfo(crashReportCategory, blockPos, iBlockState.getBlock(), iBlockState.getBlock().getMetaFromState(iBlockState));
            throw new ReportedException(crashReport);
        }
    }

    public BlockModelRenderer getBlockModelRenderer() {
        return this.blockModelRenderer;
    }

    public void renderBlockBrightness(IBlockState iBlockState, float f) {
        int n = iBlockState.getBlock().getRenderType();
        if (n != -1) {
            switch (n) {
                default: {
                    break;
                }
                case 2: {
                    this.chestRenderer.renderChestBrightness(iBlockState.getBlock(), f);
                    break;
                }
                case 3: {
                    IBakedModel iBakedModel = this.getBakedModel(iBlockState, null);
                    this.blockModelRenderer.renderModelBrightness(iBakedModel, iBlockState, f, true);
                }
            }
        }
    }

    private IBakedModel getBakedModel(IBlockState iBlockState, BlockPos blockPos) {
        IBakedModel iBakedModel = this.blockModelShapes.getModelForState(iBlockState);
        if (blockPos != null && this.gameSettings.allowBlockAlternatives && iBakedModel instanceof WeightedBakedModel) {
            iBakedModel = ((WeightedBakedModel)iBakedModel).getAlternativeModel(MathHelper.getPositionRandom(blockPos));
        }
        return iBakedModel;
    }

    public BlockRendererDispatcher(BlockModelShapes blockModelShapes, GameSettings gameSettings) {
        this.fluidRenderer = new BlockFluidRenderer();
        this.blockModelShapes = blockModelShapes;
        this.gameSettings = gameSettings;
    }

    public IBakedModel getModelFromBlockState(IBlockState iBlockState, IBlockAccess iBlockAccess, BlockPos blockPos) {
        Block block = iBlockState.getBlock();
        if (iBlockAccess.getWorldType() != WorldType.DEBUG_WORLD) {
            try {
                iBlockState = block.getActualState(iBlockState, iBlockAccess, blockPos);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        IBakedModel iBakedModel = this.blockModelShapes.getModelForState(iBlockState);
        if (blockPos != null && this.gameSettings.allowBlockAlternatives && iBakedModel instanceof WeightedBakedModel) {
            iBakedModel = ((WeightedBakedModel)iBakedModel).getAlternativeModel(MathHelper.getPositionRandom(blockPos));
        }
        return iBakedModel;
    }

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        this.fluidRenderer.initAtlasSprites();
    }

    public boolean isRenderTypeChest(Block block, int n) {
        if (block == null) {
            return false;
        }
        int n2 = block.getRenderType();
        return n2 == 3 ? false : n2 == 2;
    }

    public void renderBlockDamage(IBlockState iBlockState, BlockPos blockPos, TextureAtlasSprite textureAtlasSprite, IBlockAccess iBlockAccess) {
        Block block = iBlockState.getBlock();
        int n = block.getRenderType();
        if (n == 3) {
            iBlockState = block.getActualState(iBlockState, iBlockAccess, blockPos);
            IBakedModel iBakedModel = this.blockModelShapes.getModelForState(iBlockState);
            IBakedModel iBakedModel2 = new SimpleBakedModel.Builder(iBakedModel, textureAtlasSprite).makeBakedModel();
            this.blockModelRenderer.renderModel(iBlockAccess, iBakedModel2, iBlockState, blockPos, Tessellator.getInstance().getWorldRenderer());
        }
    }

    public BlockModelShapes getBlockModelShapes() {
        return this.blockModelShapes;
    }
}

