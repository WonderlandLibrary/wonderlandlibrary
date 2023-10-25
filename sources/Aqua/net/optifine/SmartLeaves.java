package net.optifine;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.optifine.model.ModelUtils;

public class SmartLeaves {
    private static IBakedModel modelLeavesCullAcacia = null;
    private static IBakedModel modelLeavesCullBirch = null;
    private static IBakedModel modelLeavesCullDarkOak = null;
    private static IBakedModel modelLeavesCullJungle = null;
    private static IBakedModel modelLeavesCullOak = null;
    private static IBakedModel modelLeavesCullSpruce = null;
    private static List generalQuadsCullAcacia = null;
    private static List generalQuadsCullBirch = null;
    private static List generalQuadsCullDarkOak = null;
    private static List generalQuadsCullJungle = null;
    private static List generalQuadsCullOak = null;
    private static List generalQuadsCullSpruce = null;
    private static IBakedModel modelLeavesDoubleAcacia = null;
    private static IBakedModel modelLeavesDoubleBirch = null;
    private static IBakedModel modelLeavesDoubleDarkOak = null;
    private static IBakedModel modelLeavesDoubleJungle = null;
    private static IBakedModel modelLeavesDoubleOak = null;
    private static IBakedModel modelLeavesDoubleSpruce = null;

    public static IBakedModel getLeavesModel(IBakedModel model, IBlockState stateIn) {
        if (!Config.isTreesSmart()) {
            return model;
        }
        List list = model.getGeneralQuads();
        return list == generalQuadsCullAcacia ? modelLeavesDoubleAcacia : (list == generalQuadsCullBirch ? modelLeavesDoubleBirch : (list == generalQuadsCullDarkOak ? modelLeavesDoubleDarkOak : (list == generalQuadsCullJungle ? modelLeavesDoubleJungle : (list == generalQuadsCullOak ? modelLeavesDoubleOak : (list == generalQuadsCullSpruce ? modelLeavesDoubleSpruce : model)))));
    }

    public static boolean isSameLeaves(IBlockState state1, IBlockState state2) {
        Block block1;
        if (state1 == state2) {
            return true;
        }
        Block block = state1.getBlock();
        return block != (block1 = state2.getBlock()) ? false : (block instanceof BlockOldLeaf ? ((BlockPlanks.EnumType)state1.getValue((IProperty)BlockOldLeaf.VARIANT)).equals((Object)state2.getValue((IProperty)BlockOldLeaf.VARIANT)) : (block instanceof BlockNewLeaf ? ((BlockPlanks.EnumType)state1.getValue((IProperty)BlockNewLeaf.VARIANT)).equals((Object)state2.getValue((IProperty)BlockNewLeaf.VARIANT)) : false));
    }

