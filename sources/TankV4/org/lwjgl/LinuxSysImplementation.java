package org.lwjgl;

import java.awt.Toolkit;
import java.security.AccessController;
import java.security.PrivilegedAction;

final class LinuxSysImplementation extends J2SESysImplementation {
   private static final int JNI_VERSION = 19;

   public int getRequiredJNIVersion() {
      return 19;
   }

   public boolean openURL(String var1) {
      String[] var2 = new String[]{"sensible-browser", "xdg-open", "google-chrome", "chromium", "firefox", "iceweasel", "mozilla", "opera", "konqueror", "nautilus", "galeon", "netscape"};
      String[] var3 = var2;
      int var4 = var2.length;
      int var5 = 0;

      while(var5 < var4) {
         String var6 = var3[var5];

         try {
            LWJGLUtil.execPrivileged(new String[]{var6, var1});
            return true;
         } catch (Exception var8) {
            var8.printStackTrace(System.err);
            ++var5;
         }
      }

      return false;
   }

   public boolean has64Bit() {
      return true;
   }

   static {
      Toolkit.getDefaultToolkit();
      AccessController.doPrivileged(new PrivilegedAction() {
         public Object run() {
            try {
               System.loadLibrary("jawt");
            } catch (UnsatisfiedLinkError var2) {
            }

            return null;
         }
      });
   }
}
