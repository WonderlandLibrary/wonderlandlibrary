// 
// Decompiled by Procyon v0.5.36
// 

package kotlinx.coroutines.internal;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public final class ArrayQueue<T>
{
    private Object[] elements;
    private int head;
    private int tail;
    
    public final boolean isEmpty() {
        return this.head == this.tail;
    }
    
    public final void addLast(final T element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        this.elements[this.tail] = element;
        this.tail = (this.tail + 1 & this.elements.length - 1);
        if (this.tail == this.head) {
            final int length;
            final Object[] elements = new Object[(length = this.elements.length) << 1];
            ArraysKt___ArraysJvmKt.copyInto$default$2f2b04ee(this.elements, elements, 0, this.head, 0, 10);
            ArraysKt___ArraysJvmKt.copyInto$default$2f2b04ee(this.elements, elements, this.elements.length - this.head, 0, this.head, 4);
            this.elements = elements;
            this.head = 0;
            this.tail = length;
        }
    }
    
    public final T removeFirstOrNull() {
        if (this.head == this.tail) {
            return null;
        }
        final Object element = this.elements[this.head];
        this.elements[this.head] = null;
        this.head = (this.head + 1 & this.elements.length - 1);
        final Object o = element;
        if (o == null) {
            throw new TypeCastException("null cannot be cast to non-null type T");
        }
        return (T)o;
    }
    
    public ArrayQueue() {
        this.elements = new Object[16];
    }
}
