package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLEventCallback extends PointerWrapperAbstract {
   private CLObjectRegistry eventRegistry;

   protected CLEventCallback() {
      super(CallbackUtil.getEventCallback());
   }

   void setRegistry(CLObjectRegistry var1) {
      this.eventRegistry = var1;
   }

   private void handleMessage(long var1, int var3) {
      this.handleMessage((CLEvent)this.eventRegistry.getObject(var1), var3);
   }

   protected abstract void handleMessage(CLEvent var1, int var2);
}
