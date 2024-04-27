/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections.builders;

import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=2, xi=48, d1={"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u001a!\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\u0005\u001a+\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00070\u0001\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\b\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\t\u001a%\u0010\n\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\f\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\r\u001a-\u0010\u000e\u001a\u00020\u000b\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\u0010\u0011\u001a9\u0010\u0012\u001a\u00020\u0013\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u00042\n\u0010\u0016\u001a\u0006\u0012\u0002\b\u00030\u0017H\u0002\u00a2\u0006\u0002\u0010\u0018\u001a-\u0010\u0019\u001a\u00020\u0004\"\u0004\b\u0000\u0010\u0007*\b\u0012\u0004\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\u001a\u001a/\u0010\u001b\u001a\u00020\u001c\"\u0004\b\u0000\u0010\u0007*\n\u0012\u0006\b\u0001\u0012\u0002H\u00070\u00012\u0006\u0010\u0014\u001a\u00020\u00042\u0006\u0010\u0015\u001a\u00020\u0004H\u0002\u00a2\u0006\u0002\u0010\u001d\u00a8\u0006\u001e"}, d2={"arrayOfUninitializedElements", "", "E", "size", "", "(I)[Ljava/lang/Object;", "copyOfUninitializedElements", "T", "newSize", "([Ljava/lang/Object;I)[Ljava/lang/Object;", "resetAt", "", "index", "([Ljava/lang/Object;I)V", "resetRange", "fromIndex", "toIndex", "([Ljava/lang/Object;II)V", "subarrayContentEquals", "", "offset", "length", "other", "", "([Ljava/lang/Object;IILjava/util/List;)Z", "subarrayContentHashCode", "([Ljava/lang/Object;II)I", "subarrayContentToString", "", "([Ljava/lang/Object;II)Ljava/lang/String;", "kotlin-stdlib"})
public final class ListBuilderKt {
    @NotNull
    public static final <E> E[] arrayOfUninitializedElements(int size) {
        boolean bl;
        boolean bl2 = bl = size >= 0;
        if (!bl) {
            boolean bl3 = false;
            String string = "capacity must be non-negative.";
            throw new IllegalArgumentException(string.toString());
        }
        return new Object[size];
    }

    private static final <T> String subarrayContentToString(T[] $this$subarrayContentToString, int offset, int length) {
        StringBuilder sb = new StringBuilder(2 + length * 3);
        sb.append("[");
        int i = 0;
        while (i < length) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append($this$subarrayContentToString[offset + i]);
            int n = i;
            i = n + 1;
        }
        sb.append("]");
        String string = sb.toString();
        Intrinsics.checkNotNullExpressionValue(string, "sb.toString()");
        return string;
    }

    private static final <T> int subarrayContentHashCode(T[] $this$subarrayContentHashCode, int offset, int length) {
        int result = 1;
        int i = 0;
        while (i < length) {
            T nextElement = $this$subarrayContentHashCode[offset + i];
            T t = nextElement;
            result = result * 31 + (t == null ? 0 : t.hashCode());
            int n = i;
            i = n + 1;
        }
        return result;
    }

    private static final <T> boolean subarrayContentEquals(T[] $this$subarrayContentEquals, int offset, int length, List<?> other) {
        if (length != other.size()) {
            return false;
        }
        int i = 0;
        while (i < length) {
            if (!Intrinsics.areEqual($this$subarrayContentEquals[offset + i], other.get(i))) {
                return false;
            }
            int n = i;
            i = n + 1;
        }
        return true;
    }

    @NotNull
    public static final <T> T[] copyOfUninitializedElements(@NotNull T[] $this$copyOfUninitializedElements, int newSize) {
        Intrinsics.checkNotNullParameter($this$copyOfUninitializedElements, "<this>");
        T[] TArray = $this$copyOfUninitializedElements;
        T[] TArray2 = Arrays.copyOf(TArray, newSize);
        Intrinsics.checkNotNullExpressionValue(TArray2, "copyOf(this, newSize)");
        return TArray2;
    }

    public static final <E> void resetAt(@NotNull E[] $this$resetAt, int index) {
        Intrinsics.checkNotNullParameter($this$resetAt, "<this>");
        $this$resetAt[index] = null;
    }

    public static final <E> void resetRange(@NotNull E[] $this$resetRange, int fromIndex, int toIndex) {
        Intrinsics.checkNotNullParameter($this$resetRange, "<this>");
        int n = fromIndex;
        while (n < toIndex) {
            int index = n++;
            ListBuilderKt.resetAt($this$resetRange, index);
        }
    }

    public static final /* synthetic */ int access$subarrayContentHashCode(Object[] $receiver, int offset, int length) {
        return ListBuilderKt.subarrayContentHashCode($receiver, offset, length);
    }

    public static final /* synthetic */ String access$subarrayContentToString(Object[] $receiver, int offset, int length) {
        return ListBuilderKt.subarrayContentToString($receiver, offset, length);
    }

    public static final /* synthetic */ boolean access$subarrayContentEquals(Object[] $receiver, int offset, int length, List other) {
        return ListBuilderKt.subarrayContentEquals($receiver, offset, length, other);
    }
}

