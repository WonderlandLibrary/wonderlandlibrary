package org.lwjgl.opencl;

import org.lwjgl.LWJGLUtil;

abstract class CLObjectChild extends CLObjectRetainable {
   private final CLObject parent;

   protected CLObjectChild(long var1, CLObject var3) {
      super(var1);
      if (LWJGLUtil.DEBUG && var3 != null && !var3.isValid()) {
         throw new IllegalStateException("The parent specified is not a valid CL object.");
      } else {
         this.parent = var3;
      }
   }

   public CLObject getParent() {
      return this.parent;
   }
}
