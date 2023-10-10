package org.lwjgl.input;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.InputImplementation;

public class Mouse {
   public static final int EVENT_SIZE = 22;
   private static boolean created;
   private static ByteBuffer buttons;
   private static int x;
   private static int y;
   private static int absolute_x;
   private static int absolute_y;
   private static IntBuffer coord_buffer;
   private static int dx;
   private static int dy;
   private static int dwheel;
   private static int buttonCount = -1;
   private static boolean hasWheel;
   private static Cursor currentCursor;
   private static String[] buttonName;
   private static final Map buttonMap = new HashMap(16);
   private static boolean initialized;
   private static ByteBuffer readBuffer;
   private static int eventButton;
   private static boolean eventState;
   private static int event_dx;
   private static int event_dy;
   private static int event_dwheel;
   private static int event_x;
   private static int event_y;
   private static long event_nanos;
   private static int grab_x;
   private static int grab_y;
   private static int last_event_raw_x;
   private static int last_event_raw_y;
   private static final int BUFFER_SIZE = 50;
   private static boolean isGrabbed;
   private static InputImplementation implementation;
   private static final boolean emulateCursorAnimation = LWJGLUtil.getPlatform() == 3 || LWJGLUtil.getPlatform() == 2;
   private static boolean clipMouseCoordinatesToWindow = !getPrivilegedBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");

   private Mouse() {
   }

