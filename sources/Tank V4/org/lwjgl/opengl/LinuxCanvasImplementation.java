package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class LinuxCanvasImplementation implements AWTCanvasImplementation {
   static int getScreenFromDevice(GraphicsDevice var0) throws LWJGLException {
      try {
         Method var1 = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction(var0) {
            final GraphicsDevice val$device;

            {
               this.val$device = var1;
            }

            public Method run() throws Exception {
               return this.val$device.getClass().getMethod("getScreen");
            }

            public Object run() throws Exception {
               return this.run();
            }
         });
         Integer var2 = (Integer)var1.invoke(var0);
         return var2;
      } catch (Exception var3) {
         throw new LWJGLException(var3);
      }
   }

   private static int getVisualIDFromConfiguration(GraphicsConfiguration var0) throws LWJGLException {
      try {
         Method var1 = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction(var0) {
            final GraphicsConfiguration val$configuration;

            {
               this.val$configuration = var1;
            }

            public Method run() throws Exception {
               return this.val$configuration.getClass().getMethod("getVisual");
            }

            public Object run() throws Exception {
               return this.run();
            }
         });
         Integer var2 = (Integer)var1.invoke(var0);
         return var2;
      } catch (Exception var3) {
         throw new LWJGLException(var3);
      }
   }

   public PeerInfo createPeerInfo(Canvas var1, PixelFormat var2, ContextAttribs var3) throws LWJGLException {
      return new LinuxAWTGLCanvasPeerInfo(var1);
   }

   public GraphicsConfiguration findConfiguration(GraphicsDevice var1, PixelFormat var2) throws LWJGLException {
      try {
         int var3 = getScreenFromDevice(var1);
         int var4 = findVisualIDFromFormat(var3, var2);
         GraphicsConfiguration[] var5 = var1.getConfigurations();
         GraphicsConfiguration[] var6 = var5;
         int var7 = var5.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            GraphicsConfiguration var9 = var6[var8];
            int var10 = getVisualIDFromConfiguration(var9);
            if (var10 == var4) {
               return var9;
            }
         }
      } catch (LWJGLException var11) {
         LWJGLUtil.log("Got exception while trying to determine configuration: " + var11);
      }

      return null;
   }

   private static int findVisualIDFromFormat(int var0, PixelFormat var1) throws LWJGLException {
      LinuxDisplay.lockAWT();
      GLContext.loadOpenGLLibrary();
      LinuxDisplay.incDisplay();
      int var2 = nFindVisualIDFromFormat(LinuxDisplay.getDisplay(), var0, var1);
      LinuxDisplay.decDisplay();
      GLContext.unloadOpenGLLibrary();
      LinuxDisplay.unlockAWT();
      return var2;
   }

   private static native int nFindVisualIDFromFormat(long var0, int var2, PixelFormat var3) throws LWJGLException;
}
