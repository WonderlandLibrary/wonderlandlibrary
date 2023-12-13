package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public interface IBakedModel {
   boolean isAmbientOcclusion();

   boolean isGui3d();

   boolean isBuiltInRenderer();

   TextureAtlasSprite getParticleTexture();

   List getFaceQuads(EnumFacing var1);

   ItemCameraTransforms getItemCameraTransforms();

   List getGeneralQuads();
}
