package com.sun.glass.ui;

import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.util.Map;

public abstract class View {
   public static final int GESTURE_NO_VALUE = Integer.MAX_VALUE;
   public static final double GESTURE_NO_DOUBLE_VALUE = Double.NaN;
   public static final byte IME_ATTR_INPUT = 0;
   public static final byte IME_ATTR_TARGET_CONVERTED = 1;
   public static final byte IME_ATTR_CONVERTED = 2;
   public static final byte IME_ATTR_TARGET_NOTCONVERTED = 3;
   public static final byte IME_ATTR_INPUT_ERROR = 4;
   static final boolean accessible = (Boolean)AccessController.doPrivileged(() -> {
      String var0 = System.getProperty("glass.accessible.force");
      if (var0 != null) {
         return Boolean.parseBoolean(var0);
      } else {
         try {
            String var1 = Platform.determinePlatform();
            String var2 = System.getProperty("os.version").replaceFirst("(\\d+)\\.\\d+.*", "$1");
            String var3 = System.getProperty("os.version").replaceFirst("\\d+\\.(\\d+).*", "$1");
            int var4 = Integer.parseInt(var2) * 100 + Integer.parseInt(var3);
            return var1.equals("Mac") && var4 >= 1009 || var1.equals("Win") && var4 >= 601;
         } catch (Exception var5) {
            return false;
         }
      }
   });
   private volatile long ptr;
   private Window window;
   private EventHandler eventHandler;
   private int width = -1;
   private int height = -1;
   private boolean isValid = false;
   private boolean isVisible = false;
   private boolean inFullscreen = false;
   private static WeakReference lastClickedView = null;
   private static int lastClickedButton;
   private static long lastClickedTime;
   private static int lastClickedX;
   private static int lastClickedY;
   private static int clickCount;
   private static boolean dragProcessed = false;
   private ClipboardAssistance dropSourceAssistant;
   ClipboardAssistance dropTargetAssistant;

   public static long getMultiClickTime() {
      Application.checkEventThread();
      return Application.GetApplication().staticView_getMultiClickTime();
   }

   public static int getMultiClickMaxX() {
      Application.checkEventThread();
      return Application.GetApplication().staticView_getMultiClickMaxX();
   }

   public static int getMultiClickMaxY() {
      Application.checkEventThread();
      return Application.GetApplication().staticView_getMultiClickMaxY();
   }

   protected abstract void _enableInputMethodEvents(long var1, boolean var3);

   protected void _finishInputMethodComposition(long var1) {
   }

   protected abstract long _create(Map var1);

   protected View() {
      Application.checkEventThread();
      Application.GetApplication();
      this.ptr = this._create(Application.getDeviceDetails());
      if (this.ptr == 0L) {
         throw new RuntimeException("could not create platform view");
      }
   }

   private void checkNotClosed() {
      if (this.ptr == 0L) {
         throw new IllegalStateException("The view has already been closed");
      }
   }

   public boolean isClosed() {
      Application.checkEventThread();
      return this.ptr == 0L;
   }

   protected abstract long _getNativeView(long var1);

   public long getNativeView() {
      Application.checkEventThread();
      this.checkNotClosed();
      return this._getNativeView(this.ptr);
   }

   public int getNativeRemoteLayerId(String var1) {
      Application.checkEventThread();
      throw new RuntimeException("This operation is not supported on this platform");
   }

   public Window getWindow() {
      Application.checkEventThread();
      return this.window;
   }

   protected abstract int _getX(long var1);

   public int getX() {
      Application.checkEventThread();
      this.checkNotClosed();
      return this._getX(this.ptr);
   }

   protected abstract int _getY(long var1);

   public int getY() {
      Application.checkEventThread();
      this.checkNotClosed();
      return this._getY(this.ptr);
   }

   public int getWidth() {
      Application.checkEventThread();
      return this.width;
   }

   public int getHeight() {
      Application.checkEventThread();
      return this.height;
   }

   protected abstract void _setParent(long var1, long var3);

   void setWindow(Window var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this.window = var1;
      this._setParent(this.ptr, var1 == null ? 0L : var1.getNativeHandle());
      this.isValid = this.ptr != 0L && var1 != null;
   }

