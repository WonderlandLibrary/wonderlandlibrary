/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.ExperimentalUnsignedTypes;
import kotlin.Metadata;
import kotlin.SinceKotlin;
import kotlin.UInt;
import kotlin.UnsignedKt;
import kotlin.collections.UIntIterator;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mv={1, 1, 15}, bv={1, 0, 3}, k=1, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B \u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\t\u0010\n\u001a\u00020\u000bH\u0096\u0002J\u0010\u0010\r\u001a\u00020\u0003H\u0016\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000eR\u0013\u0010\b\u001a\u00020\u0003X\u0082\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\n\u0002\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0013\u0010\f\u001a\u00020\u0003X\u0082\u000e\u00f8\u0001\u0000\u00a2\u0006\u0004\n\u0002\u0010\tR\u0013\u0010\u0005\u001a\u00020\u0003X\u0082\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\n\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000f"}, d2={"Lkotlin/ranges/UIntProgressionIterator;", "Lkotlin/collections/UIntIterator;", "first", "Lkotlin/UInt;", "last", "step", "", "(IIILkotlin/jvm/internal/DefaultConstructorMarker;)V", "finalElement", "I", "hasNext", "", "next", "nextUInt", "()I", "kotlin-stdlib"})
@SinceKotlin(version="1.3")
@ExperimentalUnsignedTypes
final class UIntProgressionIterator
extends UIntIterator {
    private final int finalElement;
    private boolean hasNext;
    private final int step;
    private int next;

    @Override
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override
    public int nextUInt() {
        int value = this.next;
        if (value == this.finalElement) {
            if (!this.hasNext) {
                throw (Throwable)new NoSuchElementException();
            }
            this.hasNext = false;
        } else {
            int n;
            UIntProgressionIterator uIntProgressionIterator = this;
            int n2 = uIntProgressionIterator.next;
            int n3 = this.step;
            UIntProgressionIterator uIntProgressionIterator2 = uIntProgressionIterator;
            boolean bl = false;
            uIntProgressionIterator2.next = n = UInt.constructor-impl(n2 + n3);
        }
        return value;
    }

    private UIntProgressionIterator(int first, int last, int step) {
        boolean bl;
        UIntProgressionIterator uIntProgressionIterator;
        int n;
        boolean bl2;
        UIntProgressionIterator uIntProgressionIterator2;
        int n2;
        this.finalElement = last;
        UIntProgressionIterator uIntProgressionIterator3 = this;
        if (step > 0) {
            n2 = first;
            uIntProgressionIterator2 = uIntProgressionIterator3;
            bl2 = false;
            n = UnsignedKt.uintCompare(n2, last);
            uIntProgressionIterator = uIntProgressionIterator2;
            bl = n <= 0;
        } else {
            n2 = first;
            uIntProgressionIterator2 = uIntProgressionIterator3;
            bl2 = false;
            n = UnsignedKt.uintCompare(n2, last);
            uIntProgressionIterator = uIntProgressionIterator2;
            bl = n >= 0;
        }
        uIntProgressionIterator.hasNext = bl;
        n2 = step;
        uIntProgressionIterator2 = this;
        bl2 = false;
        uIntProgressionIterator2.step = n = UInt.constructor-impl(n2);
        this.next = this.hasNext ? first : this.finalElement;
    }

    public /* synthetic */ UIntProgressionIterator(int first, int last, int step, DefaultConstructorMarker $constructor_marker) {
        this(first, last, step);
    }
}

