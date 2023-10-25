package net.optifine.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.src.Config;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.util.vector.Vector3f;

public class BlockModelUtils
{
    private static final float VERTEX_COORD_ACCURACY = 1.0E-6F;

    public static IBakedModel makeModelCube(String spriteName, int tintIndex)
    {
        TextureAtlasSprite textureatlassprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(spriteName);
        return makeModelCube(textureatlassprite, tintIndex);
    }

    public static IBakedModel makeModelCube(TextureAtlasSprite sprite, int tintIndex)
    {
        List list = new ArrayList();
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();

        for (int i = 0; i < aenumfacing.length; ++i)
        {
            EnumFacing enumfacing = aenumfacing[i];
            List list1 = new ArrayList();
            list1.add(makeBakedQuad(enumfacing, sprite, tintIndex));
            map.put(enumfacing, list1);
        }

        ItemOverrideList itemoverridelist = new ItemOverrideList(new ArrayList());
        IBakedModel ibakedmodel = new SimpleBakedModel(list, map, true, true, sprite, ItemCameraTransforms.DEFAULT, itemoverridelist);
        return ibakedmodel;
    }

    public static IBakedModel joinModelsCube(IBakedModel modelBase, IBakedModel modelAdd)
    {
        List<BakedQuad> list = new ArrayList<BakedQuad>();
        list.addAll(modelBase.getQuads((IBlockState)null, (EnumFacing)null, 0L));
        list.addAll(modelAdd.getQuads((IBlockState)null, (EnumFacing)null, 0L));
        EnumFacing[] aenumfacing = EnumFacing.VALUES;
        Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();

        for (int i = 0; i < aenumfacing.length; ++i)
        {
            EnumFacing enumfacing = aenumfacing[i];
            List list1 = new ArrayList();
            list1.addAll(modelBase.getQuads((IBlockState)null, enumfacing, 0L));
            list1.addAll(modelAdd.getQuads((IBlockState)null, enumfacing, 0L));
            map.put(enumfacing, list1);
        }

        boolean flag = modelBase.isAmbientOcclusion();
        boolean flag1 = modelBase.isBuiltInRenderer();
        TextureAtlasSprite textureatlassprite = modelBase.getParticleTexture();
        ItemCameraTransforms itemcameratransforms = modelBase.getItemCameraTransforms();
        ItemOverrideList itemoverridelist = modelBase.getOverrides();
        IBakedModel ibakedmodel = new SimpleBakedModel(list, map, flag, flag1, textureatlassprite, itemcameratransforms, itemoverridelist);
        return ibakedmodel;
    }

    public static BakedQuad makeBakedQuad(EnumFacing facing, TextureAtlasSprite sprite, int tintIndex)
    {
        Vector3f vector3f = new Vector3f(0.0F, 0.0F, 0.0F);
        Vector3f vector3f1 = new Vector3f(16.0F, 16.0F, 16.0F);
        BlockFaceUV blockfaceuv = new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0);
        BlockPartFace blockpartface = new BlockPartFace(facing, tintIndex, "#" + facing.getName(), blockfaceuv);
        ModelRotation modelrotation = ModelRotation.X0_Y0;
        BlockPartRotation blockpartrotation = null;
        boolean flag = false;
        boolean flag1 = true;
        FaceBakery facebakery = new FaceBakery();
        BakedQuad bakedquad = facebakery.makeBakedQuad(vector3f, vector3f1, blockpartface, sprite, facing, modelrotation, blockpartrotation, flag, flag1);
        return bakedquad;
    }

    public static IBakedModel makeModel(String modelName, String spriteOldName, String spriteNewName)
    {
        TextureMap texturemap = Config.getMinecraft().getTextureMapBlocks();
        TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(spriteOldName);
        TextureAtlasSprite textureatlassprite1 = texturemap.getSpriteSafe(spriteNewName);
        return makeModel(modelName, textureatlassprite, textureatlassprite1);
    }

    public static IBakedModel makeModel(String modelName, TextureAtlasSprite spriteOld, TextureAtlasSprite spriteNew)
    {
        if (spriteOld != null && spriteNew != null)
        {
            ModelManager modelmanager = Config.getModelManager();

            if (modelmanager == null)
            {
                return null;
            }
            else
            {
                ModelResourceLocation modelresourcelocation = new ModelResourceLocation(modelName, "normal");
                IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);

                if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel())
                {
                    IBakedModel ibakedmodel1 = ModelUtils.duplicateModel(ibakedmodel);
                    EnumFacing[] aenumfacing = EnumFacing.VALUES;

                    for (int i = 0; i < aenumfacing.length; ++i)
                    {
                        EnumFacing enumfacing = aenumfacing[i];
                        List<BakedQuad> list = ibakedmodel1.getQuads((IBlockState)null, enumfacing, 0L);
                        replaceTexture(list, spriteOld, spriteNew);
                    }

                    List<BakedQuad> list1 = ibakedmodel1.getQuads((IBlockState)null, (EnumFacing)null, 0L);
                    replaceTexture(list1, spriteOld, spriteNew);
                    return ibakedmodel1;
                }
                else
                {
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }

    private static void replaceTexture(List<BakedQuad> quads, TextureAtlasSprite spriteOld, TextureAtlasSprite spriteNew)
    {
        List<BakedQuad> list = new ArrayList<BakedQuad>();

        for (BakedQuad bakedquad : quads)
        {
            if (bakedquad.getSprite() == spriteOld)
            {
                bakedquad = new BakedQuadRetextured(bakedquad, spriteNew);
            }

            list.add(bakedquad);
        }

        quads.clear();
        quads.addAll(list);
    }

    public static void snapVertexPosition(Vector3f pos)
    {
        pos.setX(snapVertexCoord(pos.getX()));
        pos.setY(snapVertexCoord(pos.getY()));
        pos.setZ(snapVertexCoord(pos.getZ()));
    }

    private static float snapVertexCoord(float x)
    {
        if (x > -1.0E-6F && x < 1.0E-6F)
        {
            return 0.0F;
        }
        else
        {
            return x > 0.999999F && x < 1.000001F ? 1.0F : x;
        }
    }

    public static AxisAlignedBB getOffsetBoundingBox(AxisAlignedBB aabb, Block.EnumOffsetType offsetType, BlockPos pos)
    {
        int i = pos.getX();
        int j = pos.getZ();
        long k = (long)(i * 3129871) ^ (long)j * 116129781L;
        k = k * k * 42317861L + k * 11L;
        double d0 = ((double)((float)(k >> 16 & 15L) / 15.0F) - 0.5D) * 0.5D;
        double d1 = ((double)((float)(k >> 24 & 15L) / 15.0F) - 0.5D) * 0.5D;
        double d2 = 0.0D;

        if (offsetType == Block.EnumOffsetType.XYZ)
        {
            d2 = ((double)((float)(k >> 20 & 15L) / 15.0F) - 1.0D) * 0.2D;
        }

        return aabb.offset(d0, d2, d1);
    }
}
