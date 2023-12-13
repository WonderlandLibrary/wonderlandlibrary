package com.sun.prism.d3d;

import com.sun.glass.ui.Screen;
import com.sun.prism.MediaFrame;
import com.sun.prism.Mesh;
import com.sun.prism.MeshView;
import com.sun.prism.MultiTexture;
import com.sun.prism.PhongMaterial;
import com.sun.prism.PixelFormat;
import com.sun.prism.Presentable;
import com.sun.prism.PresentableState;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.impl.TextureResourcePool;
import com.sun.prism.impl.ps.BaseShaderFactory;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.WeakHashMap;

class D3DResourceFactory extends BaseShaderFactory {
   private static final Map clampTexCache = new WeakHashMap();
   private static final Map repeatTexCache = new WeakHashMap();
   private static final Map mipmapTexCache = new WeakHashMap();
   private final D3DContext context;
   private final int maxTextureSize;
   private final LinkedList records = new LinkedList();
   static final int STATS_FREQUENCY;
   private int nFrame = -1;
   private D3DFrameStats frameStats;

   D3DResourceFactory(long var1, Screen var3) {
      super(clampTexCache, repeatTexCache, mipmapTexCache);
      this.context = new D3DContext(var1, var3, this);
      this.context.initState();
      this.maxTextureSize = this.computeMaxTextureSize();
      if (PrismSettings.noClampToZero && PrismSettings.verbose) {
         System.out.println("prism.noclamptozero not supported by D3D");
      }

   }

   D3DContext getContext() {
      return this.context;
   }

   public TextureResourcePool getTextureResourcePool() {
      return D3DVramPool.instance;
   }

   private void displayPrismStatistics() {
      if (STATS_FREQUENCY > 0 && ++this.nFrame == STATS_FREQUENCY) {
         this.nFrame = 0;
         this.frameStats = this.context.getFrameStats(true, this.frameStats);
         if (this.frameStats != null) {
            System.err.println(this.frameStats.toDebugString(STATS_FREQUENCY));
         }
      }

   }

   public boolean isDeviceReady() {
      this.displayPrismStatistics();
      return this.context.testLostStateAndReset();
   }

   static int nextPowerOfTwo(int var0, int var1) {
      if (var0 > var1) {
         return 0;
      } else {
         int var2;
         for(var2 = 1; var2 < var0; var2 *= 2) {
         }

         return var2;
      }
   }

   public boolean isCompatibleTexture(Texture var1) {
      return var1 instanceof D3DTexture;
   }

