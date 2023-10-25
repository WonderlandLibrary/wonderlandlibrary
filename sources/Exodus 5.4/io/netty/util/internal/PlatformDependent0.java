/*
 * Decompiled with CFR 0.152.
 */
package io.netty.util.internal;

import io.netty.util.internal.Cleaner0;
import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.UnsafeAtomicIntegerFieldUpdater;
import io.netty.util.internal.UnsafeAtomicLongFieldUpdater;
import io.netty.util.internal.UnsafeAtomicReferenceFieldUpdater;
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
    private static final boolean BIG_ENDIAN;
    private static final Unsafe UNSAFE;
    private static final long UNSAFE_COPY_THRESHOLD = 0x100000L;
    private static final long ADDRESS_FIELD_OFFSET;
    private static final boolean UNALIGNED;
    private static final InternalLogger logger;

    private static long getLong(Object object, long l) {
        return UNSAFE.getLong(object, l);
    }

    static void putByte(long l, byte by) {
        UNSAFE.putByte(l, by);
    }

    static void putShort(long l, short s) {
        if (UNALIGNED) {
            UNSAFE.putShort(l, s);
        } else if (BIG_ENDIAN) {
            PlatformDependent0.putByte(l, (byte)(s >>> 8));
            PlatformDependent0.putByte(l + 1L, (byte)s);
        } else {
            PlatformDependent0.putByte(l + 1L, (byte)(s >>> 8));
            PlatformDependent0.putByte(l, (byte)s);
        }
    }

    static short getShort(long l) {
        if (UNALIGNED) {
            return UNSAFE.getShort(l);
        }
        if (BIG_ENDIAN) {
            return (short)(PlatformDependent0.getByte(l) << 8 | PlatformDependent0.getByte(l + 1L) & 0xFF);
        }
        return (short)(PlatformDependent0.getByte(l + 1L) << 8 | PlatformDependent0.getByte(l) & 0xFF);
    }

    static void copyMemory(Object object, long l, Object object2, long l2, long l3) {
        while (l3 > 0L) {
            long l4 = Math.min(l3, 0x100000L);
            UNSAFE.copyMemory(object, l, object2, l2, l4);
            l3 -= l4;
            l += l4;
            l2 += l4;
        }
    }

    static long objectFieldOffset(Field field) {
        return UNSAFE.objectFieldOffset(field);
    }

    static long allocateMemory(long l) {
        return UNSAFE.allocateMemory(l);
    }

    static void putOrderedObject(Object object, long l, Object object2) {
        UNSAFE.putOrderedObject(object, l, object2);
    }

    static void copyMemory(long l, long l2, long l3) {
        while (l3 > 0L) {
            long l4 = Math.min(l3, 0x100000L);
            UNSAFE.copyMemory(l, l2, l4);
            l3 -= l4;
            l += l4;
            l2 += l4;
        }
    }

    static <T> AtomicIntegerFieldUpdater<T> newAtomicIntegerFieldUpdater(Class<?> clazz, String string) throws Exception {
        return new UnsafeAtomicIntegerFieldUpdater(UNSAFE, clazz, string);
    }

    static long getLong(long l) {
        if (UNALIGNED) {
            return UNSAFE.getLong(l);
        }
        if (BIG_ENDIAN) {
            return (long)PlatformDependent0.getByte(l) << 56 | ((long)PlatformDependent0.getByte(l + 1L) & 0xFFL) << 48 | ((long)PlatformDependent0.getByte(l + 2L) & 0xFFL) << 40 | ((long)PlatformDependent0.getByte(l + 3L) & 0xFFL) << 32 | ((long)PlatformDependent0.getByte(l + 4L) & 0xFFL) << 24 | ((long)PlatformDependent0.getByte(l + 5L) & 0xFFL) << 16 | ((long)PlatformDependent0.getByte(l + 6L) & 0xFFL) << 8 | (long)PlatformDependent0.getByte(l + 7L) & 0xFFL;
        }
        return (long)PlatformDependent0.getByte(l + 7L) << 56 | ((long)PlatformDependent0.getByte(l + 6L) & 0xFFL) << 48 | ((long)PlatformDependent0.getByte(l + 5L) & 0xFFL) << 40 | ((long)PlatformDependent0.getByte(l + 4L) & 0xFFL) << 32 | ((long)PlatformDependent0.getByte(l + 3L) & 0xFFL) << 24 | ((long)PlatformDependent0.getByte(l + 2L) & 0xFFL) << 16 | ((long)PlatformDependent0.getByte(l + 1L) & 0xFFL) << 8 | (long)PlatformDependent0.getByte(l) & 0xFFL;
    }

    static long arrayBaseOffset() {
        return UNSAFE.arrayBaseOffset(byte[].class);
    }

    static int getInt(long l) {
        if (UNALIGNED) {
            return UNSAFE.getInt(l);
        }
        if (BIG_ENDIAN) {
            return PlatformDependent0.getByte(l) << 24 | (PlatformDependent0.getByte(l + 1L) & 0xFF) << 16 | (PlatformDependent0.getByte(l + 2L) & 0xFF) << 8 | PlatformDependent0.getByte(l + 3L) & 0xFF;
        }
        return PlatformDependent0.getByte(l + 3L) << 24 | (PlatformDependent0.getByte(l + 2L) & 0xFF) << 16 | (PlatformDependent0.getByte(l + 1L) & 0xFF) << 8 | PlatformDependent0.getByte(l) & 0xFF;
    }

    static void putInt(long l, int n) {
        if (UNALIGNED) {
            UNSAFE.putInt(l, n);
        } else if (BIG_ENDIAN) {
            PlatformDependent0.putByte(l, (byte)(n >>> 24));
            PlatformDependent0.putByte(l + 1L, (byte)(n >>> 16));
            PlatformDependent0.putByte(l + 2L, (byte)(n >>> 8));
            PlatformDependent0.putByte(l + 3L, (byte)n);
        } else {
            PlatformDependent0.putByte(l + 3L, (byte)(n >>> 24));
            PlatformDependent0.putByte(l + 2L, (byte)(n >>> 16));
            PlatformDependent0.putByte(l + 1L, (byte)(n >>> 8));
            PlatformDependent0.putByte(l, (byte)n);
        }
    }

    static void freeDirectBuffer(ByteBuffer byteBuffer) {
        Cleaner0.freeDirectBuffer(byteBuffer);
    }

    static void putLong(long l, long l2) {
        if (UNALIGNED) {
            UNSAFE.putLong(l, l2);
        } else if (BIG_ENDIAN) {
            PlatformDependent0.putByte(l, (byte)(l2 >>> 56));
            PlatformDependent0.putByte(l + 1L, (byte)(l2 >>> 48));
            PlatformDependent0.putByte(l + 2L, (byte)(l2 >>> 40));
            PlatformDependent0.putByte(l + 3L, (byte)(l2 >>> 32));
            PlatformDependent0.putByte(l + 4L, (byte)(l2 >>> 24));
            PlatformDependent0.putByte(l + 5L, (byte)(l2 >>> 16));
            PlatformDependent0.putByte(l + 6L, (byte)(l2 >>> 8));
            PlatformDependent0.putByte(l + 7L, (byte)l2);
        } else {
            PlatformDependent0.putByte(l + 7L, (byte)(l2 >>> 56));
            PlatformDependent0.putByte(l + 6L, (byte)(l2 >>> 48));
            PlatformDependent0.putByte(l + 5L, (byte)(l2 >>> 40));
            PlatformDependent0.putByte(l + 4L, (byte)(l2 >>> 32));
            PlatformDependent0.putByte(l + 3L, (byte)(l2 >>> 24));
            PlatformDependent0.putByte(l + 2L, (byte)(l2 >>> 16));
            PlatformDependent0.putByte(l + 1L, (byte)(l2 >>> 8));
            PlatformDependent0.putByte(l, (byte)l2);
        }
    }

    static ClassLoader getClassLoader(final Class<?> clazz) {
        if (System.getSecurityManager() == null) {
            return clazz.getClassLoader();
        }
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return clazz.getClassLoader();
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        if (System.getSecurityManager() == null) {
            return ClassLoader.getSystemClassLoader();
        }
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return ClassLoader.getSystemClassLoader();
            }
        });
    }

    static void freeMemory(long l) {
        UNSAFE.freeMemory(l);
    }

    static ClassLoader getContextClassLoader() {
        if (System.getSecurityManager() == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>(){

            @Override
            public ClassLoader run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
    }

    static void throwException(Throwable throwable) {
        UNSAFE.throwException(throwable);
    }

    static byte getByte(long l) {
        return UNSAFE.getByte(l);
    }

    static {
        Unsafe unsafe;
        Field field;
        logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
        BIG_ENDIAN = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1);
        try {
            field = Buffer.class.getDeclaredField("address");
            field.setAccessible(true);
            if (field.getLong(ByteBuffer.allocate(1)) != 0L) {
                field = null;
            } else if (field.getLong(byteBuffer) == 0L) {
                field = null;
            }
        }
        catch (Throwable throwable) {
            field = null;
        }
        logger.debug("java.nio.Buffer.address: {}", (Object)(field != null ? "available" : "unavailable"));
        if (field != null) {
            try {
                Field field2 = Unsafe.class.getDeclaredField("theUnsafe");
                field2.setAccessible(true);
                unsafe = (Unsafe)field2.get(null);
                logger.debug("sun.misc.Unsafe.theUnsafe: {}", (Object)(unsafe != null ? "available" : "unavailable"));
                try {
                    if (unsafe != null) {
                        unsafe.getClass().getDeclaredMethod("copyMemory", Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE);
                        logger.debug("sun.misc.Unsafe.copyMemory: available");
                    }
                }
                catch (NoSuchMethodError noSuchMethodError) {
                    logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
                    throw noSuchMethodError;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    logger.debug("sun.misc.Unsafe.copyMemory: unavailable");
                    throw noSuchMethodException;
                }
            }
            catch (Throwable throwable) {
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
            boolean bl;
            ADDRESS_FIELD_OFFSET = PlatformDependent0.objectFieldOffset(field);
            try {
                Class<?> clazz = Class.forName("java.nio.Bits", false, ClassLoader.getSystemClassLoader());
                Method method = clazz.getDeclaredMethod("unaligned", new Class[0]);
                method.setAccessible(true);
                bl = Boolean.TRUE.equals(method.invoke(null, new Object[0]));
            }
            catch (Throwable throwable) {
                String string = SystemPropertyUtil.get("os.arch", "");
                bl = string.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
            }
            UNALIGNED = bl;
            logger.debug("java.nio.Bits.unaligned: {}", (Object)UNALIGNED);
        }
    }

    static <U, W> AtomicReferenceFieldUpdater<U, W> newAtomicReferenceFieldUpdater(Class<U> clazz, String string) throws Exception {
        return new UnsafeAtomicReferenceFieldUpdater(UNSAFE, clazz, string);
    }

    static boolean hasUnsafe() {
        return UNSAFE != null;
    }

    static Object getObjectVolatile(Object object, long l) {
        return UNSAFE.getObjectVolatile(object, l);
    }

    static Object getObject(Object object, long l) {
        return UNSAFE.getObject(object, l);
    }

    static <T> AtomicLongFieldUpdater<T> newAtomicLongFieldUpdater(Class<?> clazz, String string) throws Exception {
        return new UnsafeAtomicLongFieldUpdater(UNSAFE, clazz, string);
    }

    private PlatformDependent0() {
    }

    static long directBufferAddress(ByteBuffer byteBuffer) {
        return PlatformDependent0.getLong(byteBuffer, ADDRESS_FIELD_OFFSET);
    }

    static int getInt(Object object, long l) {
        return UNSAFE.getInt(object, l);
    }

    static int addressSize() {
        return UNSAFE.addressSize();
    }
}

