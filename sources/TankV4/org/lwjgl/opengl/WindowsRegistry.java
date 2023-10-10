package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;

final class WindowsRegistry {
   static final int HKEY_CLASSES_ROOT = 1;
   static final int HKEY_CURRENT_USER = 2;
   static final int HKEY_LOCAL_MACHINE = 3;
   static final int HKEY_USERS = 4;

   static String queryRegistrationKey(int var0, String var1, String var2) throws LWJGLException {
      switch(var0) {
      case 1:
      case 2:
      case 3:
      case 4:
         return nQueryRegistrationKey(var0, var1, var2);
      default:
         throw new IllegalArgumentException("Invalid enum: " + var0);
      }
   }

   private static native String nQueryRegistrationKey(int var0, String var1, String var2) throws LWJGLException;

   static {
      Sys.initialize();
   }
}
