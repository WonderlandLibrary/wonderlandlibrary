package com.sun.prism.impl;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Pixels;
import com.sun.prism.PixelSource;
import java.lang.ref.WeakReference;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class QueuedPixelSource implements PixelSource {
   private volatile Pixels beingConsumed;
   private volatile Pixels enqueued;
   private final List saved = new ArrayList(3);
   private final boolean useDirectBuffers;

   public QueuedPixelSource(boolean var1) {
      this.useDirectBuffers = var1;
   }

   public synchronized Pixels getLatestPixels() {
      if (this.beingConsumed != null) {
         throw new IllegalStateException("already consuming pixels: " + this.beingConsumed);
      } else {
         if (this.enqueued != null) {
            this.beingConsumed = this.enqueued;
            this.enqueued = null;
         }

         return this.beingConsumed;
      }
   }

   public synchronized void doneWithPixels(Pixels var1) {
      if (this.beingConsumed != var1) {
         throw new IllegalStateException("wrong pixels buffer: " + var1 + " != " + this.beingConsumed);
      } else {
         this.beingConsumed = null;
      }
   }

   public synchronized void skipLatestPixels() {
      if (this.beingConsumed != null) {
         throw new IllegalStateException("cannot skip while processing: " + this.beingConsumed);
      } else {
         this.enqueued = null;
      }
   }

   private boolean usesSameBuffer(Pixels var1, Pixels var2) {
      if (var1 == var2) {
         return true;
      } else if (var1 != null && var2 != null) {
         return var1.getPixels() == var2.getPixels();
      } else {
         return false;
      }
   }

   public synchronized Pixels getUnusedPixels(int var1, int var2, float var3, float var4) {
      int var5 = 0;
      IntBuffer var6 = null;

      while(var5 < this.saved.size()) {
         WeakReference var7 = (WeakReference)this.saved.get(var5);
         Pixels var8 = (Pixels)var7.get();
         if (var8 == null) {
            this.saved.remove(var5);
         } else if (!this.usesSameBuffer(var8, this.beingConsumed) && !this.usesSameBuffer(var8, this.enqueued)) {
            if (var8.getWidthUnsafe() == var1 && var8.getHeightUnsafe() == var2 && var8.getScaleXUnsafe() == var3 && var8.getScaleYUnsafe() == var4) {
               return var8;
            }

            this.saved.remove(var5);
            var6 = (IntBuffer)var8.getPixels();
            if (var6.capacity() >= var1 * var2) {
               break;
            }

            var6 = null;
         } else {
            ++var5;
         }
      }

      if (var6 == null) {
         int var9 = var1 * var2;
         if (this.useDirectBuffers) {
            var6 = BufferUtil.newIntBuffer(var9);
         } else {
            var6 = IntBuffer.allocate(var9);
         }
      }

      Pixels var10 = Application.GetApplication().createPixels(var1, var2, var6, var3, var4);
      this.saved.add(new WeakReference(var10));
      return var10;
   }

   public synchronized void enqueuePixels(Pixels var1) {
      this.enqueued = var1;
   }
}
