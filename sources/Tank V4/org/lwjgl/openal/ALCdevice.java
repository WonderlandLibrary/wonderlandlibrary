package org.lwjgl.openal;

import java.util.HashMap;
import java.util.Iterator;

public final class ALCdevice {
   final long device;
   private boolean valid;
   private final HashMap contexts = new HashMap();

   ALCdevice(long var1) {
      this.device = var1;
      this.valid = true;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof ALCdevice) {
         return ((ALCdevice)var1).device == this.device;
      } else {
         return super.equals(var1);
      }
   }

   void addContext(ALCcontext var1) {
      HashMap var2;
      synchronized(var2 = this.contexts){}
      this.contexts.put(var1.context, var1);
   }

   void removeContext(ALCcontext var1) {
      HashMap var2;
      synchronized(var2 = this.contexts){}
      this.contexts.remove(var1.context);
   }

   void setInvalid() {
      this.valid = false;
      HashMap var1;
      synchronized(var1 = this.contexts){}
      Iterator var2 = this.contexts.values().iterator();

      while(var2.hasNext()) {
         ALCcontext var3 = (ALCcontext)var2.next();
         var3.setInvalid();
      }

      this.contexts.clear();
   }

   public boolean isValid() {
      return this.valid;
   }
}
