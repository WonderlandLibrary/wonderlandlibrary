package net.optifine.model;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;

public class ModelUtils {
    public static void dbgModel(IBakedModel model) {
        if (model == null) {
            return;
        }
        Config.dbg((String)("Model: " + (Object)model + ", ao: " + model.isAmbientOcclusion() + ", gui3d: " + model.isGui3d() + ", builtIn: " + model.isBuiltInRenderer() + ", particle: " + (Object)model.getParticleTexture()));
        EnumFacing[] faces = EnumFacing.VALUES;
        for (int i = 0; i < faces.length; ++i) {
            EnumFacing face = faces[i];
            List faceQuads = model.getFaceQuads(face);
            ModelUtils.dbgQuads(face.getName(), faceQuads, "  ");
        }
        List generalQuads = model.getGeneralQuads();
        ModelUtils.dbgQuads("General", generalQuads, "  ");
    }

    private static void dbgQuads(String name, List quads, String prefix) {
        for (Object quad : quads) {
            ModelUtils.dbgQuad(name, (BakedQuad) quad, prefix);
        }
    }

    public static void dbgQuad(String name, BakedQuad quad, String prefix) {
        Config.dbg((String)(prefix + "Quad: " + quad.getClass().getName() + ", type: " + name + ", face: " + (Object)quad.getFace() + ", tint: " + quad.getTintIndex() + ", sprite: " + (Object)quad.getSprite()));
        ModelUtils.dbgVertexData(quad.getVertexData(), "  " + prefix);
    }

    public static void dbgVertexData(int[] vd, String prefix) {
        int step = vd.length / 4;
        Config.dbg((String)(prefix + "Length: " + vd.length + ", step: " + step));
        for (int i = 0; i < 4; ++i) {
            int pos = i * step;
            float x = Float.intBitsToFloat(vd[pos + 0]);
            float y = Float.intBitsToFloat(vd[pos + 1]);
            float z = Float.intBitsToFloat(vd[pos + 2]);
            int col = vd[pos + 3];
            float u = Float.intBitsToFloat(vd[pos + 4]);
            float v = Float.intBitsToFloat(vd[pos + 5]);
            Config.dbg((String)(prefix + i + " xyz: " + x + "," + y + "," + z + " col: " + col + " u,v: " + u + "," + v));
        }
    }

    public static IBakedModel duplicateModel(IBakedModel model) {
        List generalQuads2 = ModelUtils.duplicateQuadList(model.getGeneralQuads());
        EnumFacing[] faces = EnumFacing.VALUES;
        List<List<BakedQuad>> faceQuads2 = new ArrayList<List<BakedQuad>>();
        for (int i = 0; i < faces.length; ++i) {
            EnumFacing face = faces[i];
            List quads = model.getFaceQuads(face);
            List quads2 = ModelUtils.duplicateQuadList(quads);
            faceQuads2.add(quads2);
        }
        SimpleBakedModel model2 = new SimpleBakedModel(generalQuads2, faceQuads2, model.isAmbientOcclusion(), model.isGui3d(), model.getParticleTexture(), model.getItemCameraTransforms());
        return model2;
    }

    public static List duplicateQuadList(List<BakedQuad> list) {
        ArrayList<BakedQuad> list2 = new ArrayList<BakedQuad>();
        for (BakedQuad quad : list) {
            BakedQuad quad2 = ModelUtils.duplicateQuad(quad);
            list2.add(quad2);
        }
        return list2;
    }

    public static BakedQuad duplicateQuad(BakedQuad quad) {
        BakedQuad quad2 = new BakedQuad((int[])quad.getVertexData().clone(), quad.getTintIndex(), quad.getFace(), quad.getSprite());
        return quad2;
    }
}

