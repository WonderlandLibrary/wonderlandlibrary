/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengles.GLContext
 *  org.lwjgl.opengles.PixelFormat
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.opengl.AWTCanvasImplementation;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayImplementation;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.DrawableGLES;
import org.lwjgl.opengl.DrawableLWJGL;
import org.lwjgl.opengl.GlobalLock;
import org.lwjgl.opengl.LinuxDisplayPeerInfo;
import org.lwjgl.opengl.LinuxEvent;
import org.lwjgl.opengl.LinuxKeyboard;
import org.lwjgl.opengl.LinuxMouse;
import org.lwjgl.opengl.LinuxPbufferPeerInfo;
import org.lwjgl.opengl.LinuxPeerInfo;
import org.lwjgl.opengl.PeerInfo;
import org.lwjgl.opengl.XRandR;
import org.lwjgl.opengles.GLContext;
import org.lwjgl.opengles.PixelFormat;

final class LinuxDisplay
implements DisplayImplementation {
    public static final int CurrentTime = 0;
    public static final int GrabSuccess = 0;
    public static final int AutoRepeatModeOff = 0;
    public static final int AutoRepeatModeOn = 1;
    public static final int AutoRepeatModeDefault = 2;
    public static final int None = 0;
    private static final int KeyPressMask = 1;
    private static final int KeyReleaseMask = 2;
    private static final int ButtonPressMask = 4;
    private static final int ButtonReleaseMask = 8;
    private static final int NotifyAncestor = 0;
    private static final int NotifyNonlinear = 3;
    private static final int NotifyPointer = 5;
    private static final int NotifyPointerRoot = 6;
    private static final int NotifyDetailNone = 7;
    private static final int SetModeInsert = 0;
    private static final int SaveSetRoot = 1;
    private static final int SaveSetUnmap = 1;
    private static final int X_SetInputFocus = 42;
    private static final int FULLSCREEN_LEGACY = 1;
    private static final int FULLSCREEN_NETWM = 2;
    private static final int WINDOWED = 3;
    private static int current_window_mode = 3;
    private static final int XRANDR = 10;
    private static final int XF86VIDMODE = 11;
    private static final int NONE = 12;
    private static long display;
    private static long current_window;
    private static long saved_error_handler;
    private static int display_connection_usage_count;
    private final LinuxEvent event_buffer = new LinuxEvent();
    private final LinuxEvent tmp_event_buffer = new LinuxEvent();
    private int current_displaymode_extension = 12;
    private long delete_atom;
    private PeerInfo peer_info;
    private ByteBuffer saved_gamma;
    private ByteBuffer current_gamma;
    private DisplayMode saved_mode;
    private DisplayMode current_mode;
    private boolean keyboard_grabbed;
    private boolean pointer_grabbed;
    private boolean input_released;
    private boolean grab;
    private boolean focused;
    private boolean minimized;
    private boolean dirty;
    private boolean close_requested;
    private long current_cursor;
    private long blank_cursor;
    private boolean mouseInside = true;
    private boolean resizable;
    private boolean resized;
    private int window_x;
    private int window_y;
    private int window_width;
    private int window_height;
    private Canvas parent;
    private long parent_window;
    private static boolean xembedded;
    private long parent_proxy_focus_window;
    private boolean parent_focused;
    private boolean parent_focus_changed;
    private long last_window_focus = 0L;
    private LinuxKeyboard keyboard;
    private LinuxMouse mouse;
    private String wm_class;
    private final FocusListener focus_listener = new FocusListener(){

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void focusGained(FocusEvent e) {
            Object object = GlobalLock.lock;
            synchronized (object) {
                LinuxDisplay.this.parent_focused = true;
                LinuxDisplay.this.parent_focus_changed = true;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public void focusLost(FocusEvent e) {
            Object object = GlobalLock.lock;
            synchronized (object) {
                LinuxDisplay.this.parent_focused = false;
                LinuxDisplay.this.parent_focus_changed = true;
            }
        }
    };

    LinuxDisplay() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static ByteBuffer getCurrentGammaRamp() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            block8: {
                ByteBuffer byteBuffer;
                LinuxDisplay.incDisplay();
                try {
                    if (!LinuxDisplay.isXF86VidModeSupported()) break block8;
                    byteBuffer = LinuxDisplay.nGetCurrentGammaRamp(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
                }
                catch (Throwable throwable) {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                LinuxDisplay.decDisplay();
                return byteBuffer;
            }
            ByteBuffer byteBuffer = null;
            LinuxDisplay.decDisplay();
            return byteBuffer;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native ByteBuffer nGetCurrentGammaRamp(long var0, int var2) throws LWJGLException;

    private static int getBestDisplayModeExtension() {
        int result;
        if (LinuxDisplay.isXrandrSupported()) {
            LWJGLUtil.log("Using Xrandr for display mode switching");
            result = 10;
        } else if (LinuxDisplay.isXF86VidModeSupported()) {
            LWJGLUtil.log("Using XF86VidMode for display mode switching");
            result = 11;
        } else {
            LWJGLUtil.log("No display mode extensions available");
            result = 12;
        }
        return result;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean isXrandrSupported() {
        if (Display.getPrivilegedBoolean("LWJGL_DISABLE_XRANDR")) {
            return false;
        }
        LinuxDisplay.lockAWT();
        try {
            boolean bl;
            LinuxDisplay.incDisplay();
            try {
                bl = LinuxDisplay.nIsXrandrSupported(LinuxDisplay.getDisplay());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Got exception while querying Xrandr support: " + e);
                    boolean bl2 = false;
                    return bl2;
                }
            }
            LinuxDisplay.decDisplay();
            return bl;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native boolean nIsXrandrSupported(long var0) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean isXF86VidModeSupported() {
        LinuxDisplay.lockAWT();
        try {
            boolean bl;
            LinuxDisplay.incDisplay();
            try {
                bl = LinuxDisplay.nIsXF86VidModeSupported(LinuxDisplay.getDisplay());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Got exception while querying XF86VM support: " + e);
                    boolean bl2 = false;
                    return bl2;
                }
            }
            LinuxDisplay.decDisplay();
            return bl;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native boolean nIsXF86VidModeSupported(long var0) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static boolean isNetWMFullscreenSupported() throws LWJGLException {
        if (Display.getPrivilegedBoolean("LWJGL_DISABLE_NETWM")) {
            return false;
        }
        LinuxDisplay.lockAWT();
        try {
            boolean bl;
            LinuxDisplay.incDisplay();
            try {
                bl = LinuxDisplay.nIsNetWMFullscreenSupported(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Got exception while querying NetWM support: " + e);
                    boolean bl2 = false;
                    return bl2;
                }
            }
            LinuxDisplay.decDisplay();
            return bl;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native boolean nIsNetWMFullscreenSupported(long var0, int var2) throws LWJGLException;

    static void lockAWT() {
        try {
            LinuxDisplay.nLockAWT();
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while locking AWT: " + e);
        }
    }

    private static native void nLockAWT() throws LWJGLException;

    static void unlockAWT() {
        try {
            LinuxDisplay.nUnlockAWT();
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while unlocking AWT: " + e);
        }
    }

    private static native void nUnlockAWT() throws LWJGLException;

    static void incDisplay() throws LWJGLException {
        if (display_connection_usage_count == 0) {
            try {
                org.lwjgl.opengl.GLContext.loadOpenGLLibrary();
                GLContext.loadOpenGLLibrary();
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            saved_error_handler = LinuxDisplay.setErrorHandler();
            display = LinuxDisplay.openDisplay();
        }
        ++display_connection_usage_count;
    }

    private static native int callErrorHandler(long var0, long var2, long var4);

    private static native long setErrorHandler();

    private static native long resetErrorHandler(long var0);

    private static native void synchronize(long var0, boolean var2);

    private static int globalErrorHandler(long display, long event_ptr, long error_display, long serial, long error_code, long request_code, long minor_code) throws LWJGLException {
        if (xembedded && request_code == 42L) {
            return 0;
        }
        if (display == LinuxDisplay.getDisplay()) {
            String error_msg = LinuxDisplay.getErrorText(display, error_code);
            throw new LWJGLException("X Error - disp: 0x" + Long.toHexString(error_display) + " serial: " + serial + " error: " + error_msg + " request_code: " + request_code + " minor_code: " + minor_code);
        }
        if (saved_error_handler != 0L) {
            return LinuxDisplay.callErrorHandler(saved_error_handler, display, event_ptr);
        }
        return 0;
    }

    private static native String getErrorText(long var0, long var2);

    static void decDisplay() {
    }

    static native long openDisplay() throws LWJGLException;

    static native void closeDisplay(long var0);

    private int getWindowMode(boolean fullscreen) throws LWJGLException {
        if (fullscreen) {
            if (this.current_displaymode_extension == 10 && LinuxDisplay.isNetWMFullscreenSupported()) {
                LWJGLUtil.log("Using NetWM for fullscreen window");
                return 2;
            }
            LWJGLUtil.log("Using legacy mode for fullscreen window");
            return 1;
        }
        return 3;
    }

    static long getDisplay() {
        if (display_connection_usage_count <= 0) {
            throw new InternalError("display_connection_usage_count = " + display_connection_usage_count);
        }
        return display;
    }

    static int getDefaultScreen() {
        return LinuxDisplay.nGetDefaultScreen(LinuxDisplay.getDisplay());
    }

    static native int nGetDefaultScreen(long var0);

    static long getWindow() {
        return current_window;
    }

    private void ungrabKeyboard() {
        if (this.keyboard_grabbed) {
            LinuxDisplay.nUngrabKeyboard(LinuxDisplay.getDisplay());
            this.keyboard_grabbed = false;
        }
    }

    static native int nUngrabKeyboard(long var0);

    private void grabKeyboard() {
        int res;
        if (!this.keyboard_grabbed && (res = LinuxDisplay.nGrabKeyboard(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow())) == 0) {
            this.keyboard_grabbed = true;
        }
    }

    static native int nGrabKeyboard(long var0, long var2);

    private void grabPointer() {
        int result;
        if (!this.pointer_grabbed && (result = LinuxDisplay.nGrabPointer(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), 0L)) == 0) {
            this.pointer_grabbed = true;
            if (LinuxDisplay.isLegacyFullscreen()) {
                LinuxDisplay.nSetViewPort(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), LinuxDisplay.getDefaultScreen());
            }
        }
    }

    static native int nGrabPointer(long var0, long var2, long var4);

    private static native void nSetViewPort(long var0, long var2, int var4);

    private void ungrabPointer() {
        if (this.pointer_grabbed) {
            this.pointer_grabbed = false;
            LinuxDisplay.nUngrabPointer(LinuxDisplay.getDisplay());
        }
    }

    static native int nUngrabPointer(long var0);

    private static boolean isFullscreen() {
        return current_window_mode == 1 || current_window_mode == 2;
    }

    private boolean shouldGrab() {
        return !this.input_released && this.grab && this.mouse != null;
    }

    private void updatePointerGrab() {
        if (LinuxDisplay.isFullscreen() || this.shouldGrab()) {
            this.grabPointer();
        } else {
            this.ungrabPointer();
        }
        this.updateCursor();
    }

    private void updateCursor() {
        long cursor = this.shouldGrab() ? this.blank_cursor : this.current_cursor;
        LinuxDisplay.nDefineCursor(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), cursor);
    }

    private static native void nDefineCursor(long var0, long var2, long var4);

    private static boolean isLegacyFullscreen() {
        return current_window_mode == 1;
    }

    private void updateKeyboardGrab() {
        if (LinuxDisplay.isLegacyFullscreen()) {
            this.grabKeyboard();
        } else {
            this.ungrabKeyboard();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void createWindow(DrawableLWJGL drawable, DisplayMode mode, Canvas parent, int x, int y) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplay.incDisplay();
            try {
                if (drawable instanceof DrawableGLES) {
                    this.peer_info = new LinuxDisplayPeerInfo();
                }
                ByteBuffer handle = this.peer_info.lockAndGetHandle();
                try {
                    current_window_mode = this.getWindowMode(Display.isFullscreen());
                    if (current_window_mode != 3) {
                        Compiz.setLegacyFullscreenSupport(true);
                    }
                    boolean undecorated = Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated") || current_window_mode != 3 && Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.undecorated_fs");
                    this.parent = parent;
                    this.parent_window = parent != null ? LinuxDisplay.getHandle(parent) : LinuxDisplay.getRootWindow(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
                    this.resizable = Display.isResizable();
                    this.resized = false;
                    this.window_x = x;
                    this.window_y = y;
                    this.window_width = mode.getWidth();
                    this.window_height = mode.getHeight();
                    if (mode.isFullscreenCapable() && this.current_displaymode_extension == 10) {
                        XRandR.Screen primaryScreen = XRandR.DisplayModetoScreen(Display.getDisplayMode());
                        x = primaryScreen.xPos;
                        y = primaryScreen.yPos;
                    }
                    current_window = LinuxDisplay.nCreateWindow(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), handle, mode, current_window_mode, x, y, undecorated, this.parent_window, this.resizable);
                    this.wm_class = Display.getPrivilegedString("LWJGL_WM_CLASS");
                    if (this.wm_class == null) {
                        this.wm_class = Display.getTitle();
                    }
                    this.setClassHint(Display.getTitle(), this.wm_class);
                    LinuxDisplay.mapRaised(LinuxDisplay.getDisplay(), current_window);
                    xembedded = parent != null && LinuxDisplay.isAncestorXEmbedded(this.parent_window);
                    this.blank_cursor = LinuxDisplay.createBlankCursor();
                    this.current_cursor = 0L;
                    this.focused = false;
                    this.input_released = false;
                    this.pointer_grabbed = false;
                    this.keyboard_grabbed = false;
                    this.close_requested = false;
                    this.grab = false;
                    this.minimized = false;
                    this.dirty = true;
                    if (drawable instanceof DrawableGLES) {
                        ((DrawableGLES)drawable).initialize(current_window, LinuxDisplay.getDisplay(), 4, (PixelFormat)drawable.getPixelFormat());
                    }
                    if (parent != null) {
                        parent.addFocusListener(this.focus_listener);
                        this.parent_focused = parent.isFocusOwner();
                        this.parent_focus_changed = true;
                    }
                }
                finally {
                    this.peer_info.unlock();
                }
            }
            catch (LWJGLException e) {
                LinuxDisplay.decDisplay();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native long nCreateWindow(long var0, int var2, ByteBuffer var3, DisplayMode var4, int var5, int var6, int var7, boolean var8, long var9, boolean var11) throws LWJGLException;

    private static native long getRootWindow(long var0, int var2);

    private static native boolean hasProperty(long var0, long var2, long var4);

    private static native long getParentWindow(long var0, long var2) throws LWJGLException;

    private static native int getChildCount(long var0, long var2) throws LWJGLException;

    private static native void mapRaised(long var0, long var2);

    private static native void reparentWindow(long var0, long var2, long var4, int var6, int var7);

    private static native long nGetInputFocus(long var0) throws LWJGLException;

    private static native void nSetInputFocus(long var0, long var2, long var4);

    private static native void nSetWindowSize(long var0, long var2, int var4, int var5, boolean var6);

    private static native int nGetX(long var0, long var2);

    private static native int nGetY(long var0, long var2);

    private static native int nGetWidth(long var0, long var2);

    private static native int nGetHeight(long var0, long var2);

    private static boolean isAncestorXEmbedded(long window) throws LWJGLException {
        long xembed_atom = LinuxDisplay.internAtom("_XEMBED_INFO", true);
        if (xembed_atom != 0L) {
            long w = window;
            while (w != 0L) {
                if (LinuxDisplay.hasProperty(LinuxDisplay.getDisplay(), w, xembed_atom)) {
                    return true;
                }
                w = LinuxDisplay.getParentWindow(LinuxDisplay.getDisplay(), w);
            }
        }
        return false;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static long getHandle(Canvas parent) throws LWJGLException {
        AWTCanvasImplementation awt_impl = AWTGLCanvas.createImplementation();
        LinuxPeerInfo parent_peer_info = (LinuxPeerInfo)awt_impl.createPeerInfo(parent, null, null);
        ByteBuffer parent_peer_info_handle = parent_peer_info.lockAndGetHandle();
        try {
            long l = parent_peer_info.getDrawable();
            return l;
        }
        finally {
            parent_peer_info.unlock();
        }
    }

    private void updateInputGrab() {
        this.updatePointerGrab();
        this.updateKeyboardGrab();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroyWindow() {
        LinuxDisplay.lockAWT();
        try {
            if (this.parent != null) {
                this.parent.removeFocusListener(this.focus_listener);
            }
            try {
                this.setNativeCursor(null);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to reset cursor: " + e.getMessage());
            }
            LinuxDisplay.nDestroyCursor(LinuxDisplay.getDisplay(), this.blank_cursor);
            this.blank_cursor = 0L;
            this.ungrabKeyboard();
            LinuxDisplay.nDestroyWindow(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
            LinuxDisplay.decDisplay();
            if (current_window_mode != 3) {
                Compiz.setLegacyFullscreenSupport(false);
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    static native void nDestroyWindow(long var0, long var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void switchDisplayMode(DisplayMode mode) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            this.switchDisplayModeOnTmpDisplay(mode);
            this.current_mode = mode;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void switchDisplayModeOnTmpDisplay(DisplayMode mode) throws LWJGLException {
        if (this.current_displaymode_extension == 10) {
            XRandR.setConfiguration(false, XRandR.DisplayModetoScreen(mode));
        } else {
            LinuxDisplay.incDisplay();
            try {
                LinuxDisplay.nSwitchDisplayMode(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.current_displaymode_extension, mode);
            }
            finally {
                LinuxDisplay.decDisplay();
            }
        }
    }

    private static native void nSwitchDisplayMode(long var0, int var2, int var3, DisplayMode var4) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static long internAtom(String atom_name, boolean only_if_exists) throws LWJGLException {
        LinuxDisplay.incDisplay();
        try {
            long l = LinuxDisplay.nInternAtom(LinuxDisplay.getDisplay(), atom_name, only_if_exists);
            return l;
        }
        finally {
            LinuxDisplay.decDisplay();
        }
    }

    static native long nInternAtom(long var0, String var2, boolean var3);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void resetDisplayMode() {
        LinuxDisplay.lockAWT();
        try {
            if (this.current_displaymode_extension == 10) {
                AccessController.doPrivileged(new PrivilegedAction<Object>(){

                    @Override
                    public Object run() {
                        XRandR.restoreConfiguration();
                        return null;
                    }
                });
            } else {
                this.switchDisplayMode(this.saved_mode);
            }
            if (LinuxDisplay.isXF86VidModeSupported()) {
                this.doSetGamma(this.saved_gamma);
            }
            Compiz.setLegacyFullscreenSupport(false);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Caught exception while resetting mode: " + e);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     * Loose catch block
     * Enabled aggressive exception aggregation
     */
    public int getGammaRampLength() {
        if (!LinuxDisplay.isXF86VidModeSupported()) {
            return 0;
        }
        LinuxDisplay.lockAWT();
        try {
            int n;
            LinuxDisplay.incDisplay();
            try {
                n = LinuxDisplay.nGetGammaRampLength(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Got exception while querying gamma length: " + e);
                int n2 = 0;
                LinuxDisplay.decDisplay();
                LinuxDisplay.unlockAWT();
                return n2;
                {
                    catch (Throwable throwable) {
                        try {
                            LinuxDisplay.decDisplay();
                            throw throwable;
                        }
                        catch (LWJGLException e2) {
                            LWJGLUtil.log("Failed to get gamma ramp length: " + e2);
                            int n3 = 0;
                            return n3;
                        }
                    }
                }
            }
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native int nGetGammaRampLength(long var0, int var2) throws LWJGLException;

    public void setGammaRamp(FloatBuffer gammaRamp) throws LWJGLException {
        if (!LinuxDisplay.isXF86VidModeSupported()) {
            throw new LWJGLException("No gamma ramp support (Missing XF86VM extension)");
        }
        this.doSetGamma(LinuxDisplay.convertToNativeRamp(gammaRamp));
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void doSetGamma(ByteBuffer native_gamma) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplay.setGammaRampOnTmpDisplay(native_gamma);
            this.current_gamma = native_gamma;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void setGammaRampOnTmpDisplay(ByteBuffer native_gamma) throws LWJGLException {
        LinuxDisplay.incDisplay();
        try {
            LinuxDisplay.nSetGammaRamp(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), native_gamma);
        }
        finally {
            LinuxDisplay.decDisplay();
        }
    }

    private static native void nSetGammaRamp(long var0, int var2, ByteBuffer var3) throws LWJGLException;

    private static ByteBuffer convertToNativeRamp(FloatBuffer ramp) throws LWJGLException {
        return LinuxDisplay.nConvertToNativeRamp(ramp, ramp.position(), ramp.remaining());
    }

    private static native ByteBuffer nConvertToNativeRamp(FloatBuffer var0, int var1, int var2) throws LWJGLException;

    public String getAdapter() {
        return null;
    }

    public String getVersion() {
        return null;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DisplayMode init() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            Compiz.init();
            this.delete_atom = LinuxDisplay.internAtom("WM_DELETE_WINDOW", false);
            this.current_displaymode_extension = LinuxDisplay.getBestDisplayModeExtension();
            if (this.current_displaymode_extension == 12) {
                throw new LWJGLException("No display mode extension is available");
            }
            DisplayMode[] modes = this.getAvailableDisplayModes();
            if (modes == null || modes.length == 0) {
                throw new LWJGLException("No modes available");
            }
            switch (this.current_displaymode_extension) {
                case 10: {
                    this.saved_mode = AccessController.doPrivileged(new PrivilegedAction<DisplayMode>(){

                        @Override
                        public DisplayMode run() {
                            XRandR.saveConfiguration();
                            return XRandR.ScreentoDisplayMode(XRandR.getConfiguration());
                        }
                    });
                    break;
                }
                case 11: {
                    this.saved_mode = modes[0];
                    break;
                }
                default: {
                    throw new LWJGLException("Unknown display mode extension: " + this.current_displaymode_extension);
                }
            }
            this.current_mode = this.saved_mode;
            this.current_gamma = this.saved_gamma = LinuxDisplay.getCurrentGammaRamp();
            DisplayMode displayMode = this.saved_mode;
            return displayMode;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static DisplayMode getCurrentXRandrMode() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            DisplayMode displayMode;
            LinuxDisplay.incDisplay();
            try {
                displayMode = LinuxDisplay.nGetCurrentXRandrMode(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
            }
            catch (Throwable throwable) {
                LinuxDisplay.decDisplay();
                throw throwable;
            }
            LinuxDisplay.decDisplay();
            return displayMode;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native DisplayMode nGetCurrentXRandrMode(long var0, int var2) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setTitle(String title) {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer titleText = MemoryUtil.encodeUTF8(title);
            LinuxDisplay.nSetTitle(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), MemoryUtil.getAddress(titleText), titleText.remaining() - 1);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nSetTitle(long var0, long var2, long var4, int var6);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setClassHint(String wm_name, String wm_class) {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer nameText = MemoryUtil.encodeUTF8(wm_name);
            ByteBuffer classText = MemoryUtil.encodeUTF8(wm_class);
            LinuxDisplay.nSetClassHint(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), MemoryUtil.getAddress(nameText), MemoryUtil.getAddress(classText));
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nSetClassHint(long var0, long var2, long var4, long var6);

    public boolean isCloseRequested() {
        boolean result = this.close_requested;
        this.close_requested = false;
        return result;
    }

    public boolean isVisible() {
        return !this.minimized;
    }

    public boolean isActive() {
        return this.focused || LinuxDisplay.isLegacyFullscreen();
    }

    public boolean isDirty() {
        boolean result = this.dirty;
        this.dirty = false;
        return result;
    }

    public PeerInfo createPeerInfo(org.lwjgl.opengl.PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
        this.peer_info = new LinuxDisplayPeerInfo(pixel_format);
        return this.peer_info;
    }

    private void relayEventToParent(LinuxEvent event_buffer, int event_mask) {
        this.tmp_event_buffer.copyFrom(event_buffer);
        this.tmp_event_buffer.setWindow(this.parent_window);
        this.tmp_event_buffer.sendEvent(LinuxDisplay.getDisplay(), this.parent_window, true, event_mask);
    }

    private void relayEventToParent(LinuxEvent event_buffer) {
        if (this.parent == null) {
            return;
        }
        switch (event_buffer.getType()) {
            case 2: {
                this.relayEventToParent(event_buffer, 1);
                break;
            }
            case 3: {
                this.relayEventToParent(event_buffer, 1);
                break;
            }
            case 4: {
                if (!xembedded && this.focused) break;
                this.relayEventToParent(event_buffer, 1);
                break;
            }
            case 5: {
                if (!xembedded && this.focused) break;
                this.relayEventToParent(event_buffer, 1);
                break;
            }
        }
    }

    private void processEvents() {
        while (LinuxEvent.getPending(LinuxDisplay.getDisplay()) > 0) {
            this.event_buffer.nextEvent(LinuxDisplay.getDisplay());
            long event_window = this.event_buffer.getWindow();
            this.relayEventToParent(this.event_buffer);
            if (event_window != LinuxDisplay.getWindow() || this.event_buffer.filterEvent(event_window) || this.mouse != null && this.mouse.filterEvent(this.grab, this.shouldWarpPointer(), this.event_buffer) || this.keyboard != null && this.keyboard.filterEvent(this.event_buffer)) continue;
            switch (this.event_buffer.getType()) {
                case 9: {
                    this.setFocused(true, this.event_buffer.getFocusDetail());
                    break;
                }
                case 10: {
                    this.setFocused(false, this.event_buffer.getFocusDetail());
                    break;
                }
                case 33: {
                    if (this.event_buffer.getClientFormat() != 32 || (long)this.event_buffer.getClientData(0) != this.delete_atom) break;
                    this.close_requested = true;
                    break;
                }
                case 19: {
                    this.dirty = true;
                    this.minimized = false;
                    break;
                }
                case 18: {
                    this.dirty = true;
                    this.minimized = true;
                    break;
                }
                case 12: {
                    this.dirty = true;
                    break;
                }
                case 22: {
                    int x = LinuxDisplay.nGetX(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
                    int y = LinuxDisplay.nGetY(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
                    int width = LinuxDisplay.nGetWidth(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
                    int height = LinuxDisplay.nGetHeight(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
                    this.window_x = x;
                    this.window_y = y;
                    if (this.window_width == width && this.window_height == height) break;
                    this.resized = true;
                    this.window_width = width;
                    this.window_height = height;
                    break;
                }
                case 7: {
                    this.mouseInside = true;
                    break;
                }
                case 8: {
                    this.mouseInside = false;
                    break;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void update() {
        LinuxDisplay.lockAWT();
        try {
            this.processEvents();
            this.checkInput();
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void reshape(int x, int y, int width, int height) {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplay.nReshape(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), x, y, width, height);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nReshape(long var0, long var2, int var4, int var5, int var6, int var7);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            DisplayMode[] arrdisplayMode;
            LinuxDisplay.incDisplay();
            if (this.current_displaymode_extension == 10) {
                DisplayMode[] nDisplayModes = LinuxDisplay.nGetAvailableDisplayModes(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.current_displaymode_extension);
                int bpp = 24;
                if (nDisplayModes.length > 0) {
                    bpp = nDisplayModes[0].getBitsPerPixel();
                }
                XRandR.Screen[] resolutions = XRandR.getResolutions(XRandR.getScreenNames()[0]);
                DisplayMode[] modes = new DisplayMode[resolutions.length];
                for (int i = 0; i < modes.length; ++i) {
                    modes[i] = new DisplayMode(resolutions[i].width, resolutions[i].height, bpp, resolutions[i].freq);
                }
                DisplayMode[] arrdisplayMode2 = modes;
                return arrdisplayMode2;
            }
            try {
                DisplayMode[] modes;
                arrdisplayMode = modes = LinuxDisplay.nGetAvailableDisplayModes(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen(), this.current_displaymode_extension);
            }
            catch (Throwable throwable) {
                LinuxDisplay.decDisplay();
                throw throwable;
            }
            LinuxDisplay.decDisplay();
            return arrdisplayMode;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native DisplayMode[] nGetAvailableDisplayModes(long var0, int var2, int var3) throws LWJGLException;

    public boolean hasWheel() {
        return true;
    }

    public int getButtonCount() {
        return this.mouse.getButtonCount();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void createMouse() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            this.mouse = new LinuxMouse(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), LinuxDisplay.getWindow());
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    public void destroyMouse() {
        this.mouse = null;
        this.updateInputGrab();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void pollMouse(IntBuffer coord_buffer, ByteBuffer buttons) {
        LinuxDisplay.lockAWT();
        try {
            this.mouse.poll(this.grab, coord_buffer, buttons);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void readMouse(ByteBuffer buffer) {
        LinuxDisplay.lockAWT();
        try {
            this.mouse.read(buffer);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setCursorPosition(int x, int y) {
        LinuxDisplay.lockAWT();
        try {
            this.mouse.setCursorPosition(x, y);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private void checkInput() {
        if (this.parent == null) {
            return;
        }
        if (xembedded) {
            long current_focus_window = 0L;
            if (this.last_window_focus != current_focus_window || this.parent_focused != this.focused) {
                if (this.isParentWindowActive(current_focus_window)) {
                    if (this.parent_focused) {
                        LinuxDisplay.nSetInputFocus(LinuxDisplay.getDisplay(), current_window, 0L);
                        this.last_window_focus = current_window;
                        this.focused = true;
                    } else {
                        LinuxDisplay.nSetInputFocus(LinuxDisplay.getDisplay(), this.parent_proxy_focus_window, 0L);
                        this.last_window_focus = this.parent_proxy_focus_window;
                        this.focused = false;
                    }
                } else {
                    this.last_window_focus = current_focus_window;
                    this.focused = false;
                }
            }
        } else if (this.parent_focus_changed && this.parent_focused) {
            this.setInputFocusUnsafe(LinuxDisplay.getWindow());
            this.parent_focus_changed = false;
        }
    }

    private void setInputFocusUnsafe(long window) {
        try {
            LinuxDisplay.nSetInputFocus(LinuxDisplay.getDisplay(), window, 0L);
            LinuxDisplay.nSync(LinuxDisplay.getDisplay(), false);
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Got exception while trying to focus: " + e);
        }
    }

    private static native void nSync(long var0, boolean var2) throws LWJGLException;

    private boolean isParentWindowActive(long window) {
        try {
            if (window == current_window) {
                return true;
            }
            if (LinuxDisplay.getChildCount(LinuxDisplay.getDisplay(), window) != 0) {
                return false;
            }
            long parent_window = LinuxDisplay.getParentWindow(LinuxDisplay.getDisplay(), window);
            if (parent_window == 0L) {
                return false;
            }
            long w = current_window;
            while (w != 0L) {
                w = LinuxDisplay.getParentWindow(LinuxDisplay.getDisplay(), w);
                if (w != parent_window) continue;
                this.parent_proxy_focus_window = window;
                return true;
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Failed to detect if parent window is active: " + e.getMessage());
            return true;
        }
        return false;
    }

    private void setFocused(boolean got_focus, int focus_detail) {
        if (this.focused == got_focus || focus_detail == 7 || focus_detail == 5 || focus_detail == 6 || xembedded) {
            return;
        }
        this.focused = got_focus;
        if (this.focused) {
            this.acquireInput();
        } else {
            this.releaseInput();
        }
    }

    private void releaseInput() {
        if (LinuxDisplay.isLegacyFullscreen() || this.input_released) {
            return;
        }
        if (this.keyboard != null) {
            this.keyboard.releaseAll();
        }
        this.input_released = true;
        this.updateInputGrab();
        if (current_window_mode == 2) {
            LinuxDisplay.nIconifyWindow(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), LinuxDisplay.getDefaultScreen());
            try {
                if (this.current_displaymode_extension == 10) {
                    AccessController.doPrivileged(new PrivilegedAction<Object>(){

                        @Override
                        public Object run() {
                            XRandR.restoreConfiguration();
                            return null;
                        }
                    });
                } else {
                    this.switchDisplayModeOnTmpDisplay(this.saved_mode);
                }
                LinuxDisplay.setGammaRampOnTmpDisplay(this.saved_gamma);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to restore saved mode: " + e.getMessage());
            }
        }
    }

    private static native void nIconifyWindow(long var0, long var2, int var4);

    private void acquireInput() {
        if (LinuxDisplay.isLegacyFullscreen() || !this.input_released) {
            return;
        }
        this.input_released = false;
        this.updateInputGrab();
        if (current_window_mode == 2) {
            try {
                this.switchDisplayModeOnTmpDisplay(this.current_mode);
                LinuxDisplay.setGammaRampOnTmpDisplay(this.current_gamma);
            }
            catch (LWJGLException e) {
                LWJGLUtil.log("Failed to restore mode: " + e.getMessage());
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void grabMouse(boolean new_grab) {
        LinuxDisplay.lockAWT();
        try {
            if (new_grab != this.grab) {
                this.grab = new_grab;
                this.updateInputGrab();
                this.mouse.changeGrabbed(this.grab, this.shouldWarpPointer());
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private boolean shouldWarpPointer() {
        return this.pointer_grabbed && this.shouldGrab();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getNativeCursorCapabilities() {
        LinuxDisplay.lockAWT();
        try {
            int n;
            LinuxDisplay.incDisplay();
            try {
                n = LinuxDisplay.nGetNativeCursorCapabilities(LinuxDisplay.getDisplay());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native int nGetNativeCursorCapabilities(long var0) throws LWJGLException;

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void setNativeCursor(Object handle) throws LWJGLException {
        this.current_cursor = LinuxDisplay.getCursorHandle(handle);
        LinuxDisplay.lockAWT();
        try {
            this.updateCursor();
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getMinCursorSize() {
        LinuxDisplay.lockAWT();
        try {
            int n;
            LinuxDisplay.incDisplay();
            try {
                n = LinuxDisplay.nGetMinCursorSize(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Exception occurred in getMinCursorSize: " + e);
                    int n2 = 0;
                    return n2;
                }
            }
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native int nGetMinCursorSize(long var0, long var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getMaxCursorSize() {
        LinuxDisplay.lockAWT();
        try {
            int n;
            LinuxDisplay.incDisplay();
            try {
                n = LinuxDisplay.nGetMaxCursorSize(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Exception occurred in getMaxCursorSize: " + e);
                    int n2 = 0;
                    return n2;
                }
            }
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native int nGetMaxCursorSize(long var0, long var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void createKeyboard() throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            this.keyboard = new LinuxKeyboard(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroyKeyboard() {
        LinuxDisplay.lockAWT();
        try {
            this.keyboard.destroy(LinuxDisplay.getDisplay());
            this.keyboard = null;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void pollKeyboard(ByteBuffer keyDownBuffer) {
        LinuxDisplay.lockAWT();
        try {
            this.keyboard.poll(keyDownBuffer);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void readKeyboard(ByteBuffer buffer) {
        LinuxDisplay.lockAWT();
        try {
            this.keyboard.read(buffer);
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native long nCreateCursor(long var0, int var2, int var3, int var4, int var5, int var6, IntBuffer var7, int var8, IntBuffer var9, int var10) throws LWJGLException;

    private static long createBlankCursor() {
        return LinuxDisplay.nCreateBlankCursor(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow());
    }

    static native long nCreateBlankCursor(long var0, long var2);

    public Object createCursor(int width, int height, int xHotspot, int yHotspot, int numImages, IntBuffer images, IntBuffer delays) throws LWJGLException {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplay.incDisplay();
            try {
                long cursor = LinuxDisplay.nCreateCursor(LinuxDisplay.getDisplay(), width, height, xHotspot, yHotspot, numImages, images, images.position(), delays, delays != null ? delays.position() : -1);
                Long l = cursor;
                return l;
            }
            catch (LWJGLException e) {
                LinuxDisplay.decDisplay();
                throw e;
            }
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static long getCursorHandle(Object cursor_handle) {
        return cursor_handle != null ? (Long)cursor_handle : 0L;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void destroyCursor(Object cursorHandle) {
        LinuxDisplay.lockAWT();
        try {
            LinuxDisplay.nDestroyCursor(LinuxDisplay.getDisplay(), LinuxDisplay.getCursorHandle(cursorHandle));
            LinuxDisplay.decDisplay();
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    static native void nDestroyCursor(long var0, long var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getPbufferCapabilities() {
        LinuxDisplay.lockAWT();
        try {
            int n;
            LinuxDisplay.incDisplay();
            try {
                n = LinuxDisplay.nGetPbufferCapabilities(LinuxDisplay.getDisplay(), LinuxDisplay.getDefaultScreen());
            }
            catch (Throwable throwable) {
                try {
                    LinuxDisplay.decDisplay();
                    throw throwable;
                }
                catch (LWJGLException e) {
                    LWJGLUtil.log("Exception occurred in getPbufferCapabilities: " + e);
                    int n2 = 0;
                    return n2;
                }
            }
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native int nGetPbufferCapabilities(long var0, int var2);

    public boolean isBufferLost(PeerInfo handle) {
        return false;
    }

    public PeerInfo createPbuffer(int width, int height, org.lwjgl.opengl.PixelFormat pixel_format, ContextAttribs attribs, IntBuffer pixelFormatCaps, IntBuffer pBufferAttribs) throws LWJGLException {
        return new LinuxPbufferPeerInfo(width, height, pixel_format);
    }

    public void setPbufferAttrib(PeerInfo handle, int attrib, int value) {
        throw new UnsupportedOperationException();
    }

    public void bindTexImageToPbuffer(PeerInfo handle, int buffer) {
        throw new UnsupportedOperationException();
    }

    public void releaseTexImageFromPbuffer(PeerInfo handle, int buffer) {
        throw new UnsupportedOperationException();
    }

    private static ByteBuffer convertIcons(ByteBuffer[] icons) {
        int bufferSize = 0;
        for (ByteBuffer icon : icons) {
            int size = icon.limit() / 4;
            int dimension = (int)Math.sqrt(size);
            if (dimension <= 0) continue;
            bufferSize += 8;
            bufferSize += dimension * dimension * 4;
        }
        if (bufferSize == 0) {
            return null;
        }
        ByteBuffer icon_argb = BufferUtils.createByteBuffer(bufferSize);
        icon_argb.order(ByteOrder.BIG_ENDIAN);
        for (ByteBuffer icon : icons) {
            int size = icon.limit() / 4;
            int dimension = (int)Math.sqrt(size);
            icon_argb.putInt(dimension);
            icon_argb.putInt(dimension);
            for (int y = 0; y < dimension; ++y) {
                for (int x = 0; x < dimension; ++x) {
                    byte r = icon.get(x * 4 + y * dimension * 4);
                    byte g = icon.get(x * 4 + y * dimension * 4 + 1);
                    byte b = icon.get(x * 4 + y * dimension * 4 + 2);
                    byte a = icon.get(x * 4 + y * dimension * 4 + 3);
                    icon_argb.put(a);
                    icon_argb.put(r);
                    icon_argb.put(g);
                    icon_argb.put(b);
                }
            }
        }
        return icon_argb;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int setIcon(ByteBuffer[] icons) {
        LinuxDisplay.lockAWT();
        try {
            ByteBuffer icons_data;
            block10: {
                int n;
                LinuxDisplay.incDisplay();
                try {
                    icons_data = LinuxDisplay.convertIcons(icons);
                    if (icons_data != null) break block10;
                    n = 0;
                }
                catch (Throwable throwable) {
                    try {
                        LinuxDisplay.decDisplay();
                        throw throwable;
                    }
                    catch (LWJGLException e) {
                        LWJGLUtil.log("Failed to set display icon: " + e);
                        int n2 = 0;
                        return n2;
                    }
                }
                LinuxDisplay.decDisplay();
                return n;
            }
            LinuxDisplay.nSetWindowIcon(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), icons_data, icons_data.capacity());
            int n = icons.length;
            LinuxDisplay.decDisplay();
            return n;
        }
        finally {
            LinuxDisplay.unlockAWT();
        }
    }

    private static native void nSetWindowIcon(long var0, long var2, ByteBuffer var4, int var5);

    public int getX() {
        return this.window_x;
    }

    public int getY() {
        return this.window_y;
    }

    public int getWidth() {
        return this.window_width;
    }

    public int getHeight() {
        return this.window_height;
    }

    public boolean isInsideWindow() {
        return this.mouseInside;
    }

    public void setResizable(boolean resizable) {
        if (this.resizable == resizable) {
            return;
        }
        this.resizable = resizable;
        LinuxDisplay.nSetWindowSize(LinuxDisplay.getDisplay(), LinuxDisplay.getWindow(), this.window_width, this.window_height, resizable);
    }

    public boolean wasResized() {
        if (this.resized) {
            this.resized = false;
            return true;
        }
        return false;
    }

    public float getPixelScaleFactor() {
        return 1.0f;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static final class Compiz {
        private static boolean applyFix;
        private static Provider provider;

        private Compiz() {
        }

        static void init() {
            if (Display.getPrivilegedBoolean("org.lwjgl.opengl.Window.nocompiz_lfs")) {
                return;
            }
            AccessController.doPrivileged(new PrivilegedAction<Object>(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                @Override
                public Object run() {
                    try {
                        if (!Compiz.isProcessActive("compiz")) {
                            Object var1_1 = null;
                            return var1_1;
                        }
                        provider = null;
                        String providerName = null;
                        if (Compiz.isProcessActive("dbus-daemon")) {
                            providerName = "Dbus";
                            provider = new Provider(){
                                private static final String KEY = "/org/freedesktop/compiz/workarounds/allscreens/legacy_fullscreen";

                                public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                    List output = Compiz.run(new String[]{"dbus-send", "--print-reply", "--type=method_call", "--dest=org.freedesktop.compiz", KEY, "org.freedesktop.compiz.get"});
                                    if (output == null || output.size() < 2) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    String line = (String)output.get(0);
                                    if (!line.startsWith("method return")) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    line = ((String)output.get(1)).trim();
                                    if (!line.startsWith("boolean") || line.length() < 12) {
                                        throw new LWJGLException("Invalid Dbus reply.");
                                    }
                                    return "true".equalsIgnoreCase(line.substring("boolean".length() + 1));
                                }

                                public void setLegacyFullscreenSupport(boolean state) throws LWJGLException {
                                    if (Compiz.run(new String[]{"dbus-send", "--type=method_call", "--dest=org.freedesktop.compiz", KEY, "org.freedesktop.compiz.set", "boolean:" + Boolean.toString(state)}) == null) {
                                        throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                    }
                                }
                            };
                        } else {
                            try {
                                Runtime.getRuntime().exec("gconftool");
                                providerName = "gconftool";
                                provider = new Provider(){
                                    private static final String KEY = "/apps/compiz/plugins/workarounds/allscreens/options/legacy_fullscreen";

                                    public boolean hasLegacyFullscreenSupport() throws LWJGLException {
                                        List output = Compiz.run(new String[]{"gconftool", "-g", KEY});
                                        if (output == null || output.size() == 0) {
                                            throw new LWJGLException("Invalid gconftool reply.");
                                        }
                                        return Boolean.parseBoolean(((String)output.get(0)).trim());
                                    }

                                    public void setLegacyFullscreenSupport(boolean state) throws LWJGLException {
                                        if (Compiz.run(new String[]{"gconftool", "-s", KEY, "-s", Boolean.toString(state), "-t", "bool"}) == null) {
                                            throw new LWJGLException("Failed to apply Compiz LFS workaround.");
                                        }
                                        if (state) {
                                            try {
                                                Thread.sleep(200L);
                                            }
                                            catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                };
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                        }
                        if (provider != null) {
                            if (!provider.hasLegacyFullscreenSupport()) {
                                applyFix = true;
                                LWJGLUtil.log("Using " + providerName + " to apply Compiz LFS workaround.");
                            }
                        }
                    }
                    catch (LWJGLException lWJGLException) {}
                    finally {
                        return null;
                    }
                }
            });
        }

        static void setLegacyFullscreenSupport(final boolean enabled) {
            if (!applyFix) {
                return;
            }
            AccessController.doPrivileged(new PrivilegedAction<Object>(){

                @Override
                public Object run() {
                    try {
                        provider.setLegacyFullscreenSupport(enabled);
                    }
                    catch (LWJGLException e) {
                        LWJGLUtil.log("Failed to change Compiz Legacy Fullscreen Support. Reason: " + e.getMessage());
                    }
                    return null;
                }
            });
        }

        private static List<String> run(String ... command) throws LWJGLException {
            ArrayList<String> output = new ArrayList<String>();
            try {
                String line;
                Process p = Runtime.getRuntime().exec(command);
                try {
                    int exitValue = p.waitFor();
                    if (exitValue != 0) {
                        return null;
                    }
                }
                catch (InterruptedException e) {
                    throw new LWJGLException("Process interrupted.", e);
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((line = br.readLine()) != null) {
                    output.add(line);
                }
                br.close();
            }
            catch (IOException e) {
                throw new LWJGLException("Process failed.", e);
            }
            return output;
        }

        private static boolean isProcessActive(String processName) throws LWJGLException {
            List<String> output = Compiz.run("ps", "-C", processName);
            if (output == null) {
                return false;
            }
            for (String line : output) {
                if (!line.contains(processName)) continue;
                return true;
            }
            return false;
        }

        private static interface Provider {
            public boolean hasLegacyFullscreenSupport() throws LWJGLException;

            public void setLegacyFullscreenSupport(boolean var1) throws LWJGLException;
        }
    }
}

