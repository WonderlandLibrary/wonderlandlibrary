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

    public static final class MouseButton
    extends Enum<MouseButton> {
        public static final /* enum */ MouseButton LEFT;
        public static final /* enum */ MouseButton MIDDLE;
        public static final /* enum */ MouseButton RIGHT;
        private int index;
        private static final /* synthetic */ MouseButton[] $VALUES;

        public static MouseButton[] values() {
            return (MouseButton[])$VALUES.clone();
        }

        public static MouseButton valueOf(String name) {
            return Enum.valueOf(MouseButton.class, name);
        }

        private MouseButton(int index) {
            this.index = index;
        }

        private int getIndex() {
            return this.index;
        }

        static {
            MouseButton mouseButton;
            MouseButton mouseButton2;
            MouseButton mouseButton3;
            MouseButton mouseButton4 = mouseButton3;
            MouseButton mouseButton5 = mouseButton3;
            String string = "LEFT";
            LEFT = (MouseButton)0;
            MouseButton mouseButton6 = mouseButton2;
            MouseButton mouseButton7 = mouseButton2;
            String string2 = "MIDDLE";
            MIDDLE = (MouseButton)0;
            MouseButton mouseButton8 = mouseButton;
            MouseButton mouseButton9 = mouseButton;
            String string3 = "RIGHT";
            RIGHT = (MouseButton)0;
            $VALUES = new MouseButton[]{LEFT, MIDDLE, RIGHT};
        }
    }
}

