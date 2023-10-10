// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.Logger;

@PerformanceSensitive({ "allocation" })
public class Unbox
{
    private static final Logger LOGGER;
    private static final int BITS_PER_INT = 32;
    private static final int RINGBUFFER_MIN_SIZE = 32;
    private static final int RINGBUFFER_SIZE;
    private static final int MASK;
    private static ThreadLocal<State> threadLocalState;
    private static WebSafeState webSafeState;
    
    private Unbox() {
    }
    
    private static int calculateRingBufferSize(final String propertyName) {
        final String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, String.valueOf(32));
        try {
            int size = Integer.parseInt(userPreferredRBSize);
            if (size < 32) {
                size = 32;
                Unbox.LOGGER.warn("Invalid {} {}, using minimum size {}.", propertyName, userPreferredRBSize, 32);
            }
            return ceilingNextPowerOfTwo(size);
        }
        catch (Exception ex) {
            Unbox.LOGGER.warn("Invalid {} {}, using default size {}.", propertyName, userPreferredRBSize, 32);
            return 32;
        }
    }
    
    private static int ceilingNextPowerOfTwo(final int x) {
        return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final float value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final double value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final short value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final int value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final char value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final long value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final byte value) {
        return getSB().append(value);
    }
    
    @PerformanceSensitive({ "allocation" })
    public static StringBuilder box(final boolean value) {
        return getSB().append(value);
    }
    
    private static State getState() {
        State state = Unbox.threadLocalState.get();
        if (state == null) {
            state = new State();
            Unbox.threadLocalState.set(state);
        }
        return state;
    }
    
    private static StringBuilder getSB() {
        return Constants.ENABLE_THREADLOCALS ? getState().getStringBuilder() : Unbox.webSafeState.getStringBuilder();
    }
    
    static int getRingbufferSize() {
        return Unbox.RINGBUFFER_SIZE;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        RINGBUFFER_SIZE = calculateRingBufferSize("log4j.unbox.ringbuffer.size");
        MASK = Unbox.RINGBUFFER_SIZE - 1;
        Unbox.threadLocalState = new ThreadLocal<State>();
        Unbox.webSafeState = new WebSafeState();
    }
    
    private static class WebSafeState
    {
        private final ThreadLocal<StringBuilder[]> ringBuffer;
        private final ThreadLocal<int[]> current;
        
        private WebSafeState() {
            this.ringBuffer = new ThreadLocal<StringBuilder[]>();
            this.current = new ThreadLocal<int[]>();
        }
        
        public StringBuilder getStringBuilder() {
            StringBuilder[] array = this.ringBuffer.get();
            if (array == null) {
                array = new StringBuilder[Unbox.RINGBUFFER_SIZE];
                for (int i = 0; i < array.length; ++i) {
                    array[i] = new StringBuilder(21);
                }
                this.ringBuffer.set(array);
                this.current.set(new int[1]);
            }
            final int[] index = this.current.get();
            final StringBuilder result = array[Unbox.MASK & index[0]++];
            result.setLength(0);
            return result;
        }
        
        public boolean isBoxedPrimitive(final StringBuilder text) {
            final StringBuilder[] array = this.ringBuffer.get();
            if (array == null) {
                return false;
            }
            for (int i = 0; i < array.length; ++i) {
                if (text == array[i]) {
                    return true;
                }
            }
            return false;
        }
    }
    
    private static class State
    {
        private final StringBuilder[] ringBuffer;
        private int current;
        
        State() {
            this.ringBuffer = new StringBuilder[Unbox.RINGBUFFER_SIZE];
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                this.ringBuffer[i] = new StringBuilder(21);
            }
        }
        
        public StringBuilder getStringBuilder() {
            final StringBuilder result = this.ringBuffer[Unbox.MASK & this.current++];
            result.setLength(0);
            return result;
        }
        
        public boolean isBoxedPrimitive(final StringBuilder text) {
            for (int i = 0; i < this.ringBuffer.length; ++i) {
                if (text == this.ringBuffer[i]) {
                    return true;
                }
            }
            return false;
        }
    }
}