    public static void updateLeavesModels() {
        ArrayList list = new ArrayList();
        modelLeavesCullAcacia = SmartLeaves.getModelCull("acacia", (List)list);
        modelLeavesCullBirch = SmartLeaves.getModelCull("birch", (List)list);
        modelLeavesCullDarkOak = SmartLeaves.getModelCull("dark_oak", (List)list);
        modelLeavesCullJungle = SmartLeaves.getModelCull("jungle", (List)list);
        modelLeavesCullOak = SmartLeaves.getModelCull("oak", (List)list);
        modelLeavesCullSpruce = SmartLeaves.getModelCull("spruce", (List)list);
        generalQuadsCullAcacia = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullAcacia);
        generalQuadsCullBirch = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullBirch);
        generalQuadsCullDarkOak = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullDarkOak);
        generalQuadsCullJungle = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullJungle);
        generalQuadsCullOak = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullOak);
        generalQuadsCullSpruce = SmartLeaves.getGeneralQuadsSafe(modelLeavesCullSpruce);
        modelLeavesDoubleAcacia = SmartLeaves.getModelDoubleFace(modelLeavesCullAcacia);
        modelLeavesDoubleBirch = SmartLeaves.getModelDoubleFace(modelLeavesCullBirch);
        modelLeavesDoubleDarkOak = SmartLeaves.getModelDoubleFace(modelLeavesCullDarkOak);
        modelLeavesDoubleJungle = SmartLeaves.getModelDoubleFace(modelLeavesCullJungle);
        modelLeavesDoubleOak = SmartLeaves.getModelDoubleFace(modelLeavesCullOak);
        modelLeavesDoubleSpruce = SmartLeaves.getModelDoubleFace(modelLeavesCullSpruce);
        if (list.size() > 0) {
            Config.dbg((String)("Enable face culling: " + Config.arrayToString((Object[])list.toArray())));
        }
    }

    private static List getGeneralQuadsSafe(IBakedModel model) {
        return model == null ? null : model.getGeneralQuads();
    }

    static IBakedModel getModelCull(String type, List updatedTypes) {
        ModelManager modelmanager = Config.getModelManager();
        if (modelmanager == null) {
            return null;
        }
        ResourceLocation resourcelocation = new ResourceLocation("blockstates/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack((ResourceLocation)resourcelocation) != Config.getDefaultResourcePack()) {
            return null;
        }
        ResourceLocation resourcelocation1 = new ResourceLocation("models/block/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack((ResourceLocation)resourcelocation1) != Config.getDefaultResourcePack()) {
            return null;
        }
        ModelResourceLocation modelresourcelocation = new ModelResourceLocation(type + "_leaves", "normal");
        IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
        if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel()) {
            List list = ibakedmodel.getGeneralQuads();
            if (list.size() == 0) {
                return ibakedmodel;
            }
            if (list.size() != 6) {
                return null;
            }
            for (BakedQuad bakedquad : list) {
                List list1 = ibakedmodel.getFaceQuads(bakedquad.getFace());
                if (list1.size() > 0) {
                    return null;
                }
                list1.add((Object)bakedquad);
            }
            list.clear();
            updatedTypes.add((Object)(type + "_leaves"));
            return ibakedmodel;
        }
        return null;
    }

    private static IBakedModel getModelDoubleFace(IBakedModel model) {
        if (model == null) {
            return null;
        }
        if (model.getGeneralQuads().size() > 0) {
            Config.warn((String)("SmartLeaves: Model is not cube, general quads: " + model.getGeneralQuads().size() + ", model: " + model));
            return model;
        }
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        for (int i = 0; i < aenumfacing.length; ++i) {
            EnumFacing enumfacing = aenumfacing[i];
            List list = model.getFaceQuads(enumfacing);
            if (list.size() == 1) continue;
            Config.warn((String)("SmartLeaves: Model is not cube, side: " + enumfacing + ", quads: " + list.size() + ", model: " + model));
            return model;
        }
        IBakedModel ibakedmodel = ModelUtils.duplicateModel((IBakedModel)model);
        List[] alist = new List[aenumfacing.length];
        for (int k = 0; k < aenumfacing.length; ++k) {
            EnumFacing enumfacing1 = aenumfacing[k];
            List list1 = ibakedmodel.getFaceQuads(enumfacing1);
            BakedQuad bakedquad = (BakedQuad)list1.get(0);
            BakedQuad bakedquad1 = new BakedQuad((int[])bakedquad.getVertexData().clone(), bakedquad.getTintIndex(), bakedquad.getFace(), bakedquad.getSprite());
            int[] aint = bakedquad1.getVertexData();
            int[] aint1 = (int[])aint.clone();
            int j = aint.length / 4;
            System.arraycopy((Object)aint, (int)(0 * j), (Object)aint1, (int)(3 * j), (int)j);
            System.arraycopy((Object)aint, (int)(1 * j), (Object)aint1, (int)(2 * j), (int)j);
            System.arraycopy((Object)aint, (int)(2 * j), (Object)aint1, (int)(1 * j), (int)j);
            System.arraycopy((Object)aint, (int)(3 * j), (Object)aint1, (int)(0 * j), (int)j);
            System.arraycopy((Object)aint1, (int)0, (Object)aint, (int)0, (int)aint1.length);
            list1.add((Object)bakedquad1);
        }
        return ibakedmodel;
    }
}
