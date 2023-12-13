package org.lwjgl.openal;

import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public final class ALCcontext {
   final long context;
   private boolean valid;

   ALCcontext(long var1) {
      this.context = var1;
      this.valid = true;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof ALCcontext) {
         return ((ALCcontext)var1).context == this.context;
      } else {
         return super.equals(var1);
      }
   }

   static IntBuffer createAttributeList(int var0, int var1, int var2) {
      IntBuffer var3 = BufferUtils.createIntBuffer(7);
      var3.put(4103);
      var3.put(var0);
      var3.put(4104);
      var3.put(var1);
      var3.put(4105);
      var3.put(var2);
      var3.put(0);
      return var3;
   }

   void setInvalid() {
      this.valid = false;
   }

   public boolean isValid() {
      return this.valid;
   }
}
