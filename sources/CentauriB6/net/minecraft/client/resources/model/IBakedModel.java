package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
   boolean isGui3d();

   List getGeneralQuads();

   List getFaceQuads(EnumFacing var1);

   boolean isBuiltInRenderer();

   ItemCameraTransforms getItemCameraTransforms();

   TextureAtlasSprite getParticleTexture();

   boolean isAmbientOcclusion();
}
