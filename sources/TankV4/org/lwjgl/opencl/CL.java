package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.Sys;

public final class CL {
   private static boolean created;

   private CL() {
   }

   private static native void nCreate(String var0) throws LWJGLException;

   private static native void nCreateDefault() throws LWJGLException;

   private static native void nDestroy();

   public static boolean isCreated() {
      return created;
   }

   public static void create() throws LWJGLException {
      if (!created) {
         String var0;
         String[] var1;
         switch(LWJGLUtil.getPlatform()) {
         case 1:
            var0 = "OpenCL";
            var1 = new String[]{"libOpenCL64.so", "libOpenCL.so"};
            break;
         case 2:
            var0 = "OpenCL";
            var1 = new String[]{"OpenCL.dylib"};
            break;
         case 3:
            var0 = "OpenCL";
            var1 = new String[]{"OpenCL.dll"};
            break;
         default:
            throw new LWJGLException("Unknown platform: " + LWJGLUtil.getPlatform());
         }

         String[] var2 = LWJGLUtil.getLibraryPaths(var0, var1, CL.class.getClassLoader());
         LWJGLUtil.log("Found " + var2.length + " OpenCL paths");
         String[] var3 = var2;
         int var4 = var2.length;
         int var5 = 0;

         while(var5 < var4) {
            String var6 = var3[var5];

            try {
               nCreate(var6);
               created = true;
               break;
            } catch (LWJGLException var8) {
               LWJGLUtil.log("Failed to load " + var6 + ": " + var8.getMessage());
               ++var5;
            }
         }

         if (!created && LWJGLUtil.getPlatform() == 2) {
            nCreateDefault();
            created = true;
         }

         if (!created) {
            throw new LWJGLException("Could not locate OpenCL library.");
         } else if (!CLCapabilities.OpenCL10) {
            throw new RuntimeException("OpenCL 1.0 not supported.");
         }
      }
   }

   public static void destroy() {
   }

   static long getFunctionAddress(String[] var0) {
      String[] var1 = var0;
      int var2 = var0.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         String var4 = var1[var3];
         long var5 = getFunctionAddress(var4);
         if (var5 != 0L) {
            return var5;
         }
      }

      return 0L;
   }

   static long getFunctionAddress(String var0) {
      ByteBuffer var1 = MemoryUtil.encodeASCII(var0);
      return ngetFunctionAddress(MemoryUtil.getAddress(var1));
   }

   private static native long ngetFunctionAddress(long var0);

   static native ByteBuffer getHostBuffer(long var0, int var2);

   private static native void resetNativeStubs(Class var0);

   static {
      Sys.initialize();
   }
}