   void setVisible(boolean var1) {
      this.isVisible = var1;
   }

   protected abstract boolean _close(long var1);

   public void close() {
      Application.checkEventThread();
      if (this.ptr != 0L) {
         if (this.isInFullscreen()) {
            this._exitFullscreen(this.ptr, false);
         }

         Window var1 = this.getWindow();
         if (var1 != null) {
            var1.setView((View)null);
         }

         this.isValid = false;
         this._close(this.ptr);
         this.ptr = 0L;
      }
   }

   public EventHandler getEventHandler() {
      Application.checkEventThread();
      return this.eventHandler;
   }

   public void setEventHandler(EventHandler var1) {
      Application.checkEventThread();
      this.eventHandler = var1;
   }

   private void handleViewEvent(long var1, int var3) {
      if (this.eventHandler != null) {
         this.eventHandler.handleViewEvent(this, var1, var3);
      }

   }

   private void handleKeyEvent(long var1, int var3, int var4, char[] var5, int var6) {
      if (this.eventHandler != null) {
         this.eventHandler.handleKeyEvent(this, var1, var3, var4, var5, var6);
      }

   }

   private void handleMouseEvent(long var1, int var3, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, boolean var11) {
      if (this.eventHandler != null) {
         this.eventHandler.handleMouseEvent(this, var1, var3, var4, var5, var6, var7, var8, var9, var10, var11);
      }

   }

   private void handleMenuEvent(int var1, int var2, int var3, int var4, boolean var5) {
      if (this.eventHandler != null) {
         this.eventHandler.handleMenuEvent(this, var1, var2, var3, var4, var5);
      }

   }

   public void handleBeginTouchEvent(View var1, long var2, int var4, boolean var5, int var6) {
      if (this.eventHandler != null) {
         this.eventHandler.handleBeginTouchEvent(var1, var2, var4, var5, var6);
      }

   }

   public void handleNextTouchEvent(View var1, long var2, int var4, long var5, int var7, int var8, int var9, int var10) {
      if (this.eventHandler != null) {
         this.eventHandler.handleNextTouchEvent(var1, var2, var4, var5, var7, var8, var9, var10);
      }

   }

   public void handleEndTouchEvent(View var1, long var2) {
      if (this.eventHandler != null) {
         this.eventHandler.handleEndTouchEvent(var1, var2);
      }

   }

