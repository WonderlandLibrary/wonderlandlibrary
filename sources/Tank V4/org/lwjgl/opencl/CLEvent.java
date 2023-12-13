package org.lwjgl.opencl;

public final class CLEvent extends CLObjectChild {
   private static final CLEvent.CLEventUtil util = (CLEvent.CLEventUtil)CLPlatform.getInfoUtilInstance(CLEvent.class, "CL_EVENT_UTIL");
   private final CLCommandQueue queue;

   CLEvent(long var1, CLContext var3) {
      this(var1, var3, (CLCommandQueue)null);
   }

   CLEvent(long var1, CLCommandQueue var3) {
      this(var1, (CLContext)var3.getParent(), var3);
   }

   CLEvent(long var1, CLContext var3, CLCommandQueue var4) {
      super(var1, var3);
      if (this.isValid()) {
         this.queue = var4;
         if (var4 == null) {
            var3.getCLEventRegistry().registerObject(this);
         } else {
            var4.getCLEventRegistry().registerObject(this);
         }
      } else {
         this.queue = null;
      }

   }

   public CLCommandQueue getCLCommandQueue() {
      return this.queue;
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public long getProfilingInfoLong(int var1) {
      return util.getProfilingInfoLong(this, var1);
   }

   CLObjectRegistry getParentRegistry() {
      return this.queue == null ? ((CLContext)this.getParent()).getCLEventRegistry() : this.queue.getCLEventRegistry();
   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         if (this.queue == null) {
            ((CLContext)this.getParent()).getCLEventRegistry().unregisterObject(this);
         } else {
            this.queue.getCLEventRegistry().unregisterObject(this);
         }
      }

      return var1;
   }

   interface CLEventUtil extends InfoUtil {
      long getProfilingInfoLong(CLEvent var1, int var2);
   }
}