   public static Cursor getNativeCursor() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return currentCursor;
   }

   public static Cursor setNativeCursor(Cursor var0) throws LWJGLException {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      if ((Cursor.getCapabilities() & 1) == 0) {
         throw new IllegalStateException("Mouse doesn't support native cursors");
      } else {
         Cursor var2 = currentCursor;
         currentCursor = var0;
         if (isCreated()) {
            if (currentCursor != null) {
               implementation.setNativeCursor(currentCursor.getHandle());
               currentCursor.setTimeout();
            } else {
               implementation.setNativeCursor((Object)null);
            }
         }

         return var2;
      }
   }

   public static boolean isClipMouseCoordinatesToWindow() {
      return clipMouseCoordinatesToWindow;
   }

   public static void setClipMouseCoordinatesToWindow(boolean var0) {
      clipMouseCoordinatesToWindow = var0;
   }

   public static void setCursorPosition(int var0, int var1) {
      Object var2;
      synchronized(var2 = OpenGLPackageAccess.global_lock){}
      if (!isCreated()) {
         throw new IllegalStateException("Mouse is not created");
      } else {
         event_x = var0;
         x = var0;
         event_y = var1;
         y = var1;
         if (!isGrabbed() && (Cursor.getCapabilities() & 1) != 0) {
            implementation.setCursorPosition(x, y);
         } else {
            grab_x = var0;
            grab_y = var1;
         }

      }
   }

   private static void initialize() {
      Sys.initialize();
      buttonName = new String[16];

      for(int var0 = 0; var0 < 16; ++var0) {
         buttonName[var0] = "BUTTON" + var0;
         buttonMap.put(buttonName[var0], var0);
      }

      initialized = true;
   }

   private static void resetMouse() {
      dwheel = 0;
      dy = 0;
      dx = 0;
      readBuffer.position(readBuffer.limit());
   }

   static InputImplementation getImplementation() {
      return implementation;
   }

   private static void create(InputImplementation var0) throws LWJGLException {
      if (!created) {
         if (!initialized) {
            initialize();
         }

         implementation = var0;
         implementation.createMouse();
         hasWheel = implementation.hasWheel();
         created = true;
         buttonCount = implementation.getButtonCount();
         buttons = BufferUtils.createByteBuffer(buttonCount);
         coord_buffer = BufferUtils.createIntBuffer(3);
         if (currentCursor != null && implementation.getNativeCursorCapabilities() != 0) {
            setNativeCursor(currentCursor);
         }

         readBuffer = ByteBuffer.allocate(1100);
         readBuffer.limit(0);
         setGrabbed(isGrabbed);
      }
   }

   public static void create() throws LWJGLException {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (!Display.isCreated()) {
         throw new IllegalStateException("Display must be created.");
      } else {
         create(OpenGLPackageAccess.createImplementation());
      }
   }

   public static boolean isCreated() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return created;
   }

   public static void destroy() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (created) {
         created = false;
         buttons = null;
         coord_buffer = null;
         implementation.destroyMouse();
      }
   }

   public static void poll() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (!created) {
         throw new IllegalStateException("Mouse must be created before you can poll it");
      } else {
         implementation.pollMouse(coord_buffer, buttons);
         int var1 = coord_buffer.get(0);
         int var2 = coord_buffer.get(1);
         int var3 = coord_buffer.get(2);
         if (isGrabbed()) {
            dx += var1;
            dy += var2;
            x += var1;
            y += var2;
            absolute_x += var1;
            absolute_y += var2;
         } else {
            dx = var1 - absolute_x;
            dy = var2 - absolute_y;
            x = var1;
            absolute_x = var1;
            y = var2;
            absolute_y = var2;
         }

         if (clipMouseCoordinatesToWindow) {
            x = Math.min(Display.getWidth() - 1, Math.max(0, x));
            y = Math.min(Display.getHeight() - 1, Math.max(0, y));
         }

         dwheel += var3;
         read();
      }
   }

   private static void read() {
      readBuffer.compact();
      implementation.readMouse(readBuffer);
      readBuffer.flip();
   }

   public static boolean isButtonDown(int var0) {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      if (!created) {
         throw new IllegalStateException("Mouse must be created before you can poll the button state");
      } else if (var0 < buttonCount && var0 >= 0) {
         return buttons.get(var0) == 1;
      } else {
         return false;
      }
   }

   public static String getButtonName(int var0) {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      return var0 < buttonName.length && var0 >= 0 ? buttonName[var0] : null;
   }

   public static int getButtonIndex(String var0) {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      Integer var2 = (Integer)buttonMap.get(var0);
      return var2 == null ? -1 : var2;
   }

   public static boolean next() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (!created) {
         throw new IllegalStateException("Mouse must be created before you can read events");
      } else if (readBuffer.hasRemaining()) {
         eventButton = readBuffer.get();
         eventState = readBuffer.get() != 0;
         if (isGrabbed()) {
            event_dx = readBuffer.getInt();
            event_dy = readBuffer.getInt();
            event_x += event_dx;
            event_y += event_dy;
            last_event_raw_x = event_x;
            last_event_raw_y = event_y;
         } else {
            int var1 = readBuffer.getInt();
            int var2 = readBuffer.getInt();
            event_dx = var1 - last_event_raw_x;
            event_dy = var2 - last_event_raw_y;
            event_x = var1;
            event_y = var2;
            last_event_raw_x = var1;
            last_event_raw_y = var2;
         }

         if (clipMouseCoordinatesToWindow) {
            event_x = Math.min(Display.getWidth() - 1, Math.max(0, event_x));
            event_y = Math.min(Display.getHeight() - 1, Math.max(0, event_y));
         }

         event_dwheel = readBuffer.getInt();
         event_nanos = readBuffer.getLong();
         return true;
      } else {
         return false;
      }
   }

   public static int getEventButton() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return eventButton;
   }

   public static boolean getEventButtonState() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return eventState;
   }

   public static int getEventDX() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_dx;
   }

   public static int getEventDY() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_dy;
   }

   public static int getEventX() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_x;
   }

   public static int getEventY() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_y;
   }

   public static int getEventDWheel() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_dwheel;
   }

   public static long getEventNanoseconds() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return event_nanos;
   }

   public static int getX() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return x;
   }

   public static int getY() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return y;
   }

   public static int getDX() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      int var1 = dx;
      dx = 0;
      return var1;
   }

   public static int getDY() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      int var1 = dy;
      dy = 0;
      return var1;
   }

   public static int getDWheel() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      int var1 = dwheel;
      dwheel = 0;
      return var1;
   }

   public static int getButtonCount() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return buttonCount;
   }

   public static boolean hasWheel() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return hasWheel;
   }

   public static boolean isGrabbed() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      return isGrabbed;
   }

   public static void setGrabbed(boolean var0) {
      Object var1;
      synchronized(var1 = OpenGLPackageAccess.global_lock){}
      boolean var2 = isGrabbed;
      isGrabbed = var0;
      if (isCreated()) {
         if (var0 && !var2) {
            grab_x = x;
            grab_y = y;
         } else if (!var0 && var2 && (Cursor.getCapabilities() & 1) != 0) {
            implementation.setCursorPosition(grab_x, grab_y);
         }

         implementation.grabMouse(var0);
         poll();
         event_x = x;
         event_y = y;
         last_event_raw_x = x;
         last_event_raw_y = y;
         resetMouse();
      }

   }

   public static void updateCursor() {
      Object var0;
      synchronized(var0 = OpenGLPackageAccess.global_lock){}
      if (emulateCursorAnimation && currentCursor != null && currentCursor.hasTimedOut() && isInsideWindow()) {
         currentCursor.nextCursor();

         try {
            setNativeCursor(currentCursor);
         } catch (LWJGLException var3) {
            if (LWJGLUtil.DEBUG) {
               var3.printStackTrace();
            }
         }
      }

   }

   static boolean getPrivilegedBoolean(String var0) {
      Boolean var1 = (Boolean)AccessController.doPrivileged(new PrivilegedAction(var0) {
         final String val$property_name;

         {
            this.val$property_name = var1;
         }

         public Boolean run() {
            return Boolean.getBoolean(this.val$property_name);
         }

         public Object run() {
            return this.run();
         }
      });
      return var1;
   }

   public static boolean isInsideWindow() {
      return implementation.isInsideWindow();
   }
}
