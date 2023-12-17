// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.model;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BakedQuadRetextured;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ModelRotation;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import org.lwjgl.util.vector.Vector3f;
import java.util.Collection;
import net.minecraft.block.state.IBlockState;
import java.util.Map;
import net.minecraft.client.renderer.block.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import java.util.HashMap;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.src.Config;
import net.minecraft.client.renderer.block.model.IBakedModel;

public class BlockModelUtils
{
    private static final float VERTEX_COORD_ACCURACY = 1.0E-6f;
    
    public static IBakedModel makeModelCube(final String spriteName, final int tintIndex) {
        final TextureAtlasSprite textureatlassprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(spriteName);
        return makeModelCube(textureatlassprite, tintIndex);
    }
    
    public static IBakedModel makeModelCube(final TextureAtlasSprite sprite, final int tintIndex) {
        final List list = new ArrayList();
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list2 = new ArrayList();
            list2.add(makeBakedQuad(enumfacing, sprite, tintIndex));
            map.put(enumfacing, list2);
        }
        final ItemOverrideList itemoverridelist = new ItemOverrideList(new ArrayList<ItemOverride>());
        final IBakedModel ibakedmodel = new SimpleBakedModel(list, map, true, true, sprite, ItemCameraTransforms.DEFAULT, itemoverridelist);
        return ibakedmodel;
    }
    
    public static IBakedModel joinModelsCube(final IBakedModel modelBase, final IBakedModel modelAdd) {
        final List<BakedQuad> list = new ArrayList<BakedQuad>();
        list.addAll(modelBase.getQuads(null, null, 0L));
        list.addAll(modelAdd.getQuads(null, null, 0L));
        final EnumFacing[] aenumfacing = EnumFacing.VALUES;
        final Map<EnumFacing, List<BakedQuad>> map = new HashMap<EnumFacing, List<BakedQuad>>();
        for (int i = 0; i < aenumfacing.length; ++i) {
            final EnumFacing enumfacing = aenumfacing[i];
            final List list2 = new ArrayList();
            list2.addAll(modelBase.getQuads(null, enumfacing, 0L));
            list2.addAll(modelAdd.getQuads(null, enumfacing, 0L));
            map.put(enumfacing, list2);
        }
        final boolean flag = modelBase.isAmbientOcclusion();
        final boolean flag2 = modelBase.isBuiltInRenderer();
        final TextureAtlasSprite textureatlassprite = modelBase.getParticleTexture();
        final ItemCameraTransforms itemcameratransforms = modelBase.getItemCameraTransforms();
        final ItemOverrideList itemoverridelist = modelBase.getOverrides();
        final IBakedModel ibakedmodel = new SimpleBakedModel(list, map, flag, flag2, textureatlassprite, itemcameratransforms, itemoverridelist);
        return ibakedmodel;
    }
    
    public static BakedQuad makeBakedQuad(final EnumFacing facing, final TextureAtlasSprite sprite, final int tintIndex) {
        final Vector3f vector3f = new Vector3f(0.0f, 0.0f, 0.0f);
        final Vector3f vector3f2 = new Vector3f(16.0f, 16.0f, 16.0f);
        final BlockFaceUV blockfaceuv = new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0);
        final BlockPartFace blockpartface = new BlockPartFace(facing, tintIndex, "#" + facing.getName(), blockfaceuv);
        final ModelRotation modelrotation = ModelRotation.X0_Y0;
        final BlockPartRotation blockpartrotation = null;
        final boolean flag = false;
        final boolean flag2 = true;
        final FaceBakery facebakery = new FaceBakery();
        final BakedQuad bakedquad = facebakery.makeBakedQuad(vector3f, vector3f2, blockpartface, sprite, facing, modelrotation, blockpartrotation, flag, flag2);
        return bakedquad;
    }
    
    public static IBakedModel makeModel(final String modelName, final String spriteOldName, final String spriteNewName) {
        final TextureMap texturemap = Config.getMinecraft().getTextureMapBlocks();
        final TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe(spriteOldName);
        final TextureAtlasSprite textureatlassprite2 = texturemap.getSpriteSafe(spriteNewName);
        return makeModel(modelName, textureatlassprite, textureatlassprite2);
    }
    
    public static IBakedModel makeModel(final String modelName, final TextureAtlasSprite spriteOld, final TextureAtlasSprite spriteNew) {
        if (spriteOld == null || spriteNew == null) {
            return null;
        }
        final ModelManager modelmanager = Config.getModelManager();
        if (modelmanager == null) {
            return null;
        }
        final ModelResourceLocation modelresourcelocation = new ModelResourceLocation(modelName, "normal");
        final IBakedModel ibakedmodel = modelmanager.getModel(modelresourcelocation);
        if (ibakedmodel != null && ibakedmodel != modelmanager.getMissingModel()) {
            final IBakedModel ibakedmodel2 = ModelUtils.duplicateModel(ibakedmodel);
            final EnumFacing[] aenumfacing = EnumFacing.VALUES;
            for (int i = 0; i < aenumfacing.length; ++i) {
                final EnumFacing enumfacing = aenumfacing[i];
                final List<BakedQuad> list = ibakedmodel2.getQuads(null, enumfacing, 0L);
                replaceTexture(list, spriteOld, spriteNew);
            }
            final List<BakedQuad> list2 = ibakedmodel2.getQuads(null, null, 0L);
            replaceTexture(list2, spriteOld, spriteNew);
            return ibakedmodel2;
        }
        return null;
    }
    
    private static void replaceTexture(final List<BakedQuad> quads, final TextureAtlasSprite spriteOld, final TextureAtlasSprite spriteNew) {
        final List<BakedQuad> list = new ArrayList<BakedQuad>();
        for (BakedQuad bakedquad : quads) {
            if (bakedquad.getSprite() == spriteOld) {
                bakedquad = new BakedQuadRetextured(bakedquad, spriteNew);
            }
            list.add(bakedquad);
        }
        quads.clear();
        quads.addAll(list);
    }
    
    public static void snapVertexPosition(final Vector3f pos) {
        pos.setX(snapVertexCoord(pos.getX()));
        pos.setY(snapVertexCoord(pos.getY()));
        pos.setZ(snapVertexCoord(pos.getZ()));
    }
    
    private static float snapVertexCoord(final float x) {
        if (x > -1.0E-6f && x < 1.0E-6f) {
            return 0.0f;
        }
        return (x > 0.999999f && x < 1.000001f) ? 1.0f : x;
    }
    
    public static AxisAlignedBB getOffsetBoundingBox(final AxisAlignedBB aabb, final Block.EnumOffsetType offsetType, final BlockPos pos) {
        final int i = pos.getX();
        final int j = pos.getZ();
        long k = (long)(i * 3129871) ^ j * 116129781L;
        k = k * k * 42317861L + k * 11L;
        final double d0 = ((k >> 16 & 0xFL) / 15.0f - 0.5) * 0.5;
        final double d2 = ((k >> 24 & 0xFL) / 15.0f - 0.5) * 0.5;
        double d3 = 0.0;
        if (offsetType == Block.EnumOffsetType.XYZ) {
            d3 = ((k >> 20 & 0xFL) / 15.0f - 1.0) * 0.2;
        }
        return aabb.offset(d0, d3, d2);
    }
}
