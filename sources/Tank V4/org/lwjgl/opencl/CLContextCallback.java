package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerWrapperAbstract;

public abstract class CLContextCallback extends PointerWrapperAbstract {
   private final boolean custom;

   protected CLContextCallback() {
      super(CallbackUtil.getContextCallback());
      this.custom = false;
   }

   protected CLContextCallback(long var1) {
      super(var1);
      if (var1 == 0L) {
         throw new RuntimeException("Invalid callback function pointer specified.");
      } else {
         this.custom = true;
      }
   }

   final boolean isCustom() {
      return this.custom;
   }

   protected abstract void handleMessage(String var1, ByteBuffer var2);
}
