package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontResource;
import com.sun.javafx.font.FontStrikeDesc;
import java.lang.ref.WeakReference;

class CTStrikeDisposer implements DisposerRecord {
   private FontResource fontResource;
   private FontStrikeDesc desc;
   private long fontRef = 0L;
   private boolean disposed = false;

   public CTStrikeDisposer(FontResource var1, FontStrikeDesc var2, long var3) {
      this.fontResource = var1;
      this.desc = var2;
      this.fontRef = var3;
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

         if (this.fontRef != 0L) {
            OS.CFRelease(this.fontRef);
            this.fontRef = 0L;
         }

         this.disposed = true;
      }

   }
}
