/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.prism.impl.PrismSettings;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Window {
    private long ptr;
    private volatile long delegatePtr = 0L;
    private static final LinkedList<Window> visibleWindows = new LinkedList();
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
    public static final int MODAL = 512;
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
    private float alpha = 1.0f;
    protected float platformScaleX = 1.0f;
    protected float platformScaleY = 1.0f;
    private float outputScaleX = 1.0f;
    private float outputScaleY = 1.0f;
    private float renderScaleX = 1.0f;
    private float renderScaleY = 1.0f;
    private boolean appletMode = false;
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

    public static synchronized List<Window> getWindows() {
        Application.checkEventThread();
        return Collections.unmodifiableList(visibleWindows);
    }

    public static List<Window> getWindowsClone() {
        Application.checkEventThread();
        return (List)visibleWindows.clone();
    }

    protected static void add(Window window) {
        visibleWindows.add(window);
    }

    protected static void addFirst(Window window) {
        visibleWindows.addFirst(window);
    }

    protected static void remove(Window window) {
        visibleWindows.remove(window);
    }

    protected abstract long _createWindow(long var1, long var3, int var5);

    protected Window(Window window, Screen screen, int n) {
        Application.checkEventThread();
        switch (n & 3) {
            case 0: 
            case 1: 
            case 2: {
                break;
            }
            default: {
                throw new RuntimeException("The visual kind should be UNTITLED, TITLED, or TRANSPARENT, but not a combination of these");
            }
        }
        switch (n & 0xC) {
            case 0: 
            case 4: 
            case 8: {
                break;
            }
            default: {
                throw new RuntimeException("The functional type should be NORMAL, POPUP, or UTILITY, but not a combination of these");
            }
        }
        if ((n & 0x100) != 0 && !Application.GetApplication().supportsUnifiedWindows()) {
            n &= 0xFFFFFEFF;
        }
        if ((n & 2) != 0 && !Application.GetApplication().supportsTransparentWindows()) {
            n &= 0xFFFFFFFD;
        }
        this.owner = window;
        this.parent = 0L;
        this.styleMask = n;
        this.isDecorated = (this.styleMask & 1) != 0;
        Screen screen2 = this.screen = screen != null ? screen : Screen.getMainScreen();
        if (PrismSettings.allowHiDPIScaling) {
            this.platformScaleX = this.screen.getPlatformScaleX();
            this.platformScaleY = this.screen.getPlatformScaleY();
            this.outputScaleX = this.screen.getRecommendedOutputScaleX();
            this.outputScaleY = this.screen.getRecommendedOutputScaleY();
        }
        this.ptr = this._createWindow(window != null ? window.getNativeHandle() : 0L, this.screen.getNativeScreen(), this.styleMask);
        if (this.ptr == 0L) {
            throw new RuntimeException("could not create platform window");
        }
    }

    protected abstract long _createChildWindow(long var1);

    protected Window(long l) {
        Application.checkEventThread();
        this.owner = null;
        this.parent = l;
        this.styleMask = 0;
        this.isDecorated = false;
        this.screen = null;
        this.ptr = this._createChildWindow(l);
        if (this.ptr == 0L) {
            throw new RuntimeException("could not create platform window");
        }
        if (this.screen == null) {
            this.screen = Screen.getMainScreen();
            if (PrismSettings.allowHiDPIScaling) {
                this.platformScaleX = this.screen.getPlatformScaleX();
                this.platformScaleY = this.screen.getPlatformScaleY();
                this.outputScaleX = this.screen.getRecommendedOutputScaleX();
                this.outputScaleY = this.screen.getRecommendedOutputScaleY();
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
                this._setView(this.ptr, null);
            }
            this.view.setWindow(null);
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

    public void setView(View view) {
        Window window;
        Application.checkEventThread();
        this.checkNotClosed();
        View view2 = this.getView();
        if (view2 == view) {
            return;
        }
        if (view2 != null) {
            view2.setWindow(null);
        }
        if (view != null && (window = view.getWindow()) != null) {
            window.setView(null);
        }
        if (view != null && this._setView(this.ptr, view)) {
            this.view = view;
            this.view.setWindow(this);
            if (!this.isDecorated) {
                this.helper = new UndecoratedMoveResizeHelper();
            }
        } else {
            this._setView(this.ptr, null);
            this.view = null;
        }
    }

    public Screen getScreen() {
        Application.checkEventThread();
        return this.screen;
    }

    protected void setScreen(Screen screen) {
        Application.checkEventThread();
        Screen screen2 = this.screen;
        this.screen = screen;
        if (this.eventHandler != null && (screen2 == null && this.screen != null || screen2 != null && !screen2.equals(this.screen))) {
            this.eventHandler.handleScreenChangedEvent(this, System.nanoTime(), screen2, this.screen);
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

    public void setMenuBar(MenuBar menuBar) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (this._setMenubar(this.ptr, menuBar.getNativeMenu())) {
            this.menubar = menuBar;
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

    public boolean minimize(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._minimize(this.ptr, bl);
        return this.isMinimized();
    }

    public boolean isMaximized() {
        Application.checkEventThread();
        return this.state == 3;
    }

    protected abstract boolean _maximize(long var1, boolean var3, boolean var4);

    public boolean maximize(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._maximize(this.ptr, bl, this.isMaximized());
        return this.isMaximized();
    }

    protected void notifyScaleChanged(float f, float f2, float f3, float f4) {
        if (!PrismSettings.allowHiDPIScaling) {
            return;
        }
        this.platformScaleX = f;
        this.platformScaleY = f2;
        this.outputScaleX = f3;
        this.outputScaleY = f4;
        this.notifyRescale();
    }

    public final float getPlatformScaleX() {
        return this.platformScaleX;
    }

    public final float getPlatformScaleY() {
        return this.platformScaleY;
    }

    public void setRenderScaleX(float f) {
        if (!PrismSettings.allowHiDPIScaling) {
            return;
        }
        this.renderScaleX = f;
    }

    public void setRenderScaleY(float f) {
        if (!PrismSettings.allowHiDPIScaling) {
            return;
        }
        this.renderScaleY = f;
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

    public void setBounds(float f, float f2, boolean bl, boolean bl2, float f3, float f4, float f5, float f6, float f7, float f8) {
        Application.checkEventThread();
        this.checkNotClosed();
        float f9 = this.platformScaleX;
        float f10 = this.platformScaleY;
        int n = this.screen.getPlatformX() + Math.round((f - (float)this.screen.getX()) * f9);
        int n2 = this.screen.getPlatformY() + Math.round((f2 - (float)this.screen.getY()) * f10);
        int n3 = (int)(f3 > 0.0f ? Math.ceil(f3 * f9) : (double)f3);
        int n4 = (int)(f4 > 0.0f ? Math.ceil(f4 * f10) : (double)f4);
        int n5 = (int)(f5 > 0.0f ? Math.ceil(f5 * f9) : (double)f5);
        int n6 = (int)(f6 > 0.0f ? Math.ceil(f6 * f10) : (double)f6);
        this._setBounds(this.ptr, n, n2, bl, bl2, n3, n4, n5, n6, f7, f8);
    }

    public void setPosition(int n, int n2) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._setBounds(this.ptr, n, n2, true, true, 0, 0, 0, 0, 0.0f, 0.0f);
    }

    public void setSize(int n, int n2) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._setBounds(this.ptr, 0, 0, false, false, n, n2, 0, 0, 0.0f, 0.0f);
    }

    public void setContentSize(int n, int n2) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._setBounds(this.ptr, 0, 0, false, false, 0, 0, n, n2, 0.0f, 0.0f);
    }

    public boolean isVisible() {
        Application.checkEventThread();
        return this.isVisible;
    }

    private void synthesizeViewMoveEvent() {
        View view = this.getView();
        if (view != null) {
            view.notifyView(423);
        }
    }

    protected abstract boolean _setVisible(long var1, boolean var3);

    public void setVisible(boolean bl) {
        Application.checkEventThread();
        if (this.isVisible != bl) {
            if (!bl) {
                if (this.getView() != null) {
                    this.getView().setVisible(bl);
                }
                this.isVisible = this.ptr != 0L ? this._setVisible(this.ptr, bl) : bl;
                Window.remove(this);
                if (this.parent != 0L) {
                    this.embeddedLocationTimer.stop();
                }
            } else {
                this.checkNotClosed();
                this.isVisible = this._setVisible(this.ptr, bl);
                if (this.getView() != null) {
                    this.getView().setVisible(this.isVisible);
                }
                Window.add(this);
                if (this.parent != 0L) {
                    Runnable runnable = () -> this.checkScreenLocation();
                    Runnable runnable2 = () -> Application.invokeLater(runnable);
                    this.embeddedLocationTimer = Application.GetApplication().createTimer(runnable2);
                    this.embeddedLocationTimer.start(16);
                }
                this.synthesizeViewMoveEvent();
            }
        }
    }

    protected abstract boolean _setResizable(long var1, boolean var3);

    public boolean setResizable(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (this.isResizable != bl && this._setResizable(this.ptr, bl)) {
            this.isResizable = bl;
            this.synthesizeViewMoveEvent();
        }
        return this.isResizable;
    }

    public boolean isResizable() {
        Application.checkEventThread();
        return this.isResizable;
    }

    public boolean isUnifiedWindow() {
        return (this.styleMask & 0x100) != 0;
    }

    public boolean isTransparentWindow() {
        return (this.styleMask & 2) != 0;
    }

    public boolean isFocused() {
        Application.checkEventThread();
        return this.isFocused;
    }

    protected abstract boolean _requestFocus(long var1, int var3);

    public boolean requestFocus(int n) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (!this.isChild() && n != 542) {
            throw new IllegalArgumentException("Invalid focus event ID for top-level window");
        }
        if (this.isChild() && (n < 541 || n > 544)) {
            throw new IllegalArgumentException("Invalid focus event ID for child window");
        }
        if (n == 541 && !this.isFocused()) {
            return true;
        }
        if (!this.isFocusable) {
            return false;
        }
        return this._requestFocus(this.ptr, n);
    }

    public boolean requestFocus() {
        Application.checkEventThread();
        return this.requestFocus(542);
    }

    protected abstract void _setFocusable(long var1, boolean var3);

    public void setFocusable(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        this.isFocusable = bl;
        if (this.isEnabled()) {
            this._setFocusable(this.ptr, bl);
        }
    }

    protected abstract boolean _grabFocus(long var1);

    protected abstract void _ungrabFocus(long var1);

    public boolean grabFocus() {
        Application.checkEventThread();
        this.checkNotClosed();
        if (!this.isFocused()) {
            throw new IllegalStateException("The window must be focused when calling grabFocus()");
        }
        return this._grabFocus(this.ptr);
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

    public void setTitle(String string) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (string == null) {
            string = "";
        }
        if (!string.equals(this.title) && this._setTitle(this.ptr, string)) {
            this.title = string;
        }
    }

    protected abstract void _setLevel(long var1, int var3);

    public void setLevel(int n) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (n < 1 || n > 3) {
            throw new IllegalArgumentException("Level should be in the range [1..3]");
        }
        if (this.level != n) {
            this._setLevel(this.ptr, n);
            this.level = n;
        }
    }

    public int getLevel() {
        Application.checkEventThread();
        return this.level;
    }

    private boolean isInFullscreen() {
        View view = this.getView();
        return view == null ? false : view.isInFullscreen();
    }

    void notifyFullscreen(boolean bl) {
        float f = this.getAlpha();
        if (f < 1.0f) {
            if (bl) {
                this._setAlpha(this.ptr, 1.0f);
            } else {
                this.setAlpha(f);
            }
        }
    }

    protected abstract void _setAlpha(long var1, float var3);

    public void setAlpha(float f) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (f < 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Alpha should be in the range [0f..1f]");
        }
        this.alpha = f;
        if (f < 1.0f && this.isInFullscreen()) {
            return;
        }
        this._setAlpha(this.ptr, this.alpha);
    }

    public float getAlpha() {
        Application.checkEventThread();
        return this.alpha;
    }

    public boolean getAppletMode() {
        return this.appletMode;
    }

    public void setAppletMode(boolean bl) {
        this.appletMode = bl;
    }

    protected abstract boolean _setBackground(long var1, float var3, float var4, float var5);

    public boolean setBackground(float f, float f2, float f3) {
        Application.checkEventThread();
        this.checkNotClosed();
        return this._setBackground(this.ptr, f, f2, f3);
    }

    public boolean isEnabled() {
        Application.checkEventThread();
        return this.disableCount == 0;
    }

    protected abstract void _setEnabled(long var1, boolean var3);

    public void setEnabled(boolean bl) {
        Application.checkEventThread();
        this.checkNotClosed();
        if (!bl) {
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

    public void setMinimumSize(int n, int n2) {
        Application.checkEventThread();
        if (n < 0 || n2 < 0) {
            throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + n + "; height=" + n2);
        }
        this.checkNotClosed();
        if (this._setMinimumSize(this.ptr, n, n2)) {
            this.minimumWidth = n;
            this.minimumHeight = n2;
        }
    }

    protected abstract boolean _setMaximumSize(long var1, int var3, int var4);

    public void setMaximumSize(int n, int n2) {
        Application.checkEventThread();
        if (n < 0 || n2 < 0) {
            throw new IllegalArgumentException("The width and height must be >= 0. Got: width=" + n + "; height=" + n2);
        }
        this.checkNotClosed();
        if (this._setMaximumSize(this.ptr, n == Integer.MAX_VALUE ? -1 : n, n2 == Integer.MAX_VALUE ? -1 : n2)) {
            this.maximumWidth = n;
            this.maximumHeight = n2;
        }
    }

    protected abstract void _setIcon(long var1, Pixels var3);

    public void setIcon(Pixels pixels) {
        Application.checkEventThread();
        this.checkNotClosed();
        this._setIcon(this.ptr, pixels);
    }

    protected abstract void _setCursor(long var1, Cursor var3);

    public void setCursor(Cursor cursor) {
        Application.checkEventThread();
        this._setCursor(this.ptr, cursor);
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

    public void enterModal(Window window) {
        this.checkNotClosed();
        if (!this.isModal) {
            this.isModal = true;
            this._enterModalWithWindow(this.ptr, window.getNativeHandle());
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

    public void dispatchNpapiEvent(Map map) {
        Application.checkEventThread();
        throw new RuntimeException("This operation is not supported on this platform");
    }

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    public void setShouldStartUndecoratedMove(boolean bl) {
        Application.checkEventThread();
        this.shouldStartUndecoratedMove = bl;
    }

    protected void notifyClose() {
        this.handleWindowEvent(System.nanoTime(), 521);
    }

    protected void notifyDestroy() {
        if (this.ptr == 0L) {
            return;
        }
        this.handleWindowEvent(System.nanoTime(), 522);
        this.ptr = 0L;
        this.setVisible(false);
    }

    protected void notifyMove(int n, int n2) {
        this.x = n;
        this.y = n2;
        this.handleWindowEvent(System.nanoTime(), 512);
    }

    protected void notifyRescale() {
        this.handleWindowEvent(System.nanoTime(), 513);
    }

    protected void notifyMoveToAnotherScreen(Screen screen) {
        this.setScreen(screen);
    }

    protected void setState(int n) {
        this.state = n;
    }

    protected void notifyResize(int n, int n2, int n3) {
        if (n == 531) {
            this.state = 2;
        } else {
            this.state = n == 532 ? 3 : 1;
            this.width = n2;
            this.height = n3;
            if (this.helper != null) {
                this.helper.updateRectangles();
            }
        }
        this.handleWindowEvent(System.nanoTime(), n);
        if (n == 532 || n == 533) {
            this.handleWindowEvent(System.nanoTime(), 511);
        }
    }

    protected void notifyFocus(int n) {
        boolean bl;
        boolean bl2 = bl = n != 541;
        if (this.isFocused != bl) {
            this.isFocused = bl;
            this.handleWindowEvent(System.nanoTime(), n);
        }
    }

    protected void notifyFocusDisabled() {
        this.handleWindowEvent(System.nanoTime(), 545);
    }

    protected void notifyFocusUngrab() {
        this.handleWindowEvent(System.nanoTime(), 546);
    }

    protected void notifyDelegatePtr(long l) {
        this.delegatePtr = l;
    }

    protected void handleWindowEvent(long l, int n) {
        if (this.eventHandler != null) {
            this.eventHandler.handleWindowEvent(this, l, n);
        }
    }

    public void setUndecoratedMoveRectangle(int n) {
        Application.checkEventThread();
        if (this.isDecorated) {
            System.err.println("Glass Window.setUndecoratedMoveRectangle is only valid for Undecorated Window. In the future this will be hard error.");
            Thread.dumpStack();
            return;
        }
        if (this.helper != null) {
            this.helper.setMoveRectangle(n);
        }
    }

    public boolean shouldStartUndecoratedMove(int n, int n2) {
        Application.checkEventThread();
        if (this.shouldStartUndecoratedMove) {
            return true;
        }
        if (this.isDecorated) {
            return false;
        }
        if (this.helper != null) {
            return this.helper.shouldStartMove(n, n2);
        }
        return false;
    }

    public void setUndecoratedResizeRectangle(int n) {
        Application.checkEventThread();
        if (this.isDecorated || !this.isResizable) {
            System.err.println("Glass Window.setUndecoratedResizeRectangle is only valid for Undecorated Resizable Window. In the future this will be hard error.");
            Thread.dumpStack();
            return;
        }
        if (this.helper != null) {
            this.helper.setResizeRectangle(n);
        }
    }

    public boolean shouldStartUndecoratedResize(int n, int n2) {
        Application.checkEventThread();
        if (this.isDecorated || !this.isResizable) {
            return false;
        }
        if (this.helper != null) {
            return this.helper.shouldStartResize(n, n2);
        }
        return false;
    }

    boolean handleMouseEvent(int n, int n2, int n3, int n4, int n5, int n6) {
        if (!this.isDecorated) {
            return this.helper.handleMouseEvent(n, n2, n3, n4, n5, n6);
        }
        return false;
    }

    public String toString() {
        Application.checkEventThread();
        return "Window:\n    ptr: " + this.getNativeWindow() + "\n    screen ptr: " + (Serializable)(this.screen != null ? Long.valueOf(this.screen.getNativeScreen()) : "null") + "\n    isDecorated: " + this.isDecorated() + "\n    title: " + this.getTitle() + "\n    visible: " + this.isVisible() + "\n    focused: " + this.isFocused() + "\n    modal: " + this.isModal() + "\n    state: " + this.state + "\n    x: " + this.getX() + ", y: " + this.getY() + ", w: " + this.getWidth() + ", h: " + this.getHeight() + "\n";
    }

    protected void notifyLevelChanged(int n) {
        this.level = n;
        if (this.eventHandler != null) {
            this.eventHandler.handleLevelEvent(n);
        }
    }

    public void requestInput(String string, int n, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        Application.checkEventThread();
        this._requestInput(this.ptr, string, n, d, d2, d3, d4, d5, d6, d7, d8, d9, d10, d11, d12, d13, d14);
    }

    public void releaseInput() {
        Application.checkEventThread();
        this._releaseInput(this.ptr);
    }

    protected abstract void _requestInput(long var1, String var3, int var4, double var5, double var7, double var9, double var11, double var13, double var15, double var17, double var19, double var21, double var23, double var25, double var27, double var29, double var31);

    protected abstract void _releaseInput(long var1);

    private class UndecoratedMoveResizeHelper {
        TrackingRectangle moveRect = new TrackingRectangle();
        TrackingRectangle resizeRect = new TrackingRectangle();
        boolean inMove = false;
        boolean inResize = false;
        int startMouseX;
        int startMouseY;
        int startX;
        int startY;
        int startWidth;
        int startHeight;

        UndecoratedMoveResizeHelper() {
        }

        void setMoveRectangle(int n) {
            this.moveRect.size = n;
            this.moveRect.x = 0;
            this.moveRect.y = 0;
            this.moveRect.width = Window.this.getWidth();
            this.moveRect.height = this.moveRect.size;
        }

        boolean shouldStartMove(int n, int n2) {
            return this.moveRect.contains(n, n2);
        }

        boolean inMove() {
            return this.inMove;
        }

        void startMove(int n, int n2) {
            this.inMove = true;
            this.startMouseX = n;
            this.startMouseY = n2;
            this.startX = Window.this.getX();
            this.startY = Window.this.getY();
        }

        void deltaMove(int n, int n2) {
            int n3 = n - this.startMouseX;
            int n4 = n2 - this.startMouseY;
            Window.this.setPosition(this.startX + n3, this.startY + n4);
        }

        void stopMove() {
            this.inMove = false;
        }

        void setResizeRectangle(int n) {
            this.resizeRect.size = n;
            this.resizeRect.x = Window.this.getWidth() - this.resizeRect.size;
            this.resizeRect.y = Window.this.getHeight() - this.resizeRect.size;
            this.resizeRect.width = this.resizeRect.size;
            this.resizeRect.height = this.resizeRect.size;
        }

        boolean shouldStartResize(int n, int n2) {
            return this.resizeRect.contains(n, n2);
        }

        boolean inResize() {
            return this.inResize;
        }

        void startResize(int n, int n2) {
            this.inResize = true;
            this.startMouseX = n;
            this.startMouseY = n2;
            this.startWidth = Window.this.getWidth();
            this.startHeight = Window.this.getHeight();
        }

        void deltaResize(int n, int n2) {
            int n3 = n - this.startMouseX;
            int n4 = n2 - this.startMouseY;
            Window.this.setSize(this.startWidth + n3, this.startHeight + n4);
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

        boolean handleMouseEvent(int n, int n2, int n3, int n4, int n5, int n6) {
            switch (n) {
                case 221: {
                    if (n2 != 212) break;
                    if (Window.this.shouldStartUndecoratedMove(n3, n4)) {
                        this.startMove(n5, n6);
                        return true;
                    }
                    if (!Window.this.shouldStartUndecoratedResize(n3, n4)) break;
                    this.startResize(n5, n6);
                    return true;
                }
                case 223: 
                case 224: {
                    if (this.inMove()) {
                        this.deltaMove(n5, n6);
                        return true;
                    }
                    if (!this.inResize()) break;
                    this.deltaResize(n5, n6);
                    return true;
                }
                case 222: {
                    boolean bl = this.inMove() || this.inResize();
                    this.stopResize();
                    this.stopMove();
                    return bl;
                }
            }
            return false;
        }
    }

    public static final class State {
        public static final int NORMAL = 1;
        public static final int MINIMIZED = 2;
        public static final int MAXIMIZED = 3;
    }

    public static final class Level {
        private static final int _MIN = 1;
        public static final int NORMAL = 1;
        public static final int FLOATING = 2;
        public static final int TOPMOST = 3;
        private static final int _MAX = 3;
    }

    public static class EventHandler {
        public void handleWindowEvent(Window window, long l, int n) {
        }

        public void handleScreenChangedEvent(Window window, long l, Screen screen, Screen screen2) {
        }

        public void handleLevelEvent(int n) {
        }
    }

    private static class TrackingRectangle {
        int size = 0;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        private TrackingRectangle() {
        }

        boolean contains(int n, int n2) {
            return this.size > 0 && n >= this.x && n < this.x + this.width && n2 >= this.y && n2 < this.y + this.height;
        }
    }
}

