/*
 * Decompiled with CFR 0.150.
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.Accessible;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.Cursor;
import com.sun.glass.ui.GlassRobot;
import com.sun.glass.ui.InvokeLaterDispatcher;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Size;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.win.WinAccessible;
import com.sun.glass.ui.win.WinChildWindow;
import com.sun.glass.ui.win.WinCommonDialogs;
import com.sun.glass.ui.win.WinCursor;
import com.sun.glass.ui.win.WinPixels;
import com.sun.glass.ui.win.WinRobot;
import com.sun.glass.ui.win.WinTimer;
import com.sun.glass.ui.win.WinView;
import com.sun.glass.ui.win.WinWindow;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.tk.Toolkit;
import com.sun.prism.impl.PrismSettings;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ResourceBundle;

final class WinApplication
extends Application
implements InvokeLaterDispatcher.InvokeLaterSubmitter {
    static float overrideUIScale;
    private static final String BASE_NAME = "com/sun/glass/ui/win/themes";
    private final InvokeLaterDispatcher invokeLaterDispatcher;
    private static boolean verbose;
    private static final int Process_DPI_Unaware = 0;
    private static final int Process_System_DPI_Aware = 1;
    private static final int Process_Per_Monitor_DPI_Aware = 2;

    private static boolean getBoolean(String string, boolean bl, String string2) {
        String string3 = System.getProperty(string);
        if (string3 == null) {
            string3 = System.getenv(string);
        }
        if (string3 == null) {
            return bl;
        }
        Boolean bl2 = Boolean.parseBoolean(string3);
        if (PrismSettings.verbose) {
            System.out.println((bl2 != false ? "" : "not ") + string2);
        }
        return bl2;
    }

    private static float getFloat(String string, float f, String string2) {
        String string3 = System.getProperty(string);
        if (string3 == null) {
            string3 = System.getenv(string);
        }
        if (string3 == null) {
            return f;
        }
        float f2 = (string3 = string3.trim()).endsWith("%") ? (float)Integer.parseInt(string3.substring(0, string3.length() - 1)) / 100.0f : (string3.endsWith("DPI") || string3.endsWith("dpi") ? (float)Integer.parseInt(string3.substring(0, string3.length() - 3)) / 96.0f : Float.parseFloat(string3));
        if (PrismSettings.verbose) {
            System.out.println(string2 + f2);
        }
        return f2;
    }

    private static native void initIDs(float var0);

    WinApplication() {
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.embed.isEventThread"));
        if (!bl) {
            this.invokeLaterDispatcher = new InvokeLaterDispatcher(this);
            this.invokeLaterDispatcher.start();
        } else {
            this.invokeLaterDispatcher = null;
        }
    }

    private native long _init(int var1);

    private native void _setClassLoader(ClassLoader var1);

    private native void _runLoop(Runnable var1);

    private native void _terminateLoop();

    private static int getDesiredAwarenesslevel() {
        if (!PrismSettings.allowHiDPIScaling) {
            return 0;
        }
        String string = AccessController.doPrivileged(() -> System.getProperty("javafx.glass.winDPIawareness"));
        if (string != null) {
            if ((string = string.toLowerCase()).equals("aware")) {
                return 1;
            }
            if (string.equals("permonitor")) {
                return 2;
            }
            if (!string.equals("unaware")) {
                System.err.println("unrecognized DPI awareness request, defaulting to unaware: " + string);
            }
            return 0;
        }
        return 2;
    }

    @Override
    protected void runLoop(Runnable runnable) {
        boolean bl = AccessController.doPrivileged(() -> Boolean.getBoolean("javafx.embed.isEventThread"));
        int n = WinApplication.getDesiredAwarenesslevel();
        ClassLoader classLoader = WinApplication.class.getClassLoader();
        this._setClassLoader(classLoader);
        if (bl) {
            this._init(n);
            WinApplication.setEventThread(Thread.currentThread());
            runnable.run();
            return;
        }
        Thread thread = AccessController.doPrivileged(() -> new Thread(() -> {
            this._init(n);
            this._runLoop(runnable);
        }, "WindowsNativeRunloopThread"));
        WinApplication.setEventThread(thread);
        thread.start();
    }

    @Override
    protected void finishTerminating() {
        Thread thread = WinApplication.getEventThread();
        if (thread != null) {
            this._terminateLoop();
            WinApplication.setEventThread(null);
        }
        super.finishTerminating();
    }

    @Override
    public boolean shouldUpdateWindow() {
        return true;
    }

    private native Object _enterNestedEventLoopImpl();

    private native void _leaveNestedEventLoopImpl(Object var1);

    @Override
    protected Object _enterNestedEventLoop() {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        try {
            Object object = this._enterNestedEventLoopImpl();
            return object;
        }
        finally {
            if (this.invokeLaterDispatcher != null) {
                this.invokeLaterDispatcher.notifyLeftNestedEventLoop();
            }
        }
    }

    @Override
    protected void _leaveNestedEventLoop(Object object) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyLeavingNestedEventLoop();
        }
        this._leaveNestedEventLoopImpl(object);
    }

    @Override
    public Window createWindow(Window window, Screen screen, int n) {
        return new WinWindow(window, screen, n);
    }

    @Override
    public Window createWindow(long l) {
        return new WinChildWindow(l);
    }

    @Override
    public View createView() {
        return new WinView();
    }

    @Override
    public Cursor createCursor(int n) {
        return new WinCursor(n);
    }

    @Override
    public Cursor createCursor(int n, int n2, Pixels pixels) {
        return new WinCursor(n, n2, pixels);
    }

    @Override
    protected void staticCursor_setVisible(boolean bl) {
        WinCursor.setVisible_impl(bl);
    }

    @Override
    protected Size staticCursor_getBestSize(int n, int n2) {
        return WinCursor.getBestSize_impl(n, n2);
    }

    @Override
    public Pixels createPixels(int n, int n2, ByteBuffer byteBuffer) {
        return new WinPixels(n, n2, byteBuffer);
    }

    @Override
    public Pixels createPixels(int n, int n2, IntBuffer intBuffer) {
        return new WinPixels(n, n2, intBuffer);
    }

    @Override
    public Pixels createPixels(int n, int n2, IntBuffer intBuffer, float f, float f2) {
        return new WinPixels(n, n2, intBuffer, f, f2);
    }

    @Override
    protected int staticPixels_getNativeFormat() {
        return WinPixels.getNativeFormat_impl();
    }

    @Override
    public GlassRobot createRobot() {
        return new WinRobot();
    }

    @Override
    protected double staticScreen_getVideoRefreshPeriod() {
        return 0.0;
    }

    @Override
    protected native Screen[] staticScreen_getScreens();

    @Override
    public Timer createTimer(Runnable runnable) {
        return new WinTimer(runnable);
    }

    @Override
    protected int staticTimer_getMinPeriod() {
        return WinTimer.getMinPeriod_impl();
    }

    @Override
    protected int staticTimer_getMaxPeriod() {
        return WinTimer.getMaxPeriod_impl();
    }

    @Override
    public Accessible createAccessible() {
        return new WinAccessible();
    }

    @Override
    protected CommonDialogs.FileChooserResult staticCommonDialogs_showFileChooser(Window window, String string, String string2, String string3, int n, boolean bl, CommonDialogs.ExtensionFilter[] arrextensionFilter, int n2) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        return WinCommonDialogs.showFileChooser_impl(window, string, string2, string3, n, bl, arrextensionFilter, n2);
    }

    @Override
    protected File staticCommonDialogs_showFolderChooser(Window window, String string, String string2) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.notifyEnteringNestedEventLoop();
        }
        return WinCommonDialogs.showFolderChooser_impl(window, string, string2);
    }

    @Override
    protected long staticView_getMultiClickTime() {
        return WinView.getMultiClickTime_impl();
    }

    @Override
    protected int staticView_getMultiClickMaxX() {
        return WinView.getMultiClickMaxX_impl();
    }

    @Override
    protected int staticView_getMultiClickMaxY() {
        return WinView.getMultiClickMaxY_impl();
    }

    @Override
    protected native void _invokeAndWait(Runnable var1);

    private native void _submitForLaterInvocation(Runnable var1);

    @Override
    public void submitForLaterInvocation(Runnable runnable) {
        this._submitForLaterInvocation(runnable);
    }

    @Override
    protected void _invokeLater(Runnable runnable) {
        if (this.invokeLaterDispatcher != null) {
            this.invokeLaterDispatcher.invokeLater(runnable);
        } else {
            this.submitForLaterInvocation(runnable);
        }
    }

    @Override
    public String getHighContrastScheme(String string) {
        return PlatformImpl.HighContrastScheme.fromThemeName(ResourceBundle.getBundle("com/sun/glass/ui/win/themes")::getString, string);
    }

    private native String _getHighContrastTheme();

    @Override
    public String getHighContrastTheme() {
        WinApplication.checkEventThread();
        return this.getHighContrastScheme(this._getHighContrastTheme());
    }

    @Override
    protected boolean _supportsInputMethods() {
        return true;
    }

    @Override
    protected boolean _supportsTransparentWindows() {
        return true;
    }

    @Override
    protected native boolean _supportsUnifiedWindows();

    @Override
    public String getDataDirectory() {
        WinApplication.checkEventThread();
        String string = AccessController.doPrivileged(() -> System.getenv("APPDATA"));
        if (string == null || string.length() == 0) {
            return super.getDataDirectory();
        }
        return string + File.separator + this.name + File.separator;
    }

    @Override
    protected native int _getKeyCodeForChar(char var1);

    @Override
    protected native int _isKeyLocked(int var1);

    static {
        Void void_ = AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                verbose = Boolean.getBoolean("javafx.verbose");
                if (PrismSettings.allowHiDPIScaling) {
                    overrideUIScale = WinApplication.getFloat("glass.win.uiScale", -1.0f, "Forcing UI scaling factor: ");
                    if (PrismSettings.verbose) {
                        WinApplication.getFloat("glass.win.renderScale", -1.0f, "(No longer supported) Rendering scaling factor: ");
                        WinApplication.getFloat("glass.win.minHiDPI", 1.5f, "(No longer supported) UI scaling threshold: ");
                        WinApplication.getBoolean("glass.win.forceIntegerRenderScale", true, "(No longer supported) force integer rendering scale");
                    }
                } else {
                    overrideUIScale = 1.0f;
                }
                Toolkit.loadMSWindowsLibraries();
                WinApplication.loadNativeLibrary();
                return null;
            }
        });
        WinApplication.initIDs(overrideUIScale);
    }
}

