// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.model;

import com.google.common.collect.ImmutableList;
import net.optifine.NaturalTextures;
import net.optifine.ConnectedTextures;
import net.optifine.BetterGrass;
import net.minecraft.src.Config;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.optifine.SmartLeaves;
import net.optifine.render.RenderEnv;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

public class BlockModelCustomizer
{
    private static final List<BakedQuad> NO_QUADS;
    
    public static IBakedModel getRenderModel(IBakedModel modelIn, final IBlockState stateIn, final RenderEnv renderEnv) {
        if (renderEnv.isSmartLeaves()) {
            modelIn = SmartLeaves.getLeavesModel(modelIn, stateIn);
        }
        return modelIn;
    }
    
    public static List<BakedQuad> getRenderQuads(List<BakedQuad> quads, final IBlockAccess worldIn, final IBlockState stateIn, final BlockPos posIn, final EnumFacing enumfacing, final BlockRenderLayer layer, final long rand, final RenderEnv renderEnv) {
        if (enumfacing != null) {
            if (renderEnv.isSmartLeaves() && SmartLeaves.isSameLeaves(worldIn.getBlockState(posIn.offset(enumfacing)), stateIn)) {
                return BlockModelCustomizer.NO_QUADS;
            }
            if (!renderEnv.isBreakingAnimation(quads) && Config.isBetterGrass()) {
                quads = (List<BakedQuad>)BetterGrass.getFaceQuads(worldIn, stateIn, posIn, enumfacing, quads);
            }
        }
        final List<BakedQuad> list = renderEnv.getListQuadsCustomizer();
        list.clear();
        for (int i = 0; i < quads.size(); ++i) {
            final BakedQuad bakedquad = quads.get(i);
            final BakedQuad[] abakedquad = getRenderQuads(bakedquad, worldIn, stateIn, posIn, enumfacing, rand, renderEnv);
            if (i == 0 && quads.size() == 1 && abakedquad.length == 1 && abakedquad[0] == bakedquad && bakedquad.getQuadEmissive() == null) {
                return quads;
            }
            for (int j = 0; j < abakedquad.length; ++j) {
                final BakedQuad bakedquad2 = abakedquad[j];
                list.add(bakedquad2);
                if (bakedquad2.getQuadEmissive() != null) {
                    renderEnv.getListQuadsOverlay(getEmissiveLayer(layer)).addQuad(bakedquad2.getQuadEmissive(), stateIn);
                    renderEnv.setOverlaysRendered(true);
                }
            }
        }
        return list;
    }
    
    private static BlockRenderLayer getEmissiveLayer(final BlockRenderLayer layer) {
        return (layer != null && layer != BlockRenderLayer.SOLID) ? layer : BlockRenderLayer.CUTOUT_MIPPED;
    }
    
    private static BakedQuad[] getRenderQuads(BakedQuad quad, final IBlockAccess worldIn, final IBlockState stateIn, final BlockPos posIn, final EnumFacing enumfacing, final long rand, final RenderEnv renderEnv) {
        if (renderEnv.isBreakingAnimation(quad)) {
            return renderEnv.getArrayQuadsCtm(quad);
        }
        final BakedQuad bakedquad = quad;
        if (Config.isConnectedTextures()) {
            final BakedQuad[] abakedquad = ConnectedTextures.getConnectedTexture(worldIn, stateIn, posIn, quad, renderEnv);
            if (abakedquad.length != 1 || abakedquad[0] != quad) {
                return abakedquad;
            }
        }
        if (Config.isNaturalTextures()) {
            quad = NaturalTextures.getNaturalTexture(posIn, quad);
            if (quad != bakedquad) {
                return renderEnv.getArrayQuadsCtm(quad);
            }
        }
        return renderEnv.getArrayQuadsCtm(quad);
    }
    
    static {
        NO_QUADS = (List)ImmutableList.of();
    }
}
