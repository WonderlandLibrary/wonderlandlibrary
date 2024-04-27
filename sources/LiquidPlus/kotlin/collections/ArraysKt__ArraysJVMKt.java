/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.collections;

import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import kotlin.Metadata;
import kotlin.PublishedApi;
import kotlin.SinceKotlin;
import kotlin.internal.InlineOnly;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001e\n\u0002\b\u0002\u001a/\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\u0000\u00a2\u0006\u0002\u0010\u0006\u001a\u0018\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0001\u001a#\u0010\n\u001a\u00020\u0005\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0001H\u0001\u00a2\u0006\u0004\b\u000b\u0010\f\u001a,\u0010\r\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0001H\u0086\b\u00a2\u0006\u0002\u0010\u000e\u001a\u0015\u0010\u000f\u001a\u00020\u0010*\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0087\b\u001a&\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0006\b\u0000\u0010\u0002\u0018\u0001*\b\u0012\u0004\u0012\u0002H\u00020\u0015H\u0086\b\u00a2\u0006\u0002\u0010\u0016\u00a8\u0006\u0017"}, d2={"arrayOfNulls", "", "T", "reference", "size", "", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "copyOfRangeToIndexCheck", "", "toIndex", "contentDeepHashCodeImpl", "contentDeepHashCode", "([Ljava/lang/Object;)I", "orEmpty", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "", "charset", "Ljava/nio/charset/Charset;", "toTypedArray", "", "(Ljava/util/Collection;)[Ljava/lang/Object;", "kotlin-stdlib"}, xs="kotlin/collections/ArraysKt")
class ArraysKt__ArraysJVMKt {
    public static final /* synthetic */ <T> T[] orEmpty(T[] $this$orEmpty) {
        Object[] objectArray;
        boolean $i$f$orEmpty = false;
        T[] TArray = $this$orEmpty;
        if (TArray == null) {
            boolean $i$f$emptyArray = false;
            Intrinsics.reifiedOperationMarker(0, "T?");
            objectArray = new Object[]{};
        } else {
            objectArray = TArray;
        }
        return objectArray;
    }

    @InlineOnly
    private static final String toString(byte[] $this$toString, Charset charset) {
        Intrinsics.checkNotNullParameter($this$toString, "<this>");
        Intrinsics.checkNotNullParameter(charset, "charset");
        return new String($this$toString, charset);
    }

    public static final /* synthetic */ <T> T[] toTypedArray(Collection<? extends T> $this$toTypedArray) {
        Intrinsics.checkNotNullParameter($this$toTypedArray, "<this>");
        boolean $i$f$toTypedArray = false;
        Collection<Object> thisCollection = $this$toTypedArray;
        Intrinsics.reifiedOperationMarker(0, "T?");
        Object[] objectArray = thisCollection.toArray(new Object[0]);
        if (objectArray == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.toTypedArray>");
        }
        return objectArray;
    }

    @NotNull
    public static final <T> T[] arrayOfNulls(@NotNull T[] reference, int size) {
        Intrinsics.checkNotNullParameter(reference, "reference");
        Object object = Array.newInstance(reference.getClass().getComponentType(), size);
        if (object == null) {
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<T of kotlin.collections.ArraysKt__ArraysJVMKt.arrayOfNulls>");
        }
        return (Object[])object;
    }

    @SinceKotlin(version="1.3")
    public static final void copyOfRangeToIndexCheck(int toIndex, int size) {
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex (" + toIndex + ") is greater than size (" + size + ").");
        }
    }

    @SinceKotlin(version="1.3")
    @PublishedApi
    @JvmName(name="contentDeepHashCode")
    public static final <T> int contentDeepHashCode(@Nullable T[] $this$contentDeepHashCodeImpl) {
        return Arrays.deepHashCode($this$contentDeepHashCodeImpl);
    }
}

