package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerBuffer;

public final class CLProgram extends CLObjectChild {
   private static final CLProgram.CLProgramUtil util = (CLProgram.CLProgramUtil)CLPlatform.getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");
   private final CLObjectRegistry clKernels;

   CLProgram(long var1, CLContext var3) {
      super(var1, var3);
      if (this.isValid()) {
         var3.getCLProgramRegistry().registerObject(this);
         this.clKernels = new CLObjectRegistry();
      } else {
         this.clKernels = null;
      }

   }

   public CLKernel getCLKernel(long var1) {
      return (CLKernel)this.clKernels.getObject(var1);
   }

   public CLKernel[] createKernelsInProgram() {
      return util.createKernelsInProgram(this);
   }

   public String getInfoString(int var1) {
      return util.getInfoString(this, var1);
   }

   public int getInfoInt(int var1) {
      return util.getInfoInt(this, var1);
   }

   public long[] getInfoSizeArray(int var1) {
      return util.getInfoSizeArray(this, var1);
   }

   public CLDevice[] getInfoDevices() {
      return util.getInfoDevices(this);
   }

   public ByteBuffer getInfoBinaries(ByteBuffer var1) {
      return util.getInfoBinaries(this, var1);
   }

   public ByteBuffer[] getInfoBinaries(ByteBuffer[] var1) {
      return util.getInfoBinaries(this, var1);
   }

   public String getBuildInfoString(CLDevice var1, int var2) {
      return util.getBuildInfoString(this, var1, var2);
   }

   public int getBuildInfoInt(CLDevice var1, int var2) {
      return util.getBuildInfoInt(this, var1, var2);
   }

   CLObjectRegistry getCLKernelRegistry() {
      return this.clKernels;
   }

   void registerCLKernels(PointerBuffer var1) {
      for(int var2 = var1.position(); var2 < var1.limit(); ++var2) {
         long var3 = var1.get(var2);
         if (var3 != 0L) {
            new CLKernel(var3, this);
         }
      }

   }

   int release() {
      int var1 = super.release();
      if (!this.isValid()) {
         ((CLContext)this.getParent()).getCLProgramRegistry().unregisterObject(this);
      }

      return var1;
   }

   interface CLProgramUtil extends InfoUtil {
      CLKernel[] createKernelsInProgram(CLProgram var1);

      CLDevice[] getInfoDevices(CLProgram var1);

      ByteBuffer getInfoBinaries(CLProgram var1, ByteBuffer var2);

      ByteBuffer[] getInfoBinaries(CLProgram var1, ByteBuffer[] var2);

      String getBuildInfoString(CLProgram var1, CLDevice var2, int var3);

      int getBuildInfoInt(CLProgram var1, CLDevice var2, int var3);
   }
}
