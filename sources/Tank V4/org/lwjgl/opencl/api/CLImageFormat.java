package org.lwjgl.opencl.api;

public final class CLImageFormat {
   public static final int STRUCT_SIZE = 8;
   private final int channelOrder;
   private final int channelType;

   public CLImageFormat(int var1, int var2) {
      this.channelOrder = var1;
      this.channelType = var2;
   }

   public int getChannelOrder() {
      return this.channelOrder;
   }

   public int getChannelType() {
      return this.channelType;
   }
}
