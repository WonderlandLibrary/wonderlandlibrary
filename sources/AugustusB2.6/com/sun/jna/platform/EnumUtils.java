// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna.platform;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import com.sun.jna.platform.win32.FlagEnum;

public class EnumUtils
{
    public static final int UNINITIALIZED = -1;
    
    public static <E extends Enum<E>> int toInteger(final E val) {
        final E[] vals = (E[])val.getClass().getEnumConstants();
        for (int idx = 0; idx < vals.length; ++idx) {
            if (vals[idx] == val) {
                return idx;
            }
        }
        throw new IllegalArgumentException();
    }
    
    public static <E extends Enum<E>> E fromInteger(final int idx, final Class<E> clazz) {
        if (idx == -1) {
            return null;
        }
        final E[] vals = clazz.getEnumConstants();
        return vals[idx];
    }
    
    public static <T extends FlagEnum> Set<T> setFromInteger(final int flags, final Class<T> clazz) {
        final T[] vals = clazz.getEnumConstants();
        final Set<T> result = new HashSet<T>();
        for (final T val : vals) {
            if ((flags & val.getFlag()) != 0x0) {
                result.add(val);
            }
        }
        return result;
    }
    
    public static <T extends FlagEnum> int setToInteger(final Set<T> set) {
        int sum = 0;
        for (final T t : set) {
            sum |= t.getFlag();
        }
        return sum;
    }
}
