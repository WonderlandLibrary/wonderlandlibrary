// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Arrays;
import com.google.common.annotations.GwtIncompatible;
import java.lang.reflect.Array;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible(emulated = true)
public final class ObjectArrays
{
    private ObjectArrays() {
    }
    
    @GwtIncompatible
    public static <T> T[] newArray(final Class<T> type, final int length) {
        return (T[])Array.newInstance(type, length);
    }
    
    public static <T> T[] newArray(final T[] reference, final int length) {
        return Platform.newArray(reference, length);
    }
    
    @GwtIncompatible
    public static <T> T[] concat(final T[] first, final T[] second, final Class<T> type) {
        final T[] result = newArray(type, first.length + second.length);
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
    
    public static <T> T[] concat(@ParametricNullness final T element, final T[] array) {
        final T[] result = newArray(array, array.length + 1);
        result[0] = element;
        System.arraycopy(array, 0, result, 1, array.length);
        return result;
    }
    
    public static <T> T[] concat(final T[] array, @ParametricNullness final T element) {
        final T[] result = Arrays.copyOf(array, array.length + 1);
        result[array.length] = element;
        return result;
    }
    
    static <T> T[] toArrayImpl(final Collection<?> c, T[] array) {
        final int size = c.size();
        if (array.length < size) {
            array = newArray(array, size);
        }
        fillArray(c, array);
        if (array.length > size) {
            final Object[] unsoundlyCovariantArray = array;
            unsoundlyCovariantArray[size] = null;
        }
        return array;
    }
    
    static <T> T[] toArrayImpl(final Object[] src, final int offset, final int len, T[] dst) {
        Preconditions.checkPositionIndexes(offset, offset + len, src.length);
        if (dst.length < len) {
            dst = newArray(dst, len);
        }
        else if (dst.length > len) {
            final Object[] unsoundlyCovariantArray = dst;
            unsoundlyCovariantArray[len] = null;
        }
        System.arraycopy(src, offset, dst, 0, len);
        return dst;
    }
    
    static Object[] toArrayImpl(final Collection<?> c) {
        return fillArray(c, new Object[c.size()]);
    }
    
    static Object[] copyAsObjectArray(final Object[] elements, final int offset, final int length) {
        Preconditions.checkPositionIndexes(offset, offset + length, elements.length);
        if (length == 0) {
            return new Object[0];
        }
        final Object[] result = new Object[length];
        System.arraycopy(elements, offset, result, 0, length);
        return result;
    }
    
    @CanIgnoreReturnValue
    private static Object[] fillArray(final Iterable<?> elements, final Object[] array) {
        int i = 0;
        for (final Object element : elements) {
            array[i++] = element;
        }
        return array;
    }
    
    static void swap(final Object[] array, final int i, final int j) {
        final Object temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    @CanIgnoreReturnValue
    static Object[] checkElementsNotNull(final Object... array) {
        return checkElementsNotNull(array, array.length);
    }
    
    @CanIgnoreReturnValue
    static Object[] checkElementsNotNull(final Object[] array, final int length) {
        for (int i = 0; i < length; ++i) {
            checkElementNotNull(array[i], i);
        }
        return array;
    }
    
    @CanIgnoreReturnValue
    static Object checkElementNotNull(final Object element, final int index) {
        if (element == null) {
            throw new NullPointerException(new StringBuilder(20).append("at index ").append(index).toString());
        }
        return element;
    }
}
