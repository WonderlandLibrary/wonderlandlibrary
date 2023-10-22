package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import com.sun.scenario.effect.LockableResource;

public class PrTexture implements LockableResource {
   private final Texture tex;
   private final Rectangle bounds;

   public PrTexture(Texture var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("Texture must be non-null");
      } else {
         this.tex = var1;
         this.bounds = new Rectangle(var1.getPhysicalWidth(), var1.getPhysicalHeight());
      }
   }

   public void lock() {
      if (this.tex != null) {
         this.tex.lock();
      }

   }

   public void unlock() {
      if (this.tex != null) {
         this.tex.unlock();
      }

   }

   public boolean isLost() {
      return this.tex.isSurfaceLost();
   }

   public Rectangle getNativeBounds() {
      return this.bounds;
   }

   public Texture getTextureObject() {
      return this.tex;
   }
}
