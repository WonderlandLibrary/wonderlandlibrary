package org.lwjgl.input;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

final class OpenGLPackageAccess {
   static final Object global_lock;

   static InputImplementation createImplementation() {
      try {
         return (InputImplementation)AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public InputImplementation run() throws Exception {
               Method var1 = Display.class.getDeclaredMethod("getImplementation");
               var1.setAccessible(true);
               return (InputImplementation)var1.invoke((Object)null);
            }

            public Object run() throws Exception {
               return this.run();
            }
         });
      } catch (PrivilegedActionException var1) {
         throw new Error(var1);
      }
   }

   static {
      try {
         global_lock = AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws Exception {
               Field var1 = Class.forName("org.lwjgl.opengl.GlobalLock").getDeclaredField("lock");
               var1.setAccessible(true);
               return var1.get((Object)null);
            }
         });
      } catch (PrivilegedActionException var1) {
         throw new Error(var1);
      }
   }
}
