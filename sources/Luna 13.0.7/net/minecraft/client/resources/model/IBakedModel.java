package net.minecraft.client.resources.model;

import java.util.List;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public abstract interface IBakedModel
{
  public abstract List func_177551_a(EnumFacing paramEnumFacing);
  
  public abstract List func_177550_a();
  
  public abstract boolean isGui3d();
  
  public abstract boolean isAmbientOcclusionEnabled();
  
  public abstract boolean isBuiltInRenderer();
  
  public abstract TextureAtlasSprite getTexture();
  
  public abstract ItemCameraTransforms getItemCameraTransforms();
}
