package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

abstract class CLProgramCallback extends PointerWrapperAbstract {
   private CLContext context;

   protected CLProgramCallback() {
      super(CallbackUtil.getProgramCallback());
   }

   final void setContext(CLContext var1) {
      this.context = var1;
   }

   private void handleMessage(long var1) {
      this.handleMessage(this.context.getCLProgram(var1));
   }

   protected abstract void handleMessage(CLProgram var1);
}
