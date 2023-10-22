package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.PrismFontFactory;

class DWDisposer implements DisposerRecord {
   IUnknown resource;

   DWDisposer(IUnknown var1) {
      this.resource = var1;
   }

   public synchronized void dispose() {
      if (this.resource != null) {
         this.resource.Release();
         if (PrismFontFactory.debugFonts) {
            System.err.println("DisposerRecord=" + this.resource);
         }

         this.resource = null;
      }

   }
}
