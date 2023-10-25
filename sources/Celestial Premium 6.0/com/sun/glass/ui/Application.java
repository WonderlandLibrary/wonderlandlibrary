/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.EventLoop;
import com.sun.glass.ui.GlassRobot;
import com.sun.glass.ui.Menu;
import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.PlatformFactory;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.utils.NativeLibLoader;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public abstract class Application {
    private static final String DEFAULT_NAME = "java";
    protected String name = "java";
    private EventHandler eventHandler;
    private boolean initialActiveEventReceived = false;
    private String[] initialOpenedFiles = null;
    private static boolean loaded = false;
    private static Application application;
    private static Thread eventThread;
    private static final boolean disableThreadChecks;
    private static volatile Map deviceDetails;
    private boolean terminateWhenLastWindowClosed = true;
    private static int nestedEventLoopCounter;

    protected static synchronized void loadNativeLibrary(String string) {
        if (!loaded) {
            NativeLibLoader.loadLibrary(string);
            loaded = true;
        }
    }

    protected static synchronized void loadNativeLibrary() {
        Application.loadNativeLibrary("glass");
    }

    public static void setDeviceDetails(Map map) {
        deviceDetails = map;
    }

    public static Map getDeviceDetails() {
        return deviceDetails;
    }

    protected Application() {
    }

    public static void run(Runnable runnable) {
        if (application != null) {
            throw new IllegalStateException("Application is already running");
        }
        application = PlatformFactory.getPlatformFactory().createApplication();
        try {
            application.runLoop(() -> {
                Screen.initScreens();
                runnable.run();
            });
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    protected abstract void runLoop(Runnable var1);

    protected void finishTerminating() {
        application = null;
    }

    public String getName() {
        Application.checkEventThread();
        return this.name;
    }

    public void setName(String string) {
        Application.checkEventThread();
        if (string != null && "java".equals(this.name)) {
            this.name = string;
        }
    }

    public String getDataDirectory() {
        Application.checkEventThread();
        String string = AccessController.doPrivileged(() -> System.getProperty("user.home"));
        return string + File.separator + "." + this.name + File.separator;
    }

    protected void notifyWillFinishLaunching() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleWillFinishLaunchingAction(this, System.nanoTime());
        }
    }

    protected void notifyDidFinishLaunching() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidFinishLaunchingAction(this, System.nanoTime());
        }
    }

    protected void notifyWillBecomeActive() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleWillBecomeActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyDidBecomeActive() {
        this.initialActiveEventReceived = true;
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidBecomeActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyWillResignActive() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleWillResignActiveAction(this, System.nanoTime());
        }
    }

    protected boolean notifyThemeChanged(String string) {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            return eventHandler.handleThemeChanged(string);
        }
        return false;
    }

    protected void notifyDidResignActive() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidResignActiveAction(this, System.nanoTime());
        }
    }

    protected void notifyDidReceiveMemoryWarning() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidReceiveMemoryWarning(this, System.nanoTime());
        }
    }

    protected void notifyWillHide() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleWillHideAction(this, System.nanoTime());
        }
    }

    protected void notifyDidHide() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidHideAction(this, System.nanoTime());
        }
    }

    protected void notifyWillUnhide() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleWillUnhideAction(this, System.nanoTime());
        }
    }

    protected void notifyDidUnhide() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleDidUnhideAction(this, System.nanoTime());
        }
    }

    protected void notifyOpenFiles(String[] arrstring) {
        EventHandler eventHandler;
        if (!this.initialActiveEventReceived && this.initialOpenedFiles == null) {
            this.initialOpenedFiles = arrstring;
        }
        if ((eventHandler = this.getEventHandler()) != null && arrstring != null) {
            eventHandler.handleOpenFilesAction(this, System.nanoTime(), arrstring);
        }
    }

    protected void notifyWillQuit() {
        EventHandler eventHandler = this.getEventHandler();
        if (eventHandler != null) {
            eventHandler.handleQuitAction(this, System.nanoTime());
        }
    }

    public void installDefaultMenus(MenuBar menuBar) {
        Application.checkEventThread();
    }

    public EventHandler getEventHandler() {
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        boolean bl = this.eventHandler != null && this.initialOpenedFiles != null;
        this.eventHandler = eventHandler;
        if (bl) {
            this.notifyOpenFiles(this.initialOpenedFiles);
        }
    }

    public final boolean shouldTerminateWhenLastWindowClosed() {
        Application.checkEventThread();
        return this.terminateWhenLastWindowClosed;
    }

    public final void setTerminateWhenLastWindowClosed(boolean bl) {
        Application.checkEventThread();
        this.terminateWhenLastWindowClosed = bl;
    }

    public boolean shouldUpdateWindow() {
        Application.checkEventThread();
        return false;
    }

    public boolean hasWindowManager() {
        return true;
    }

    public void notifyRenderingFinished() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void terminate() {
        Application.checkEventThread();
        try {
            LinkedList<Window> linkedList = new LinkedList<Window>(Window.getWindows());
            for (Window window : linkedList) {
                window.setVisible(false);
            }
            for (Window window : linkedList) {
                window.close();
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        finally {
            this.finishTerminating();
        }
    }

    public static Application GetApplication() {
        return application;
    }

    protected static void setEventThread(Thread thread) {
        eventThread = thread;
    }

    protected static Thread getEventThread() {
        return eventThread;
    }

    public static boolean isEventThread() {
        return Thread.currentThread() == eventThread;
    }

    public static void checkEventThread() {
        if (!disableThreadChecks && Thread.currentThread() != eventThread) {
            throw new IllegalStateException("This operation is permitted on the event thread only; currentThread = " + Thread.currentThread().getName());
        }
    }

    public static void reportException(Throwable throwable) {
        Thread thread = Thread.currentThread();
        Thread.UncaughtExceptionHandler uncaughtExceptionHandler = thread.getUncaughtExceptionHandler();
        uncaughtExceptionHandler.uncaughtException(thread, throwable);
    }

    protected abstract void _invokeAndWait(Runnable var1);

    public static void invokeAndWait(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        if (Application.isEventThread()) {
            runnable.run();
        } else {
            Application.GetApplication()._invokeAndWait(runnable);
        }
    }

    protected abstract void _invokeLater(Runnable var1);

    public static void invokeLater(Runnable runnable) {
        if (runnable == null) {
            return;
        }
        Application.GetApplication()._invokeLater(runnable);
    }

    protected abstract Object _enterNestedEventLoop();

    protected abstract void _leaveNestedEventLoop(Object var1);

    static Object enterNestedEventLoop() {
        Application.checkEventThread();
        ++nestedEventLoopCounter;
        try {
            Object object = Application.GetApplication()._enterNestedEventLoop();
            return object;
        }
        finally {
            --nestedEventLoopCounter;
        }
    }

    static void leaveNestedEventLoop(Object object) {
        Application.checkEventThread();
        if (nestedEventLoopCounter == 0) {
            throw new IllegalStateException("Not in a nested event loop");
        }
        Application.GetApplication()._leaveNestedEventLoop(object);
    }

    public static boolean isNestedLoopRunning() {
        Application.checkEventThread();
        return nestedEventLoopCounter > 0;
    }

    public void menuAboutAction() {
        System.err.println("about");
    }

    public abstract Window createWindow(Window var1, Screen var2, int var3);

    public final Window createWindow(Screen screen, int n) {
        return this.createWindow(null, screen, n);
    }

    public abstract Window createWindow(long var1);

    public abstract View createView();

    public abstract Cursor createCursor(int var1);

    public abstract Cursor createCursor(int var1, int var2, Pixels var3);

    protected abstract void staticCursor_setVisible(boolean var1);

    protected abstract Size staticCursor_getBestSize(int var1, int var2);

    public final Menu createMenu(String string) {
        return new Menu(string);
    }

    public final Menu createMenu(String string, boolean bl) {
        return new Menu(string, bl);
    }

    public final MenuBar createMenuBar() {
        return new MenuBar();
    }

    public final MenuItem createMenuItem(String string) {
        return this.createMenuItem(string, null);
    }

    public final MenuItem createMenuItem(String string, MenuItem.Callback callback) {
        return this.createMenuItem(string, callback, 0, 0);
    }

    public final MenuItem createMenuItem(String string, MenuItem.Callback callback, int n, int n2) {
        return this.createMenuItem(string, callback, n, n2, null);
    }

    public final MenuItem createMenuItem(String string, MenuItem.Callback callback, int n, int n2, Pixels pixels) {
        return new MenuItem(string, callback, n, n2, pixels);
    }

    public abstract Pixels createPixels(int var1, int var2, ByteBuffer var3);

    public abstract Pixels createPixels(int var1, int var2, IntBuffer var3);

    public abstract Pixels createPixels(int var1, int var2, IntBuffer var3, float var4, float var5);

    protected abstract int staticPixels_getNativeFormat();

    static Pixels createPixels(int n, int n2, int[] arrn, float f, float f2) {
        return Application.GetApplication().createPixels(n, n2, IntBuffer.wrap(arrn), f, f2);
    }

    static float getScaleFactor(int n, int n2, int n3, int n4) {
        float f = 0.0f;
        for (Screen screen : Screen.getScreens()) {
            int n5 = screen.getX();
            int n6 = screen.getY();
            int n7 = screen.getWidth();
            int n8 = screen.getHeight();
            if (n >= n5 + n7 || n + n3 <= n5 || n2 >= n6 + n8 || n2 + n4 <= n6) continue;
            if (f < screen.getRecommendedOutputScaleX()) {
                f = screen.getRecommendedOutputScaleX();
            }
            if (!(f < screen.getRecommendedOutputScaleY())) continue;
            f = screen.getRecommendedOutputScaleY();
        }
        return f == 0.0f ? 1.0f : f;
    }

    public abstract GlassRobot createRobot();

    protected abstract double staticScreen_getVideoRefreshPeriod();

    protected abstract Screen[] staticScreen_getScreens();

    public abstract Timer createTimer(Runnable var1);

    protected abstract int staticTimer_getMinPeriod();

    protected abstract int staticTimer_getMaxPeriod();

    public final EventLoop createEventLoop() {
        return new EventLoop();
    }

    public Accessible createAccessible() {
        return null;
    }

    protected abstract CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window var1, String var2, String var3, String var4, int var5, boolean var6, CommonDialogs.ExtensionFilter[] var7, int var8);

    protected abstract File staticCommonDialogs_showFolderChooser(Window var1, String var2, String var3);

    protected abstract long staticView_getMultiClickTime();

    protected abstract int staticView_getMultiClickMaxX();

    protected abstract int staticView_getMultiClickMaxY();

    public String getHighContrastScheme(String string) {
        return string;
    }

    public String getHighContrastTheme() {
        Application.checkEventThread();
        return null;
    }

    protected boolean _supportsInputMethods() {
        return false;
    }

    public final boolean supportsInputMethods() {
        Application.checkEventThread();
        return this._supportsInputMethods();
    }

    protected abstract boolean _supportsTransparentWindows();

    public final boolean supportsTransparentWindows() {
        Application.checkEventThread();
        return this._supportsTransparentWindows();
    }

    public boolean hasTwoLevelFocus() {
        return false;
    }

    public boolean hasVirtualKeyboard() {
        return false;
    }

    public boolean hasTouch() {
        return false;
    }

    public boolean hasMultiTouch() {
        return false;
    }

    public boolean hasPointer() {
        return true;
    }

    protected abstract boolean _supportsUnifiedWindows();

    public final boolean supportsUnifiedWindows() {
        Application.checkEventThread();
        return this._supportsUnifiedWindows();
    }

    protected boolean _supportsSystemMenu() {
        return false;
    }

    public final boolean supportsSystemMenu() {
        Application.checkEventThread();
        return this._supportsSystemMenu();
    }

    protected abstract int _getKeyCodeForChar(char var1);

    public static int getKeyCodeForChar(char c) {
        return application._getKeyCodeForChar(c);
    }

    protected int _isKeyLocked(int n) {
        return -1;
    }

    public final Optional<Boolean> isKeyLocked(int n) {
        Application.checkEventThread();
        int n2 = this._isKeyLocked(n);
        switch (n2) {
            case 0: {
                return Optional.of(false);
            }
            case 1: {
                return Optional.of(true);
            }
        }
        return Optional.empty();
    }

    static {
        disableThreadChecks = AccessController.doPrivileged(() -> {
            String string = System.getProperty("glass.disableThreadChecks", "false");
            return "true".equalsIgnoreCase(string);
        });
        deviceDetails = null;
        nestedEventLoopCounter = 0;
    }

    public static class EventHandler {
        public void handleWillFinishLaunchingAction(Application application, long l) {
        }

        public void handleDidFinishLaunchingAction(Application application, long l) {
        }

        public void handleWillBecomeActiveAction(Application application, long l) {
        }

        public void handleDidBecomeActiveAction(Application application, long l) {
        }

        public void handleWillResignActiveAction(Application application, long l) {
        }

        public void handleDidResignActiveAction(Application application, long l) {
        }

        public void handleDidReceiveMemoryWarning(Application application, long l) {
        }

        public void handleWillHideAction(Application application, long l) {
        }

        public void handleDidHideAction(Application application, long l) {
        }

        public void handleWillUnhideAction(Application application, long l) {
        }

        public void handleDidUnhideAction(Application application, long l) {
        }

        public void handleOpenFilesAction(Application application, long l, String[] arrstring) {
        }

        public void handleQuitAction(Application application, long l) {
        }

        public boolean handleThemeChanged(String string) {
            return false;
        }
    }
}

