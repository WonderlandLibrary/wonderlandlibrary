// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.nio.ByteOrder;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import sun.misc.Unsafe;
import java.util.Objects;
import java.util.Arrays;
import com.google.common.annotations.VisibleForTesting;
import java.util.Comparator;
import com.google.common.annotations.Beta;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
public final class UnsignedBytes
{
    public static final byte MAX_POWER_OF_TWO = Byte.MIN_VALUE;
    public static final byte MAX_VALUE = -1;
    private static final int UNSIGNED_MASK = 255;
    
    private UnsignedBytes() {
    }
    
    public static int toInt(final byte value) {
        return value & 0xFF;
    }
    
    @CanIgnoreReturnValue
    public static byte checkedCast(final long value) {
        Preconditions.checkArgument(value >> 8 == 0L, "out of range: %s", value);
        return (byte)value;
    }
    
    public static byte saturatedCast(final long value) {
        if (value > toInt((byte)(-1))) {
            return -1;
        }
        if (value < 0L) {
            return 0;
        }
        return (byte)value;
    }
    
    public static int compare(final byte a, final byte b) {
        return toInt(a) - toInt(b);
    }
    
    public static byte min(final byte... array) {
        Preconditions.checkArgument(array.length > 0);
        int min = toInt(array[0]);
        for (int i = 1; i < array.length; ++i) {
            final int next = toInt(array[i]);
            if (next < min) {
                min = next;
            }
        }
        return (byte)min;
    }
    
    public static byte max(final byte... array) {
        Preconditions.checkArgument(array.length > 0);
        int max = toInt(array[0]);
        for (int i = 1; i < array.length; ++i) {
            final int next = toInt(array[i]);
            if (next > max) {
                max = next;
            }
        }
        return (byte)max;
    }
    
    @Beta
    public static String toString(final byte x) {
        return toString(x, 10);
    }
    
    @Beta
    public static String toString(final byte x, final int radix) {
        Preconditions.checkArgument(radix >= 2 && radix <= 36, "radix (%s) must be between Character.MIN_RADIX and Character.MAX_RADIX", radix);
        return Integer.toString(toInt(x), radix);
    }
    
    @Beta
    @CanIgnoreReturnValue
    public static byte parseUnsignedByte(final String string) {
        return parseUnsignedByte(string, 10);
    }
    
    @Beta
    @CanIgnoreReturnValue
    public static byte parseUnsignedByte(final String string, final int radix) {
        final int parse = Integer.parseInt(Preconditions.checkNotNull(string), radix);
        if (parse >> 8 == 0) {
            return (byte)parse;
        }
        throw new NumberFormatException(new StringBuilder(25).append("out of range: ").append(parse).toString());
    }
    
