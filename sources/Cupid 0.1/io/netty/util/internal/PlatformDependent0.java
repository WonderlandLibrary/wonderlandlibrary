package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import sun.misc.Unsafe;

final class PlatformDependent0 {
  private static final InternalLogger logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
  
  private static final Unsafe UNSAFE;
  
  private static final boolean BIG_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN);
  
  private static final long ADDRESS_FIELD_OFFSET;
  
  private static final long UNSAFE_COPY_THRESHOLD = 1048576L;
  
  private static final boolean UNALIGNED;
  
  static {
    Field field;
    Unsafe unsafe;
  }
  
  static {
    ByteBuffer direct = ByteBuffer.allocateDirect(1);
    try {
      field = Buffer.class.getDeclaredField("address");
      field.setAccessible(true);
      if (field.getLong(ByteBuffer.allocate(1)) != 0L) {
        field = null;
      } else if (field.getLong(direct) == 0L) {
        field = null;
      } 
    } catch (Throwable t) {
      field = null;
    } 
    logger.debug("java.nio.Buffer.address: {}", (field != null) ? "available" : "unavailable");
    if (field != null) {
      try {
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        unsafe = (Unsafe)unsafeField.get(null);
        logger.debug("sun.misc.Unsafe.theUnsafe: {}", (unsafe != null) ? "available" : "unavailable");
        try {
          if (unsafe != null) {
            unsafe.getClass().getDeclaredMethod("copyMemory", new Class[] { Object.class, long.class, Object.class, long.class, long.class });
            logger.debug("sun.misc.Unsafe.copyMemory: available");
          } 
        } catch (NoSuchMethodError t) {
          logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
          throw t;
        } catch (NoSuchMethodException e) {
          logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
          throw e;
        } 
      } catch (Throwable cause) {
        unsafe = null;
      } 
    } else {
      unsafe = null;
    } 
    UNSAFE = unsafe;
    if (unsafe == null) {
      ADDRESS_FIELD_OFFSET = -1L;
      UNALIGNED = false;
    } else {
      boolean bool;
      ADDRESS_FIELD_OFFSET = objectFieldOffset(field);
      try {
        Class<?> bitsClass = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
        Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", new Class[0]);
        unalignedMethod.setAccessible(true);
        bool = Boolean.TRUE.equals(unalignedMethod.invoke(null, new Object[0]));
      } catch (Throwable t) {
        String arch = SystemPropertyUtil.get("os.arch", "");
        bool = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
      } 
      UNALIGNED = bool;
      logger.debug("java.nio.Bits.unaligned: {}", Boolean.valueOf(UNALIGNED));
    } 
  }
  
  static boolean hasUnsafe() {
    return (UNSAFE != null);
  }
  
  static void throwException(Throwable t) {
    UNSAFE.throwException(t);
  }
  
  static void freeDirectBuffer(ByteBuffer buffer) {
    Cleaner0.freeDirectBuffer(buffer);
  }
  
  static long directBufferAddress(ByteBuffer buffer) {
    return getLong(buffer, ADDRESS_FIELD_OFFSET);
  }
  
  static long arrayBaseOffset() {
    return UNSAFE.arrayBaseOffset(byte[].class);
  }
  
  static Object getObject(Object object, long fieldOffset) {
    return UNSAFE.getObject(object, fieldOffset);
  }
  
  static Object getObjectVolatile(Object object, long fieldOffset) {
    return UNSAFE.getObjectVolatile(object, fieldOffset);
  }
  
  static int getInt(Object object, long fieldOffset) {
    return UNSAFE.getInt(object, fieldOffset);
  }
  
  private static long getLong(Object object, long fieldOffset) {
    return UNSAFE.getLong(object, fieldOffset);
  }
  
  static long objectFieldOffset(Field field) {
    return UNSAFE.objectFieldOffset(field);
  }
  
  static byte getByte(long address) {
    return UNSAFE.getByte(address);
  }
  
  static short getShort(long address) {
    if (UNALIGNED)
      return UNSAFE.getShort(address); 
    if (BIG_ENDIAN)
      return (short)(getByte(address) << 8 | getByte(address + 1L) & 0xFF); 
    return (short)(getByte(address + 1L) << 8 | getByte(address) & 0xFF);
  }
  
  static int getInt(long address) {
    if (UNALIGNED)
      return UNSAFE.getInt(address); 
    if (BIG_ENDIAN)
      return getByte(address) << 24 | (getByte(address + 1L) & 0xFF) << 16 | (getByte(address + 2L) & 0xFF) << 8 | getByte(address + 3L) & 0xFF; 
    return getByte(address + 3L) << 24 | (getByte(address + 2L) & 0xFF) << 16 | (getByte(address + 1L) & 0xFF) << 8 | getByte(address) & 0xFF;
  }
  
  static long getLong(long address) {
    if (UNALIGNED)
      return UNSAFE.getLong(address); 
    if (BIG_ENDIAN)
      return getByte(address) << 56L | (getByte(address + 1L) & 0xFFL) << 48L | (getByte(address + 2L) & 0xFFL) << 40L | (getByte(address + 3L) & 0xFFL) << 32L | (getByte(address + 4L) & 0xFFL) << 24L | (getByte(address + 5L) & 0xFFL) << 16L | (getByte(address + 6L) & 0xFFL) << 8L | getByte(address + 7L) & 0xFFL; 
    return getByte(address + 7L) << 56L | (getByte(address + 6L) & 0xFFL) << 48L | (getByte(address + 5L) & 0xFFL) << 40L | (getByte(address + 4L) & 0xFFL) << 32L | (getByte(address + 3L) & 0xFFL) << 24L | (getByte(address + 2L) & 0xFFL) << 16L | (getByte(address + 1L) & 0xFFL) << 8L | getByte(address) & 0xFFL;
  }
  
  static void putOrderedObject(Object object, long address, Object value) {
    UNSAFE.putOrderedObject(object, address, value);
  }
  
  static void putByte(long address, byte value) {
    UNSAFE.putByte(address, value);
  }
  
  static void putShort(long address, short value) {
    if (UNALIGNED) {
      UNSAFE.putShort(address, value);
    } else if (BIG_ENDIAN) {
      putByte(address, (byte)(value >>> 8));
      putByte(address + 1L, (byte)value);
    } else {
      putByte(address + 1L, (byte)(value >>> 8));
      putByte(address, (byte)value);
    } 
  }
  
  static void putInt(long address, int value) {
    if (UNALIGNED) {
      UNSAFE.putInt(address, value);
    } else if (BIG_ENDIAN) {
      putByte(address, (byte)(value >>> 24));
      putByte(address + 1L, (byte)(value >>> 16));
      putByte(address + 2L, (byte)(value >>> 8));
      putByte(address + 3L, (byte)value);
    } else {
      putByte(address + 3L, (byte)(value >>> 24));
      putByte(address + 2L, (byte)(value >>> 16));
      putByte(address + 1L, (byte)(value >>> 8));
      putByte(address, (byte)value);
    } 
  }
  
  static void putLong(long address, long value) {
    if (UNALIGNED) {
      UNSAFE.putLong(address, value);
    } else if (BIG_ENDIAN) {
      putByte(address, (byte)(int)(value >>> 56L));
      putByte(address + 1L, (byte)(int)(value >>> 48L));
      putByte(address + 2L, (byte)(int)(value >>> 40L));
      putByte(address + 3L, (byte)(int)(value >>> 32L));
      putByte(address + 4L, (byte)(int)(value >>> 24L));
      putByte(address + 5L, (byte)(int)(value >>> 16L));
      putByte(address + 6L, (byte)(int)(value >>> 8L));
      putByte(address + 7L, (byte)(int)value);
    } else {
      putByte(address + 7L, (byte)(int)(value >>> 56L));
      putByte(address + 6L, (byte)(int)(value >>> 48L));
      putByte(address + 5L, (byte)(int)(value >>> 40L));
      putByte(address + 4L, (byte)(int)(value >>> 32L));
      putByte(address + 3L, (byte)(int)(value >>> 24L));
      putByte(address + 2L, (byte)(int)(value >>> 16L));
      putByte(address + 1L, (byte)(int)(value >>> 8L));
      putByte(address, (byte)(int)value);
    } 
  }
  
  static void copyMemory(long srcAddr, long dstAddr, long length) {
    while (length > 0L) {
      long size = Math.min(length, 1048576L);
      UNSAFE.copyMemory(srcAddr, dstAddr, size);
      length -= size;
      srcAddr += size;
      dstAddr += size;
    } 
  }
  
  static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length) {
    while (length > 0L) {
      long size = Math.min(length, 1048576L);
      UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
      length -= size;
      srcOffset += size;
      dstOffset += size;
    } 
  }
  
  static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> tclass, String fieldName) throws Exception {
    return new UnsafeAtomicReferenceFieldUpdater<U, W>(UNSAFE, tclass, fieldName);
  }
  
  static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> tclass, String fieldName) throws Exception {
    return new UnsafeAtomicIntegerFieldUpdater<T>(UNSAFE, tclass, fieldName);
  }
  
  static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> tclass, String fieldName) throws Exception {
    return new UnsafeAtomicLongFieldUpdater<T>(UNSAFE, tclass, fieldName);
  }
  
  static ClassLoader getClassLoader(final Class<?> clazz) {
    if (System.getSecurityManager() == null)
      return clazz.getClassLoader(); 
    return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
          public ClassLoader run() {
            return clazz.getClassLoader();
          }
        });
  }
  
  static ClassLoader getContextClassLoader() {
    if (System.getSecurityManager() == null)
      return Thread.currentThread().getContextClassLoader(); 
    return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
          public ClassLoader run() {
            return Thread.currentThread().getContextClassLoader();
          }
        });
  }
  
  static ClassLoader getSystemClassLoader() {
    if (System.getSecurityManager() == null)
      return ClassLoader.getSystemClassLoader(); 
    return AccessController.<ClassLoader>doPrivileged(new PrivilegedAction<ClassLoader>() {
          public ClassLoader run() {
            return ClassLoader.getSystemClassLoader();
          }
        });
  }
  
  static int addressSize() {
    return UNSAFE.addressSize();
  }
  
  static long allocateMemory(long size) {
    return UNSAFE.allocateMemory(size);
  }
  
  static void freeMemory(long address) {
    UNSAFE.freeMemory(address);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\io\nett\\util\internal\PlatformDependent0.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */