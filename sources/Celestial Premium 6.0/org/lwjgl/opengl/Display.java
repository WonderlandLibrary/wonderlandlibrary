/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Canvas;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashSet;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.Sys;
import org.lwjgl.input.Controllers;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Context;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.ContextGL;
import org.lwjgl.opengl.DisplayImplementation;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.Drawable;
import org.lwjgl.opengl.DrawableGL;
import org.lwjgl.opengl.DrawableGLES;
import org.lwjgl.opengl.DrawableLWJGL;
import org.lwjgl.opengl.GlobalLock;
import org.lwjgl.opengl.LinuxDisplay;
import org.lwjgl.opengl.MacOSXDisplay;
import org.lwjgl.opengl.OpenGLException;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.opengl.PixelFormatLWJGL;
import org.lwjgl.opengl.Sync;
import org.lwjgl.opengl.WindowsDisplay;

public final class Display {
    private static final Thread shutdown_hook = new Thread(){

        public void run() {
            Display.reset();
        }
    };
    private static final DisplayImplementation display_impl;
    private static final DisplayMode initial_mode;
    private static Canvas parent;
    private static DisplayMode current_mode;
    private static int x;
    private static ByteBuffer[] cached_icons;
    private static int y;
    private static int width;
    private static int height;
    private static String title;
    private static boolean fullscreen;
    private static int swap_interval;
    private static DrawableLWJGL drawable;
    private static boolean window_created;
    private static boolean parent_resized;
    private static boolean window_resized;
    private static boolean window_resizable;
    private static float r;
    private static float g;
    private static float b;
    private static final ComponentListener component_listener;

    public static Drawable getDrawable() {
        return drawable;
    }

    private static DisplayImplementation createDisplayImplementation() {
        switch (LWJGLUtil.getPlatform()) {
            case 1: {
                return new LinuxDisplay();
            }
            case 3: {
                return new WindowsDisplay();
            }
            case 2: {
                return new MacOSXDisplay();
            }
        }
        throw new IllegalStateException("Unsupported platform");
    }

