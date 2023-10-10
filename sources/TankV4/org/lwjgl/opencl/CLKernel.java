package org.lwjgl.opencl;

public final class CLKernel extends CLObjectChild {
   private static final CLKernel.CLKernelUtil util = (CLKernel.CLKernelUtil)CLPlatform.getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");

   CLKernel(long var1, CLProgram var3) {
      super(var1, var3);
      if (this.isValid()) {
         var3.getCLKernelRegistry().registerObject(this);
      }

   }

   public CLKernel setArg(int var1, byte var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, short var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, int var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, long var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, float var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, double var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArg(int var1, CLObject var2) {
      util.setArg(this, var1, var2);
      return this;
   }

   public CLKernel setArgSize(int var1, long var2) {
      util.setArgSize(this, var1, var2);
      return this;
   }

   public String getInfoString(int var1) {
      return util.getInfoString(this, var1);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public long getWorkGroupInfoSize(CLDevice var1, int var2) {
      return util.getWorkGroupInfoSize(this, var1, var2);
   }

   public long[] getWorkGroupInfoSizeArray(CLDevice var1, int var2) {
      return util.getWorkGroupInfoSizeArray(this, var1, var2);
   }

   public long getWorkGroupInfoLong(CLDevice var1, int var2) {
      return util.getWorkGroupInfoLong(this, var1, var2);
   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         ((CLProgram)this.getParent()).getCLKernelRegistry().unregisterObject(this);
      }

      return var1;
   }

   interface CLKernelUtil extends InfoUtil {
      void setArg(CLKernel var1, int var2, byte var3);

      void setArg(CLKernel var1, int var2, short var3);

      void setArg(CLKernel var1, int var2, int var3);

      void setArg(CLKernel var1, int var2, long var3);

      void setArg(CLKernel var1, int var2, float var3);

      void setArg(CLKernel var1, int var2, double var3);

      void setArg(CLKernel var1, int var2, CLObject var3);

      void setArgSize(CLKernel var1, int var2, long var3);

      long getWorkGroupInfoSize(CLKernel var1, CLDevice var2, int var3);

      long[] getWorkGroupInfoSizeArray(CLKernel var1, CLDevice var2, int var3);

      long getWorkGroupInfoLong(CLKernel var1, CLDevice var2, int var3);
   }
}
