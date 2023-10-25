package org.lwjgl.util.mapped;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.MemoryUtil;
import org.lwjgl.PointerBuffer;

public final class CacheUtil {
  private static final int CACHE_LINE_SIZE;
  
  static {
    Integer size = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineSize");
    if (size != null) {
      if (size.intValue() < 1)
        throw new IllegalStateException("Invalid CacheLineSize specified: " + size); 
      CACHE_LINE_SIZE = size.intValue();
    } else if (Runtime.getRuntime().availableProcessors() == 1) {
      if (LWJGLUtil.DEBUG)
        LWJGLUtil.log("Cannot detect cache line size on single-core CPUs, assuming 64 bytes."); 
      CACHE_LINE_SIZE = 64;
    } else {
      CACHE_LINE_SIZE = CacheLineSize.getCacheLineSize();
    } 
  }
  
  public static int getCacheLineSize() {
    return CACHE_LINE_SIZE;
  }
  
  public static ByteBuffer createByteBuffer(int size) {
    ByteBuffer buffer = ByteBuffer.allocateDirect(size + CACHE_LINE_SIZE);
    if (MemoryUtil.getAddress(buffer) % CACHE_LINE_SIZE != 0L)
      buffer.position(CACHE_LINE_SIZE - (int)(MemoryUtil.getAddress(buffer) & (CACHE_LINE_SIZE - 1))); 
    buffer.limit(buffer.position() + size);
    return buffer.slice().order(ByteOrder.nativeOrder());
  }
  
  public static ShortBuffer createShortBuffer(int size) {
    return createByteBuffer(size << 1).asShortBuffer();
  }
  
  public static CharBuffer createCharBuffer(int size) {
    return createByteBuffer(size << 1).asCharBuffer();
  }
  
  public static IntBuffer createIntBuffer(int size) {
    return createByteBuffer(size << 2).asIntBuffer();
  }
  
  public static LongBuffer createLongBuffer(int size) {
    return createByteBuffer(size << 3).asLongBuffer();
  }
  
  public static FloatBuffer createFloatBuffer(int size) {
    return createByteBuffer(size << 2).asFloatBuffer();
  }
  
  public static DoubleBuffer createDoubleBuffer(int size) {
    return createByteBuffer(size << 3).asDoubleBuffer();
  }
  
  public static PointerBuffer createPointerBuffer(int size) {
    return new PointerBuffer(createByteBuffer(size * PointerBuffer.getPointerSize()));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjg\\util\mapped\CacheUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */