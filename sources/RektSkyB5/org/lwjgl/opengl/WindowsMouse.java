/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.EventQueue;
import org.lwjgl.opengl.WindowsDisplay;

final class WindowsMouse {
    private final long hwnd;
    private final int mouse_button_count;
    private final boolean has_wheel;
    private final EventQueue event_queue = new EventQueue(22);
    private final ByteBuffer mouse_event = ByteBuffer.allocate(22);
    private final Object blank_cursor;
    private boolean mouse_grabbed;
    private byte[] button_states;
    private int accum_dx;
    private int accum_dy;
    private int accum_dwheel;
    private int last_x;
    private int last_y;

    WindowsMouse(long hwnd) throws LWJGLException {
        this.hwnd = hwnd;
        this.mouse_button_count = Math.min(5, WindowsDisplay.getSystemMetrics(43));
        this.has_wheel = WindowsDisplay.getSystemMetrics(75) != 0;
        this.blank_cursor = this.createBlankCursor();
        this.button_states = new byte[this.mouse_button_count];
    }

    private Object createBlankCursor() throws LWJGLException {
        int width = WindowsDisplay.getSystemMetrics(13);
        int height = WindowsDisplay.getSystemMetrics(14);
        IntBuffer pixels = BufferUtils.createIntBuffer(width * height);
        return WindowsDisplay.doCreateCursor(width, height, 0, 0, 1, pixels, null);
    }

    public boolean isGrabbed() {
        return this.mouse_grabbed;
    }

    public boolean hasWheel() {
        return this.has_wheel;
    }

    public int getButtonCount() {
        return this.mouse_button_count;
    }

    public void poll(IntBuffer coord_buffer, ByteBuffer buttons, WindowsDisplay display) {
        for (int i2 = 0; i2 < coord_buffer.remaining(); ++i2) {
            coord_buffer.put(coord_buffer.position() + i2, 0);
        }
        int num_buttons = this.mouse_button_count;
        coord_buffer.put(coord_buffer.position() + 2, this.accum_dwheel);
        if (num_buttons > this.button_states.length) {
            num_buttons = this.button_states.length;
        }
        for (int j2 = 0; j2 < num_buttons; ++j2) {
            buttons.put(buttons.position() + j2, this.button_states[j2]);
        }
        if (this.isGrabbed()) {
            coord_buffer.put(coord_buffer.position() + 0, this.accum_dx);
            coord_buffer.put(coord_buffer.position() + 1, this.accum_dy);
            if (display.isActive() && display.isVisible() && (this.accum_dx != 0 || this.accum_dy != 0)) {
                this.centerCursor();
            }
        } else {
            coord_buffer.put(coord_buffer.position() + 0, this.last_x);
            coord_buffer.put(coord_buffer.position() + 1, this.last_y);
        }
        this.accum_dwheel = 0;
        this.accum_dy = 0;
        this.accum_dx = 0;
    }

    private void putMouseEventWithCoords(byte button, byte state, int coord1, int coord2, int dz, long nanos) {
        this.mouse_event.clear();
        this.mouse_event.put(button).put(state).putInt(coord1).putInt(coord2).putInt(dz).putLong(nanos);
        this.mouse_event.flip();
        this.event_queue.putEvent(this.mouse_event);
    }

    private void putMouseEvent(byte button, byte state, int dz, long nanos) {
        if (this.mouse_grabbed) {
            this.putMouseEventWithCoords(button, state, 0, 0, dz, nanos);
        } else {
            this.putMouseEventWithCoords(button, state, this.last_x, this.last_y, dz, nanos);
        }
    }

    public void read(ByteBuffer buffer) {
        this.event_queue.copyEvents(buffer);
    }

    public Object getBlankCursor() {
        return this.blank_cursor;
    }

    public void grab(boolean grab, boolean should_center) {
        if (grab) {
            if (!this.mouse_grabbed) {
                this.mouse_grabbed = true;
                if (should_center) {
                    try {
                        WindowsDisplay.setupCursorClipping(this.hwnd);
                    }
                    catch (LWJGLException e2) {
                        LWJGLUtil.log("Failed to setup cursor clipping: " + e2);
                    }
                    this.centerCursor();
                }
            }
        } else if (this.mouse_grabbed) {
            this.mouse_grabbed = false;
            WindowsDisplay.resetCursorClipping();
        }
        this.event_queue.clearEvents();
    }

    public void handleMouseScrolled(int event_dwheel, long millis) {
        this.accum_dwheel += event_dwheel;
        this.putMouseEvent((byte)-1, (byte)0, event_dwheel, millis * 1000000L);
    }

    private void centerCursor() {
        WindowsDisplay.centerCursor(this.hwnd);
    }

    public void setPosition(int x2, int y2) {
        this.last_x = x2;
        this.last_y = y2;
    }

    public void destroy() {
        WindowsDisplay.doDestroyCursor(this.blank_cursor);
    }

    public void handleMouseMoved(int x2, int y2, long millis) {
        int dx = x2 - this.last_x;
        int dy = y2 - this.last_y;
        if (dx != 0 || dy != 0) {
            this.accum_dx += dx;
            this.accum_dy += dy;
            this.last_x = x2;
            this.last_y = y2;
            long nanos = millis * 1000000L;
            if (this.mouse_grabbed) {
                this.putMouseEventWithCoords((byte)-1, (byte)0, dx, dy, 0, nanos);
            } else {
                this.putMouseEventWithCoords((byte)-1, (byte)0, x2, y2, 0, nanos);
            }
        }
    }

    public void handleMouseButton(byte button, byte state, long millis) {
        this.putMouseEvent(button, state, 0, millis * 1000000L);
        if (button < this.button_states.length) {
            this.button_states[button] = state != 0 ? (byte)1 : 0;
        }
    }
}

