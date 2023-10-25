package net.optifine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;

public class ModelUtils
{
    public static void dbgModel(IBakedModel model)
    {
        if (model != null)
        {
            Config.dbg("Model: " + model + ", ao: " + model.isAmbientOcclusion() + ", gui3d: " + model.isGui3d() + ", builtIn: " + model.isBuiltInRenderer() + ", particle: " + model.getParticleTexture());
            EnumFacing[] aenumfacing = EnumFacing.VALUES;

            for (int i = 0; i < aenumfacing.length; ++i)
            {
                EnumFacing enumfacing = aenumfacing[i];
                List list = model.getQuads((IBlockState)null, enumfacing, 0L);
                dbgQuads(enumfacing.getName(), list, "  ");
            }

            List list1 = model.getQuads((IBlockState)null, (EnumFacing)null, 0L);
            dbgQuads("General", list1, "  ");
        }
    }

    private static void dbgQuads(String name, List quads, String prefix)
    {
        for (Object bakedquad : quads)
        {
            dbgQuad(name, (BakedQuad) bakedquad, prefix);
        }
    }

    public static void dbgQuad(String name, BakedQuad quad, String prefix)
    {
        Config.dbg(prefix + "Quad: " + quad.getClass().getName() + ", type: " + name + ", face: " + quad.getFace() + ", tint: " + quad.getTintIndex() + ", sprite: " + quad.getSprite());
        dbgVertexData(quad.getVertexData(), "  " + prefix);
    }

    public static void dbgVertexData(int[] vd, String prefix)
    {
        int i = vd.length / 4;
        Config.dbg(prefix + "Length: " + vd.length + ", step: " + i);

        for (int j = 0; j < 4; ++j)
        {
            int k = j * i;
            float f = Float.intBitsToFloat(vd[k + 0]);
            float f1 = Float.intBitsToFloat(vd[k + 1]);
            float f2 = Float.intBitsToFloat(vd[k + 2]);
            int l = vd[k + 3];
            float f3 = Float.intBitsToFloat(vd[k + 4]);
            float f4 = Float.intBitsToFloat(vd[k + 5]);
            Config.dbg(prefix + j + " xyz: " + f + "," + f1 + "," + f2 + " col: " + l + " u,v: " + f3 + "," + f4);
        }
    }

    public static IBakedModel duplicateModel(IBakedModel model)
    {
        List list = duplicateQuadList(model.getQuads((IBlockState)null, (EnumFacing)null, 0L));
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();

        for (int i = 0; i < aenumfacing.length; ++i)
        {
            EnumFacing enumfacing = aenumfacing[i];
            List list1 = model.getQuads((IBlockState)null, enumfacing, 0L);
            List list2 = duplicateQuadList(list1);
            map.put(enumfacing, list2);
        }

        SimpleBakedModel simplebakedmodel = new SimpleBakedModel(list, map, model.isAmbientOcclusion(), model.isGui3d(), model.getParticleTexture(), model.getItemCameraTransforms(), model.getOverrides());
        return simplebakedmodel;
    }

    public static List duplicateQuadList(List list)
    {
        List<BakedQuad> list2 = new ArrayList<>();

        for (Object bakedquad : list)
        {
            BakedQuad bakedquad1 = duplicateQuad((BakedQuad) bakedquad);
            list2.add(bakedquad1);
        }

        return list2;
    }

    public static BakedQuad duplicateQuad(BakedQuad quad)
    {
        BakedQuad bakedquad = new BakedQuad((int[])quad.getVertexData().clone(), quad.getTintIndex(), quad.getFace(), quad.getSprite());
        return bakedquad;
    }
}
