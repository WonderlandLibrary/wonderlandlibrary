// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine;

import net.optifine.model.ModelUtils;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.util.EnumFacing;
import net.minecraft.src.Config;
import net.minecraft.block.state.IBlockState;
import java.util.List;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class SmartLeaves
{
    private static IBakedModel modelLeavesCullAcacia;
    private static IBakedModel modelLeavesCullBirch;
    private static IBakedModel modelLeavesCullDarkOak;
    private static IBakedModel modelLeavesCullJungle;
    private static IBakedModel modelLeavesCullOak;
    private static IBakedModel modelLeavesCullSpruce;
    private static List generalQuadsCullAcacia;
    private static List generalQuadsCullBirch;
    private static List generalQuadsCullDarkOak;
    private static List generalQuadsCullJungle;
    private static List generalQuadsCullOak;
    private static List generalQuadsCullSpruce;
    private static IBakedModel modelLeavesDoubleAcacia;
    private static IBakedModel modelLeavesDoubleBirch;
    private static IBakedModel modelLeavesDoubleDarkOak;
    private static IBakedModel modelLeavesDoubleJungle;
    private static IBakedModel modelLeavesDoubleOak;
    private static IBakedModel modelLeavesDoubleSpruce;
    
    public static IBakedModel getLeavesModel(final IBakedModel model, final IBlockState stateIn) {
        if (!Config.isTreesSmart()) {
            return model;
        }
        final List list = model.getQuads(stateIn, null, 0L);
        if (list == SmartLeaves.generalQuadsCullAcacia) {
            return SmartLeaves.modelLeavesDoubleAcacia;
        }
        if (list == SmartLeaves.generalQuadsCullBirch) {
            return SmartLeaves.modelLeavesDoubleBirch;
        }
        if (list == SmartLeaves.generalQuadsCullDarkOak) {
            return SmartLeaves.modelLeavesDoubleDarkOak;
        }
        if (list == SmartLeaves.generalQuadsCullJungle) {
            return SmartLeaves.modelLeavesDoubleJungle;
        }
        if (list == SmartLeaves.generalQuadsCullOak) {
            return SmartLeaves.modelLeavesDoubleOak;
        }
        return (list == SmartLeaves.generalQuadsCullSpruce) ? SmartLeaves.modelLeavesDoubleSpruce : model;
    }
    
    public static boolean isSameLeaves(final IBlockState state1, final IBlockState state2) {
        if (state1 == state2) {
            return true;
        }
        final Block block = state1.getBlock();
        final Block block2 = state2.getBlock();
        if (block != block2) {
            return false;
        }
        if (block instanceof BlockOldLeaf) {
            return state1.getValue(BlockOldLeaf.VARIANT).equals(state2.getValue(BlockOldLeaf.VARIANT));
        }
        return block instanceof BlockNewLeaf && state1.getValue(BlockNewLeaf.VARIANT).equals(state2.getValue(BlockNewLeaf.VARIANT));
    }
    
    public static void updateLeavesModels() {
        final List list = new ArrayList();
        SmartLeaves.modelLeavesCullAcacia = getModelCull("acacia", list);
        SmartLeaves.modelLeavesCullBirch = getModelCull("birch", list);
        SmartLeaves.modelLeavesCullDarkOak = getModelCull("dark_oak", list);
        SmartLeaves.modelLeavesCullJungle = getModelCull("jungle", list);
        SmartLeaves.modelLeavesCullOak = getModelCull("oak", list);
        SmartLeaves.modelLeavesCullSpruce = getModelCull("spruce", list);
        SmartLeaves.generalQuadsCullAcacia = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullAcacia);
        SmartLeaves.generalQuadsCullBirch = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullBirch);
        SmartLeaves.generalQuadsCullDarkOak = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullDarkOak);
        SmartLeaves.generalQuadsCullJungle = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullJungle);
        SmartLeaves.generalQuadsCullOak = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullOak);
        SmartLeaves.generalQuadsCullSpruce = getGeneralQuadsSafe(SmartLeaves.modelLeavesCullSpruce);
        SmartLeaves.modelLeavesDoubleAcacia = getModelDoubleFace(SmartLeaves.modelLeavesCullAcacia);
        SmartLeaves.modelLeavesDoubleBirch = getModelDoubleFace(SmartLeaves.modelLeavesCullBirch);
        SmartLeaves.modelLeavesDoubleDarkOak = getModelDoubleFace(SmartLeaves.modelLeavesCullDarkOak);
        SmartLeaves.modelLeavesDoubleJungle = getModelDoubleFace(SmartLeaves.modelLeavesCullJungle);
        SmartLeaves.modelLeavesDoubleOak = getModelDoubleFace(SmartLeaves.modelLeavesCullOak);
        SmartLeaves.modelLeavesDoubleSpruce = getModelDoubleFace(SmartLeaves.modelLeavesCullSpruce);
        if (list.size() > 0) {
            Config.dbg("Enable face culling: " + Config.arrayToString(list.toArray()));
        }
    }
    
    private static List getGeneralQuadsSafe(final IBakedModel model) {
        return (model == null) ? null : model.getQuads(null, null, 0L);
    }
    
    static IBakedModel getModelCull(final String type, final List updatedTypes) {
        final ModelManager modelmanager = Config.getModelManager();
        if (modelmanager == null) {
            return null;
        }
        final ResourceLocation resourcelocation = new ResourceLocation("blockstates/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack(resourcelocation) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ResourceLocation resourcelocation2 = new ResourceLocation("models/block/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack(resourcelocation2) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(type + "_leaves", "normal");
        final IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
        if (ibakedmodel == null || ibakedmodel == modelmanager.getMissingModel()) {
            return null;
        }
        final List<BakedQuad> list = ibakedmodel.getQuads(null, null, 0L);
        if (list.size() == 0) {
            return ibakedmodel;
        }
        if (list.size() != 6) {
            return null;
        }
        for (final BakedQuad bakedquad : list) {
            final List list2 = ibakedmodel.getQuads(null, bakedquad.getFace(), 0L);
            if (list2.size() > 0) {
                return null;
            }
            list2.add(bakedquad);
        }
        list.clear();
        updatedTypes.add(type + "_leaves");
        return ibakedmodel;
    }
    
    private static IBakedModel getModelDoubleFace(final IBakedModel model) {
        if (model == null) {
            return null;
        }
        if (model.getQuads(null, null, 0L).size() > 0) {
            Config.warn("SmartLeaves: Model is not cube, general quads: " + model.getQuads(null, null, 0L).size() + ", model: " + model);
            return model;
        }
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List<BakedQuad> list = model.getQuads(null, enumfacing, 0L);
            if (list.size() != 1) {
                Config.warn("SmartLeaves: Model is not cube, side: " + enumfacing + ", quads: " + list.size() + ", model: " + model);
                return model;
            }
        }
        final IBakedModel ibakedmodel = ModelUtils.duplicateModel(model);
        final List[] alist = new List[aenumfacing.length];
        for (int k = 0; k < aenumfacing.length; ++k) {
            final EnumFacing enumfacing2 = aenumfacing[k];
            final List<BakedQuad> list2 = ibakedmodel.getQuads(null, enumfacing2, 0L);
            final BakedQuad bakedquad = list2.get(0);
            final BakedQuad bakedquad2 = new BakedQuad(bakedquad.getVertexData().clone(), bakedquad.getTintIndex(), bakedquad.getFace(), bakedquad.getSprite());
            final int[] aint = bakedquad2.getVertexData();
            final int[] aint2 = aint.clone();
            final int j = aint.length / 4;
            System.arraycopy(aint, 0 * j, aint2, 3 * j, j);
            System.arraycopy(aint, 1 * j, aint2, 2 * j, j);
            System.arraycopy(aint, 2 * j, aint2, 1 * j, j);
            System.arraycopy(aint, 3 * j, aint2, 0 * j, j);
            System.arraycopy(aint2, 0, aint, 0, aint2.length);
            list2.add(bakedquad2);
        }
        return ibakedmodel;
    }
    
    static {
        SmartLeaves.modelLeavesCullAcacia = null;
        SmartLeaves.modelLeavesCullBirch = null;
        SmartLeaves.modelLeavesCullDarkOak = null;
        SmartLeaves.modelLeavesCullJungle = null;
        SmartLeaves.modelLeavesCullOak = null;
        SmartLeaves.modelLeavesCullSpruce = null;
        SmartLeaves.generalQuadsCullAcacia = null;
        SmartLeaves.generalQuadsCullBirch = null;
        SmartLeaves.generalQuadsCullDarkOak = null;
        SmartLeaves.generalQuadsCullJungle = null;
        SmartLeaves.generalQuadsCullOak = null;
        SmartLeaves.generalQuadsCullSpruce = null;
        SmartLeaves.modelLeavesDoubleAcacia = null;
        SmartLeaves.modelLeavesDoubleBirch = null;
        SmartLeaves.modelLeavesDoubleDarkOak = null;
        SmartLeaves.modelLeavesDoubleJungle = null;
        SmartLeaves.modelLeavesDoubleOak = null;
        SmartLeaves.modelLeavesDoubleSpruce = null;
    }
}
