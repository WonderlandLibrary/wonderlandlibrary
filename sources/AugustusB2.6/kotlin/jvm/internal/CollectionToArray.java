// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import kotlin.TypeCastException;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collection;

public final class CollectionToArray
{
    private static final Object[] EMPTY;
    
    public static final Object[] toArray(final Collection<?> collection) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        final int size$iv;
        if ((size$iv = collection.size()) == 0) {
            return CollectionToArray.EMPTY;
        }
        final Iterator<?> iterator;
        if (!(iterator = collection.iterator()).hasNext()) {
            return CollectionToArray.EMPTY;
        }
        Object[] result$iv = new Object[size$iv];
        int i$iv = 0;
        while (true) {
            result$iv[i$iv++] = iterator.next();
            if (i$iv >= result$iv.length) {
                if (!iterator.hasNext()) {
                    return result$iv;
                }
                int newSize$iv;
                if ((newSize$iv = i$iv * 3 + 1 >>> 1) <= i$iv) {
                    if (i$iv >= 2147483645) {
                        throw new OutOfMemoryError();
                    }
                    newSize$iv = 2147483645;
                }
                final Object[] copy = Arrays.copyOf(result$iv, newSize$iv);
                Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, newSize)");
                result$iv = copy;
            }
            else {
                if (!iterator.hasNext()) {
                    final Object[] copy2 = Arrays.copyOf(result$iv = result$iv, (int)collection);
                    Intrinsics.checkExpressionValueIsNotNull(copy2, "Arrays.copyOf(result, size)");
                    return copy2;
                }
                continue;
            }
        }
    }
    
    public static final Object[] toArray(final Collection<?> collection, final Object[] a) {
        Intrinsics.checkParameterIsNotNull(collection, "collection");
        if (a == null) {
            throw new NullPointerException();
        }
        final int size$iv;
        if ((size$iv = collection.size()) == 0) {
            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }
        final Iterator iter$iv;
        if (!(iter$iv = collection.iterator()).hasNext()) {
            if (a.length > 0) {
                a[0] = null;
            }
            return a;
        }
        Object[] array;
        if (size$iv <= a.length) {
            array = a;
        }
        else {
            final Object instance = Array.newInstance(a.getClass().getComponentType(), size$iv);
            if (instance == null) {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
            }
            array = (Object[])instance;
        }
        Object[] result$iv = array;
        int n = 0;
        while (true) {
            result$iv[n++] = iter$iv.next();
            if (n >= result$iv.length) {
                if (!iter$iv.hasNext()) {
                    return result$iv;
                }
                int newSize$iv;
                if ((newSize$iv = n * 3 + 1 >>> 1) <= n) {
                    if (n >= 2147483645) {
                        throw new OutOfMemoryError();
                    }
                    newSize$iv = 2147483645;
                }
                final Object[] copy = Arrays.copyOf(result$iv, newSize$iv);
                Intrinsics.checkExpressionValueIsNotNull(copy, "Arrays.copyOf(result, newSize)");
                result$iv = copy;
            }
            else {
                if (iter$iv.hasNext()) {
                    continue;
                }
                final Object[] result;
                if ((result = result$iv) == a) {
                    a[collection] = null;
                    return a;
                }
                final Object[] copy2 = Arrays.copyOf(result, (int)collection);
                Intrinsics.checkExpressionValueIsNotNull(copy2, "Arrays.copyOf(result, size)");
                return copy2;
            }
        }
    }
    
    static {
        EMPTY = new Object[0];
    }
}
