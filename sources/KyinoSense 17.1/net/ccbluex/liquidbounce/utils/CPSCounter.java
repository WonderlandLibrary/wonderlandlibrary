/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.RollingArrayLongBuffer;

public class CPSCounter {
    private static final int MAX_CPS = 50;
    private static final RollingArrayLongBuffer[] TIMESTAMP_BUFFERS = new RollingArrayLongBuffer[MouseButton.values().length];

    public static void registerClick(MouseButton button) {
        TIMESTAMP_BUFFERS[button.getIndex()].add(System.currentTimeMillis());
    }

    public static int getCPS(MouseButton button) {
        return TIMESTAMP_BUFFERS[button.getIndex()].getTimestampsSince(System.currentTimeMillis() - 1000L);
    }

    static {
        for (int i = 0; i < TIMESTAMP_BUFFERS.length; ++i) {
            CPSCounter.TIMESTAMP_BUFFERS[i] = new RollingArrayLongBuffer(50);
        }
    }

    public static enum MouseButton {
        LEFT(0),
        MIDDLE(1),
        RIGHT(2);

        private int index;

        private MouseButton(int index) {
            this.index = index;
        }

        private int getIndex() {
            return this.index;
        }
    }
}

