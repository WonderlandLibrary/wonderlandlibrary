// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.model;

import net.minecraft.client.resources.model.SimpleBakedModel;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.src.Config;
import net.minecraft.client.resources.model.IBakedModel;

public class ModelUtils
{
    public static void dbgModel(final IBakedModel model) {
        if (model != null) {
            Config.dbg("Model: " + model + ", ao: " + model.isAmbientOcclusion() + ", gui3d: " + model.isGui3d() + ", builtIn: " + model.isBuiltInRenderer() + ", particle: " + model.getParticleTexture());
            final EnumFacing[] aenumfacing = EnumFacing.VALUES;
            for (int i = 0; i < aenumfacing.length; ++i) {
                final EnumFacing enumfacing = aenumfacing[i];
                final List list = model.getFaceQuads(enumfacing);
                dbgQuads(enumfacing.getName(), list, "  ");
            }
            final List list2 = model.getGeneralQuads();
            dbgQuads("General", list2, "  ");
        }
    }
    
    private static void dbgQuads(final String name, final List quads, final String prefix) {
        for (final Object o : quads) {
            final BakedQuad bakedquad = (BakedQuad)o;
            dbgQuad(name, bakedquad, prefix);
        }
    }
    
    public static void dbgQuad(final String name, final BakedQuad quad, final String prefix) {
        Config.dbg(String.valueOf(prefix) + "Quad: " + quad.getClass().getName() + ", type: " + name + ", face: " + quad.getFace() + ", tint: " + quad.getTintIndex() + ", sprite: " + quad.getSprite());
        dbgVertexData(quad.getVertexData(), "  " + prefix);
    }
    
    public static void dbgVertexData(final int[] vd, final String prefix) {
        final int i = vd.length / 4;
        Config.dbg(String.valueOf(prefix) + "Length: " + vd.length + ", step: " + i);
        for (int j = 0; j < 4; ++j) {
            final int k = j * i;
            final float f = Float.intBitsToFloat(vd[k + 0]);
            final float f2 = Float.intBitsToFloat(vd[k + 1]);
            final float f3 = Float.intBitsToFloat(vd[k + 2]);
            final int l = vd[k + 3];
            final float f4 = Float.intBitsToFloat(vd[k + 4]);
            final float f5 = Float.intBitsToFloat(vd[k + 5]);
            Config.dbg(String.valueOf(prefix) + j + " xyz: " + f + "," + f2 + "," + f3 + " col: " + l + " u,v: " + f4 + "," + f5);
        }
    }
    
    public static IBakedModel duplicateModel(final IBakedModel model) {
        final List list = duplicateQuadList(model.getGeneralQuads());
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final List list2 = new ArrayList();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list3 = model.getFaceQuads(enumfacing);
            final List list4 = duplicateQuadList(list3);
            list2.add(list4);
        }
        final SimpleBakedModel simplebakedmodel = new SimpleBakedModel(list, list2, model.isAmbientOcclusion(), model.isGui3d(), model.getParticleTexture(), model.getItemCameraTransforms());
        return simplebakedmodel;
    }
    
    public static List duplicateQuadList(final List lists) {
        final List list = new ArrayList();
        for (final Object o : lists) {
            final BakedQuad bakedquad = (BakedQuad)o;
            final BakedQuad bakedquad2 = duplicateQuad(bakedquad);
            list.add(bakedquad2);
        }
        return list;
    }
    
    public static BakedQuad duplicateQuad(final BakedQuad quad) {
        final BakedQuad bakedquad = new BakedQuad(quad.getVertexData().clone(), quad.getTintIndex(), quad.getFace(), quad.getSprite());
        return bakedquad;
    }
}
