package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLPrintfCallback extends PointerWrapperAbstract {
   protected CLPrintfCallback() {
      super(CallbackUtil.getPrintfCallback());
   }

   protected abstract void handleMessage(String var1);
}