   public D3DTexture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5) {
      return this.createTexture(var1, var2, var3, var4, var5, false);
   }

   public D3DTexture createTexture(PixelFormat var1, Texture.Usage var2, Texture.WrapMode var3, int var4, int var5, boolean var6) {
      if (!this.isFormatSupported(var1)) {
         throw new UnsupportedOperationException("Pixel format " + var1 + " not supported on this device");
      } else if (var1 == PixelFormat.MULTI_YCbCr_420) {
         throw new UnsupportedOperationException("MULTI_YCbCr_420 textures require a MediaFrame");
      } else {
         int var7;
         int var8;
         if (PrismSettings.forcePow2) {
            var7 = nextPowerOfTwo(var4, Integer.MAX_VALUE);
            var8 = nextPowerOfTwo(var5, Integer.MAX_VALUE);
         } else {
            var7 = var4;
            var8 = var5;
         }

         D3DVramPool var9 = D3DVramPool.instance;
         long var10 = var9.estimateTextureSize(var7, var8, var1);
         if (!var9.prepareForAllocation(var10)) {
            return null;
         } else {
            long var12 = nCreateTexture(this.context.getContextHandle(), var1.ordinal(), var2.ordinal(), false, var7, var8, 0, var6);
            if (var12 == 0L) {
               return null;
            } else {
               int var14 = nGetTextureWidth(var12);
               int var15 = nGetTextureHeight(var12);
               if (var3 != Texture.WrapMode.CLAMP_NOT_NEEDED && (var4 < var14 || var5 < var15)) {
                  var3 = var3.simulatedVersion();
               }

               return new D3DTexture(this.context, var1, var3, var12, var14, var15, var4, var5, var6);
            }
         }
      }
   }

   public Texture createTexture(MediaFrame var1) {
      var1.holdFrame();
      int var2 = var1.getWidth();
      int var3 = var1.getHeight();
      int var4 = var1.getEncodedWidth();
      int var5 = var1.getEncodedHeight();
      PixelFormat var6 = var1.getPixelFormat();
      if (var6 == PixelFormat.MULTI_YCbCr_420) {
         MultiTexture var16 = new MultiTexture(var6, Texture.WrapMode.CLAMP_TO_EDGE, var2, var3);

         for(int var17 = 0; var17 < var1.planeCount(); ++var17) {
            int var9 = var4;
            int var18 = var5;
            if (var17 == 2 || var17 == 1) {
               var9 = var4 / 2;
               var18 = var5 / 2;
            }

            D3DTexture var11 = this.createTexture(PixelFormat.BYTE_ALPHA, Texture.Usage.DYNAMIC, Texture.WrapMode.CLAMP_TO_EDGE, var9, var18);
            if (var11 == null) {
               var16.dispose();
               return null;
            }

            var16.setTexture(var11, var17);
         }

         var1.releaseFrame();
         return var16;
      } else {
         D3DVramPool var7 = D3DVramPool.instance;
         long var8 = var7.estimateTextureSize(var4, var5, var6);
         if (!var7.prepareForAllocation(var8)) {
            return null;
         } else {
            long var10 = nCreateTexture(this.context.getContextHandle(), var6.ordinal(), Texture.Usage.DYNAMIC.ordinal(), false, var4, var5, 0, false);
            if (0L == var10) {
               return null;
            } else {
               int var12 = nGetTextureWidth(var10);
               int var13 = nGetTextureHeight(var10);
               Texture.WrapMode var14 = var4 >= var12 && var5 >= var13 ? Texture.WrapMode.CLAMP_TO_EDGE : Texture.WrapMode.CLAMP_TO_EDGE_SIMULATED;
               D3DTexture var15 = new D3DTexture(this.context, var6, var14, var10, var12, var13, var2, var3, false);
               var1.releaseFrame();
               return var15;
            }
         }
      }
   }

   public int getRTTWidth(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public int getRTTHeight(int var1, Texture.WrapMode var2) {
      return var1;
   }

   public D3DRTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3) {
      return this.createRTTexture(var1, var2, var3, false);
   }

   public D3DRTTexture createRTTexture(int var1, int var2, Texture.WrapMode var3, boolean var4) {
      if (PrismSettings.verbose && this.context.isLost()) {
         System.err.println("RT Texture allocation while the device is lost");
      }

      int var5 = var1;
      int var6 = var2;
      byte var7 = 0;
      byte var8 = 0;
      if (PrismSettings.forcePow2) {
         var5 = nextPowerOfTwo(var1, Integer.MAX_VALUE);
         var6 = nextPowerOfTwo(var2, Integer.MAX_VALUE);
      }

      D3DVramPool var9 = D3DVramPool.instance;
      int var10;
      if (var4) {
         int var11 = D3DPipeline.getInstance().getMaxSamples();
         var10 = var11 < 2 ? 0 : (var11 < 4 ? 2 : 4);
      } else {
         var10 = 0;
      }

      long var18 = var9.estimateRTTextureSize(var1, var2, false);
      if (!var9.prepareForAllocation(var18)) {
         return null;
      } else {
         long var13 = nCreateTexture(this.context.getContextHandle(), PixelFormat.INT_ARGB_PRE.ordinal(), Texture.Usage.DEFAULT.ordinal(), true, var5, var6, var10, false);
         if (var13 == 0L) {
            return null;
         } else {
            int var15 = nGetTextureWidth(var13);
            int var16 = nGetTextureHeight(var13);
            D3DRTTexture var17 = new D3DRTTexture(this.context, var3, var13, var15, var16, var7, var8, var1, var2, var10);
            var17.createGraphics().clear();
            return var17;
         }
      }
   }

   public Presentable createPresentable(PresentableState var1) {
      if (PrismSettings.verbose && this.context.isLost()) {
         System.err.println("SwapChain allocation while the device is lost");
      }

      long var2 = nCreateSwapChain(this.context.getContextHandle(), var1.getNativeView(), PrismSettings.isVsyncEnabled);
      if (var2 != 0L) {
         int var4 = var1.getRenderWidth();
         int var5 = var1.getRenderHeight();
         D3DRTTexture var6 = this.createRTTexture(var4, var5, Texture.WrapMode.CLAMP_NOT_NEEDED, var1.isMSAA());
         if (PrismSettings.dirtyOptsEnabled) {
            var6.contentsUseful();
         }

         if (var6 != null) {
            return new D3DSwapChain(this.context, var2, var6, var1.getRenderScaleX(), var1.getRenderScaleY());
         }

         nReleaseResource(this.context.getContextHandle(), var2);
      }

      return null;
   }

   private static ByteBuffer getBuffer(InputStream var0) {
      if (var0 == null) {
         throw new RuntimeException("InputStream must be non-null");
      } else {
         try {
            int var1 = 4096;
            byte[] var2 = new byte[var1];
            BufferedInputStream var3 = new BufferedInputStream(var0, var1);
            int var4 = 0;
            boolean var5 = true;

            int var8;
            while((var8 = var3.read(var2, var4, var1 - var4)) != -1) {
               var4 += var8;
               if (var1 - var4 == 0) {
                  var1 *= 2;
                  byte[] var6 = new byte[var1];
                  System.arraycopy(var2, 0, var6, 0, var2.length);
                  var2 = var6;
               }
            }

            var3.close();
            ByteBuffer var9 = ByteBuffer.allocateDirect(var4);
            var9.put(var2, 0, var4);
            return var9;
         } catch (IOException var7) {
            throw new RuntimeException("Error loading D3D shader object", var7);
         }
      }
   }

   public Shader createShader(InputStream var1, Map var2, Map var3, int var4, boolean var5, boolean var6) {
      long var7 = D3DShader.init(this.context.getContextHandle(), getBuffer(var1), var4, var5, var6);
      return new D3DShader(this.context, var7, var3);
   }

   public Shader createStockShader(String var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Shader name must be non-null");
      } else {
         try {
            InputStream var2 = (InputStream)AccessController.doPrivileged(() -> {
               return D3DResourceFactory.class.getResourceAsStream("hlsl/" + var1 + ".obj");
            });
            Class var3 = Class.forName("com.sun.prism.shader." + var1 + "_Loader");
            Method var4 = var3.getMethod("loadShader", ShaderFactory.class, InputStream.class);
            return (Shader)var4.invoke((Object)null, this, var2);
         } catch (Throwable var5) {
            var5.printStackTrace();
            throw new InternalError("Error loading stock shader " + var1);
         }
      }
   }

   public void dispose() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public boolean isFormatSupported(PixelFormat var1) {
      return true;
   }

   private int computeMaxTextureSize() {
      int var1 = nGetMaximumTextureSize(this.context.getContextHandle());
      if (PrismSettings.verbose) {
         System.err.println("Maximum supported texture size: " + var1);
      }

      if (var1 > PrismSettings.maxTextureSize) {
         var1 = PrismSettings.maxTextureSize;
         if (PrismSettings.verbose) {
            System.err.println("Maximum texture size clamped to " + var1);
         }
      }

      return var1;
   }

   public int getMaximumTextureSize() {
      return this.maxTextureSize;
   }

   protected void notifyReset() {
      ListIterator var1 = this.records.listIterator();

      while(var1.hasNext()) {
         D3DResource.D3DRecord var2 = (D3DResource.D3DRecord)var1.next();
         if (var2.isDefaultPool()) {
            var2.markDisposed();
            var1.remove();
         }
      }

      super.notifyReset();
   }

   protected void notifyReleased() {
      ListIterator var1 = this.records.listIterator();

      while(var1.hasNext()) {
         D3DResource.D3DRecord var2 = (D3DResource.D3DRecord)var1.next();
         var2.markDisposed();
      }

      this.records.clear();
      super.notifyReleased();
   }

   void addRecord(D3DResource.D3DRecord var1) {
      this.records.add(var1);
   }

   void removeRecord(D3DResource.D3DRecord var1) {
      this.records.remove(var1);
   }

   public PhongMaterial createPhongMaterial() {
      return D3DPhongMaterial.create(this.context);
   }

   public MeshView createMeshView(Mesh var1) {
      return D3DMeshView.create(this.context, (D3DMesh)var1);
   }

   public Mesh createMesh() {
      return D3DMesh.create(this.context);
   }

   static native long nGetContext(int var0);

   static native boolean nIsDefaultPool(long var0);

   static native int nTestCooperativeLevel(long var0);

   static native int nResetDevice(long var0);

   static native long nCreateTexture(long var0, int var2, int var3, boolean var4, int var5, int var6, int var7, boolean var8);

   static native long nCreateSwapChain(long var0, long var2, boolean var4);

   static native int nReleaseResource(long var0, long var2);

   static native int nGetMaximumTextureSize(long var0);

   static native int nGetTextureWidth(long var0);

   static native int nGetTextureHeight(long var0);

   static native int nReadPixelsI(long var0, long var2, long var4, Buffer var6, int[] var7, int var8, int var9);

   static native int nReadPixelsB(long var0, long var2, long var4, Buffer var6, byte[] var7, int var8, int var9);

   static native int nUpdateTextureI(long var0, long var2, IntBuffer var4, int[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12);

   static native int nUpdateTextureF(long var0, long var2, FloatBuffer var4, float[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12);

   static native int nUpdateTextureB(long var0, long var2, ByteBuffer var4, byte[] var5, int var6, int var7, int var8, int var9, int var10, int var11, int var12, int var13);

   static native long nGetDevice(long var0);

   static native long nGetNativeTextureObject(long var0);

   static {
      STATS_FREQUENCY = PrismSettings.prismStatFrequency;
   }
}
