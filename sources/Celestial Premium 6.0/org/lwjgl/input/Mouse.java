/*
 * Decompiled with CFR 0.150.
 */
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
import org.lwjgl.input.Cursor;
import org.lwjgl.input.OpenGLPackageAccess;
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
    private static int buttonCount;
    private static boolean hasWheel;
    private static Cursor currentCursor;
    private static String[] buttonName;
    private static final Map<String, Integer> buttonMap;
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
    private static final boolean emulateCursorAnimation;
    private static boolean clipMouseCoordinatesToWindow;

    private Mouse() {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Cursor getNativeCursor() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return currentCursor;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Cursor setNativeCursor(Cursor cursor) throws LWJGLException {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if ((Cursor.getCapabilities() & 1) == 0) {
                throw new IllegalStateException("Mouse doesn't support native cursors");
            }
            Cursor oldCursor = currentCursor;
            currentCursor = cursor;
            if (Mouse.isCreated()) {
                if (currentCursor != null) {
                    implementation.setNativeCursor(currentCursor.getHandle());
                    currentCursor.setTimeout();
                } else {
                    implementation.setNativeCursor(null);
                }
            }
            return oldCursor;
        }
    }

    public static boolean isClipMouseCoordinatesToWindow() {
        return clipMouseCoordinatesToWindow;
    }

    public static void setClipMouseCoordinatesToWindow(boolean clip) {
        clipMouseCoordinatesToWindow = clip;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setCursorPosition(int new_x, int new_y) {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!Mouse.isCreated()) {
                throw new IllegalStateException("Mouse is not created");
            }
            x = event_x = new_x;
            y = event_y = new_y;
            if (!Mouse.isGrabbed() && (Cursor.getCapabilities() & 1) != 0) {
                implementation.setCursorPosition(x, y);
            } else {
                grab_x = new_x;
                grab_y = new_y;
            }
        }
    }

    private static void initialize() {
        Sys.initialize();
        buttonName = new String[16];
        for (int i = 0; i < 16; ++i) {
            Mouse.buttonName[i] = "BUTTON" + i;
            buttonMap.put(buttonName[i], i);
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

    private static void create(InputImplementation impl) throws LWJGLException {
        if (created) {
            return;
        }
        if (!initialized) {
            Mouse.initialize();
        }
        implementation = impl;
        implementation.createMouse();
        hasWheel = implementation.hasWheel();
        created = true;
        buttonCount = implementation.getButtonCount();
        buttons = BufferUtils.createByteBuffer(buttonCount);
        coord_buffer = BufferUtils.createIntBuffer(3);
        if (currentCursor != null && implementation.getNativeCursorCapabilities() != 0) {
            Mouse.setNativeCursor(currentCursor);
        }
        readBuffer = ByteBuffer.allocate(1100);
        readBuffer.limit(0);
        Mouse.setGrabbed(isGrabbed);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void create() throws LWJGLException {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!Display.isCreated()) {
                throw new IllegalStateException("Display must be created.");
            }
            Mouse.create(OpenGLPackageAccess.createImplementation());
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isCreated() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return created;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void destroy() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!created) {
                return;
            }
            created = false;
            buttons = null;
            coord_buffer = null;
            implementation.destroyMouse();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void poll() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can poll it");
            }
            implementation.pollMouse(coord_buffer, buttons);
            int poll_coord1 = coord_buffer.get(0);
            int poll_coord2 = coord_buffer.get(1);
            int poll_dwheel = coord_buffer.get(2);
            if (Mouse.isGrabbed()) {
                dx += poll_coord1;
                dy += poll_coord2;
                x += poll_coord1;
                y += poll_coord2;
                absolute_x += poll_coord1;
                absolute_y += poll_coord2;
            } else {
                dx = poll_coord1 - absolute_x;
                dy = poll_coord2 - absolute_y;
                absolute_x = x = poll_coord1;
                absolute_y = y = poll_coord2;
            }
            if (clipMouseCoordinatesToWindow) {
                x = Math.min(Display.getWidth() - 1, Math.max(0, x));
                y = Math.min(Display.getHeight() - 1, Math.max(0, y));
            }
            dwheel += poll_dwheel;
            Mouse.read();
        }
    }

    private static void read() {
        readBuffer.compact();
        implementation.readMouse(readBuffer);
        readBuffer.flip();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isButtonDown(int button) {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can poll the button state");
            }
            if (button >= buttonCount || button < 0) {
                return false;
            }
            return buttons.get(button) == 1;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static String getButtonName(int button) {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (button >= buttonName.length || button < 0) {
                return null;
            }
            return buttonName[button];
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getButtonIndex(String buttonName) {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            Integer ret = buttonMap.get(buttonName);
            if (ret == null) {
                return -1;
            }
            return ret;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean next() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            if (!created) {
                throw new IllegalStateException("Mouse must be created before you can read events");
            }
            if (readBuffer.hasRemaining()) {
                eventButton = readBuffer.get();
                boolean bl = eventState = readBuffer.get() != 0;
                if (Mouse.isGrabbed()) {
                    event_dx = readBuffer.getInt();
                    event_dy = readBuffer.getInt();
                    last_event_raw_x = event_x += event_dx;
                    last_event_raw_y = event_y += event_dy;
                } else {
                    int new_event_x = readBuffer.getInt();
                    int new_event_y = readBuffer.getInt();
                    event_dx = new_event_x - last_event_raw_x;
                    event_dy = new_event_y - last_event_raw_y;
                    event_x = new_event_x;
                    event_y = new_event_y;
                    last_event_raw_x = new_event_x;
                    last_event_raw_y = new_event_y;
                }
                if (clipMouseCoordinatesToWindow) {
                    event_x = Math.min(Display.getWidth() - 1, Math.max(0, event_x));
                    event_y = Math.min(Display.getHeight() - 1, Math.max(0, event_y));
                }
                event_dwheel = readBuffer.getInt();
                event_nanos = readBuffer.getLong();
                return true;
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventButton() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return eventButton;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean getEventButtonState() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return eventState;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventDX() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_dx;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventDY() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_dy;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventX() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_x;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventY() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_y;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getEventDWheel() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_dwheel;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static long getEventNanoseconds() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return event_nanos;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getX() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return x;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getY() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return y;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getDX() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            int result = dx;
            dx = 0;
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getDY() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            int result = dy;
            dy = 0;
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getDWheel() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            int result = dwheel;
            dwheel = 0;
            return result;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static int getButtonCount() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return buttonCount;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean hasWheel() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return hasWheel;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static boolean isGrabbed() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            return isGrabbed;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void setGrabbed(boolean grab) {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            boolean grabbed = isGrabbed;
            isGrabbed = grab;
            if (Mouse.isCreated()) {
                if (grab && !grabbed) {
                    grab_x = x;
                    grab_y = y;
                } else if (!grab && grabbed && (Cursor.getCapabilities() & 1) != 0) {
                    implementation.setCursorPosition(grab_x, grab_y);
                }
                implementation.grabMouse(grab);
                Mouse.poll();
                event_x = x;
                event_y = y;
                last_event_raw_x = x;
                last_event_raw_y = y;
                Mouse.resetMouse();
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void updateCursor() {
        Object object = OpenGLPackageAccess.global_lock;
        synchronized (object) {
            block6: {
                if (emulateCursorAnimation && currentCursor != null && currentCursor.hasTimedOut() && Mouse.isInsideWindow()) {
                    currentCursor.nextCursor();
                    try {
                        Mouse.setNativeCursor(currentCursor);
                    }
                    catch (LWJGLException e) {
                        if (!LWJGLUtil.DEBUG) break block6;
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static boolean getPrivilegedBoolean(final String property_name) {
        Boolean value = AccessController.doPrivileged(new PrivilegedAction<Boolean>(){

            @Override
            public Boolean run() {
                return Boolean.getBoolean(property_name);
            }
        });
        return value;
    }

    public static boolean isInsideWindow() {
        return implementation.isInsideWindow();
    }

    static {
        buttonCount = -1;
        buttonMap = new HashMap<String, Integer>(16);
        emulateCursorAnimation = LWJGLUtil.getPlatform() == 3 || LWJGLUtil.getPlatform() == 2;
        clipMouseCoordinatesToWindow = !Mouse.getPrivilegedBoolean("org.lwjgl.input.Mouse.allowNegativeMouseCoords");
    }
}

