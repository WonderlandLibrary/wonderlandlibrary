package org.lwjgl.opencl.api;

public final class CLImageFormat {
  public static final int STRUCT_SIZE = 8;
  
  private final int channelOrder;
  
  private final int channelType;
  
  public CLImageFormat(int channelOrder, int channelType) {
    this.channelOrder = channelOrder;
    this.channelType = channelType;
  }
  
  public int getChannelOrder() {
    return this.channelOrder;
  }
  
  public int getChannelType() {
    return this.channelType;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\api\CLImageFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */