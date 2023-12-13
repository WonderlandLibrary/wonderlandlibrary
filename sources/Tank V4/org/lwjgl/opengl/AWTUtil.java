package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.IllegalComponentStateException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

final class AWTUtil {
   public static boolean hasWheel() {
      return true;
   }

   public static int getButtonCount() {
      return 3;
   }

   public static int getNativeCursorCapabilities() {
      if (LWJGLUtil.getPlatform() == 2 && !LWJGLUtil.isMacOSXEqualsOrBetterThan(10, 4)) {
         return 0;
      } else {
         int var0 = Toolkit.getDefaultToolkit().getMaximumCursorColors();
         boolean var1 = var0 >= 32767 && getMaxCursorSize() > 0;
         int var2 = var1 ? 3 : 4;
         return var2;
      }
   }

   public static Robot createRobot(Component var0) {
      try {
         return (Robot)AccessController.doPrivileged(new PrivilegedExceptionAction(var0) {
            final Component val$component;

            {
               this.val$component = var1;
            }

            public Robot run() throws Exception {
               return new Robot(this.val$component.getGraphicsConfiguration().getDevice());
            }

            public Object run() throws Exception {
               return this.run();
            }
         });
      } catch (PrivilegedActionException var2) {
         LWJGLUtil.log("Got exception while creating robot: " + var2.getCause());
         return null;
      }
   }

   private static int transformY(Component var0, int var1) {
      return var0.getHeight() - 1 - var1;
   }

   private static Point getPointerLocation(Component var0) {
      try {
         GraphicsConfiguration var1 = var0.getGraphicsConfiguration();
         if (var1 != null) {
            PointerInfo var2 = (PointerInfo)AccessController.doPrivileged(new PrivilegedExceptionAction() {
               public PointerInfo run() throws Exception {
                  return MouseInfo.getPointerInfo();
               }

               public Object run() throws Exception {
                  return this.run();
               }
            });
            GraphicsDevice var3 = var2.getDevice();
            if (var3 == var1.getDevice()) {
               return var2.getLocation();
            }

            return null;
         }
      } catch (Exception var4) {
         LWJGLUtil.log("Failed to query pointer location: " + var4.getCause());
      }

      return null;
   }

   public static Point getCursorPosition(Component var0) {
      try {
         Point var1 = getPointerLocation(var0);
         if (var1 != null) {
            Point var2 = var0.getLocationOnScreen();
            var1.translate(-var2.x, -var2.y);
            var1.move(var1.x, transformY(var0, var1.y));
            return var1;
         }
      } catch (IllegalComponentStateException var3) {
         LWJGLUtil.log("Failed to set cursor position: " + var3);
      } catch (NoClassDefFoundError var4) {
         LWJGLUtil.log("Failed to query cursor position: " + var4);
      }

      return null;
   }

   public static void setCursorPosition(Component var0, Robot var1, int var2, int var3) {
      if (var1 != null) {
         try {
            Point var4 = var0.getLocationOnScreen();
            int var5 = var4.x + var2;
            int var6 = var4.y + transformY(var0, var3);
            var1.mouseMove(var5, var6);
         } catch (IllegalComponentStateException var7) {
            LWJGLUtil.log("Failed to set cursor position: " + var7);
         }
      }

   }

   public static int getMinCursorSize() {
      Dimension var0 = Toolkit.getDefaultToolkit().getBestCursorSize(0, 0);
      return Math.max(var0.width, var0.height);
   }

   public static int getMaxCursorSize() {
      Dimension var0 = Toolkit.getDefaultToolkit().getBestCursorSize(10000, 10000);
      return Math.min(var0.width, var0.height);
   }

   public static Cursor createCursor(int var0, int var1, int var2, int var3, int var4, IntBuffer var5, IntBuffer var6) throws LWJGLException {
      BufferedImage var7 = new BufferedImage(var0, var1, 2);
      int[] var8 = new int[var5.remaining()];
      int var9 = var5.position();
      var5.get(var8);
      var5.position(var9);
      var7.setRGB(0, 0, var0, var1, var8, 0, var0);
      return Toolkit.getDefaultToolkit().createCustomCursor(var7, new Point(var2, var3), "LWJGL Custom cursor");
   }
}
