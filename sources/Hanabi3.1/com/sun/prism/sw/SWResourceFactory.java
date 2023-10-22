package com.sun.prism.sw;

import com.sun.glass.ui.Screen;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BaseResourceFactory;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.shape.BasicRoundRectRep;
import com.sun.prism.impl.shape.BasicShapeRep;
import com.sun.prism.shape.ShapeRep;
import java.util.Map;
import java.util.WeakHashMap;

final class SWResourceFactory extends BaseResourceFactory implements ResourceFactory {
   private static final Map clampTexCache = new WeakHashMap();
   private static final Map repeatTexCache = new WeakHashMap();
   private static final Map mipmapTexCache = new WeakHashMap();
   private static final ShapeRep theRep = new BasicShapeRep();
   private static final ShapeRep rectRep = new BasicRoundRectRep();
   private Screen screen;
   private final SWContext context;

   public SWResourceFactory(Screen var1) {
      super(clampTexCache, repeatTexCache, mipmapTexCache);
      this.screen = var1;
      this.context = new SWContext(this);
   }

   public TextureResourcePool getTextureResourcePool() {
      return SWTexturePool.instance;
   }

   public Screen getScreen() {
      return this.screen;
   }

   SWContext getContext() {
      return this.context;
   }

   public void dispose() {
      this.context.dispose();
   }

   public ShapeRep createArcRep() {
      return theRep;
   }

   public ShapeRep createEllipseRep() {
      return theRep;
   }

   public ShapeRep createRoundRectRep() {
      return rectRep;
   }

   public ShapeRep createPathRep() {
      return theRep;
   }

   public Presentable createPresentable(PresentableState var1) {
      if (PrismSettings.debug) {
         System.out.println("+ SWRF.createPresentable()");
      }

      return new SWPresentable(var1, this);
   }

   public int getRTTWidth(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public int getRTTHeight(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public boolean isCompatibleTexture(Texture var1) {
      return var1 instanceof SWTexture;
   }

   public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3, boolean var4) {
      return this.createRTTexture(var1, var2, var3);
   }

   public RTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3) {
      SWTexturePool var4 = SWTexturePool.instance;
      long var5 = var4.estimateRTTextureSize(var1, var2, false);
      return !var4.prepareForAllocation(var5) ? null : new SWRTTexture(this, var1, var2);
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

   protected boolean canClampToZero() {
      return false;
   }

   public Texture createTexture(MediaFrame var1) {
      return new SWArgbPreTexture(this, Texture.WrapMode.CLAMP_TO_EDGE, var1.getWidth(), var1.getHeight());
   }

   public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5) {
      SWTexturePool var6 = SWTexturePool.instance;
      long var7 = var6.estimateTextureSize(var4, var5, var1);
      return !var6.prepareForAllocation(var7) ? null : SWTexture.create(this, var1, var3, var4, var5);
   }

   public Texture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5, boolean var6) {
      return this.createTexture(var1, var2, var3, var4, var5);
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
