package com.sun.scenario.effect.impl.prism;

import com.sun.glass.ui.Screen;
import com.sun.scenario.effect.FilterContext;
import java.util.Map;
import java.util.WeakHashMap;

public class PrFilterContext extends FilterContext {
   private static Screen defaultScreen;
   private static final Map ctxMap = new WeakHashMap();
   private static PrFilterContext printerFilterContext = null;
   private PrFilterContext swinstance;
   private boolean forceSW;

   public static PrFilterContext getPrinterContext(Object var0) {
      if (printerFilterContext == null) {
         printerFilterContext = new PrFilterContext(var0);
      }

      return printerFilterContext;
   }

   private PrFilterContext(Object var1) {
      super(var1);
   }

   public static PrFilterContext getInstance(Screen var0) {
      if (var0 == null) {
         throw new IllegalArgumentException("Screen must be non-null");
      } else {
         PrFilterContext var1 = (PrFilterContext)ctxMap.get(var0);
         if (var1 == null) {
            var1 = new PrFilterContext(var0);
            ctxMap.put(var0, var1);
         }

         return var1;
      }
   }

   public static PrFilterContext getDefaultInstance() {
      if (defaultScreen == null) {
         defaultScreen = Screen.getMainScreen();
      }

      return getInstance(defaultScreen);
   }

   public PrFilterContext getSoftwareInstance() {
      if (this.swinstance == null) {
         if (this.forceSW) {
            this.swinstance = this;
         } else {
            this.swinstance = new PrFilterContext(this.getReferent());
            this.swinstance.forceSW = true;
         }
      }

      return this.swinstance;
   }

   public boolean isForceSoftware() {
      return this.forceSW;
   }

   private static int hashCode(boolean var0) {
      return var0 ? 1231 : 1237;
   }

   public int hashCode() {
      return this.getReferent().hashCode() ^ hashCode(this.forceSW);
   }

   public boolean equals(Object var1) {
      if (!(var1 instanceof PrFilterContext)) {
         return false;
      } else {
         PrFilterContext var2 = (PrFilterContext)var1;
         return this.getReferent().equals(var2.getReferent()) && this.forceSW == var2.forceSW;
      }
   }
}
