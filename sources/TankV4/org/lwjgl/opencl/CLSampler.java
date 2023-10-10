package org.lwjgl.opencl;

public final class CLSampler extends CLObjectChild {
   private static final InfoUtil util = CLPlatform.getInfoUtilInstance(CLSampler.class, "CL_SAMPLER_UTIL");

   CLSampler(long var1, CLContext var3) {
      super(var1, var3);
      if (this.isValid()) {
         var3.getCLSamplerRegistry().registerObject(this);
      }

   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public long getInfoLong(int var1) {
      return util.getInfoLong(this, var1);
   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         ((CLContext)this.getParent()).getCLSamplerRegistry().unregisterObject(this);
      }

      return var1;
   }
}
