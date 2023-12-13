package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;

public final class CLCommandQueue extends CLObjectChild {
   private static final InfoUtil util = CLPlatform.getInfoUtilInstance(CLCommandQueue.class, "CL_COMMAND_QUEUE_UTIL");
   private final CLDevice device;
   private final CLObjectRegistry clEvents;

   CLCommandQueue(long var1, CLContext var3, CLDevice var4) {
      super(var1, var3);
      if (this.isValid()) {
         this.device = var4;
         this.clEvents = new CLObjectRegistry();
         var3.getCLCommandQueueRegistry().registerObject(this);
      } else {
         this.device = null;
         this.clEvents = null;
      }

   }

   public CLDevice getCLDevice() {
      return this.device;
   }

   public CLEvent getCLEvent(long var1) {
      return (CLEvent)this.clEvents.getObject(var1);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   CLObjectRegistry getCLEventRegistry() {
      return this.clEvents;
   }

   void registerCLEvent(PointerBuffer var1) {
      if (var1 != null) {
         new CLEvent(var1.get(var1.position()), this);
      }

   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         ((CLContext)this.getParent()).getCLCommandQueueRegistry().unregisterObject(this);
      }

      return var1;
   }
}
