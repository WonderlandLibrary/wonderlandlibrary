package com.sun.javafx.media;

import com.sun.glass.ui.Screen;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmedia.control.VideoDataBuffer;
import com.sun.media.jfxmedia.control.VideoFormat;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.MediaFrame;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class PrismMediaFrameHandler implements ResourceFactoryListener {
   private final Map textures = new WeakHashMap(1);
   private static Map handlers;
   private boolean registeredWithFactory = false;
   private final RenderJob releaseRenderJob = new RenderJob(() -> {
      this.releaseData();
   });

   public static synchronized PrismMediaFrameHandler getHandler(Object var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("provider must be non-null");
      } else {
         if (handlers == null) {
            handlers = new WeakHashMap(1);
         }

         PrismMediaFrameHandler var1 = (PrismMediaFrameHandler)handlers.get(var0);
         if (var1 == null) {
            var1 = new PrismMediaFrameHandler(var0);
            handlers.put(var0, var1);
         }

         return var1;
      }
   }

   private PrismMediaFrameHandler(Object var1) {
   }

   public Texture getTexture(Graphics var1, VideoDataBuffer var2) {
      Screen var3 = var1.getAssociatedScreen();
      TextureMapEntry var4 = (TextureMapEntry)this.textures.get(var3);
      if (null == var2) {
         if (this.textures.containsKey(var3)) {
            this.textures.remove(var3);
         }

         return null;
      } else {
         if (null == var4) {
            var4 = new TextureMapEntry();
            this.textures.put(var3, var4);
         }

         if (var4.texture != null) {
            var4.texture.lock();
            if (var4.texture.isSurfaceLost()) {
               var4.texture = null;
            }
         }

         if (null == var4.texture || var4.lastFrameTime != var2.getTimestamp()) {
            this.updateTexture(var1, var2, var4);
         }

         return var4.texture;
      }
   }

   private void updateTexture(Graphics var1, VideoDataBuffer var2, TextureMapEntry var3) {
      Screen var4 = var1.getAssociatedScreen();
      if (var3.texture != null && (var3.encodedWidth != var2.getEncodedWidth() || var3.encodedHeight != var2.getEncodedHeight())) {
         var3.texture.dispose();
         var3.texture = null;
      }

      PrismFrameBuffer var5 = new PrismFrameBuffer(var2);
      if (var3.texture == null) {
         if (!this.registeredWithFactory) {
            GraphicsPipeline.getDefaultResourceFactory().addFactoryListener(this);
            this.registeredWithFactory = true;
         }

         var3.texture = GraphicsPipeline.getPipeline().getResourceFactory(var4).createTexture(var5);
         var3.encodedWidth = var2.getEncodedWidth();
         var3.encodedHeight = var2.getEncodedHeight();
      }

      if (var3.texture != null) {
         var3.texture.update(var5, false);
      }

      var3.lastFrameTime = var2.getTimestamp();
   }

   private void releaseData() {
      Iterator var1 = this.textures.values().iterator();

      while(var1.hasNext()) {
         TextureMapEntry var2 = (TextureMapEntry)var1.next();
         if (var2 != null && var2.texture != null) {
            var2.texture.dispose();
         }
      }

      this.textures.clear();
   }

   public void releaseTextures() {
      Toolkit var1 = Toolkit.getToolkit();
      var1.addRenderJob(this.releaseRenderJob);
   }

   public void factoryReset() {
      this.releaseData();
   }

   public void factoryReleased() {
      this.releaseData();
   }

   private static class TextureMapEntry {
      public double lastFrameTime;
      public Texture texture;
      public int encodedWidth;
      public int encodedHeight;

      private TextureMapEntry() {
         this.lastFrameTime = -1.0;
      }

      // $FF: synthetic method
      TextureMapEntry(Object var1) {
         this();
      }
   }

   private class PrismFrameBuffer implements MediaFrame {
      private final PixelFormat videoFormat;
      private final VideoDataBuffer primary;

      public PrismFrameBuffer(VideoDataBuffer var2) {
         if (null == var2) {
            throw new NullPointerException();
         } else {
            this.primary = var2;
            switch (this.primary.getFormat()) {
               case BGRA_PRE:
                  this.videoFormat = PixelFormat.INT_ARGB_PRE;
                  break;
               case YCbCr_420p:
                  this.videoFormat = PixelFormat.MULTI_YCbCr_420;
                  break;
               case YCbCr_422:
                  this.videoFormat = PixelFormat.BYTE_APPLE_422;
                  break;
               case ARGB:
               default:
                  throw new IllegalArgumentException("Unsupported video format " + this.primary.getFormat());
            }

         }
      }

      public ByteBuffer getBufferForPlane(int var1) {
         return this.primary.getBufferForPlane(var1);
      }

      public void holdFrame() {
         this.primary.holdFrame();
      }

      public void releaseFrame() {
         this.primary.releaseFrame();
      }

      public PixelFormat getPixelFormat() {
         return this.videoFormat;
      }

      public int getWidth() {
         return this.primary.getWidth();
      }

      public int getHeight() {
         return this.primary.getHeight();
      }

      public int getEncodedWidth() {
         return this.primary.getEncodedWidth();
      }

      public int getEncodedHeight() {
         return this.primary.getEncodedHeight();
      }

      public int planeCount() {
         return this.primary.getPlaneCount();
      }

      public int[] planeStrides() {
         return this.primary.getPlaneStrides();
      }

      public int strideForPlane(int var1) {
         return this.primary.getStrideForPlane(var1);
      }

      public MediaFrame convertToFormat(PixelFormat var1) {
         if (var1 == this.getPixelFormat()) {
            return this;
         } else if (var1 != PixelFormat.INT_ARGB_PRE) {
            return null;
         } else {
            VideoDataBuffer var2 = this.primary.convertToFormat(VideoFormat.BGRA_PRE);
            return null == var2 ? null : PrismMediaFrameHandler.this.new PrismFrameBuffer(var2);
         }
      }
   }
}