    private Display() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static DisplayMode[] getAvailableDisplayModes() throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            DisplayMode[] unfilteredModes = display_impl.getAvailableDisplayModes();
            if (unfilteredModes == null) {
                return new DisplayMode[0];
            }
            HashSet<DisplayMode> modes = new HashSet<DisplayMode>(unfilteredModes.length);
            modes.addAll(Arrays.asList(unfilteredModes));
            DisplayMode[] filteredModes = new DisplayMode[modes.size()];
            modes.toArray(filteredModes);
            LWJGLUtil.log("Removed " + (unfilteredModes.length - filteredModes.length) + " duplicate displaymodes");
            return filteredModes;
        }
    }

    public static DisplayMode getDesktopDisplayMode() {
        return initial_mode;
    }

    public static DisplayMode getDisplayMode() {
        return current_mode;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setDisplayMode(DisplayMode mode) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (mode == null) {
                throw new NullPointerException("mode must be non-null");
            }
            boolean was_fullscreen = Display.isFullscreen();
            current_mode = mode;
            if (Display.isCreated()) {
                Display.destroyWindow();
                try {
                    if (was_fullscreen && !Display.isFullscreen()) {
                        display_impl.resetDisplayMode();
                    } else if (Display.isFullscreen()) {
                        Display.switchDisplayMode();
                    }
                    Display.createWindow();
                    Display.makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }

    private static DisplayMode getEffectiveMode() {
        return !Display.isFullscreen() && parent != null ? new DisplayMode(parent.getWidth(), parent.getHeight()) : current_mode;
    }

    private static int getWindowX() {
        if (!Display.isFullscreen() && parent == null) {
            if (x == -1) {
                return Math.max(0, (initial_mode.getWidth() - current_mode.getWidth()) / 2);
            }
            return x;
        }
        return 0;
    }

    private static int getWindowY() {
        if (!Display.isFullscreen() && parent == null) {
            if (y == -1) {
                return Math.max(0, (initial_mode.getHeight() - current_mode.getHeight()) / 2);
            }
            return y;
        }
        return 0;
    }

    private static void createWindow() throws LWJGLException {
        Canvas tmp_parent;
        if (window_created) {
            return;
        }
        Canvas canvas = tmp_parent = Display.isFullscreen() ? null : parent;
        if (tmp_parent != null && !tmp_parent.isDisplayable()) {
            throw new LWJGLException("Parent.isDisplayable() must be true");
        }
        if (tmp_parent != null) {
            tmp_parent.addComponentListener(component_listener);
        }
        DisplayMode mode = Display.getEffectiveMode();
        display_impl.createWindow(drawable, mode, tmp_parent, Display.getWindowX(), Display.getWindowY());
        window_created = true;
        width = Display.getDisplayMode().getWidth();
        height = Display.getDisplayMode().getHeight();
        Display.setTitle(title);
        Display.initControls();
        if (cached_icons != null) {
            Display.setIcon(cached_icons);
        } else {
            Display.setIcon(new ByteBuffer[]{LWJGLUtil.LWJGLIcon32x32, LWJGLUtil.LWJGLIcon16x16});
        }
    }

    private static void releaseDrawable() {
        try {
            Context context = drawable.getContext();
            if (context != null && context.isCurrent()) {
                context.releaseCurrent();
                context.releaseDrawable();
            }
        }
        catch (LWJGLException e) {
            LWJGLUtil.log("Exception occurred while trying to release context: " + e);
        }
    }

    private static void destroyWindow() {
        if (!window_created) {
            return;
        }
        if (parent != null) {
            parent.removeComponentListener(component_listener);
        }
        Display.releaseDrawable();
        if (Mouse.isCreated()) {
            Mouse.destroy();
        }
        if (Keyboard.isCreated()) {
            Keyboard.destroy();
        }
        display_impl.destroyWindow();
        window_created = false;
    }

    private static void switchDisplayMode() throws LWJGLException {
        if (!current_mode.isFullscreenCapable()) {
            throw new IllegalStateException("Only modes acquired from getAvailableDisplayModes() can be used for fullscreen display");
        }
        display_impl.switchDisplayMode(current_mode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setDisplayConfiguration(float gamma, float brightness, float contrast) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new LWJGLException("Display not yet created.");
            }
            if (brightness < -1.0f || brightness > 1.0f) {
                throw new IllegalArgumentException("Invalid brightness value");
            }
            if (contrast < 0.0f) {
                throw new IllegalArgumentException("Invalid contrast value");
            }
            int rampSize = display_impl.getGammaRampLength();
            if (rampSize == 0) {
                throw new LWJGLException("Display configuration not supported");
            }
            FloatBuffer gammaRamp = BufferUtils.createFloatBuffer(rampSize);
            for (int i = 0; i < rampSize; ++i) {
                float intensity = (float)i / (float)(rampSize - 1);
                float rampEntry = (float)Math.pow(intensity, gamma);
                rampEntry += brightness;
                if ((rampEntry = (rampEntry - 0.5f) * contrast + 0.5f) > 1.0f) {
                    rampEntry = 1.0f;
                } else if (rampEntry < 0.0f) {
                    rampEntry = 0.0f;
                }
                gammaRamp.put(i, rampEntry);
            }
            display_impl.setGammaRamp(gammaRamp);
            LWJGLUtil.log("Gamma set, gamma = " + gamma + ", brightness = " + brightness + ", contrast = " + contrast);
        }
    }

    public static void sync(int fps) {
        Sync.sync(fps);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getTitle() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return title;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Canvas getParent() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return parent;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setParent(Canvas parent) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (Display.parent != parent) {
                Display.parent = parent;
                if (!Display.isCreated()) {
                    return;
                }
                Display.destroyWindow();
                try {
                    if (Display.isFullscreen()) {
                        Display.switchDisplayMode();
                    } else {
                        display_impl.resetDisplayMode();
                    }
                    Display.createWindow();
                    Display.makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }

    public static void setFullscreen(boolean fullscreen) throws LWJGLException {
        Display.setDisplayModeAndFullscreenInternal(fullscreen, current_mode);
    }

    public static void setDisplayModeAndFullscreen(DisplayMode mode) throws LWJGLException {
        Display.setDisplayModeAndFullscreenInternal(mode.isFullscreenCapable(), mode);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static void setDisplayModeAndFullscreenInternal(boolean fullscreen, DisplayMode mode) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (mode == null) {
                throw new NullPointerException("mode must be non-null");
            }
            DisplayMode old_mode = current_mode;
            current_mode = mode;
            boolean was_fullscreen = Display.isFullscreen();
            Display.fullscreen = fullscreen;
            if (was_fullscreen != Display.isFullscreen() || !mode.equals(old_mode)) {
                if (!Display.isCreated()) {
                    return;
                }
                Display.destroyWindow();
                try {
                    if (Display.isFullscreen()) {
                        Display.switchDisplayMode();
                    } else {
                        display_impl.resetDisplayMode();
                    }
                    Display.createWindow();
                    Display.makeCurrentAndSetSwapInterval();
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    display_impl.resetDisplayMode();
                    throw e;
                }
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isFullscreen() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return fullscreen && current_mode.isFullscreenCapable();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setTitle(String newTitle) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (newTitle == null) {
                newTitle = "";
            }
            title = newTitle;
            if (Display.isCreated()) {
                display_impl.setTitle(title);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isCloseRequested() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Cannot determine close requested state of uncreated window");
            }
            return display_impl.isCloseRequested();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isVisible() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Cannot determine minimized state of uncreated window");
            }
            return display_impl.isVisible();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isActive() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Cannot determine focused state of uncreated window");
            }
            return display_impl.isActive();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isDirty() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Cannot determine dirty state of uncreated window");
            }
            return display_impl.isDirty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void processMessages() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            display_impl.update();
        }
        Display.pollDevices();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void swapBuffers() throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            if (LWJGLUtil.DEBUG) {
                drawable.checkGLError();
            }
            drawable.swapBuffers();
        }
    }

    public static void update() {
        Display.update(true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void update(boolean processMessages) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display not created");
            }
            if (display_impl.isVisible() || display_impl.isDirty()) {
                try {
                    Display.swapBuffers();
                }
                catch (LWJGLException e) {
                    throw new RuntimeException(e);
                }
            }
            boolean bl = window_resized = !Display.isFullscreen() && parent == null && display_impl.wasResized();
            if (window_resized) {
                width = display_impl.getWidth();
                height = display_impl.getHeight();
            }
            if (parent_resized) {
                Display.reshape();
                parent_resized = false;
                window_resized = true;
            }
            if (processMessages) {
                Display.processMessages();
            }
        }
    }

    static void pollDevices() {
        if (Mouse.isCreated()) {
            Mouse.poll();
            Mouse.updateCursor();
        }
        if (Keyboard.isCreated()) {
            Keyboard.poll();
        }
        if (Controllers.isCreated()) {
            Controllers.poll();
        }
    }

    public static void releaseContext() throws LWJGLException {
        drawable.releaseContext();
    }

    public static boolean isCurrent() throws LWJGLException {
        return drawable.isCurrent();
    }

    public static void makeCurrent() throws LWJGLException {
        drawable.makeCurrent();
    }

    private static void removeShutdownHook() {
        AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                Runtime.getRuntime().removeShutdownHook(shutdown_hook);
                return null;
            }
        });
    }

    private static void registerShutdownHook() {
        AccessController.doPrivileged(new PrivilegedAction<Object>(){

            @Override
            public Object run() {
                Runtime.getRuntime().addShutdownHook(shutdown_hook);
                return null;
            }
        });
    }

    public static void create() throws LWJGLException {
        Display.create(new PixelFormat());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormat pixel_format) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, null, (ContextAttribs)null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormat pixel_format, Drawable shared_drawable) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, shared_drawable, (ContextAttribs)null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormat pixel_format, ContextAttribs attribs) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, null, attribs);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormat pixel_format, Drawable shared_drawable, ContextAttribs attribs) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (Display.isCreated()) {
                throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
            }
            if (pixel_format == null) {
                throw new NullPointerException("pixel_format cannot be null");
            }
            Display.removeShutdownHook();
            Display.registerShutdownHook();
            if (Display.isFullscreen()) {
                Display.switchDisplayMode();
            }
            DrawableGL drawable = new DrawableGL(){

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                public void destroy() {
                    Object object = GlobalLock.lock;
                    synchronized (object) {
                        if (!Display.isCreated()) {
                            return;
                        }
                        Display.releaseDrawable();
                        super.destroy();
                        Display.destroyWindow();
                        x = (y = -1);
                        Display.access$702(null);
                        Display.reset();
                        Display.removeShutdownHook();
                    }
                }
            };
            Display.drawable = drawable;
            try {
                drawable.setPixelFormat(pixel_format, attribs);
                try {
                    Display.createWindow();
                    try {
                        drawable.context = new ContextGL(drawable.peer_info, attribs, shared_drawable != null ? ((DrawableGL)shared_drawable).getContext() : null);
                        try {
                            Display.makeCurrentAndSetSwapInterval();
                            Display.initContext();
                        }
                        catch (LWJGLException e) {
                            drawable.destroy();
                            throw e;
                        }
                    }
                    catch (LWJGLException e) {
                        Display.destroyWindow();
                        throw e;
                    }
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                display_impl.resetDisplayMode();
                throw e;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormatLWJGL pixel_format) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, null, null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, shared_drawable, null);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormatLWJGL pixel_format, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.create(pixel_format, null, attribs);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create(PixelFormatLWJGL pixel_format, Drawable shared_drawable, org.lwjgl.opengles.ContextAttribs attribs) throws LWJGLException {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (Display.isCreated()) {
                throw new IllegalStateException("Only one LWJGL context may be instantiated at any one time.");
            }
            if (pixel_format == null) {
                throw new NullPointerException("pixel_format cannot be null");
            }
            Display.removeShutdownHook();
            Display.registerShutdownHook();
            if (Display.isFullscreen()) {
                Display.switchDisplayMode();
            }
            DrawableGLES drawable = new DrawableGLES(){

                public void setPixelFormat(PixelFormatLWJGL pf, ContextAttribs attribs) throws LWJGLException {
                    throw new UnsupportedOperationException();
                }

                /*
                 * WARNING - Removed try catching itself - possible behaviour change.
                 */
                public void destroy() {
                    Object object = GlobalLock.lock;
                    synchronized (object) {
                        if (!Display.isCreated()) {
                            return;
                        }
                        Display.releaseDrawable();
                        super.destroy();
                        Display.destroyWindow();
                        x = (y = -1);
                        Display.access$702(null);
                        Display.reset();
                        Display.removeShutdownHook();
                    }
                }
            };
            Display.drawable = drawable;
            try {
                drawable.setPixelFormat(pixel_format);
                try {
                    Display.createWindow();
                    try {
                        drawable.createContext(attribs, shared_drawable);
                        try {
                            Display.makeCurrentAndSetSwapInterval();
                            Display.initContext();
                        }
                        catch (LWJGLException e) {
                            drawable.destroy();
                            throw e;
                        }
                    }
                    catch (LWJGLException e) {
                        Display.destroyWindow();
                        throw e;
                    }
                }
                catch (LWJGLException e) {
                    drawable.destroy();
                    throw e;
                }
            }
            catch (LWJGLException e) {
                display_impl.resetDisplayMode();
                throw e;
            }
        }
    }

    public static void setInitialBackground(float red, float green, float blue) {
        r = red;
        g = green;
        b = blue;
    }

    private static void makeCurrentAndSetSwapInterval() throws LWJGLException {
        Display.makeCurrent();
        try {
            drawable.checkGLError();
        }
        catch (OpenGLException e) {
            LWJGLUtil.log("OpenGL error during context creation: " + e.getMessage());
        }
        Display.setSwapInterval(swap_interval);
    }

    private static void initContext() {
        drawable.initContext(r, g, b);
        Display.update();
    }

    static DisplayImplementation getImplementation() {
        return display_impl;
    }

    static boolean getPrivilegedBoolean(final String property_name) {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>(){

            @Override
            public Boolean run() {
                return Boolean.getBoolean(property_name);
            }
        });
    }

    static String getPrivilegedString(final String property_name) {
        return AccessController.doPrivileged(new PrivilegedAction<String>(){

            @Override
            public String run() {
                return System.getProperty(property_name);
            }
        });
    }

    private static void initControls() {
        if (!Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.noinput")) {
            if (!Mouse.isCreated() && !Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.nomouse")) {
                try {
                    Mouse.create();
                }
                catch (LWJGLException e) {
                    if (LWJGLUtil.DEBUG) {
                        e.printStackTrace(System.err);
                    }
                    LWJGLUtil.log("Failed to create Mouse: " + e);
                }
            }
            if (!Keyboard.isCreated() && !Display.getPrivilegedBoolean("org.lwjgl.opengl.Display.nokeyboard")) {
                try {
                    Keyboard.create();
                }
                catch (LWJGLException e) {
                    if (LWJGLUtil.DEBUG) {
                        e.printStackTrace(System.err);
                    }
                    LWJGLUtil.log("Failed to create Keyboard: " + e);
                }
            }
        }
    }

    public static void destroy() {
        if (Display.isCreated()) {
            drawable.destroy();
        }
    }

    private static void reset() {
        display_impl.resetDisplayMode();
        current_mode = initial_mode;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isCreated() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return window_created;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setSwapInterval(int value) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            swap_interval = value;
            if (Display.isCreated()) {
                drawable.setSwapInterval(swap_interval);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setVSyncEnabled(boolean sync) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            Display.setSwapInterval(sync ? 1 : 0);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setLocation(int new_x, int new_y) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            x = new_x;
            y = new_y;
            if (Display.isCreated() && !Display.isFullscreen()) {
                Display.reshape();
            }
        }
    }

    private static void reshape() {
        DisplayMode mode = Display.getEffectiveMode();
        display_impl.reshape(Display.getWindowX(), Display.getWindowY(), mode.getWidth(), mode.getHeight());
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getAdapter() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return display_impl.getAdapter();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getVersion() {
        Object object = GlobalLock.lock;
        synchronized (object) {
            return display_impl.getVersion();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int setIcon(ByteBuffer[] icons) {
        Object object = GlobalLock.lock;
        synchronized (object) {
            if (cached_icons != icons) {
                cached_icons = new ByteBuffer[icons.length];
                for (int i = 0; i < icons.length; ++i) {
                    Display.cached_icons[i] = BufferUtils.createByteBuffer(icons[i].capacity());
                    int old_position = icons[i].position();
                    cached_icons[i].put(icons[i]);
                    icons[i].position(old_position);
                    cached_icons[i].flip();
                }
            }
            if (Display.isCreated() && parent == null) {
                return display_impl.setIcon(cached_icons);
            }
            return 0;
        }
    }

    public static void setResizable(boolean resizable) {
        window_resizable = resizable;
        if (Display.isCreated()) {
            display_impl.setResizable(resizable);
        }
    }

    public static boolean isResizable() {
        return window_resizable;
    }

    public static boolean wasResized() {
        return window_resized;
    }

    public static int getX() {
        if (Display.isFullscreen()) {
            return 0;
        }
        if (parent != null) {
            return parent.getX();
        }
        return display_impl.getX();
    }

    public static int getY() {
        if (Display.isFullscreen()) {
            return 0;
        }
        if (parent != null) {
            return parent.getY();
        }
        return display_impl.getY();
    }

    public static int getWidth() {
        if (Display.isFullscreen()) {
            return Display.getDisplayMode().getWidth();
        }
        if (parent != null) {
            return parent.getWidth();
        }
        return width;
    }

    public static int getHeight() {
        if (Display.isFullscreen()) {
            return Display.getDisplayMode().getHeight();
        }
        if (parent != null) {
            return parent.getHeight();
        }
        return height;
    }

    public static float getPixelScaleFactor() {
        return display_impl.getPixelScaleFactor();
    }

    static /* synthetic */ ByteBuffer[] access$702(ByteBuffer[] x0) {
        cached_icons = x0;
        return x0;
    }

    static {
        x = -1;
        y = -1;
        width = 0;
        height = 0;
        title = "Game";
        component_listener = new ComponentAdapter(){

            /*
             * WARNING - Removed try catching itself - possible behaviour change.
             */
            public void componentResized(ComponentEvent e) {
                Object object = GlobalLock.lock;
                synchronized (object) {
                    parent_resized = true;
                }
            }
        };
        Sys.initialize();
        display_impl = Display.createDisplayImplementation();
        try {
            current_mode = initial_mode = display_impl.init();
            LWJGLUtil.log("Initial mode: " + initial_mode);
        }
        catch (LWJGLException e) {
            throw new RuntimeException(e);
        }
    }
}

