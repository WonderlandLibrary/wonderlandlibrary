package com.sun.prism;

import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.shape.ShapeRep;

public interface ResourceFactory extends GraphicsResource {
   boolean isDeviceReady();

   TextureResourcePool getTextureResourcePool();

   Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3);

   Texture createTexture(Image var1, Texture.Usage var2, Texture.WrapMode var3, boolean var4);

   Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5);

   Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5, boolean var6);

   Texture createTexture(MediaFrame var1);

   Texture getCachedTexture(Image var1, Texture.WrapMode var2);

   Texture getCachedTexture(Image var1, Texture.WrapMode var2, boolean var3);

   boolean isFormatSupported(PixelFormat var1);

   boolean isWrapModeSupported(Texture.WrapMode var1);

   int getMaximumTextureSize();

   int getRTTWidth(int var1, Texture.WrapMode var2);

   int getRTTHeight(int var1, Texture.WrapMode var2);

   Texture createMaskTexture(int var1, int var2, Texture.WrapMode var3);

   Texture createFloatTexture(int var1, int var2);

   RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3);

   RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3, boolean var4);

   boolean isCompatibleTexture(Texture var1);

   Presentable createPresentable(PresentableState var1);

   ShapeRep createPathRep();

   ShapeRep createRoundRectRep();

   ShapeRep createEllipseRep();

   ShapeRep createArcRep();

   void addFactoryListener(ResourceFactoryListener var1);

   void removeFactoryListener(ResourceFactoryListener var1);

   void setRegionTexture(Texture var1);

   Texture getRegionTexture();

   void setGlyphTexture(Texture var1);

   Texture getGlyphTexture();

   boolean isSuperShaderAllowed();

   void dispose();

   PhongMaterial createPhongMaterial();

   MeshView createMeshView(Mesh var1);

   Mesh createMesh();
}
