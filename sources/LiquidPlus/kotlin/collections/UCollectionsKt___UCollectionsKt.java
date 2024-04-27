/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.collections;

import java.util.Collection;
import java.util.Iterator;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2={"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, xs="kotlin/collections/UCollectionsKt")
class UCollectionsKt___UCollectionsKt {
    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final byte[] toUByteArray(@NotNull Collection<UByte> $this$toUByteArray) {
        Intrinsics.checkNotNullParameter($this$toUByteArray, "<this>");
        byte[] result = UByteArray.constructor-impl($this$toUByteArray.size());
        int index = 0;
        Iterator<UByte> iterator2 = $this$toUByteArray.iterator();
        while (iterator2.hasNext()) {
            byte element = iterator2.next().unbox-impl();
            int n = index;
            index = n + 1;
            UByteArray.set-VurrAj0(result, n, element);
        }
        return result;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final int[] toUIntArray(@NotNull Collection<UInt> $this$toUIntArray) {
        Intrinsics.checkNotNullParameter($this$toUIntArray, "<this>");
        int[] result = UIntArray.constructor-impl($this$toUIntArray.size());
        int index = 0;
        Iterator<UInt> iterator2 = $this$toUIntArray.iterator();
        while (iterator2.hasNext()) {
            int element = iterator2.next().unbox-impl();
            int n = index;
            index = n + 1;
            UIntArray.set-VXSXFK8(result, n, element);
        }
        return result;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final long[] toULongArray(@NotNull Collection<ULong> $this$toULongArray) {
        Intrinsics.checkNotNullParameter($this$toULongArray, "<this>");
        long[] result = ULongArray.constructor-impl($this$toULongArray.size());
        int index = 0;
        Iterator<ULong> iterator2 = $this$toULongArray.iterator();
        while (iterator2.hasNext()) {
            long element = iterator2.next().unbox-impl();
            int n = index;
            index = n + 1;
            ULongArray.set-k8EXiF4(result, n, element);
        }
        return result;
    }

    @SinceKotlin(version="1.3")
    @ExperimentalUnsignedTypes
    @NotNull
    public static final short[] toUShortArray(@NotNull Collection<UShort> $this$toUShortArray) {
        Intrinsics.checkNotNullParameter($this$toUShortArray, "<this>");
        short[] result = UShortArray.constructor-impl($this$toUShortArray.size());
        int index = 0;
        Iterator<UShort> iterator2 = $this$toUShortArray.iterator();
        while (iterator2.hasNext()) {
            short element = iterator2.next().unbox-impl();
            int n = index;
            index = n + 1;
            UShortArray.set-01HTLdE(result, n, element);
        }
        return result;
    }

    @JvmName(name="sumOfUInt")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUInt(@NotNull Iterable<UInt> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int sum = 0;
        Iterator<UInt> iterator2 = $this$sum.iterator();
        while (iterator2.hasNext()) {
            int element = iterator2.next().unbox-impl();
            sum = UInt.constructor-impl(sum + element);
        }
        return sum;
    }

    @JvmName(name="sumOfULong")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final long sumOfULong(@NotNull Iterable<ULong> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        long sum = 0L;
        Iterator<ULong> iterator2 = $this$sum.iterator();
        while (iterator2.hasNext()) {
            long element = iterator2.next().unbox-impl();
            sum = ULong.constructor-impl(sum + element);
        }
        return sum;
    }

    @JvmName(name="sumOfUByte")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUByte(@NotNull Iterable<UByte> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int sum = 0;
        Iterator<UByte> iterator2 = $this$sum.iterator();
        while (iterator2.hasNext()) {
            byte element = iterator2.next().unbox-impl();
            int n = UInt.constructor-impl(element & 0xFF);
            sum = UInt.constructor-impl(sum + n);
        }
        return sum;
    }

    @JvmName(name="sumOfUShort")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUShort(@NotNull Iterable<UShort> $this$sum) {
        Intrinsics.checkNotNullParameter($this$sum, "<this>");
        int sum = 0;
        Iterator<UShort> iterator2 = $this$sum.iterator();
        while (iterator2.hasNext()) {
            short element = iterator2.next().unbox-impl();
            int n = UInt.constructor-impl(element & 0xFFFF);
            sum = UInt.constructor-impl(sum + n);
        }
        return sum;
    }
}