    public static String join(final String separator, final byte... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * (3 + separator.length()));
        builder.append(toInt(array[0]));
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(toString(array[i]));
        }
        return builder.toString();
    }
    
    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparatorHolder.BEST_COMPARATOR;
    }
    
    @VisibleForTesting
    static Comparator<byte[]> lexicographicalComparatorJavaImpl() {
        return LexicographicalComparatorHolder.PureJavaComparator.INSTANCE;
    }
    
    private static byte flip(final byte b) {
        return (byte)(b ^ 0x80);
    }
    
    public static void sort(final byte[] array) {
        Preconditions.checkNotNull(array);
        sort(array, 0, array.length);
    }
    
    public static void sort(final byte[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; ++i) {
            array[i] = flip(array[i]);
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            array[i] = flip(array[i]);
        }
    }
    
    public static void sortDescending(final byte[] array) {
        Preconditions.checkNotNull(array);
        sortDescending(array, 0, array.length);
    }
    
    public static void sortDescending(final byte[] array, final int fromIndex, final int toIndex) {
        Preconditions.checkNotNull(array);
        Preconditions.checkPositionIndexes(fromIndex, toIndex, array.length);
        for (int i = fromIndex; i < toIndex; ++i) {
            final int n = i;
            array[n] ^= 0x7F;
        }
        Arrays.sort(array, fromIndex, toIndex);
        for (int i = fromIndex; i < toIndex; ++i) {
            final int n2 = i;
            array[n2] ^= 0x7F;
        }
    }
    
    @VisibleForTesting
    static class LexicographicalComparatorHolder
    {
        static final String UNSAFE_COMPARATOR_NAME;
        static final Comparator<byte[]> BEST_COMPARATOR;
        
        static Comparator<byte[]> getBestComparator() {
            try {
                final Class<?> theClass = Class.forName(LexicographicalComparatorHolder.UNSAFE_COMPARATOR_NAME);
                final Object[] constants = Objects.requireNonNull((Object[])theClass.getEnumConstants());
                final Comparator<byte[]> comparator = (Comparator<byte[]>)constants[0];
                return comparator;
            }
            catch (Throwable t) {
                return UnsignedBytes.lexicographicalComparatorJavaImpl();
            }
        }
        
        static {
            UNSAFE_COMPARATOR_NAME = String.valueOf(LexicographicalComparatorHolder.class.getName()).concat("$UnsafeComparator");
            BEST_COMPARATOR = getBestComparator();
        }
        
        @VisibleForTesting
        enum UnsafeComparator implements Comparator<byte[]>
        {
            INSTANCE;
            
            static final boolean BIG_ENDIAN;
            static final Unsafe theUnsafe;
            static final int BYTE_ARRAY_BASE_OFFSET;
            
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
            
            @Override
            public int compare(final byte[] left, final byte[] right) {
                final int stride = 8;
                final int minLength = Math.min(left.length, right.length);
                final int strideLimit = minLength & 0xFFFFFFF8;
                int i = 0;
                while (i < strideLimit) {
                    final long lw = UnsafeComparator.theUnsafe.getLong(left, UnsafeComparator.BYTE_ARRAY_BASE_OFFSET + (long)i);
                    final long rw = UnsafeComparator.theUnsafe.getLong(right, UnsafeComparator.BYTE_ARRAY_BASE_OFFSET + (long)i);
                    if (lw != rw) {
                        if (UnsafeComparator.BIG_ENDIAN) {
                            return UnsignedLongs.compare(lw, rw);
                        }
                        final int n = Long.numberOfTrailingZeros(lw ^ rw) & 0xFFFFFFF8;
                        return (int)(lw >>> n & 0xFFL) - (int)(rw >>> n & 0xFFL);
                    }
                    else {
                        i += 8;
                    }
                }
                while (i < minLength) {
                    final int result = UnsignedBytes.compare(left[i], right[i]);
                    if (result != 0) {
                        return result;
                    }
                    ++i;
                }
                return left.length - right.length;
            }
            
            @Override
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (sun.misc.Unsafe version)";
            }
            
            private static /* synthetic */ UnsafeComparator[] $values() {
                return new UnsafeComparator[] { UnsafeComparator.INSTANCE };
            }
            
            static {
                $VALUES = $values();
                BIG_ENDIAN = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
                theUnsafe = getUnsafe();
                BYTE_ARRAY_BASE_OFFSET = UnsafeComparator.theUnsafe.arrayBaseOffset(byte[].class);
                if (!"64".equals(System.getProperty("sun.arch.data.model")) || UnsafeComparator.BYTE_ARRAY_BASE_OFFSET % 8 != 0 || UnsafeComparator.theUnsafe.arrayIndexScale(byte[].class) != 1) {
                    throw new Error();
                }
            }
        }
        
        enum PureJavaComparator implements Comparator<byte[]>
        {
            INSTANCE;
            
            @Override
            public int compare(final byte[] left, final byte[] right) {
                for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                    final int result = UnsignedBytes.compare(left[i], right[i]);
                    if (result != 0) {
                        return result;
                    }
                }
                return left.length - right.length;
            }
            
            @Override
            public String toString() {
                return "UnsignedBytes.lexicographicalComparator() (pure Java version)";
            }
            
            private static /* synthetic */ PureJavaComparator[] $values() {
                return new PureJavaComparator[] { PureJavaComparator.INSTANCE };
            }
            
            static {
                $VALUES = $values();
            }
        }
    }
}
