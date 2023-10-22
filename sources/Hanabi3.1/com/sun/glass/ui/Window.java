package com.sun.glass.ui;

import com.sun.prism.impl.PrismSettings;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Window {
   private long ptr;
   private volatile long delegatePtr = 0L;
   private static final LinkedList visibleWindows = new LinkedList();
   public static final int UNTITLED = 0;
   public static final int TITLED = 1;
   public static final int TRANSPARENT = 2;
   public static final int NORMAL = 0;
   public static final int UTILITY = 4;
   public static final int POPUP = 8;
   public static final int CLOSABLE = 16;
   public static final int MINIMIZABLE = 32;
   public static final int MAXIMIZABLE = 64;
   public static final int RIGHT_TO_LEFT = 128;
   public static final int UNIFIED = 256;
   private final Window owner;
   private final long parent;
   private final int styleMask;
   private final boolean isDecorated;
   private boolean shouldStartUndecoratedMove = false;
   protected View view = null;
   protected Screen screen = null;
   private MenuBar menubar = null;
   private String title = "";
   private UndecoratedMoveResizeHelper helper = null;
   private int state = 1;
   private int level = 1;
   protected int x = 0;
   protected int y = 0;
   protected int width = 0;
   protected int height = 0;
   private float alpha = 1.0F;
   protected float platformScaleX = 1.0F;
   protected float platformScaleY = 1.0F;
   private float outputScaleX = 1.0F;
   private float outputScaleY = 1.0F;
   private float renderScaleX = 1.0F;
   private float renderScaleY = 1.0F;
   private Timer embeddedLocationTimer = null;
   private int lastKnownEmbeddedX = 0;
   private int lastKnownEmbeddedY = 0;
   private volatile boolean isResizable = false;
   private volatile boolean isVisible = false;
   private volatile boolean isFocused = false;
   private volatile boolean isFocusable = true;
   private volatile boolean isModal = false;
   private volatile int disableCount = 0;
   private int minimumWidth = 0;
   private int minimumHeight = 0;
   private int maximumWidth = Integer.MAX_VALUE;
   private int maximumHeight = Integer.MAX_VALUE;
   private EventHandler eventHandler;
   private static volatile Window focusedWindow = null;

   public static synchronized List getWindows() {
      Application.checkEventThread();
      return Collections.unmodifiableList(visibleWindows);
   }

   public static List getWindowsClone() {
      Application.checkEventThread();
      return (List)visibleWindows.clone();
   }

   protected static void add(Window var0) {
      visibleWindows.add(var0);
   }

   protected static void addFirst(Window var0) {
      visibleWindows.addFirst(var0);
   }

   protected static void remove(Window var0) {
      visibleWindows.remove(var0);
   }

   protected abstract long _createWindow(long var1, long var3, int var5);

   protected Window(Window var1, Screen var2, int var3) {
      Application.checkEventThread();
      switch (var3 & 3) {
         case 0:
         case 1:
         case 2:
            switch (var3 & 12) {
               case 0:
               case 4:
               case 8:
                  if ((var3 & 256) != 0 && !Application.GetApplication().supportsUnifiedWindows()) {
                     var3 &= -257;
                  }

                  if ((var3 & 2) != 0 && !Application.GetApplication().supportsTransparentWindows()) {
                     var3 &= -3;
                  }

                  this.owner = var1;
                  this.parent = 0L;
                  this.styleMask = var3;
                  this.isDecorated = (this.styleMask & 1) != 0;
                  this.screen = var2 != null ? var2 : Screen.getMainScreen();
                  if (PrismSettings.allowHiDPIScaling) {
                     this.platformScaleX = this.screen.getPlatformScaleX();
                     this.platformScaleY = this.screen.getPlatformScaleY();
                     this.outputScaleX = this.screen.getRecommendedOutputScaleX();
                     this.outputScaleY = this.screen.getRecommendedOutputScaleY();
                  }

                  this.ptr = this._createWindow(var1 != null ? var1.getNativeHandle() : 0L, this.screen.getNativeScreen(), this.styleMask);
                  if (this.ptr == 0L) {
                     throw new RuntimeException("could not create platform window");
                  }

                  return;
               default:
                  throw new RuntimeException("The functional type should be NORMAL, POPUP, or UTILITY, but not a combination of these");
            }
         default:
            throw new RuntimeException("The visual kind should be UNTITLED, TITLED, or TRANSPARENT, but not a combination of these");
      }
   }

   protected abstract long _createChildWindow(long var1);

   protected Window(long var1) {
      Application.checkEventThread();
      this.owner = null;
      this.parent = var1;
      this.styleMask = 0;
      this.isDecorated = false;
      this.screen = null;
      this.ptr = this._createChildWindow(var1);
      if (this.ptr == 0L) {
         throw new RuntimeException("could not create platform window");
      } else {
         if (this.screen == null) {
            this.screen = Screen.getMainScreen();
         }

      }
   }

   public boolean isClosed() {
      Application.checkEventThread();
      return this.ptr == 0L;
   }

   private void checkNotClosed() {
      if (this.ptr == 0L) {
         throw new IllegalStateException("The window has already been closed");
      }
   }

   protected abstract boolean _close(long var1);

   public void close() {
      Application.checkEventThread();
      if (this.view != null) {
         if (this.ptr != 0L) {
            this._setView(this.ptr, (View)null);
         }

         this.view.setWindow((Window)null);
         this.view.close();
         this.view = null;
      }

      if (this.ptr != 0L) {
         this._close(this.ptr);
      }

   }

   private boolean isChild() {
      Application.checkEventThread();
      return this.parent != 0L;
   }

   public long getNativeWindow() {
      Application.checkEventThread();
      this.checkNotClosed();
      return this.delegatePtr != 0L ? this.delegatePtr : this.ptr;
   }

   public long getNativeHandle() {
      Application.checkEventThread();
      return this.delegatePtr != 0L ? this.delegatePtr : this.ptr;
   }

   public long getRawHandle() {
      return this.ptr;
   }

   public Window getOwner() {
      Application.checkEventThread();
      return this.owner;
   }

   public View getView() {
      Application.checkEventThread();
      return this.view;
   }

   protected abstract boolean _setView(long var1, View var3);

   public void setView(View var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      View var2 = this.getView();
      if (var2 != var1) {
         if (var2 != null) {
            var2.setWindow((Window)null);
         }

         if (var1 != null) {
            Window var3 = var1.getWindow();
            if (var3 != null) {
               var3.setView((View)null);
            }
         }

         if (var1 != null && this._setView(this.ptr, var1)) {
            this.view = var1;
            this.view.setWindow(this);
            if (!this.isDecorated) {
               this.helper = new UndecoratedMoveResizeHelper();
            }
         } else {
            this._setView(this.ptr, (View)null);
            this.view = null;
         }

      }
   }

   public Screen getScreen() {
      Application.checkEventThread();
      return this.screen;
   }

   protected void setScreen(Screen var1) {
      Application.checkEventThread();
      Screen var2 = this.screen;
      this.screen = var1;
      if (this.eventHandler != null && (var2 == null && this.screen != null || var2 != null && !var2.equals(this.screen))) {
         this.eventHandler.handleScreenChangedEvent(this, System.nanoTime(), var2, this.screen);
      }

   }

   public int getStyleMask() {
      Application.checkEventThread();
      return this.styleMask;
   }

   public MenuBar getMenuBar() {
      Application.checkEventThread();
      return this.menubar;
   }

   protected abstract boolean _setMenubar(long var1, long var3);

   public void setMenuBar(MenuBar var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (this._setMenubar(this.ptr, var1.getNativeMenu())) {
         this.menubar = var1;
      }

   }

   public boolean isDecorated() {
      Application.checkEventThread();
      return this.isDecorated;
   }

   public boolean isMinimized() {
      Application.checkEventThread();
      return this.state == 2;
   }

   protected abstract boolean _minimize(long var1, boolean var3);

   public boolean minimize(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._minimize(this.ptr, var1);
      return this.isMinimized();
   }

   public boolean isMaximized() {
      Application.checkEventThread();
      return this.state == 3;
   }

   protected abstract boolean _maximize(long var1, boolean var3, boolean var4);

   public boolean maximize(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._maximize(this.ptr, var1, this.isMaximized());
      return this.isMaximized();
   }

   protected void notifyScaleChanged(float var1, float var2, float var3, float var4) {
      if (PrismSettings.allowHiDPIScaling) {
         this.platformScaleX = var1;
         this.platformScaleY = var2;
         this.outputScaleX = var3;
         this.outputScaleY = var4;
         this.notifyRescale();
      }
   }

   public final float getPlatformScaleX() {
      return this.platformScaleX;
   }

   public final float getPlatformScaleY() {
      return this.platformScaleY;
   }

   public void setRenderScaleX(float var1) {
      if (PrismSettings.allowHiDPIScaling) {
         this.renderScaleX = var1;
      }
   }

   public void setRenderScaleY(float var1) {
      if (PrismSettings.allowHiDPIScaling) {
         this.renderScaleY = var1;
      }
   }

   public final float getRenderScaleX() {
      return this.renderScaleX;
   }

   public final float getRenderScaleY() {
      return this.renderScaleY;
   }

   public float getOutputScaleX() {
      return this.outputScaleX;
   }

   public float getOutputScaleY() {
      return this.outputScaleY;
   }

   protected abstract int _getEmbeddedX(long var1);

   protected abstract int _getEmbeddedY(long var1);

   private void checkScreenLocation() {
      this.x = this._getEmbeddedX(this.ptr);
      this.y = this._getEmbeddedY(this.ptr);
      if (this.x != this.lastKnownEmbeddedX || this.y != this.lastKnownEmbeddedY) {
         this.lastKnownEmbeddedX = this.x;
         this.lastKnownEmbeddedY = this.y;
         this.handleWindowEvent(System.nanoTime(), 512);
      }

   }

   public int getX() {
      Application.checkEventThread();
      return this.x;
   }

   public int getY() {
      Application.checkEventThread();
      return this.y;
   }

   public int getWidth() {
      Application.checkEventThread();
      return this.width;
   }

   public int getHeight() {
      Application.checkEventThread();
      return this.height;
   }

   protected abstract void _setBounds(long var1, int var3, int var4, boolean var5, boolean var6, int var7, int var8, int var9, int var10, float var11, float var12);

   public void setBounds(float var1, float var2, boolean var3, boolean var4, float var5, float var6, float var7, float var8, float var9, float var10) {
      Application.checkEventThread();
      this.checkNotClosed();
      float var11 = this.platformScaleX;
      float var12 = this.platformScaleY;
      int var13 = this.screen.getPlatformX() + Math.round((var1 - (float)this.screen.getX()) * var11);
      int var14 = this.screen.getPlatformY() + Math.round((var2 - (float)this.screen.getY()) * var12);
      int var15 = (int)(var5 > 0.0F ? Math.ceil((double)(var5 * var11)) : (double)var5);
      int var16 = (int)(var6 > 0.0F ? Math.ceil((double)(var6 * var12)) : (double)var6);
      int var17 = (int)(var7 > 0.0F ? Math.ceil((double)(var7 * var11)) : (double)var7);
      int var18 = (int)(var8 > 0.0F ? Math.ceil((double)(var8 * var12)) : (double)var8);
      this._setBounds(this.ptr, var13, var14, var3, var4, var15, var16, var17, var18, var9, var10);
   }

   public void setPosition(int var1, int var2) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._setBounds(this.ptr, var1, var2, true, true, 0, 0, 0, 0, 0.0F, 0.0F);
   }

   public void setSize(int var1, int var2) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._setBounds(this.ptr, 0, 0, false, false, var1, var2, 0, 0, 0.0F, 0.0F);
   }

   public void setContentSize(int var1, int var2) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._setBounds(this.ptr, 0, 0, false, false, 0, 0, var1, var2, 0.0F, 0.0F);
   }

   public boolean isVisible() {
      Application.checkEventThread();
      return this.isVisible;
   }

   private void synthesizeViewMoveEvent() {
      View var1 = this.getView();
      if (var1 != null) {
         var1.notifyView(423);
      }

   }

   protected abstract boolean _setVisible(long var1, boolean var3);

   public void setVisible(boolean var1) {
      Application.checkEventThread();
      if (this.isVisible != var1) {
         if (!var1) {
            if (this.getView() != null) {
               this.getView().setVisible(var1);
            }

            if (this.ptr != 0L) {
               this.isVisible = this._setVisible(this.ptr, var1);
            } else {
               this.isVisible = var1;
            }

            remove(this);
            if (this.parent != 0L) {
               this.embeddedLocationTimer.stop();
            }
         } else {
            this.checkNotClosed();
            this.isVisible = this._setVisible(this.ptr, var1);
            if (this.getView() != null) {
               this.getView().setVisible(this.isVisible);
            }

            add(this);
            if (this.parent != 0L) {
               Runnable var2 = () -> {
                  this.checkScreenLocation();
               };
               Runnable var3 = () -> {
                  Application.invokeLater(var2);
               };
               this.embeddedLocationTimer = Application.GetApplication().createTimer(var3);
               this.embeddedLocationTimer.start(16);
            }

            this.synthesizeViewMoveEvent();
         }
      }

   }

   protected abstract boolean _setResizable(long var1, boolean var3);

   public boolean setResizable(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (this.isResizable != var1 && this._setResizable(this.ptr, var1)) {
         this.isResizable = var1;
         this.synthesizeViewMoveEvent();
      }

      return this.isResizable;
   }

   public boolean isResizable() {
      Application.checkEventThread();
      return this.isResizable;
   }

   public boolean isUnifiedWindow() {
      return (this.styleMask & 256) != 0;
   }

   public boolean isTransparentWindow() {
      return (this.styleMask & 2) != 0;
   }

   public static Window getFocusedWindow() {
      Application.checkEventThread();
      return focusedWindow;
   }

   private static void setFocusedWindow(Window var0) {
      focusedWindow = var0;
   }

   public boolean isFocused() {
      Application.checkEventThread();
      return this.isFocused;
   }

   protected abstract boolean _requestFocus(long var1, int var3);

   public boolean requestFocus(int var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (!this.isChild() && var1 != 542) {
         throw new IllegalArgumentException("Invalid focus event ID for top-level window");
      } else if (this.isChild() && (var1 < 541 || var1 > 544)) {
         throw new IllegalArgumentException("Invalid focus event ID for child window");
      } else if (var1 == 541 && !this.isFocused()) {
         return true;
      } else {
         return !this.isFocusable ? false : this._requestFocus(this.ptr, var1);
      }
   }

   public boolean requestFocus() {
      Application.checkEventThread();
      return this.requestFocus(542);
   }

   protected abstract void _setFocusable(long var1, boolean var3);

   public void setFocusable(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this.isFocusable = var1;
      if (this.isEnabled()) {
         this._setFocusable(this.ptr, var1);
      }

   }

   protected abstract boolean _grabFocus(long var1);

   protected abstract void _ungrabFocus(long var1);

   public boolean grabFocus() {
      Application.checkEventThread();
      this.checkNotClosed();
      if (!this.isFocused()) {
         throw new IllegalStateException("The window must be focused when calling grabFocus()");
      } else {
         return this._grabFocus(this.ptr);
      }
   }

   public void ungrabFocus() {
      Application.checkEventThread();
      this.checkNotClosed();
      this._ungrabFocus(this.ptr);
   }

   public String getTitle() {
      Application.checkEventThread();
      return this.title;
   }

   protected abstract boolean _setTitle(long var1, String var3);

   public void setTitle(String var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (var1 == null) {
         var1 = "";
      }

      if (!var1.equals(this.title) && this._setTitle(this.ptr, var1)) {
         this.title = var1;
      }

   }

   protected abstract void _setLevel(long var1, int var3);

   public void setLevel(int var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (var1 >= 1 && var1 <= 3) {
         if (this.level != var1) {
            this._setLevel(this.ptr, var1);
            this.level = var1;
         }

      } else {
         throw new IllegalArgumentException("Level should be in the range [1..3]");
      }
   }

   public int getLevel() {
      Application.checkEventThread();
      return this.level;
   }

   private boolean isInFullscreen() {
      View var1 = this.getView();
      return var1 == null ? false : var1.isInFullscreen();
   }

   void notifyFullscreen(boolean var1) {
      float var2 = this.getAlpha();
      if (var2 < 1.0F) {
         if (var1) {
            this._setAlpha(this.ptr, 1.0F);
         } else {
            this.setAlpha(var2);
         }
      }

   }

   protected abstract void _setAlpha(long var1, float var3);

   public void setAlpha(float var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (!(var1 < 0.0F) && !(var1 > 1.0F)) {
         this.alpha = var1;
         if (!(var1 < 1.0F) || !this.isInFullscreen()) {
            this._setAlpha(this.ptr, this.alpha);
         }
      } else {
         throw new IllegalArgumentException("Alpha should be in the range [0f..1f]");
      }
   }

   public float getAlpha() {
      Application.checkEventThread();
      return this.alpha;
   }

   protected abstract boolean _setBackground(long var1, float var3, float var4, float var5);

   public boolean setBackground(float var1, float var2, float var3) {
      Application.checkEventThread();
      this.checkNotClosed();
      return this._setBackground(this.ptr, var1, var2, var3);
   }

   public boolean isEnabled() {
      Application.checkEventThread();
      return this.disableCount == 0;
   }

   protected abstract void _setEnabled(long var1, boolean var3);

   public void setEnabled(boolean var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      if (!var1) {
         if (++this.disableCount > 1) {
            return;
         }
      } else {
         if (this.disableCount == 0) {
            return;
         }

         if (--this.disableCount > 0) {
            return;
         }
      }

      this._setEnabled(this.ptr, this.isEnabled());
   }

   public int getMinimumWidth() {
      Application.checkEventThread();
      return this.minimumWidth;
   }

   public int getMinimumHeight() {
      Application.checkEventThread();
      return this.minimumHeight;
   }

   public int getMaximumWidth() {
      Application.checkEventThread();
      return this.maximumWidth;
   }

   public int getMaximumHeight() {
      Application.checkEventThread();
      return this.maximumHeight;
   }

   protected abstract boolean _setMinimumSize(long var1, int var3, int var4);

   public void setMinimumSize(int var1, int var2) {
      Application.checkEventThread();
      if (var1 >= 0 && var2 >= 0) {
         this.checkNotClosed();
         if (this._setMinimumSize(this.ptr, var1, var2)) {
            this.minimumWidth = var1;
            this.minimumHeight = var2;
         }

      } else {
         throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + var1 + "; height=" + var2);
      }
   }

   protected abstract boolean _setMaximumSize(long var1, int var3, int var4);

   public void setMaximumSize(int var1, int var2) {
      Application.checkEventThread();
      if (var1 >= 0 && var2 >= 0) {
         this.checkNotClosed();
         if (this._setMaximumSize(this.ptr, var1 == Integer.MAX_VALUE ? -1 : var1, var2 == Integer.MAX_VALUE ? -1 : var2)) {
            this.maximumWidth = var1;
            this.maximumHeight = var2;
         }

      } else {
         throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + var1 + "; height=" + var2);
      }
   }

   protected abstract void _setIcon(long var1, Pixels var3);

   public void setIcon(Pixels var1) {
      Application.checkEventThread();
      this.checkNotClosed();
      this._setIcon(this.ptr, var1);
   }

   protected abstract void _setCursor(long var1, Cursor var3);

   public void setCursor(Cursor var1) {
      Application.checkEventThread();
      this._setCursor(this.ptr, var1);
   }

   protected abstract void _toFront(long var1);

   public void toFront() {
      Application.checkEventThread();
      this.checkNotClosed();
      this._toFront(this.ptr);
   }

   protected abstract void _toBack(long var1);

   public void toBack() {
      Application.checkEventThread();
      this.checkNotClosed();
      this._toBack(this.ptr);
   }

   protected abstract void _enterModal(long var1);

   public void enterModal() {
      this.checkNotClosed();
      if (!this.isModal) {
         this.isModal = true;
         this._enterModal(this.ptr);
      }

   }

   protected abstract void _enterModalWithWindow(long var1, long var3);

   public void enterModal(Window var1) {
      this.checkNotClosed();
      if (!this.isModal) {
         this.isModal = true;
         this._enterModalWithWindow(this.ptr, var1.getNativeHandle());
      }

   }

   protected abstract void _exitModal(long var1);

   public void exitModal() {
      this.checkNotClosed();
      if (this.isModal) {
         this._exitModal(this.ptr);
         this.isModal = false;
      }

   }

   public boolean isModal() {
      return this.isModal;
   }

   public void dispatchNpapiEvent(Map var1) {
      Application.checkEventThread();
      throw new RuntimeException("This operation is not supported on this platform");
   }

   public EventHandler getEventHandler() {
      Application.checkEventThread();
      return this.eventHandler;
   }

   public void setEventHandler(EventHandler var1) {
      Application.checkEventThread();
      this.eventHandler = var1;
   }

   public void setShouldStartUndecoratedMove(boolean var1) {
      Application.checkEventThread();
      this.shouldStartUndecoratedMove = var1;
   }

   protected void notifyClose() {
      this.handleWindowEvent(System.nanoTime(), 521);
   }

   protected void notifyDestroy() {
      if (this.ptr != 0L) {
         this.handleWindowEvent(System.nanoTime(), 522);
         this.ptr = 0L;
         this.setVisible(false);
      }
   }

   protected void notifyMove(int var1, int var2) {
      this.x = var1;
      this.y = var2;
      this.handleWindowEvent(System.nanoTime(), 512);
   }

   protected void notifyRescale() {
      this.handleWindowEvent(System.nanoTime(), 513);
   }

   protected void notifyMoveToAnotherScreen(Screen var1) {
      this.setScreen(var1);
   }

   protected void setState(int var1) {
      this.state = var1;
   }

   protected void notifyResize(int var1, int var2, int var3) {
      if (var1 == 531) {
         this.state = 2;
      } else {
         if (var1 == 532) {
            this.state = 3;
         } else {
            this.state = 1;
         }

         this.width = var2;
         this.height = var3;
         if (this.helper != null) {
            this.helper.updateRectangles();
         }
      }

      this.handleWindowEvent(System.nanoTime(), var1);
      if (var1 == 532 || var1 == 533) {
         this.handleWindowEvent(System.nanoTime(), 511);
      }

   }

   protected void notifyFocus(int var1) {
      boolean var2 = var1 != 541;
      if (this.isFocused != var2) {
         this.isFocused = var2;
         if (this.isFocused) {
            setFocusedWindow(this);
         } else {
            setFocusedWindow((Window)null);
         }

         this.handleWindowEvent(System.nanoTime(), var1);
      }

   }

   protected void notifyFocusDisabled() {
      this.handleWindowEvent(System.nanoTime(), 545);
   }

   protected void notifyFocusUngrab() {
      this.handleWindowEvent(System.nanoTime(), 546);
   }

   protected void notifyDelegatePtr(long var1) {
      this.delegatePtr = var1;
   }

   protected void handleWindowEvent(long var1, int var3) {
      if (this.eventHandler != null) {
         this.eventHandler.handleWindowEvent(this, var1, var3);
      }

   }

   public void setUndecoratedMoveRectangle(int var1) {
      Application.checkEventThread();
      if (this.isDecorated) {
         System.err.println("Glass Window.setUndecoratedMoveRectangle is only valid for Undecorated Window. In the future this will be hard error.");
         Thread.dumpStack();
      } else {
         if (this.helper != null) {
            this.helper.setMoveRectangle(var1);
         }

      }
   }

   public boolean shouldStartUndecoratedMove(int var1, int var2) {
      Application.checkEventThread();
      if (this.shouldStartUndecoratedMove) {
         return true;
      } else if (this.isDecorated) {
         return false;
      } else {
         return this.helper != null ? this.helper.shouldStartMove(var1, var2) : false;
      }
   }

   public void setUndecoratedResizeRectangle(int var1) {
      Application.checkEventThread();
      if (!this.isDecorated && this.isResizable) {
         if (this.helper != null) {
            this.helper.setResizeRectangle(var1);
         }

      } else {
         System.err.println("Glass Window.setUndecoratedResizeRectangle is only valid for Undecorated Resizable Window. In the future this will be hard error.");
         Thread.dumpStack();
      }
   }

   public boolean shouldStartUndecoratedResize(int var1, int var2) {
      Application.checkEventThread();
      if (!this.isDecorated && this.isResizable) {
         return this.helper != null ? this.helper.shouldStartResize(var1, var2) : false;
      } else {
         return false;
      }
   }

   boolean handleMouseEvent(int var1, int var2, int var3, int var4, int var5, int var6) {
      return !this.isDecorated ? this.helper.handleMouseEvent(var1, var2, var3, var4, var5, var6) : false;
   }

   public String toString() {
      Application.checkEventThread();
      return "Window:\n    ptr: " + this.getNativeWindow() + "\n    screen ptr: " + (this.screen != null ? this.screen.getNativeScreen() : "null") + "\n    isDecorated: " + this.isDecorated() + "\n    title: " + this.getTitle() + "\n    visible: " + this.isVisible() + "\n    focused: " + this.isFocused() + "\n    modal: " + this.isModal() + "\n    state: " + this.state + "\n    x: " + this.getX() + ", y: " + this.getY() + ", w: " + this.getWidth() + ", h: " + this.getHeight() + "\n";
   }

   protected void notifyLevelChanged(int var1) {
      this.level = var1;
      if (this.eventHandler != null) {
         this.eventHandler.handleLevelEvent(var1);
      }

   }

   public void requestInput(String var1, int var2, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29) {
      Application.checkEventThread();
      this._requestInput(this.ptr, var1, var2, var3, var5, var7, var9, var11, var13, var15, var17, var19, var21, var23, var25, var27, var29);
   }

   public void releaseInput() {
      Application.checkEventThread();
      this._releaseInput(this.ptr);
   }

   protected abstract void _requestInput(long var1, String var3, int var4, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31);

   protected abstract void _releaseInput(long var1);

   private class UndecoratedMoveResizeHelper {
      TrackingRectangle moveRect = null;
      TrackingRectangle resizeRect = null;
      boolean inMove = false;
      boolean inResize = false;
      int startMouseX;
      int startMouseY;
      int startX;
      int startY;
      int startWidth;
      int startHeight;

      UndecoratedMoveResizeHelper() {
         this.moveRect = new TrackingRectangle();
         this.resizeRect = new TrackingRectangle();
      }

      void setMoveRectangle(int var1) {
         this.moveRect.size = var1;
         this.moveRect.x = 0;
         this.moveRect.y = 0;
         this.moveRect.width = Window.this.getWidth();
         this.moveRect.height = this.moveRect.size;
      }

      boolean shouldStartMove(int var1, int var2) {
         return this.moveRect.contains(var1, var2);
      }

      boolean inMove() {
         return this.inMove;
      }

      void startMove(int var1, int var2) {
         this.inMove = true;
         this.startMouseX = var1;
         this.startMouseY = var2;
         this.startX = Window.this.getX();
         this.startY = Window.this.getY();
      }

      void deltaMove(int var1, int var2) {
         int var3 = var1 - this.startMouseX;
         int var4 = var2 - this.startMouseY;
         Window.this.setPosition(this.startX + var3, this.startY + var4);
      }

      void stopMove() {
         this.inMove = false;
      }

      void setResizeRectangle(int var1) {
         this.resizeRect.size = var1;
         this.resizeRect.x = Window.this.getWidth() - this.resizeRect.size;
         this.resizeRect.y = Window.this.getHeight() - this.resizeRect.size;
         this.resizeRect.width = this.resizeRect.size;
         this.resizeRect.height = this.resizeRect.size;
      }

      boolean shouldStartResize(int var1, int var2) {
         return this.resizeRect.contains(var1, var2);
      }

      boolean inResize() {
         return this.inResize;
      }

      void startResize(int var1, int var2) {
         this.inResize = true;
         this.startMouseX = var1;
         this.startMouseY = var2;
         this.startWidth = Window.this.getWidth();
         this.startHeight = Window.this.getHeight();
      }

      void deltaResize(int var1, int var2) {
         int var3 = var1 - this.startMouseX;
         int var4 = var2 - this.startMouseY;
         Window.this.setSize(this.startWidth + var3, this.startHeight + var4);
      }

      protected void stopResize() {
         this.inResize = false;
      }

      void updateRectangles() {
         if (this.moveRect.size > 0) {
            this.setMoveRectangle(this.moveRect.size);
         }

         if (this.resizeRect.size > 0) {
            this.setResizeRectangle(this.resizeRect.size);
         }

      }

      boolean handleMouseEvent(int var1, int var2, int var3, int var4, int var5, int var6) {
         switch (var1) {
            case 221:
               if (var2 == 212) {
                  if (Window.this.shouldStartUndecoratedMove(var3, var4)) {
                     this.startMove(var5, var6);
                     return true;
                  }

                  if (Window.this.shouldStartUndecoratedResize(var3, var4)) {
                     this.startResize(var5, var6);
                     return true;
                  }
               }
               break;
            case 222:
               boolean var7 = this.inMove() || this.inResize();
               this.stopResize();
               this.stopMove();
               return var7;
            case 223:
            case 224:
               if (this.inMove()) {
                  this.deltaMove(var5, var6);
                  return true;
               }

               if (this.inResize()) {
                  this.deltaResize(var5, var6);
                  return true;
               }
         }

         return false;
      }
   }

   private static class TrackingRectangle {
      int size;
      int x;
      int y;
      int width;
      int height;

      private TrackingRectangle() {
         this.size = 0;
         this.x = 0;
         this.y = 0;
         this.width = 0;
         this.height = 0;
      }

      boolean contains(int var1, int var2) {
         return this.size > 0 && var1 >= this.x && var1 < this.x + this.width && var2 >= this.y && var2 < this.y + this.height;
      }

      // $FF: synthetic method
      TrackingRectangle(Object var1) {
         this();
      }
   }

   public static final class Level {
      private static final int _MIN = 1;
      public static final int NORMAL = 1;
      public static final int FLOATING = 2;
      public static final int TOPMOST = 3;
      private static final int _MAX = 3;
   }

   public static final class State {
      public static final int NORMAL = 1;
      public static final int MINIMIZED = 2;
      public static final int MAXIMIZED = 3;
   }

   public static class EventHandler {
      public void handleWindowEvent(Window var1, long var2, int var4) {
      }

      public void handleScreenChangedEvent(Window var1, long var2, Screen var4, Screen var5) {
      }

      public void handleLevelEvent(int var1) {
      }
   }
}
