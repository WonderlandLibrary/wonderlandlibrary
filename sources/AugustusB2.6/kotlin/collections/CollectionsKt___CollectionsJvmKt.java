// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.collections;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import java.util.ArrayList;
import java.util.Collection;
import kotlin.jvm.internal.Intrinsics;
import java.util.List;

class CollectionsKt___CollectionsJvmKt extends CollectionsKt__MutableCollectionsKt
{
    public static <T> List<T> toList(final Iterable<? extends T> $this$toList) {
        Intrinsics.checkParameterIsNotNull($this$toList, "$this$toList");
        switch (((Collection)$this$toList).size()) {
            case 0: {
                return (List<T>)EmptyList.INSTANCE;
            }
            case 1: {
                return ArraysKt__ArraysJVMKt.listOf((T)((List<T>)$this$toList).get(0));
            }
            default: {
                return toMutableList((Collection<? extends T>)$this$toList);
            }
        }
    }
    
    public static <T> List<T> toMutableList(final Collection<? extends T> $this$toMutableList) {
        Intrinsics.checkParameterIsNotNull($this$toMutableList, "$this$toMutableList");
        return new ArrayList<T>($this$toMutableList);
    }
    
    public static <T, A extends Appendable> A joinTo(final Iterable<? extends T> $this$joinTo, final A buffer, final CharSequence separator, final CharSequence prefix, final CharSequence postfix, final int limit, final CharSequence truncated, final Function1<? super T, ? extends CharSequence> transform) {
        Intrinsics.checkParameterIsNotNull($this$joinTo, "$this$joinTo");
        Intrinsics.checkParameterIsNotNull(buffer, "buffer");
        Intrinsics.checkParameterIsNotNull(separator, "separator");
        Intrinsics.checkParameterIsNotNull(prefix, "prefix");
        Intrinsics.checkParameterIsNotNull(postfix, "postfix");
        Intrinsics.checkParameterIsNotNull(truncated, "truncated");
        buffer.append(prefix);
        int count = 0;
        for (final Object element : $this$joinTo) {
            if (++count > 1) {
                buffer.append(separator);
            }
            if (limit >= 0 && count > limit) {
                break;
            }
            final Object obj = element;
            Intrinsics.checkParameterIsNotNull(buffer, "$this$appendElement");
            if (transform != null) {
                buffer.append((CharSequence)transform.invoke((Object)obj));
            }
            else {
                final Object o = obj;
                if (o == null || o instanceof CharSequence) {
                    buffer.append((CharSequence)obj);
                }
                else if (obj instanceof Character) {
                    buffer.append((char)obj);
                }
                else {
                    buffer.append(String.valueOf(obj));
                }
            }
        }
        if (limit >= 0 && count > limit) {
            buffer.append(truncated);
        }
        buffer.append(postfix);
        return buffer;
    }
    
    public static int sumOfInt(final Iterable<Integer> $this$sum) {
        Intrinsics.checkParameterIsNotNull($this$sum, "$this$sum");
        int sum = 0;
        final Iterator<Integer> iterator = $this$sum.iterator();
        while (iterator.hasNext()) {
            final int element = iterator.next().intValue();
            sum += element;
        }
        return sum;
    }
}
