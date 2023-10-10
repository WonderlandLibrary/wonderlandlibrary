// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.lang.reflect.Array;

public class ArrayUtils
{
    public static boolean isEmpty(final byte[] array) {
        return getLength(array) == 0;
    }
    
    public static int getLength(final Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }
    
    private static Object remove(final Object array, final int index) {
        final int length = getLength(array);
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + length);
        }
        final Object result = Array.newInstance(array.getClass().getComponentType(), length - 1);
        System.arraycopy(array, 0, result, 0, index);
        if (index < length - 1) {
            System.arraycopy(array, index + 1, result, index, length - index - 1);
        }
        return result;
    }
    
    public static <T> T[] remove(final T[] array, final int index) {
        return (T[])remove((Object)array, index);
    }
}
