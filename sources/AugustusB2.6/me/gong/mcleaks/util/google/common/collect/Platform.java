// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.collect;

import java.lang.reflect.Array;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
final class Platform
{
    static <T> T[] newArray(final T[] reference, final int length) {
        final Class<?> type = reference.getClass().getComponentType();
        final T[] result = (T[])Array.newInstance(type, length);
        return result;
    }
    
    static MapMaker tryWeakKeys(final MapMaker mapMaker) {
        return mapMaker.weakKeys();
    }
    
    private Platform() {
    }
}
