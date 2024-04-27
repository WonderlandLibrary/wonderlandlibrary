/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package kotlin.collections.builders;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.collections.AbstractList;
import kotlin.collections.AbstractMutableList;
import kotlin.collections.ArrayDeque;
import kotlin.collections.ArraysKt;
import kotlin.collections.builders.ListBuilderKt;
import kotlin.collections.builders.SerializedCollection;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000r\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\f\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u001e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\b\n\u0002\u0010\u0000\n\u0002\b\b\n\u0002\u0010)\n\u0002\b\u0002\n\u0002\u0010+\n\u0002\b\u0015\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u00042\b\u0012\u0004\u0012\u0002H\u00010\u00052\u00060\u0006j\u0002`\u0007:\u0001VB\u0007\b\u0016\u00a2\u0006\u0002\u0010\bB\u000f\b\u0016\u0012\u0006\u0010\t\u001a\u00020\n\u00a2\u0006\u0002\u0010\u000bBM\b\u0002\u0012\f\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\r\u0012\u0006\u0010\u000e\u001a\u00020\n\u0012\u0006\u0010\u000f\u001a\u00020\n\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u000e\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u0012\u000e\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000\u00a2\u0006\u0002\u0010\u0014J\u0015\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001dJ\u001d\u0010\u001b\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010 J\u001e\u0010!\u001a\u00020\u00112\u0006\u0010\u001f\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0016\u0010!\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J&\u0010$\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010&\u001a\u00020\nH\u0002J\u001d\u0010'\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0002\u00a2\u0006\u0002\u0010 J\f\u0010(\u001a\b\u0012\u0004\u0012\u00028\u00000)J\b\u0010*\u001a\u00020\u001eH\u0002J\b\u0010+\u001a\u00020\u001eH\u0016J\u0014\u0010,\u001a\u00020\u00112\n\u0010-\u001a\u0006\u0012\u0002\b\u00030)H\u0002J\u0010\u0010.\u001a\u00020\u001e2\u0006\u0010/\u001a\u00020\nH\u0002J\u0010\u00100\u001a\u00020\u001e2\u0006\u0010&\u001a\u00020\nH\u0002J\u0013\u00101\u001a\u00020\u00112\b\u0010-\u001a\u0004\u0018\u000102H\u0096\u0002J\u0016\u00103\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0096\u0002\u00a2\u0006\u0002\u00104J\b\u00105\u001a\u00020\nH\u0016J\u0015\u00106\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00107J\u0018\u00108\u001a\u00020\u001e2\u0006\u0010%\u001a\u00020\n2\u0006\u0010&\u001a\u00020\nH\u0002J\b\u00109\u001a\u00020\u0011H\u0016J\u000f\u0010:\u001a\b\u0012\u0004\u0012\u00028\u00000;H\u0096\u0002J\u0015\u0010<\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u00107J\u000e\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000>H\u0016J\u0016\u0010=\u001a\b\u0012\u0004\u0012\u00028\u00000>2\u0006\u0010\u001f\u001a\u00020\nH\u0016J\u0015\u0010?\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00028\u0000H\u0016\u00a2\u0006\u0002\u0010\u001dJ\u0016\u0010@\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J\u0015\u0010A\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\nH\u0016\u00a2\u0006\u0002\u00104J\u0015\u0010B\u001a\u00028\u00002\u0006\u0010%\u001a\u00020\nH\u0002\u00a2\u0006\u0002\u00104J\u0018\u0010C\u001a\u00020\u001e2\u0006\u0010D\u001a\u00020\n2\u0006\u0010E\u001a\u00020\nH\u0002J\u0016\u0010F\u001a\u00020\u00112\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#H\u0016J.\u0010G\u001a\u00020\n2\u0006\u0010D\u001a\u00020\n2\u0006\u0010E\u001a\u00020\n2\f\u0010\"\u001a\b\u0012\u0004\u0012\u00028\u00000#2\u0006\u0010H\u001a\u00020\u0011H\u0002J\u001e\u0010I\u001a\u00028\u00002\u0006\u0010\u001f\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00028\u0000H\u0096\u0002\u00a2\u0006\u0002\u0010JJ\u001e\u0010K\u001a\b\u0012\u0004\u0012\u00028\u00000\u00022\u0006\u0010L\u001a\u00020\n2\u0006\u0010M\u001a\u00020\nH\u0016J\u0015\u0010N\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001020\rH\u0016\u00a2\u0006\u0002\u0010OJ'\u0010N\u001a\b\u0012\u0004\u0012\u0002HP0\r\"\u0004\b\u0001\u0010P2\f\u0010Q\u001a\b\u0012\u0004\u0012\u0002HP0\rH\u0016\u00a2\u0006\u0002\u0010RJ\b\u0010S\u001a\u00020TH\u0016J\b\u0010U\u001a\u000202H\u0002R\u0016\u0010\f\u001a\b\u0012\u0004\u0012\u00028\u00000\rX\u0082\u000e\u00a2\u0006\u0004\n\u0002\u0010\u0015R\u0016\u0010\u0012\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\u00020\u00118BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0013\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0000X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\u00020\n8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u001a\u00a8\u0006W"}, d2={"Lkotlin/collections/builders/ListBuilder;", "E", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "Lkotlin/collections/AbstractMutableList;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "()V", "initialCapacity", "", "(I)V", "array", "", "offset", "length", "isReadOnly", "", "backing", "root", "([Ljava/lang/Object;IIZLkotlin/collections/builders/ListBuilder;Lkotlin/collections/builders/ListBuilder;)V", "[Ljava/lang/Object;", "isEffectivelyReadOnly", "()Z", "size", "getSize", "()I", "add", "element", "(Ljava/lang/Object;)Z", "", "index", "(ILjava/lang/Object;)V", "addAll", "elements", "", "addAllInternal", "i", "n", "addAtInternal", "build", "", "checkIsMutable", "clear", "contentEquals", "other", "ensureCapacity", "minCapacity", "ensureExtraCapacity", "equals", "", "get", "(I)Ljava/lang/Object;", "hashCode", "indexOf", "(Ljava/lang/Object;)I", "insertAtInternal", "isEmpty", "iterator", "", "lastIndexOf", "listIterator", "", "remove", "removeAll", "removeAt", "removeAtInternal", "removeRangeInternal", "rangeOffset", "rangeLength", "retainAll", "retainOrRemoveAllInternal", "retain", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "toArray", "()[Ljava/lang/Object;", "T", "destination", "([Ljava/lang/Object;)[Ljava/lang/Object;", "toString", "", "writeReplace", "Itr", "kotlin-stdlib"})
public final class ListBuilder<E>
extends AbstractMutableList<E>
implements List<E>,
RandomAccess,
Serializable,
KMutableList {
    @NotNull
    private E[] array;
    private int offset;
    private int length;
    private boolean isReadOnly;
    @Nullable
    private final ListBuilder<E> backing;
    @Nullable
    private final ListBuilder<E> root;

    private ListBuilder(E[] array, int offset, int length, boolean isReadOnly, ListBuilder<E> backing, ListBuilder<E> root) {
        this.array = array;
        this.offset = offset;
        this.length = length;
        this.isReadOnly = isReadOnly;
        this.backing = backing;
        this.root = root;
    }

    public ListBuilder() {
        this(10);
    }

    public ListBuilder(int initialCapacity) {
        this(ListBuilderKt.arrayOfUninitializedElements(initialCapacity), 0, 0, false, null, null);
    }

    @NotNull
    public final List<E> build() {
        if (this.backing != null) {
            throw new IllegalStateException();
        }
        this.checkIsMutable();
        this.isReadOnly = true;
        return this;
    }

    private final Object writeReplace() {
        if (!this.isEffectivelyReadOnly()) {
            throw new NotSerializableException("The list cannot be serialized while it is being built.");
        }
        return new SerializedCollection(this, 0);
    }

    @Override
    public int getSize() {
        return this.length;
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public E get(int index) {
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        return this.array[this.offset + index];
    }

    @Override
    public E set(int index, E element) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        E old = this.array[this.offset + index];
        this.array[this.offset + index] = element;
        return old;
    }

    @Override
    public int indexOf(Object element) {
        int i = 0;
        while (i < this.length) {
            if (Intrinsics.areEqual(this.array[this.offset + i], element)) {
                return i;
            }
            int n = i;
            i = n + 1;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object element) {
        int i = this.length - 1;
        while (i >= 0) {
            if (Intrinsics.areEqual(this.array[this.offset + i], element)) {
                return i;
            }
            int n = i;
            i = n + -1;
        }
        return -1;
    }

    @Override
    @NotNull
    public Iterator<E> iterator() {
        return new Itr(this, 0);
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator() {
        return new Itr(this, 0);
    }

    @Override
    @NotNull
    public ListIterator<E> listIterator(int index) {
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        return new Itr(this, index);
    }

    @Override
    public boolean add(E element) {
        this.checkIsMutable();
        this.addAtInternal(this.offset + this.length, element);
        return true;
    }

    @Override
    public void add(int index, E element) {
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        this.addAtInternal(this.offset + index, element);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        this.checkIsMutable();
        int n = elements.size();
        this.addAllInternal(this.offset + this.length, elements, n);
        return n > 0;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends E> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        this.checkIsMutable();
        AbstractList.Companion.checkPositionIndex$kotlin_stdlib(index, this.length);
        int n = elements.size();
        this.addAllInternal(this.offset + index, elements, n);
        return n > 0;
    }

    @Override
    public void clear() {
        this.checkIsMutable();
        this.removeRangeInternal(this.offset, this.length);
    }

    @Override
    public E removeAt(int index) {
        this.checkIsMutable();
        AbstractList.Companion.checkElementIndex$kotlin_stdlib(index, this.length);
        return this.removeAtInternal(this.offset + index);
    }

    @Override
    public boolean remove(Object element) {
        this.checkIsMutable();
        int i = this.indexOf(element);
        if (i >= 0) {
            this.remove(i);
        }
        return i >= 0;
    }

    @Override
    public boolean removeAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        this.checkIsMutable();
        return this.retainOrRemoveAllInternal(this.offset, this.length, elements, false) > 0;
    }

    @Override
    public boolean retainAll(@NotNull Collection<? extends Object> elements) {
        Intrinsics.checkNotNullParameter(elements, "elements");
        this.checkIsMutable();
        return this.retainOrRemoveAllInternal(this.offset, this.length, elements, true) > 0;
    }

    @Override
    @NotNull
    public List<E> subList(int fromIndex, int toIndex) {
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(fromIndex, toIndex, this.length);
        ListBuilder<E> listBuilder = this.root;
        return new ListBuilder<E>(this.array, this.offset + fromIndex, toIndex - fromIndex, this.isReadOnly, this, listBuilder == null ? this : listBuilder);
    }

    @Override
    @NotNull
    public <T> T[] toArray(@NotNull T[] destination) {
        Intrinsics.checkNotNullParameter(destination, "destination");
        if (destination.length < this.length) {
            T[] TArray = Arrays.copyOfRange(this.array, this.offset, this.offset + this.length, destination.getClass());
            Intrinsics.checkNotNullExpressionValue(TArray, "copyOfRange(array, offse\u2026h, destination.javaClass)");
            return TArray;
        }
        ArraysKt.copyInto(this.array, destination, 0, this.offset, this.offset + this.length);
        if (destination.length > this.length) {
            destination[this.length] = null;
        }
        return destination;
    }

    @Override
    @NotNull
    public Object[] toArray() {
        E[] EArray = this.array;
        int n = this.offset;
        int n2 = this.offset + this.length;
        return ArraysKt.copyOfRange(EArray, n, n2);
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return other == this || other instanceof List && this.contentEquals((List)other);
    }

    @Override
    public int hashCode() {
        return ListBuilderKt.access$subarrayContentHashCode(this.array, this.offset, this.length);
    }

    @Override
    @NotNull
    public String toString() {
        return ListBuilderKt.access$subarrayContentToString(this.array, this.offset, this.length);
    }

    private final void ensureCapacity(int minCapacity) {
        if (this.backing != null) {
            throw new IllegalStateException();
        }
        if (minCapacity < 0) {
            throw new OutOfMemoryError();
        }
        if (minCapacity > this.array.length) {
            int newSize = ArrayDeque.Companion.newCapacity$kotlin_stdlib(this.array.length, minCapacity);
            this.array = ListBuilderKt.copyOfUninitializedElements(this.array, newSize);
        }
    }

    private final void checkIsMutable() {
        if (this.isEffectivelyReadOnly()) {
            throw new UnsupportedOperationException();
        }
    }

    private final boolean isEffectivelyReadOnly() {
        return this.isReadOnly || this.root != null && this.root.isReadOnly;
    }

    private final void ensureExtraCapacity(int n) {
        this.ensureCapacity(this.length + n);
    }

    private final boolean contentEquals(List<?> other) {
        return ListBuilderKt.access$subarrayContentEquals(this.array, this.offset, this.length, other);
    }

    private final void insertAtInternal(int i, int n) {
        this.ensureExtraCapacity(n);
        Object object = this.array;
        E[] EArray = this.array;
        int n2 = this.offset + this.length;
        int n3 = i + n;
        ArraysKt.copyInto(object, EArray, n3, i, n2);
        object = this;
        object.length += n;
    }

    private final void addAtInternal(int i, E element) {
        if (this.backing != null) {
            super.addAtInternal(i, element);
            this.array = this.backing.array;
            ListBuilder listBuilder = this;
            int n = listBuilder.length;
            listBuilder.length = n + 1;
        } else {
            this.insertAtInternal(i, 1);
            this.array[i] = element;
        }
    }

    private final void addAllInternal(int i, Collection<? extends E> elements, int n) {
        if (this.backing != null) {
            super.addAllInternal(i, elements, n);
            this.array = this.backing.array;
            ListBuilder listBuilder = this;
            listBuilder.length += n;
        } else {
            this.insertAtInternal(i, n);
            int j = 0;
            Iterator<E> it = elements.iterator();
            while (j < n) {
                this.array[i + j] = it.next();
                int n2 = j;
                j = n2 + 1;
            }
        }
    }

    private final E removeAtInternal(int i) {
        if (this.backing != null) {
            E old = super.removeAtInternal(i);
            ListBuilder listBuilder = this;
            int n = listBuilder.length;
            listBuilder.length = n + -1;
            return old;
        }
        E old = this.array[i];
        Object object = this.array;
        E[] EArray = this.array;
        int n = i + 1;
        int n2 = this.offset + this.length;
        ArraysKt.copyInto(object, EArray, i, n, n2);
        ListBuilderKt.resetAt(this.array, this.offset + this.length - 1);
        object = this;
        int n3 = object.length;
        object.length = n3 + -1;
        return old;
    }

    private final void removeRangeInternal(int rangeOffset, int rangeLength) {
        Object object;
        if (this.backing != null) {
            super.removeRangeInternal(rangeOffset, rangeLength);
        } else {
            object = this.array;
            E[] EArray = this.array;
            int n = rangeOffset + rangeLength;
            int n2 = this.length;
            ArraysKt.copyInto(object, EArray, rangeOffset, n, n2);
            ListBuilderKt.resetRange(this.array, this.length - rangeLength, this.length);
        }
        object = this;
        object.length -= rangeLength;
    }

    private final int retainOrRemoveAllInternal(int rangeOffset, int rangeLength, Collection<? extends E> elements, boolean retain) {
        if (this.backing != null) {
            int removed = super.retainOrRemoveAllInternal(rangeOffset, rangeLength, elements, retain);
            ListBuilder listBuilder = this;
            listBuilder.length -= removed;
            return removed;
        }
        int i = 0;
        int j = 0;
        while (i < rangeLength) {
            int n;
            if (elements.contains(this.array[rangeOffset + i]) == retain) {
                n = j;
                j = n + 1;
                int n2 = rangeOffset + n;
                n = i;
                i = n + 1;
                this.array[n2] = this.array[rangeOffset + n];
                continue;
            }
            n = i;
            i = n + 1;
        }
        int removed = rangeLength - j;
        Object object = this.array;
        E[] EArray = this.array;
        int n = rangeOffset + rangeLength;
        int n3 = this.length;
        int n4 = rangeOffset + j;
        ArraysKt.copyInto(object, EArray, n4, n, n3);
        ListBuilderKt.resetRange(this.array, this.length - removed, this.length);
        object = this;
        object.length -= removed;
        return removed;
    }

    @Metadata(mv={1, 6, 0}, k=1, xi=48, d1={"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010+\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\t\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001d\b\u0016\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0015\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fJ\t\u0010\r\u001a\u00020\u000eH\u0096\u0002J\b\u0010\u000f\u001a\u00020\u000eH\u0016J\u000e\u0010\u0010\u001a\u00028\u0001H\u0096\u0002\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0012\u001a\u00020\u0006H\u0016J\r\u0010\u0013\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\u0011J\b\u0010\u0014\u001a\u00020\u0006H\u0016J\b\u0010\u0015\u001a\u00020\nH\u0016J\u0015\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\fR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00010\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2={"Lkotlin/collections/builders/ListBuilder$Itr;", "E", "", "list", "Lkotlin/collections/builders/ListBuilder;", "index", "", "(Lkotlin/collections/builders/ListBuilder;I)V", "lastIndex", "add", "", "element", "(Ljava/lang/Object;)V", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "previous", "previousIndex", "remove", "set", "kotlin-stdlib"})
    private static final class Itr<E>
    implements ListIterator<E>,
    KMutableListIterator {
        @NotNull
        private final ListBuilder<E> list;
        private int index;
        private int lastIndex;

        public Itr(@NotNull ListBuilder<E> list, int index) {
            Intrinsics.checkNotNullParameter(list, "list");
            this.list = list;
            this.index = index;
            this.lastIndex = -1;
        }

        @Override
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < ((ListBuilder)this.list).length;
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public E previous() {
            if (this.index <= 0) {
                throw new NoSuchElementException();
            }
            Itr itr = this;
            itr.index += -1;
            this.lastIndex = itr.index;
            return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
        }

        @Override
        public E next() {
            if (this.index >= ((ListBuilder)this.list).length) {
                throw new NoSuchElementException();
            }
            Itr itr = this;
            int n = itr.index;
            itr.index = n + 1;
            this.lastIndex = n;
            return (E)((ListBuilder)this.list).array[((ListBuilder)this.list).offset + this.lastIndex];
        }

        @Override
        public void set(E element) {
            boolean bl;
            boolean bl2 = bl = this.lastIndex != -1;
            if (!bl) {
                boolean bl3 = false;
                String string = "Call next() or previous() before replacing element from the iterator.";
                throw new IllegalStateException(string.toString());
            }
            this.list.set(this.lastIndex, element);
        }

        @Override
        public void add(E element) {
            Itr itr = this;
            int n = itr.index;
            itr.index = n + 1;
            this.list.add(n, element);
            this.lastIndex = -1;
        }

        @Override
        public void remove() {
            boolean bl;
            boolean bl2 = bl = this.lastIndex != -1;
            if (!bl) {
                boolean bl3 = false;
                String string = "Call next() or previous() before removing element from the iterator.";
                throw new IllegalStateException(string.toString());
            }
            this.list.remove(this.lastIndex);
            this.index = this.lastIndex;
            this.lastIndex = -1;
        }
    }
}

