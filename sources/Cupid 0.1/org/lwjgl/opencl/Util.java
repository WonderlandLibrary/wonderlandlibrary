package org.lwjgl.opencl;

import java.lang.reflect.Field;
import java.util.Map;
import org.lwjgl.LWJGLUtil;

public final class Util {
  private static final Map<Integer, String> CL_ERROR_TOKENS = LWJGLUtil.getClassTokens(new LWJGLUtil.TokenFilter() {
        public boolean accept(Field field, int value) {
          return (value < 0);
        }
      },  null, new Class[] { CL10.class, CL11.class, KHRGLSharing.class, KHRICD.class, APPLEGLSharing.class, EXTDeviceFission.class });
  
  public static void checkCLError(int errcode) {
    if (errcode != 0)
      throwCLError(errcode); 
  }
  
  private static void throwCLError(int errcode) {
    String errname = CL_ERROR_TOKENS.get(Integer.valueOf(errcode));
    if (errname == null)
      errname = "UNKNOWN"; 
    throw new OpenCLException("Error Code: " + errname + " (" + LWJGLUtil.toHexString(errcode) + ")");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opencl\Util.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */