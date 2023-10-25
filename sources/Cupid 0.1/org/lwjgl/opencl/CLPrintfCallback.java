package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLPrintfCallback extends PointerWrapperAbstract {
  protected CLPrintfCallback() {
    super(CallbackUtil.getPrintfCallback());
  }
  
  protected abstract void handleMessage(String paramString);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLPrintfCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */