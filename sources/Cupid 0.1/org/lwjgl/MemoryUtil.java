package org.lwjgl;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;

public final class MemoryUtil {
  private static final Charset ascii = Charset.forName("ISO-8859-1");
  
  private static final Charset utf8 = Charset.forName("UTF-8");
  
  private static final Charset utf16 = Charset.forName("UTF-16LE");
  
  private static final Accessor memUtil;
  
  static {
    Accessor accessor;
  }
  
  static {
    try {
      accessor = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorUnsafe");
    } catch (Exception e0) {
      try {
        accessor = loadAccessor("org.lwjgl.MemoryUtilSun$AccessorReflectFast");
      } catch (Exception e1) {
        try {
          accessor = new AccessorReflect();
        } catch (Exception e2) {
          LWJGLUtil.log("Unsupported JVM detected, this will likely result in low performance. Please inform LWJGL developers.");
          accessor = new AccessorJNI();
        } 
      } 
    } 
    LWJGLUtil.log("MemoryUtil Accessor: " + accessor.getClass().getSimpleName());
    memUtil = accessor;
  }
  
  public static long getAddress0(Buffer buffer) {
    return memUtil.getAddress(buffer);
  }
  
  public static long getAddress0Safe(Buffer buffer) {
    return (buffer == null) ? 0L : memUtil.getAddress(buffer);
  }
  
  public static long getAddress0(PointerBuffer buffer) {
    return memUtil.getAddress(buffer.getBuffer());
  }
  
  public static long getAddress0Safe(PointerBuffer buffer) {
    return (buffer == null) ? 0L : memUtil.getAddress(buffer.getBuffer());
  }
  
  public static long getAddress(ByteBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(ByteBuffer buffer, int position) {
    return getAddress0(buffer) + position;
  }
  
  public static long getAddress(ShortBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(ShortBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 1);
  }
  
  public static long getAddress(CharBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(CharBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 1);
  }
  
  public static long getAddress(IntBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(IntBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 2);
  }
  
  public static long getAddress(FloatBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(FloatBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 2);
  }
  
  public static long getAddress(LongBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(LongBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 3);
  }
  
  public static long getAddress(DoubleBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(DoubleBuffer buffer, int position) {
    return getAddress0(buffer) + (position << 3);
  }
  
  public static long getAddress(PointerBuffer buffer) {
    return getAddress(buffer, buffer.position());
  }
  
  public static long getAddress(PointerBuffer buffer, int position) {
    return getAddress0(buffer) + (position * PointerBuffer.getPointerSize());
  }
  
  public static long getAddressSafe(ByteBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(ByteBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(ShortBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(ShortBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(CharBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(CharBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(IntBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(IntBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(FloatBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(FloatBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(LongBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(LongBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(DoubleBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(DoubleBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static long getAddressSafe(PointerBuffer buffer) {
    return (buffer == null) ? 0L : getAddress(buffer);
  }
  
  public static long getAddressSafe(PointerBuffer buffer, int position) {
    return (buffer == null) ? 0L : getAddress(buffer, position);
  }
  
  public static ByteBuffer encodeASCII(CharSequence text) {
    return encode(text, ascii);
  }
  
  public static ByteBuffer encodeUTF8(CharSequence text) {
    return encode(text, utf8);
  }
  
  public static ByteBuffer encodeUTF16(CharSequence text) {
    return encode(text, utf16);
  }
  
  private static ByteBuffer encode(CharSequence text, Charset charset) {
    if (text == null)
      return null; 
    return encode(CharBuffer.wrap(new CharSequenceNT(text)), charset);
  }
  
  private static ByteBuffer encode(CharBuffer in, Charset charset) {
    CharsetEncoder encoder = charset.newEncoder();
    int n = (int)(in.remaining() * encoder.averageBytesPerChar());
    ByteBuffer out = BufferUtils.createByteBuffer(n);
    if (n == 0 && in.remaining() == 0)
      return out; 
    encoder.reset();
    while (true) {
      CoderResult cr = in.hasRemaining() ? encoder.encode(in, out, true) : CoderResult.UNDERFLOW;
      if (cr.isUnderflow())
        cr = encoder.flush(out); 
      if (cr.isUnderflow())
        break; 
      if (cr.isOverflow()) {
        n = 2 * n + 1;
        ByteBuffer o = BufferUtils.createByteBuffer(n);
        out.flip();
        o.put(out);
        out = o;
        continue;
      } 
      try {
        cr.throwException();
      } catch (CharacterCodingException e) {
        throw new RuntimeException(e);
      } 
    } 
    out.flip();
    return out;
  }
  
  public static String decodeASCII(ByteBuffer buffer) {
    return decode(buffer, ascii);
  }
  
  public static String decodeUTF8(ByteBuffer buffer) {
    return decode(buffer, utf8);
  }
  
  public static String decodeUTF16(ByteBuffer buffer) {
    return decode(buffer, utf16);
  }
  
  private static String decode(ByteBuffer buffer, Charset charset) {
    if (buffer == null)
      return null; 
    return decodeImpl(buffer, charset);
  }
  
  private static String decodeImpl(ByteBuffer in, Charset charset) {
    CharsetDecoder decoder = charset.newDecoder();
    int n = (int)(in.remaining() * decoder.averageCharsPerByte());
    CharBuffer out = BufferUtils.createCharBuffer(n);
    if (n == 0 && in.remaining() == 0)
      return ""; 
    decoder.reset();
    while (true) {
      CoderResult cr = in.hasRemaining() ? decoder.decode(in, out, true) : CoderResult.UNDERFLOW;
      if (cr.isUnderflow())
        cr = decoder.flush(out); 
      if (cr.isUnderflow())
        break; 
      if (cr.isOverflow()) {
        n = 2 * n + 1;
        CharBuffer o = BufferUtils.createCharBuffer(n);
        out.flip();
        o.put(out);
        out = o;
        continue;
      } 
      try {
        cr.throwException();
      } catch (CharacterCodingException e) {
        throw new RuntimeException(e);
      } 
    } 
    out.flip();
    return out.toString();
  }
  
  private static class CharSequenceNT implements CharSequence {
    final CharSequence source;
    
    CharSequenceNT(CharSequence source) {
      this.source = source;
    }
    
    public int length() {
      return this.source.length() + 1;
    }
    
    public char charAt(int index) {
      return (index == this.source.length()) ? Character.MIN_VALUE : this.source.charAt(index);
    }
    
    public CharSequence subSequence(int start, int end) {
      return new CharSequenceNT(this.source.subSequence(start, Math.min(end, this.source.length())));
    }
  }
  
  private static Accessor loadAccessor(String className) throws Exception {
    return (Accessor)Class.forName(className).newInstance();
  }
  
  static interface Accessor {
    long getAddress(Buffer param1Buffer);
  }
  
  private static class AccessorJNI implements Accessor {
    private AccessorJNI() {}
    
    public long getAddress(Buffer buffer) {
      return BufferUtils.getBufferAddress(buffer);
    }
  }
  
  private static class AccessorReflect implements Accessor {
    private final Field address;
    
    AccessorReflect() {
      try {
        this.address = MemoryUtil.getAddressField();
      } catch (NoSuchFieldException e) {
        throw new UnsupportedOperationException(e);
      } 
      this.address.setAccessible(true);
    }
    
    public long getAddress(Buffer buffer) {
      try {
        return this.address.getLong(buffer);
      } catch (IllegalAccessException e) {
        return 0L;
      } 
    }
  }
  
  static Field getAddressField() throws NoSuchFieldException {
    return getDeclaredFieldRecursive(ByteBuffer.class, "address");
  }
  
  private static Field getDeclaredFieldRecursive(Class<?> root, String fieldName) throws NoSuchFieldException {
    Class<?> type = root;
    while (true) {
      try {
        return type.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        type = type.getSuperclass();
        if (type == null)
          break; 
      } 
    } 
    throw new NoSuchFieldException(fieldName + " does not exist in " + root.getSimpleName() + " or any of its superclasses.");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\MemoryUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */