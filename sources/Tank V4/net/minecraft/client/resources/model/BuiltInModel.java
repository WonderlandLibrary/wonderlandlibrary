package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class BuiltInModel implements IBakedModel {
   private ItemCameraTransforms cameraTransforms;

   public ItemCameraTransforms getItemCameraTransforms() {
      return this.cameraTransforms;
   }

   public boolean isBuiltInRenderer() {
      return true;
   }

   public boolean isAmbientOcclusion() {
      return false;
   }

   public BuiltInModel(ItemCameraTransforms var1) {
      this.cameraTransforms = var1;
   }

   public List getGeneralQuads() {
      return null;
   }

   public TextureAtlasSprite getParticleTexture() {
      return null;
   }

   public List getFaceQuads(EnumFacing var1) {
      return null;
   }

   public boolean isGui3d() {
      return true;
   }
}
