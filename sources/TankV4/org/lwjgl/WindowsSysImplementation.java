package org.lwjgl;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.opengl.Display;

final class WindowsSysImplementation extends DefaultSysImplementation {
   private static final int JNI_VERSION = 24;

   public int getRequiredJNIVersion() {
      return 24;
   }

   public long getTimerResolution() {
      return 1000L;
   }

   public long getTime() {
      return nGetTime();
   }

   private static native long nGetTime();

   public boolean has64Bit() {
      return true;
   }

   private static long getHwnd() {
      if (!Display.isCreated()) {
         return 0L;
      } else {
         try {
            return (Long)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public Long run() throws Exception {
                  Method var1 = Display.class.getDeclaredMethod("getImplementation");
                  var1.setAccessible(true);
                  Object var2 = var1.invoke((Object)null);
                  Class var3 = Class.forName("org.lwjgl.opengl.WindowsDisplay");
                  Method var4 = var3.getDeclaredMethod("getHwnd");
                  var4.setAccessible(true);
                  return (Long)var4.invoke(var2);
               }

               public Object run() throws Exception {
                  return this.run();
               }
            });
         } catch (PrivilegedActionException var1) {
            throw new Error(var1);
         }
      }
   }

   public void alert(String var1, String var2) {
      if (!Display.isCreated()) {
         initCommonControls();
      }

      LWJGLUtil.log(String.format("*** Alert *** %s\n%s\n", var1, var2));
      ByteBuffer var3 = MemoryUtil.encodeUTF16(var1);
      ByteBuffer var4 = MemoryUtil.encodeUTF16(var2);
      nAlert(getHwnd(), MemoryUtil.getAddress(var3), MemoryUtil.getAddress(var4));
   }

   private static native void nAlert(long var0, long var2, long var4);

   private static native void initCommonControls();

   public boolean openURL(String var1) {
      try {
         LWJGLUtil.execPrivileged(new String[]{"rundll32", "url.dll,FileProtocolHandler", var1});
         return true;
      } catch (Exception var3) {
         LWJGLUtil.log("Failed to open url (" + var1 + "): " + var3.getMessage());
         return false;
      }
   }

   public String getClipboard() {
      return nGetClipboard();
   }

   private static native String nGetClipboard();

   static {
      Sys.initialize();
   }
}
