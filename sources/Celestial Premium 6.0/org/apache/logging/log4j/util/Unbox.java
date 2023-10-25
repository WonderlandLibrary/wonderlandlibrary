/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.util.Constants;
import org.apache.logging.log4j.util.PerformanceSensitive;
import org.apache.logging.log4j.util.PropertiesUtil;

@PerformanceSensitive(value={"allocation"})
public class Unbox {
    private static final Logger LOGGER = StatusLogger.getLogger();
    private static final int BITS_PER_INT = 32;
    private static final int RINGBUFFER_MIN_SIZE = 32;
    private static final int RINGBUFFER_SIZE = Unbox.calculateRingBufferSize("log4j.unbox.ringbuffer.size");
    private static final int MASK = RINGBUFFER_SIZE - 1;
    private static ThreadLocal<State> threadLocalState = new ThreadLocal();
    private static WebSafeState webSafeState = new WebSafeState();

    private Unbox() {
    }

    private static int calculateRingBufferSize(String propertyName) {
        String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, String.valueOf(32));
        try {
            int size = Integer.parseInt(userPreferredRBSize);
            if (size < 32) {
                size = 32;
                LOGGER.warn("Invalid {} {}, using minimum size {}.", propertyName, userPreferredRBSize, 32);
            }
            return Unbox.ceilingNextPowerOfTwo(size);
        }
        catch (Exception ex) {
            LOGGER.warn("Invalid {} {}, using default size {}.", propertyName, userPreferredRBSize, 32);
            return 32;
        }
    }

    private static int ceilingNextPowerOfTwo(int x) {
        return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(float value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(double value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(short value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(int value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(char value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(long value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(byte value) {
        return Unbox.getSB().append(value);
    }

    @PerformanceSensitive(value={"allocation"})
    public static StringBuilder box(boolean value) {
        return Unbox.getSB().append(value);
    }

    private static State getState() {
        State state = threadLocalState.get();
        if (state == null) {
            state = new State();
            threadLocalState.set(state);
        }
        return state;
    }

    private static StringBuilder getSB() {
        return Constants.ENABLE_THREADLOCALS ? Unbox.getState().getStringBuilder() : webSafeState.getStringBuilder();
    }

    static int getRingbufferSize() {
        return RINGBUFFER_SIZE;
    }

    private static class State {
        private final StringBuilder[] ringBuffer = new StringBuilder[Unbox.access$000()];
        private int current;

        State() {
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                this.ringBuffer[i] = new StringBuilder(21);
            }
        }

        public StringBuilder getStringBuilder() {
            StringBuilder result = this.ringBuffer[MASK & this.current++];
            result.setLength(0);
            return result;
        }

        public boolean isBoxedPrimitive(StringBuilder text) {
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                if (text != this.ringBuffer[i]) continue;
                return true;
            }
            return false;
        }
    }

    private static class WebSafeState {
        private final ThreadLocal<StringBuilder[]> ringBuffer = new ThreadLocal();
        private final ThreadLocal<int[]> current = new ThreadLocal();

        private WebSafeState() {
        }

        public StringBuilder getStringBuilder() {
            StringBuilder[] array = this.ringBuffer.get();
            if (array == null) {
                array = new StringBuilder[RINGBUFFER_SIZE];
                for (int i = 0; i < array.length; ++i) {
                    array[i] = new StringBuilder(21);
                }
                this.ringBuffer.set(array);
                this.current.set(new int[1]);
            }
            int[] index = this.current.get();
            int n = index[0];
            index[0] = n + 1;
            StringBuilder result = array[MASK & n];
            result.setLength(0);
            return result;
        }

        public boolean isBoxedPrimitive(StringBuilder text) {
            StringBuilder[] array = this.ringBuffer.get();
            if (array == null) {
                return false;
            }
            for (int i = 0; i < array.length; ++i) {
                if (text != array[i]) continue;
                return true;
            }
            return false;
        }
    }
}

