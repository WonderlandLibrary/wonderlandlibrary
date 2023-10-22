package com.sun.scenario.effect.impl;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.FloatMap;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.LockableResource;
import java.security.AccessController;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Renderer {
   public static final String rootPkg = "com.sun.scenario.effect";
   private static final Map rendererMap = new HashMap(1);
   private Map peerCache = Collections.synchronizedMap(new HashMap(5));
   private final ImagePool imagePool = new ImagePool();
   protected static final boolean verbose = (Boolean)AccessController.doPrivileged(() -> {
      return Boolean.getBoolean("decora.verbose");
   });

   protected Renderer() {
   }

   public abstract Effect.AccelType getAccelType();

   public abstract int getCompatibleWidth(int var1);

   public abstract int getCompatibleHeight(int var1);

   public abstract PoolFilterable createCompatibleImage(int var1, int var2);

   public PoolFilterable getCompatibleImage(int var1, int var2) {
      return this.imagePool.checkOut(this, var1, var2);
   }

   public void releaseCompatibleImage(Filterable var1) {
      if (var1 instanceof PoolFilterable) {
         ImagePool var2 = ((PoolFilterable)var1).getImagePool();
         if (var2 != null) {
            var2.checkIn((PoolFilterable)var1);
            return;
         }
      }

      var1.unlock();
   }

   public void releasePurgatory() {
      this.imagePool.releasePurgatory();
   }

   public abstract void clearImage(Filterable var1);

   public abstract ImageData createImageData(FilterContext var1, Filterable var2);

   public ImageData transform(FilterContext var1, ImageData var2, int var3, int var4) {
      if (!var2.getTransform().isIdentity()) {
         throw new InternalError("transform by powers of 2 requires untransformed source");
      } else if ((var3 | var4) == 0) {
         return var2;
      } else {
         Affine2D var5;
         Rectangle var6;
         Rectangle var7;
         double var8;
         for(var5 = new Affine2D(); var3 < -1 || var4 < -1; var2 = this.transform(var1, (ImageData)var2, var5, var6, var7)) {
            var6 = var2.getUntransformedBounds();
            var7 = new Rectangle(var6);
            var8 = 1.0;
            double var10 = 1.0;
            if (var3 < 0) {
               var8 = 0.5;
               var7.width = (var6.width + 1) / 2;
               var7.x /= 2;
               ++var3;
            }

            if (var4 < 0) {
               var10 = 0.5;
               var7.height = (var6.height + 1) / 2;
               var7.y /= 2;
               ++var4;
            }

            var5.setToScale(var8, var10);
         }

         if ((var3 | var4) != 0) {
            double var12 = var3 < 0 ? 0.5 : (double)(1 << var3);
            var8 = var4 < 0 ? 0.5 : (double)(1 << var4);
            var5.setToScale(var12, var8);
            var2 = var2.transform(var5);
         }

         return var2;
      }
   }

   public abstract Filterable transform(FilterContext var1, Filterable var2, BaseTransform var3, Rectangle var4, Rectangle var5);

   public abstract ImageData transform(FilterContext var1, ImageData var2, BaseTransform var3, Rectangle var4, Rectangle var5);

   public LockableResource createFloatTexture(int var1, int var2) {
      throw new InternalError();
   }

   public void updateFloatTexture(LockableResource var1, FloatMap var2) {
      throw new InternalError();
   }

   public final synchronized EffectPeer getPeerInstance(FilterContext var1, String var2, int var3) {
      EffectPeer var4 = (EffectPeer)this.peerCache.get(var2);
      if (var4 != null) {
         return var4;
      } else {
         if (var3 > 0) {
            var4 = (EffectPeer)this.peerCache.get(var2 + "_" + var3);
            if (var4 != null) {
               return var4;
            }
         }

         var4 = this.createPeer(var1, var2, var3);
         if (var4 == null) {
            throw new RuntimeException("Could not create peer  " + var2 + " for renderer " + this);
         } else {
            this.peerCache.put(var4.getUniqueName(), var4);
            return var4;
         }
      }
   }

   public abstract RendererState getRendererState();

   protected abstract EffectPeer createPeer(FilterContext var1, String var2, int var3);

   protected Collection getPeers() {
      return this.peerCache.values();
   }

   protected static Renderer getSoftwareRenderer() {
      return RendererFactory.getSoftwareRenderer();
   }

   protected abstract Renderer getBackupRenderer();

   protected Renderer getRendererForSize(Effect var1, int var2, int var3) {
      return this;
   }

   public static synchronized Renderer getRenderer(FilterContext var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("FilterContext must be non-null");
      } else {
         Renderer var1 = (Renderer)rendererMap.get(var0);
         if (var1 != null) {
            if (var1.getRendererState() == Renderer.RendererState.OK) {
               return var1;
            }

            if (var1.getRendererState() == Renderer.RendererState.LOST) {
               return var1.getBackupRenderer();
            }

            if (var1.getRendererState() == Renderer.RendererState.DISPOSED) {
               var1 = null;
            }
         }

         if (var1 == null) {
            Collection var2 = rendererMap.values();
            Iterator var3 = var2.iterator();

            while(var3.hasNext()) {
               Renderer var4 = (Renderer)var3.next();
               if (var4.getRendererState() == Renderer.RendererState.DISPOSED) {
                  var4.imagePool.dispose();
                  var3.remove();
               }
            }

            var1 = RendererFactory.createRenderer(var0);
            if (var1 == null) {
               throw new RuntimeException("Error creating a Renderer");
            }

            if (verbose) {
               String var6 = var1.getClass().getName();
               String var7 = var6.substring(var6.lastIndexOf(".") + 1);
               Object var5 = var0.getReferent();
               System.out.println("Created " + var7 + " (AccelType=" + var1.getAccelType() + ") for " + var5);
            }

            rendererMap.put(var0, var1);
         }

         return var1;
      }
   }

   public static Renderer getRenderer(FilterContext var0, Effect var1, int var2, int var3) {
      return getRenderer(var0).getRendererForSize(var1, var2, var3);
   }

   public abstract boolean isImageDataCompatible(ImageData var1);

   public static enum RendererState {
      OK,
      LOST,
      DISPOSED;
   }
}
