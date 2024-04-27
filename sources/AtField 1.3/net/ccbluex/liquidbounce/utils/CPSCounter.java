/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.utils.RollingArrayLongBuffer;

public class CPSCounter {
    private static final RollingArrayLongBuffer[] TIMESTAMP_BUFFERS;
    private static final int MAX_CPS;

    public static int getCPS(MouseButton mouseButton) {
        return TIMESTAMP_BUFFERS[MouseButton.access$000(mouseButton)].getTimestampsSince(System.currentTimeMillis() - 1000L);
    }

    public static void registerClick(MouseButton mouseButton) {
        TIMESTAMP_BUFFERS[MouseButton.access$000(mouseButton)].add(System.currentTimeMillis());
    }

    static {
        MAX_CPS = 50;
        TIMESTAMP_BUFFERS = new RollingArrayLongBuffer[MouseButton.values().length];
        for (int i = 0; i < TIMESTAMP_BUFFERS.length; ++i) {
            CPSCounter.TIMESTAMP_BUFFERS[i] = new RollingArrayLongBuffer(50);
        }
    }

    public static enum MouseButton {
        LEFT(0),
        MIDDLE(1),
        RIGHT(2);

        private int index;

        static int access$000(MouseButton mouseButton) {
            return mouseButton.getIndex();
        }

        /*
         * WARNING - Possible parameter corruption
         * WARNING - void declaration
         */
        private MouseButton() {
            void var3_1;
            this.index = var3_1;
        }

        private int getIndex() {
            return this.index;
        }
    }
}

