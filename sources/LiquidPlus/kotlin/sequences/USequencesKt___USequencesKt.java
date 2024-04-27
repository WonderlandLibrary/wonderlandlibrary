/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package kotlin.sequences;

import java.util.Iterator;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.WasExperimental;
import kotlin.jvm.JvmName;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 6, 0}, k=5, xi=49, d1={"\u0000\"\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2={"sum", "Lkotlin/UInt;", "Lkotlin/sequences/Sequence;", "Lkotlin/UByte;", "sumOfUByte", "(Lkotlin/sequences/Sequence;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Lkotlin/sequences/Sequence;)J", "Lkotlin/UShort;", "sumOfUShort", "kotlin-stdlib"}, xs="kotlin/sequences/USequencesKt")
class USequencesKt___USequencesKt {
    @JvmName(name="sumOfUInt")
    @SinceKotlin(version="1.5")
    @WasExperimental(markerClass={ExperimentalUnsignedTypes.class})
    public static final int sumOfUInt(@NotNull Sequence<UInt> $this$sum) {
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
    public static final long sumOfULong(@NotNull Sequence<ULong> $this$sum) {
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
    public static final int sumOfUByte(@NotNull Sequence<UByte> $this$sum) {
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
    public static final int sumOfUShort(@NotNull Sequence<UShort> $this$sum) {
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

