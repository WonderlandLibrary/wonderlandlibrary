// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.optifine;

import net.minecraft.client.renderer.block.model.BlockPartRotation;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.resources.model.ModelRotation;
import net.minecraft.client.renderer.block.model.BlockPartFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.util.EnumFacing;
import java.util.ArrayList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;

public class BlockModelUtils
{
    public static IBakedModel makeModelCube(final String spriteName, final int tintIndex) {
        final TextureAtlasSprite sprite = Config.getMinecraft().getTextureMapBlocks().getAtlasSprite(spriteName);
        return makeModelCube(sprite, tintIndex);
    }
    
    public static IBakedModel makeModelCube(final TextureAtlasSprite sprite, final int tintIndex) {
        final ArrayList generalQuads = new ArrayList();
        final EnumFacing[] facings = EnumFacing.values();
        final ArrayList faceQuads = new ArrayList(facings.length);
        for (final EnumFacing facing : facings) {
            final ArrayList quads = new ArrayList();
            quads.add(makeBakedQuad(facing, sprite, tintIndex));
            faceQuads.add(quads);
        }
        final SimpleBakedModel var8 = new SimpleBakedModel(generalQuads, faceQuads, true, true, sprite, ItemCameraTransforms.field_178357_a);
        return var8;
    }
    
    private static BakedQuad makeBakedQuad(final EnumFacing facing, final TextureAtlasSprite sprite, final int tintIndex) {
        final Vector3f posFrom = new Vector3f(0.0f, 0.0f, 0.0f);
        final Vector3f posTo = new Vector3f(16.0f, 16.0f, 16.0f);
        final BlockFaceUV uv = new BlockFaceUV(new float[] { 0.0f, 0.0f, 16.0f, 16.0f }, 0);
        final BlockPartFace face = new BlockPartFace(facing, tintIndex, "#" + facing.getName(), uv);
        final ModelRotation modelRotation = ModelRotation.X0_Y0;
        final Object partRotation = null;
        final boolean uvLocked = false;
        final boolean shade = true;
        final FaceBakery faceBakery = new FaceBakery();
        final BakedQuad quad = faceBakery.func_178414_a(posFrom, posTo, face, sprite, facing, modelRotation, (BlockPartRotation)partRotation, uvLocked, shade);
        return quad;
    }
}
