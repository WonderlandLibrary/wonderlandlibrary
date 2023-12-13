package com.sun.prism;

public interface PhongMaterial extends Material {
   int DIFFUSE = PhongMaterial.MapType.DIFFUSE.ordinal();
   int SPECULAR = PhongMaterial.MapType.SPECULAR.ordinal();
   int BUMP = PhongMaterial.MapType.BUMP.ordinal();
   int SELF_ILLUM = PhongMaterial.MapType.SELF_ILLUM.ordinal();
   int MAX_MAP_TYPE = PhongMaterial.MapType.values().length;

   void setDiffuseColor(float var1, float var2, float var3, float var4);

   void setSpecularColor(boolean var1, float var2, float var3, float var4, float var5);

   void setTextureMap(TextureMap var1);

   void lockTextureMaps();

   void unlockTextureMaps();

   public static enum MapType {
      DIFFUSE,
      SPECULAR,
      BUMP,
      SELF_ILLUM;
   }
}
