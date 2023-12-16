// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.util.EnumFacing;
import java.util.Iterator;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.resources.model.IBakedModel;

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
    
    public static IBakedModel getLeavesModel(final IBakedModel model) {
        if (!Config.isTreesSmart()) {
            return model;
        }
        final List generalQuads = model.func_177550_a();
        return (generalQuads == SmartLeaves.generalQuadsCullAcacia) ? SmartLeaves.modelLeavesDoubleAcacia : ((generalQuads == SmartLeaves.generalQuadsCullBirch) ? SmartLeaves.modelLeavesDoubleBirch : ((generalQuads == SmartLeaves.generalQuadsCullDarkOak) ? SmartLeaves.modelLeavesDoubleDarkOak : ((generalQuads == SmartLeaves.generalQuadsCullJungle) ? SmartLeaves.modelLeavesDoubleJungle : ((generalQuads == SmartLeaves.generalQuadsCullOak) ? SmartLeaves.modelLeavesDoubleOak : ((generalQuads == SmartLeaves.generalQuadsCullSpruce) ? SmartLeaves.modelLeavesDoubleSpruce : model)))));
    }
    
    public static void updateLeavesModels() {
        final ArrayList updatedTypes = new ArrayList();
        SmartLeaves.modelLeavesCullAcacia = getModelCull("acacia", updatedTypes);
        SmartLeaves.modelLeavesCullBirch = getModelCull("birch", updatedTypes);
        SmartLeaves.modelLeavesCullDarkOak = getModelCull("dark_oak", updatedTypes);
        SmartLeaves.modelLeavesCullJungle = getModelCull("jungle", updatedTypes);
        SmartLeaves.modelLeavesCullOak = getModelCull("oak", updatedTypes);
        SmartLeaves.modelLeavesCullSpruce = getModelCull("spruce", updatedTypes);
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
        if (updatedTypes.size() > 0) {
            Config.dbg("Enable face culling: " + Config.arrayToString(updatedTypes.toArray()));
        }
    }
    
    private static List getGeneralQuadsSafe(final IBakedModel model) {
        return (model == null) ? null : model.func_177550_a();
    }
    
    static IBakedModel getModelCull(final String type, final List updatedTypes) {
        final ModelManager modelManager = Config.getModelManager();
        if (modelManager == null) {
            return null;
        }
        final ResourceLocation locState = new ResourceLocation("blockstates/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack(locState) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ResourceLocation locModel = new ResourceLocation("models/block/" + type + "_leaves.json");
        if (Config.getDefiningResourcePack(locModel) != Config.getDefaultResourcePack()) {
            return null;
        }
        final ModelResourceLocation mrl = new ModelResourceLocation(String.valueOf(type) + "_leaves", "normal");
        final IBakedModel model = modelManager.getModel(mrl);
        if (model == null || model == modelManager.getMissingModel()) {
            return null;
        }
        final List listGeneral = model.func_177550_a();
        if (listGeneral.size() == 0) {
            return model;
        }
        if (listGeneral.size() != 6) {
            return null;
        }
        for (final BakedQuad quad : listGeneral) {
            final List listFace = model.func_177551_a(quad.getFace());
            if (listFace.size() > 0) {
                return null;
            }
            listFace.add(quad);
        }
        listGeneral.clear();
        updatedTypes.add(String.valueOf(type) + "_leaves");
        return model;
    }
    
    private static IBakedModel getModelDoubleFace(final IBakedModel model) {
        if (model == null) {
            return null;
        }
        if (model.func_177550_a().size() > 0) {
            Config.warn("SmartLeaves: Model is not cube, general quads: " + model.func_177550_a().size() + ", model: " + model);
            return model;
        }
        final EnumFacing[] faces = EnumFacing.VALUES;
        for (int model2 = 0; model2 < faces.length; ++model2) {
            final EnumFacing faceQuads = faces[model2];
            final List i = model.func_177551_a(faceQuads);
            if (i.size() != 1) {
                Config.warn("SmartLeaves: Model is not cube, side: " + faceQuads + ", quads: " + i.size() + ", model: " + model);
                return model;
            }
        }
        final IBakedModel var12 = ModelUtils.duplicateModel(model);
        final List[] var13 = new List[faces.length];
        for (int var14 = 0; var14 < faces.length; ++var14) {
            final EnumFacing face = faces[var14];
            final List quads = var12.func_177551_a(face);
            final BakedQuad quad = quads.get(0);
            final BakedQuad quad2 = new BakedQuad(quad.func_178209_a().clone(), quad.func_178211_c(), quad.getFace(), quad.getSprite());
            final int[] vd = quad2.func_178209_a();
            final int[] vd2 = vd.clone();
            final int step = vd.length / 4;
            System.arraycopy(vd, 0 * step, vd2, 3 * step, step);
            System.arraycopy(vd, 1 * step, vd2, 2 * step, step);
            System.arraycopy(vd, 2 * step, vd2, 1 * step, step);
            System.arraycopy(vd, 3 * step, vd2, 0 * step, step);
            System.arraycopy(vd2, 0, vd, 0, vd2.length);
            quads.add(quad2);
        }
        return var12;
    }
}
