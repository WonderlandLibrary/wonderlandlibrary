package org.lwjgl;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public final class BufferUtils {
  public static ByteBuffer createByteBuffer(int size) {
    return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
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
    return PointerBuffer.allocateDirect(size);
  }
  
  public static int getElementSizeExponent(Buffer buf) {
    if (buf instanceof ByteBuffer)
      return 0; 
    if (buf instanceof ShortBuffer || buf instanceof CharBuffer)
      return 1; 
    if (buf instanceof FloatBuffer || buf instanceof IntBuffer)
      return 2; 
    if (buf instanceof LongBuffer || buf instanceof DoubleBuffer)
      return 3; 
    throw new IllegalStateException("Unsupported buffer type: " + buf);
  }
  
  public static int getOffset(Buffer buffer) {
    return buffer.position() << getElementSizeExponent(buffer);
  }
  
  public static void zeroBuffer(ByteBuffer b) {
    zeroBuffer0(b, b.position(), b.remaining());
  }
  
  public static void zeroBuffer(ShortBuffer b) {
    zeroBuffer0(b, b.position() * 2L, b.remaining() * 2L);
  }
  
  public static void zeroBuffer(CharBuffer b) {
    zeroBuffer0(b, b.position() * 2L, b.remaining() * 2L);
  }
  
  public static void zeroBuffer(IntBuffer b) {
    zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
  }
  
  public static void zeroBuffer(FloatBuffer b) {
    zeroBuffer0(b, b.position() * 4L, b.remaining() * 4L);
  }
  
  public static void zeroBuffer(LongBuffer b) {
    zeroBuffer0(b, b.position() * 8L, b.remaining() * 8L);
  }
  
  public static void zeroBuffer(DoubleBuffer b) {
    zeroBuffer0(b, b.position() * 8L, b.remaining() * 8L);
  }
  
  private static native void zeroBuffer0(Buffer paramBuffer, long paramLong1, long paramLong2);
  
  static native long getBufferAddress(Buffer paramBuffer);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\BufferUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */