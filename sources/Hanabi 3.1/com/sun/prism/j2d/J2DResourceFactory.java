package com.sun.prism.j2d;

import com.sun.glass.ui.Screen;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;
import java.awt.Graphics2D;
import java.util.Map;
import java.util.WeakHashMap;

class J2DResourceFactory extends BaseResourceFactory {
   private static final Map clampTexCache = new WeakHashMap();
   private static final Map repeatTexCache = new WeakHashMap();
   private static final Map mipmapTexCache = new WeakHashMap();
   private Screen screen;
   private static ShapeRep theRep = new BasicShapeRep();

   J2DResourceFactory(Screen var1) {
      super(clampTexCache, repeatTexCache, mipmapTexCache);
      this.screen = var1;
   }

   J2DPrismGraphics createJ2DPrismGraphics(J2DPresentable var1, Graphics2D var2) {
      return new J2DPrismGraphics(var1, var2);
   }

   public TextureResourcePool getTextureResourcePool() {
      return J2DTexturePool.instance;
   }

   Screen getScreen() {
      return this.screen;
   }

   public ShapeRep createArcRep() {
      return theRep;
   }

   public ShapeRep createEllipseRep() {
      return theRep;
   }

   public ShapeRep createRoundRectRep() {
      return theRep;
   }

   public ShapeRep createPathRep() {
      return theRep;
   }

   public Presentable createPresentable(PresentableState var1) {
      return J2DPresentable.create(var1, this);
   }

   public int getRTTWidth(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public int getRTTHeight(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3, boolean var4) {
      return this.createRTTexture(var1, var2, var3);
   }

   public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3) {
      J2DTexturePool var4 = J2DTexturePool.instance;
      long var5 = var4.estimateRTTextureSize(var1, var2, false);
      return !var4.prepareForAllocation(var5) ? null : new J2DRTTexture(var1, var2, this);
   }

   public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5) {
      return J2DTexture.create(var1, var3, var4, var5);
   }

   public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5, boolean var6) {
      return this.createTexture(var1, var2, var3, var4, var5);
   }

   public Texture createTexture(MediaFrame var1) {
      var1.holdFrame();
      if (var1.getPixelFormat() != PixelFormat.INT_ARGB_PRE) {
         MediaFrame var3 = var1.convertToFormat(PixelFormat.INT_ARGB_PRE);
         var1.releaseFrame();
         var1 = var3;
         if (null == var3) {
            return null;
         }
      }

      J2DTexture var2 = J2DTexture.create(var1.getPixelFormat(), Texture.WrapMode.CLAMP_TO_EDGE, var1.getWidth(), var1.getHeight());
      var1.releaseFrame();
      return var2;
   }

   public boolean isCompatibleTexture(Texture var1) {
      return var1 instanceof J2DTexture;
   }

   protected boolean canClampToZero() {
      return false;
   }

   public int getMaximumTextureSize() {
      return Integer.MAX_VALUE;
   }

   public boolean isFormatSupported(PixelFormat var1) {
      switch (var1) {
         case BYTE_RGB:
         case BYTE_GRAY:
         case INT_ARGB_PRE:
         case BYTE_BGRA_PRE:
            return true;
         case BYTE_ALPHA:
         case BYTE_APPLE_422:
         case MULTI_YCbCr_420:
         case FLOAT_XYZW:
         default:
            return false;
      }
   }

   public void dispose() {
   }

   public PhongMaterial createPhongMaterial() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public MeshView createMeshView(Mesh var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Mesh createMesh() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
