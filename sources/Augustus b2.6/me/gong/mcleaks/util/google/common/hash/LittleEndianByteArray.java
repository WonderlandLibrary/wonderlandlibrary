// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.hash;

import me.gong.mcleaks.util.google.common.primitives.Longs;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
import java.nio.ByteOrder;

final class LittleEndianByteArray
{
    private static final LittleEndianBytes byteArray;
    
    static long load64(final byte[] input, final int offset) {
        assert input.length >= offset + 8;
        return LittleEndianByteArray.byteArray.getLongLittleEndian(input, offset);
    }
    
    static long load64Safely(final byte[] input, final int offset, final int length) {
        long result = 0L;
        for (int limit = Math.min(length, 8), i = 0; i < limit; ++i) {
            result |= ((long)input[offset + i] & 0xFFL) << i * 8;
        }
        return result;
    }
    
    static void store64(final byte[] sink, final int offset, final long value) {
        assert offset >= 0 && offset + 8 <= sink.length;
        LittleEndianByteArray.byteArray.putLongLittleEndian(sink, offset, value);
    }
    
    static int load32(final byte[] source, final int offset) {
        return (source[offset] & 0xFF) | (source[offset + 1] & 0xFF) << 8 | (source[offset + 2] & 0xFF) << 16 | (source[offset + 3] & 0xFF) << 24;
    }
    
    static boolean usingUnsafe() {
        return LittleEndianByteArray.byteArray instanceof UnsafeByteArray;
    }
    
    private LittleEndianByteArray() {
    }
    
    static {
        LittleEndianBytes theGetter = JavaLittleEndianBytes.INSTANCE;
        try {
            final String arch = System.getProperty("os.arch");
            if ("amd64".equals(arch) || "aarch64".equals(arch)) {
                theGetter = (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN) ? UnsafeByteArray.UNSAFE_LITTLE_ENDIAN : UnsafeByteArray.UNSAFE_BIG_ENDIAN);
            }
        }
        catch (Throwable t) {}
        byteArray = theGetter;
    }
    
    private enum UnsafeByteArray implements LittleEndianBytes
    {
        UNSAFE_LITTLE_ENDIAN {
            @Override
            public long getLongLittleEndian(final byte[] array, final int offset) {
                return UnsafeByteArray.theUnsafe.getLong(array, offset + (long)UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
            }
            
            @Override
            public void putLongLittleEndian(final byte[] array, final int offset, final long value) {
                UnsafeByteArray.theUnsafe.putLong(array, offset + (long)UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, value);
            }
        }, 
        UNSAFE_BIG_ENDIAN {
            @Override
            public long getLongLittleEndian(final byte[] array, final int offset) {
                final long bigEndian = UnsafeByteArray.theUnsafe.getLong(array, offset + (long)UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET);
                return Long.reverseBytes(bigEndian);
            }
            
            @Override
            public void putLongLittleEndian(final byte[] array, final int offset, final long value) {
                final long littleEndianValue = Long.reverseBytes(value);
                UnsafeByteArray.theUnsafe.putLong(array, offset + (long)UnsafeByteArray.BYTE_ARRAY_BASE_OFFSET, littleEndianValue);
            }
        };
        
        private static final Unsafe theUnsafe;
        private static final int BYTE_ARRAY_BASE_OFFSET;
        
        private static Unsafe getUnsafe() {
            try {
                return Unsafe.getUnsafe();
            }
            catch (SecurityException ex) {
                try {
                    return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                        @Override
                        public Unsafe run() throws Exception {
                            final Class<Unsafe> k = Unsafe.class;
                            for (final Field f : k.getDeclaredFields()) {
                                f.setAccessible(true);
                                final Object x = f.get(null);
                                if (k.isInstance(x)) {
                                    return k.cast(x);
                                }
                            }
                            throw new NoSuchFieldError("the Unsafe");
                        }
                    });
                }
                catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            }
        }
        
        static {
            theUnsafe = getUnsafe();
            BYTE_ARRAY_BASE_OFFSET = UnsafeByteArray.theUnsafe.arrayBaseOffset(byte[].class);
            if (UnsafeByteArray.theUnsafe.arrayIndexScale(byte[].class) != 1) {
                throw new AssertionError();
            }
        }
    }
    
    private enum JavaLittleEndianBytes implements LittleEndianBytes
    {
        INSTANCE {
            @Override
            public long getLongLittleEndian(final byte[] source, final int offset) {
                return Longs.fromBytes(source[offset + 7], source[offset + 6], source[offset + 5], source[offset + 4], source[offset + 3], source[offset + 2], source[offset + 1], source[offset]);
            }
            
            @Override
            public void putLongLittleEndian(final byte[] sink, final int offset, final long value) {
                long mask = 255L;
                for (int i = 0; i < 8; ++i) {
                    sink[offset + i] = (byte)((value & mask) >> i * 8);
                    mask <<= 8;
                }
            }
        };
    }
    
    private interface LittleEndianBytes
    {
        long getLongLittleEndian(final byte[] p0, final int p1);
        
        void putLongLittleEndian(final byte[] p0, final int p1, final long p2);
    }
}
