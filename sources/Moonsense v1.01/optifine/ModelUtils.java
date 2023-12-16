// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.resources.model.SimpleBakedModel;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.resources.model.IBakedModel;

public class ModelUtils
{
    public static void dbgModel(final IBakedModel model) {
        if (model != null) {
            Config.dbg("Model: " + model + ", ao: " + model.isGui3d() + ", gui3d: " + model.isAmbientOcclusionEnabled() + ", builtIn: " + model.isBuiltInRenderer() + ", particle: " + model.getTexture());
            final EnumFacing[] faces = EnumFacing.VALUES;
            for (int generalQuads = 0; generalQuads < faces.length; ++generalQuads) {
                final EnumFacing face = faces[generalQuads];
                final List faceQuads = model.func_177551_a(face);
                dbgQuads(face.getName(), faceQuads, "  ");
            }
            final List var5 = model.func_177550_a();
            dbgQuads("General", var5, "  ");
        }
    }
    
    private static void dbgQuads(final String name, final List quads, final String prefix) {
        for (final BakedQuad quad : quads) {
            dbgQuad(name, quad, prefix);
        }
    }
    
    public static void dbgQuad(final String name, final BakedQuad quad, final String prefix) {
        Config.dbg(String.valueOf(prefix) + "Quad: " + quad.getClass().getName() + ", type: " + name + ", face: " + quad.getFace() + ", tint: " + quad.func_178211_c() + ", sprite: " + quad.getSprite());
        dbgVertexData(quad.func_178209_a(), "  " + prefix);
    }
    
    public static void dbgVertexData(final int[] vd, final String prefix) {
        final int step = vd.length / 4;
        Config.dbg(String.valueOf(prefix) + "Length: " + vd.length + ", step: " + step);
        for (int i = 0; i < 4; ++i) {
            final int pos = i * step;
            final float x = Float.intBitsToFloat(vd[pos + 0]);
            final float y = Float.intBitsToFloat(vd[pos + 1]);
            final float z = Float.intBitsToFloat(vd[pos + 2]);
            final int col = vd[pos + 3];
            final float u = Float.intBitsToFloat(vd[pos + 4]);
            final float v = Float.intBitsToFloat(vd[pos + 5]);
            Config.dbg(String.valueOf(prefix) + i + " xyz: " + x + "," + y + "," + z + " col: " + col + " u,v: " + u + "," + v);
        }
    }
    
    public static IBakedModel duplicateModel(final IBakedModel model) {
        final List generalQuads2 = duplicateQuadList(model.func_177550_a());
        final EnumFacing[] faces = EnumFacing.VALUES;
        final ArrayList faceQuads2 = new ArrayList();
        for (int model2 = 0; model2 < faces.length; ++model2) {
            final EnumFacing face = faces[model2];
            final List quads = model.func_177551_a(face);
            final List quads2 = duplicateQuadList(quads);
            faceQuads2.add(quads2);
        }
        final SimpleBakedModel var8 = new SimpleBakedModel(generalQuads2, faceQuads2, model.isGui3d(), model.isAmbientOcclusionEnabled(), model.getTexture(), model.getItemCameraTransforms());
        return var8;
    }
    
    public static List duplicateQuadList(final List list) {
        final ArrayList list2 = new ArrayList();
        for (final BakedQuad quad : list) {
            final BakedQuad quad2 = duplicateQuad(quad);
            list2.add(quad2);
        }
        return list2;
    }
    
    public static BakedQuad duplicateQuad(final BakedQuad quad) {
        final BakedQuad quad2 = new BakedQuad(quad.func_178209_a().clone(), quad.func_178211_c(), quad.getFace(), quad.getSprite());
        return quad2;
    }
}