   public void handleScrollGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, int var12, double var13, double var15, double var17, double var19, double var21, double var23) {
      if (this.eventHandler != null) {
         this.eventHandler.handleScrollGestureEvent(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var15, var17, var19, var21, var23);
      }

   }

   public void handleZoomGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, double var12, double var14, double var16, double var18) {
      if (this.eventHandler != null) {
         this.eventHandler.handleZoomGestureEvent(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var14, var16, var18);
      }

   }

   public void handleRotateGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, double var12, double var14) {
      if (this.eventHandler != null) {
         this.eventHandler.handleRotateGestureEvent(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var14);
      }

   }

   public void handleSwipeGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      if (this.eventHandler != null) {
         this.eventHandler.handleSwipeGestureEvent(var1, var2, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13);
      }

   }

   private void handleInputMethodEvent(long var1, String var3, int[] var4, int[] var5, byte[] var6, int var7, int var8) {
      if (this.eventHandler != null) {
         this.eventHandler.handleInputMethodEvent(var1, var3, var4, var5, var6, var7, var8);
      }

   }

   public void enableInputMethodEvents(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._enableInputMethodEvents(this.ptr, var1);
   }

   public void finishInputMethodComposition() {
      Application.checkEventThread();
      this.checkNotClosed();
      this._finishInputMethodComposition(this.ptr);
   }

   private double[] getInputMethodCandidatePos(int var1) {
      return this.eventHandler != null ? this.eventHandler.getInputMethodCandidatePos(var1) : null;
   }

   private void handleDragStart(int var1, int var2, int var3, int var4, int var5, ClipboardAssistance var6) {
      if (this.eventHandler != null) {
         this.eventHandler.handleDragStart(this, var1, var2, var3, var4, var5, var6);
      }

   }

   private void handleDragEnd(int var1) {
      if (this.eventHandler != null) {
         this.eventHandler.handleDragEnd(this, var1);
      }

   }

   private int handleDragEnter(int var1, int var2, int var3, int var4, int var5, ClipboardAssistance var6) {
      return this.eventHandler != null ? this.eventHandler.handleDragEnter(this, var1, var2, var3, var4, var5, var6) : var5;
   }

   private int handleDragOver(int var1, int var2, int var3, int var4, int var5, ClipboardAssistance var6) {
      return this.eventHandler != null ? this.eventHandler.handleDragOver(this, var1, var2, var3, var4, var5, var6) : var5;
   }

   private void handleDragLeave(ClipboardAssistance var1) {
      if (this.eventHandler != null) {
         this.eventHandler.handleDragLeave(this, var1);
      }

   }

   private int handleDragDrop(int var1, int var2, int var3, int var4, int var5, ClipboardAssistance var6) {
      return this.eventHandler != null ? this.eventHandler.handleDragDrop(this, var1, var2, var3, var4, var5, var6) : 0;
   }

   protected abstract void _scheduleRepaint(long var1);

   public void scheduleRepaint() {
      Application.checkEventThread();
      this.checkNotClosed();
      this._scheduleRepaint(this.ptr);
   }

   protected abstract void _begin(long var1);

   public void lock() {
      this.checkNotClosed();
      this._begin(this.ptr);
   }

   protected abstract void _end(long var1);

   public void unlock() {
      this.checkNotClosed();
      this._end(this.ptr);
   }

   protected abstract int _getNativeFrameBuffer(long var1);

   public int getNativeFrameBuffer() {
      return this._getNativeFrameBuffer(this.ptr);
   }

   protected abstract void _uploadPixels(long var1, Pixels var3);

   public void uploadPixels(Pixels var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this.lock();

      try {
         this._uploadPixels(this.ptr, var1);
      } finally {
         this.unlock();
      }

   }

   protected abstract boolean _enterFullscreen(long var1, boolean var3, boolean var4, boolean var5);

   public boolean enterFullscreen(boolean var1, boolean var2, boolean var3) {
      Application.checkEventThread();
      this.checkNotClosed();
      return this._enterFullscreen(this.ptr, var1, var2, var3);
   }

   protected abstract void _exitFullscreen(long var1, boolean var3);

   public void exitFullscreen(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._exitFullscreen(this.ptr, var1);
   }

   public boolean isInFullscreen() {
      Application.checkEventThread();
      return this.inFullscreen;
   }

   public boolean toggleFullscreen(boolean var1, boolean var2, boolean var3) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (!this.inFullscreen) {
         this.enterFullscreen(var1, var2, var3);
      } else {
         this.exitFullscreen(var1);
      }

      this._scheduleRepaint(this.ptr);
      return this.inFullscreen;
   }

   public void updateLocation() {
      this.notifyView(423);
   }

   protected void notifyView(int var1) {
      if (var1 == 421) {
         if (this.isValid) {
            this.handleViewEvent(System.nanoTime(), var1);
         }
      } else {
         boolean var2 = false;
         switch (var1) {
            case 411:
               this.isValid = true;
               var2 = true;
               break;
            case 412:
               this.isValid = false;
               var2 = true;
            case 422:
            case 423:
               break;
            case 431:
               this.inFullscreen = true;
               var2 = true;
               if (this.getWindow() != null) {
                  this.getWindow().notifyFullscreen(true);
               }
               break;
            case 432:
               this.inFullscreen = false;
               var2 = true;
               if (this.getWindow() != null) {
                  this.getWindow().notifyFullscreen(false);
               }
               break;
            default:
               System.err.println("Unknown view event type: " + var1);
               return;
         }

         this.handleViewEvent(System.nanoTime(), var1);
         if (var2) {
            this.handleViewEvent(System.nanoTime(), 423);
         }
      }

   }

   protected void notifyResize(int var1, int var2) {
      if (this.width != var1 || this.height != var2) {
         this.width = var1;
         this.height = var2;
         this.handleViewEvent(System.nanoTime(), 422);
      }
   }

   protected void notifyRepaint(int var1, int var2, int var3, int var4) {
      this.notifyView(421);
   }

   protected void notifyMenu(int var1, int var2, int var3, int var4, boolean var5) {
      this.handleMenuEvent(var1, var2, var3, var4, var5);
   }

   protected void notifyMouse(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.window == null || !this.window.handleMouseEvent(var1, var2, var3, var4, var5, var6)) {
         long var10 = System.nanoTime();
         if (var1 == 221) {
            View var12 = lastClickedView == null ? null : (View)lastClickedView.get();
            if (var12 == this && lastClickedButton == var2 && var10 - lastClickedTime <= 1000000L * getMultiClickTime() && Math.abs(var3 - lastClickedX) <= getMultiClickMaxX() && Math.abs(var4 - lastClickedY) <= getMultiClickMaxY()) {
               ++clickCount;
            } else {
               clickCount = 1;
               lastClickedView = new WeakReference(this);
               lastClickedButton = var2;
               lastClickedX = var3;
               lastClickedY = var4;
            }

            lastClickedTime = var10;
         }

         this.handleMouseEvent(var10, var1, var2, var3, var4, var5, var6, var7, var8, var9);
         if (var1 == 223) {
            if (!dragProcessed) {
               this.notifyDragStart(var2, var3, var4, var5, var6);
               dragProcessed = true;
            }
         } else {
            dragProcessed = false;
         }

      }
   }

   protected void notifyScroll(int var1, int var2, int var3, int var4, double var5, double var7, int var9, int var10, int var11, int var12, int var13, double var14, double var16) {
      if (this.eventHandler != null) {
         this.eventHandler.handleScrollEvent(this, System.nanoTime(), var1, var2, var3, var4, var5, var7, var9, var10, var11, var12, var13, var14, var16);
      }

   }

   protected void notifyKey(int var1, int var2, char[] var3, int var4) {
      this.handleKeyEvent(System.nanoTime(), var1, var2, var3, var4);
   }

   protected void notifyInputMethod(String var1, int[] var2, int[] var3, byte[] var4, int var5, int var6, int var7) {
      this.handleInputMethodEvent(System.nanoTime(), var1, var2, var3, var4, var5, var6);
   }

   protected double[] notifyInputMethodCandidatePosRequest(int var1) {
      double[] var2 = this.getInputMethodCandidatePos(var1);
      if (var2 == null) {
         var2 = new double[]{0.0, 0.0};
      }

      return var2;
   }

   protected void notifyDragStart(int var1, int var2, int var3, int var4, int var5) {
      this.dropSourceAssistant = new ClipboardAssistance("DND") {
         public void actionPerformed(int var1) {
            View.this.notifyDragEnd(var1);
         }
      };
      this.handleDragStart(var1, var2, var3, var4, var5, this.dropSourceAssistant);
      if (this.dropSourceAssistant != null) {
         this.dropSourceAssistant.close();
         this.dropSourceAssistant = null;
      }

   }

   protected void notifyDragEnd(int var1) {
      this.handleDragEnd(var1);
      if (this.dropSourceAssistant != null) {
         this.dropSourceAssistant.close();
         this.dropSourceAssistant = null;
      }

   }

   protected int notifyDragEnter(int var1, int var2, int var3, int var4, int var5) {
      this.dropTargetAssistant = new ClipboardAssistance("DND") {
         public void flush() {
            throw new UnsupportedOperationException("Flush is forbidden from target!");
         }
      };
      return this.handleDragEnter(var1, var2, var3, var4, var5, this.dropTargetAssistant);
   }

   protected int notifyDragOver(int var1, int var2, int var3, int var4, int var5) {
      return this.handleDragOver(var1, var2, var3, var4, var5, this.dropTargetAssistant);
   }

   protected void notifyDragLeave() {
      this.handleDragLeave(this.dropTargetAssistant);
      this.dropTargetAssistant.close();
   }

   protected int notifyDragDrop(int var1, int var2, int var3, int var4, int var5) {
      int var6 = this.handleDragDrop(var1, var2, var3, var4, var5, this.dropTargetAssistant);
      this.dropTargetAssistant.close();
      return var6;
   }

   public void notifyBeginTouchEvent(int var1, boolean var2, int var3) {
      this.handleBeginTouchEvent(this, System.nanoTime(), var1, var2, var3);
   }

   public void notifyNextTouchEvent(int var1, long var2, int var4, int var5, int var6, int var7) {
      this.handleNextTouchEvent(this, System.nanoTime(), var1, var2, var4, var5, var6, var7);
   }

   public void notifyEndTouchEvent() {
      this.handleEndTouchEvent(this, System.nanoTime());
   }

   public void notifyScrollGestureEvent(int var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, int var9, double var10, double var12, double var14, double var16, double var18, double var20) {
      this.handleScrollGestureEvent(this, System.nanoTime(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var12, var14, var16, var18, var20);
   }

   public void notifyZoomGestureEvent(int var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9, double var11, double var13, double var15) {
      this.handleZoomGestureEvent(this, System.nanoTime(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var11, var13, var15);
   }

   public void notifyRotateGestureEvent(int var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, double var9, double var11) {
      this.handleRotateGestureEvent(this, System.nanoTime(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var11);
   }

   public void notifySwipeGestureEvent(int var1, int var2, boolean var3, boolean var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.handleSwipeGestureEvent(this, System.nanoTime(), var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   long getAccessible() {
      Application.checkEventThread();
      this.checkNotClosed();
      if (accessible) {
         Accessible var1 = this.eventHandler.getSceneAccessible();
         if (var1 != null) {
            var1.setView(this);
            return var1.getNativeAccessible();
         }
      }

      return 0L;
   }

   public static final class Capability {
      public static final int k3dKeyValue = 0;
      public static final int kSyncKeyValue = 1;
      public static final int k3dProjectionKeyValue = 2;
      public static final int k3dProjectionAngleKeyValue = 3;
      public static final int k3dDepthKeyValue = 4;
      public static final int kHiDPIAwareKeyValue = 5;
      public static final Object k3dKey = 0;
      public static final Object kSyncKey = 1;
      public static final Object k3dProjectionKey = 2;
      public static final Object k3dProjectionAngleKey = 3;
      public static final Object k3dDepthKey = 4;
      public static final Object kHiDPIAwareKey = 5;
   }

   public static class EventHandler {
      public void handleViewEvent(View var1, long var2, int var4) {
      }

      public void handleKeyEvent(View var1, long var2, int var4, int var5, char[] var6, int var7) {
      }

      public void handleMenuEvent(View var1, int var2, int var3, int var4, int var5, boolean var6) {
      }

      public void handleMouseEvent(View var1, long var2, int var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11, boolean var12) {
      }

      public void handleScrollEvent(View var1, long var2, int var4, int var5, int var6, int var7, double var8, double var10, int var12, int var13, int var14, int var15, int var16, double var17, double var19) {
      }

      public void handleInputMethodEvent(long var1, String var3, int[] var4, int[] var5, byte[] var6, int var7, int var8) {
      }

      public double[] getInputMethodCandidatePos(int var1) {
         return null;
      }

      public void handleDragStart(View var1, int var2, int var3, int var4, int var5, int var6, ClipboardAssistance var7) {
      }

      public void handleDragEnd(View var1, int var2) {
      }

      public int handleDragEnter(View var1, int var2, int var3, int var4, int var5, int var6, ClipboardAssistance var7) {
         return var6;
      }

      public int handleDragOver(View var1, int var2, int var3, int var4, int var5, int var6, ClipboardAssistance var7) {
         return var6;
      }

      public void handleDragLeave(View var1, ClipboardAssistance var2) {
      }

      public int handleDragDrop(View var1, int var2, int var3, int var4, int var5, int var6, ClipboardAssistance var7) {
         return 0;
      }

      public void handleBeginTouchEvent(View var1, long var2, int var4, boolean var5, int var6) {
      }

      public void handleNextTouchEvent(View var1, long var2, int var4, long var5, int var7, int var8, int var9, int var10) {
      }

      public void handleEndTouchEvent(View var1, long var2) {
      }

      public void handleScrollGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, int var12, double var13, double var15, double var17, double var19, double var21, double var23) {
      }

      public void handleZoomGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, double var12, double var14, double var16, double var18) {
      }

      public void handleRotateGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, double var12, double var14) {
      }

      public void handleSwipeGestureEvent(View var1, long var2, int var4, int var5, boolean var6, boolean var7, int var8, int var9, int var10, int var11, int var12, int var13) {
      }

      public Accessible getSceneAccessible() {
         return null;
      }
   }
}
