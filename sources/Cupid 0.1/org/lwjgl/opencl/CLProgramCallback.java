package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

abstract class CLProgramCallback extends PointerWrapperAbstract {
  private CLContext context;
  
  protected CLProgramCallback() {
    super(CallbackUtil.getProgramCallback());
  }
  
  final void setContext(CLContext context) {
    this.context = context;
  }
  
  private void handleMessage(long program_address) {
    handleMessage(this.context.getCLProgram(program_address));
  }
  
  protected abstract void handleMessage(CLProgram paramCLProgram);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\CLProgramCallback.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */