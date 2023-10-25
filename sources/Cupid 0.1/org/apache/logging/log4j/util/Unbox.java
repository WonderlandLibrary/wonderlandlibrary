package org.apache.logging.log4j.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.status.StatusLogger;

@PerformanceSensitive({"allocation"})
public class Unbox {
  private static final Logger LOGGER = (Logger)StatusLogger.getLogger();
  
  private static final int BITS_PER_INT = 32;
  
  private static final int RINGBUFFER_MIN_SIZE = 32;
  
  private static final int RINGBUFFER_SIZE = calculateRingBufferSize("log4j.unbox.ringbuffer.size");
  
  private static final int MASK = RINGBUFFER_SIZE - 1;
  
  private static class WebSafeState {
    private final ThreadLocal<StringBuilder[]> ringBuffer = (ThreadLocal)new ThreadLocal<>();
    
    private final ThreadLocal<int[]> current = (ThreadLocal)new ThreadLocal<>();
    
    public StringBuilder getStringBuilder() {
      StringBuilder[] array = this.ringBuffer.get();
      if (array == null) {
        array = new StringBuilder[Unbox.RINGBUFFER_SIZE];
        for (int i = 0; i < array.length; i++)
          array[i] = new StringBuilder(21); 
        this.ringBuffer.set(array);
        this.current.set(new int[1]);
      } 
      int[] index = this.current.get();
      index[0] = index[0] + 1;
      StringBuilder result = array[Unbox.MASK & index[0]];
      result.setLength(0);
      return result;
    }
    
    public boolean isBoxedPrimitive(StringBuilder text) {
      StringBuilder[] array = this.ringBuffer.get();
      if (array == null)
        return false; 
      for (int i = 0; i < array.length; i++) {
        if (text == array[i])
          return true; 
      } 
      return false;
    }
    
    private WebSafeState() {}
  }
  
  private static class State {
    private final StringBuilder[] ringBuffer = new StringBuilder[Unbox.RINGBUFFER_SIZE];
    
    private int current;
    
    State() {
      for (int i = 0; i < this.ringBuffer.length; i++)
        this.ringBuffer[i] = new StringBuilder(21); 
    }
    
    public StringBuilder getStringBuilder() {
      StringBuilder result = this.ringBuffer[Unbox.MASK & this.current++];
      result.setLength(0);
      return result;
    }
    
    public boolean isBoxedPrimitive(StringBuilder text) {
      for (int i = 0; i < this.ringBuffer.length; i++) {
        if (text == this.ringBuffer[i])
          return true; 
      } 
      return false;
    }
  }
  
  private static ThreadLocal<State> threadLocalState = new ThreadLocal<>();
  
  private static WebSafeState webSafeState = new WebSafeState();
  
  private static int calculateRingBufferSize(String propertyName) {
    String userPreferredRBSize = PropertiesUtil.getProperties().getStringProperty(propertyName, 
        String.valueOf(32));
    try {
      int size = Integer.parseInt(userPreferredRBSize);
      if (size < 32) {
        size = 32;
        LOGGER.warn("Invalid {} {}, using minimum size {}.", propertyName, userPreferredRBSize, 
            Integer.valueOf(32));
      } 
      return ceilingNextPowerOfTwo(size);
    } catch (Exception ex) {
      LOGGER.warn("Invalid {} {}, using default size {}.", propertyName, userPreferredRBSize, 
          Integer.valueOf(32));
      return 32;
    } 
  }
  
  private static int ceilingNextPowerOfTwo(int x) {
    return 1 << 32 - Integer.numberOfLeadingZeros(x - 1);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(float value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(double value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(short value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(int value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(char value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(long value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(byte value) {
    return getSB().append(value);
  }
  
  @PerformanceSensitive({"allocation"})
  public static StringBuilder box(boolean value) {
    return getSB().append(value);
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
    return Constants.ENABLE_THREADLOCALS ? getState().getStringBuilder() : webSafeState.getStringBuilder();
  }
  
  static int getRingbufferSize() {
    return RINGBUFFER_SIZE;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\logging\log4\\util\Unbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */