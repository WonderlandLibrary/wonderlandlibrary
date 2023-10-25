/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.MouseEventQueue;

final class MacOSXMouseEventQueue
extends MouseEventQueue {
    private final IntBuffer delta_buffer = BufferUtils.createIntBuffer(2);
    private boolean skip_event;
    private static boolean is_grabbed;

    MacOSXMouseEventQueue(Component component) {
        super(component);
    }

    public void setGrabbed(boolean grab) {
        if (is_grabbed != grab) {
            super.setGrabbed(grab);
            this.warpCursor();
            MacOSXMouseEventQueue.grabMouse(grab);
        }
    }

    private static synchronized void grabMouse(boolean grab) {
        is_grabbed = grab;
        if (!grab) {
            MacOSXMouseEventQueue.nGrabMouse(grab);
        }
    }

    protected void resetCursorToCenter() {
        super.resetCursorToCenter();
        MacOSXMouseEventQueue.getMouseDeltas(this.delta_buffer);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    protected void updateDeltas(long nanos) {
        super.updateDeltas(nanos);
        MacOSXMouseEventQueue macOSXMouseEventQueue = this;
        synchronized (macOSXMouseEventQueue) {
            MacOSXMouseEventQueue.getMouseDeltas(this.delta_buffer);
            int dx = this.delta_buffer.get(0);
            int dy = -this.delta_buffer.get(1);
            if (this.skip_event) {
                this.skip_event = false;
                MacOSXMouseEventQueue.nGrabMouse(this.isGrabbed());
                return;
            }
            if (dx != 0 || dy != 0) {
                this.putMouseEventWithCoords((byte)-1, (byte)0, dx, dy, 0, nanos);
                this.addDelta(dx, dy);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    void warpCursor() {
        MacOSXMouseEventQueue macOSXMouseEventQueue = this;
        synchronized (macOSXMouseEventQueue) {
            this.skip_event = this.isGrabbed();
        }
        if (this.isGrabbed()) {
            Rectangle bounds = this.getComponent().getBounds();
            Point location_on_screen = this.getComponent().getLocationOnScreen();
            int x = location_on_screen.x + bounds.width / 2;
            int y = location_on_screen.y + bounds.height / 2;
            MacOSXMouseEventQueue.nWarpCursor(x, y);
        }
    }

    private static native void getMouseDeltas(IntBuffer var0);

    private static native void nWarpCursor(int var0, int var1);

    static native void nGrabMouse(boolean var0);
}

