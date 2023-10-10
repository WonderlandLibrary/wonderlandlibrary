package org.lwjgl.opencl;

import java.lang.reflect.Field;
import java.util.Map;
import org.lwjgl.LWJGLUtil;

public final class Util {
   private static final Map CL_ERROR_TOKENS = LWJGLUtil.getClassTokens(new LWJGLUtil.TokenFilter() {
      public boolean accept(Field var1, int var2) {
         return var2 < 0;
      }
   }, (Map)null, (Class[])(CL10.class, CL11.class, KHRGLSharing.class, KHRICD.class, APPLEGLSharing.class, EXTDeviceFission.class));

   private Util() {
   }

   public static void checkCLError(int var0) {
      if (var0 != 0) {
         throwCLError(var0);
      }

   }

   private static void throwCLError(int var0) {
      String var1 = (String)CL_ERROR_TOKENS.get(var0);
      if (var1 == null) {
         var1 = "UNKNOWN";
      }

      throw new OpenCLException("Error Code: " + var1 + " (" + LWJGLUtil.toHexString(var0) + ")");
   }
}
