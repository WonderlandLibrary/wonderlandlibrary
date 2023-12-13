package com.sun.javafx.font;

import java.lang.ref.WeakReference;

class CompositeStrikeDisposer implements DisposerRecord {
   FontResource fontResource;
   FontStrikeDesc desc;
   boolean disposed = false;

   public CompositeStrikeDisposer(FontResource var1, FontStrikeDesc var2) {
      this.fontResource = var1;
      this.desc = var2;
   }

   public synchronized void dispose() {
      if (!this.disposed) {
         WeakReference var1 = (WeakReference)this.fontResource.getStrikeMap().get(this.desc);
         if (var1 != null) {
            Object var2 = var1.get();
            if (var2 == null) {
               this.fontResource.getStrikeMap().remove(this.desc);
            }
         }

         this.disposed = true;
      }

   }
}
