/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.functions.Function1
 *  kotlin.jvm.internal.Intrinsics
 *  kotlin.jvm.internal.markers.KMappedMarker
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.api.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.UnaryOperator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010*\n\u0002\b\u0005\b\u0016\u0018\u0000*\u0004\b\u0000\u0010\u0001*\u0004\b\u0001\u0010\u0002*\u000e\b\u0002\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00010\u00042\u0014\u0012\u0004\u0012\u0002H\u0001\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00030\u00052\b\u0012\u0004\u0012\u0002H\u00020\u0004:\u0001\u0018B5\u0012\u0006\u0010\u0006\u001a\u00028\u0002\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00028\u0001\u0012\u0004\u0012\u00028\u00000\b\u0012\u0012\u0010\t\u001a\u000e\u0012\u0004\u0012\u00028\u0000\u0012\u0004\u0012\u00028\u00010\b\u00a2\u0006\u0002\u0010\nJ\u0016\u0010\u000b\u001a\u00028\u00012\u0006\u0010\f\u001a\u00020\rH\u0096\u0002\u00a2\u0006\u0002\u0010\u000eJ\u0015\u0010\u000f\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\u0011J\u0015\u0010\u0012\u001a\u00020\r2\u0006\u0010\u0010\u001a\u00028\u0001H\u0016\u00a2\u0006\u0002\u0010\u0011J\u000e\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00010\u0014H\u0016J\u0016\u0010\u0013\u001a\b\u0012\u0004\u0012\u00028\u00010\u00142\u0006\u0010\f\u001a\u00020\rH\u0016J\u001e\u0010\u0015\u001a\b\u0012\u0004\u0012\u00028\u00010\u00042\u0006\u0010\u0016\u001a\u00020\r2\u0006\u0010\u0017\u001a\u00020\rH\u0016\u00a8\u0006\u0019"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedList;", "O", "T", "C", "", "Lnet/ccbluex/liquidbounce/api/util/WrappedCollection;", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "get", "index", "", "(I)Ljava/lang/Object;", "indexOf", "element", "(Ljava/lang/Object;)I", "lastIndexOf", "listIterator", "", "subList", "fromIndex", "toIndex", "WrappedCollectionIterator", "LiKingSense"})
public class WrappedList<O, T, C extends List<? extends O>>
extends WrappedCollection<O, T, C>
implements List<T>,
KMappedMarker {
    @Override
    public T get(int index) {
        return (T)this.getWrapper().invoke(((List)this.getWrapped()).get(index));
    }

    @Override
    public int indexOf(Object element) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    public int lastIndexOf(Object element) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator() {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper());
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator(int index) {
        return new WrappedCollectionIterator(((List)this.getWrapped()).listIterator(index), this.getWrapper());
    }

    @Override
    @NotNull
    public List<T> subList(int fromIndex, int toIndex) {
        return new WrappedList(((List)this.getWrapped()).subList(fromIndex, toIndex), this.getUnwrapper(), this.getWrapper());
    }

    public WrappedList(@NotNull C wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, (String)"unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, (String)"wrapper");
        super((Collection)wrapped, unwrapper, wrapper);
    }

    @Override
    public void add(int n, T t2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public boolean addAll(int n, Collection<? extends T> collection) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public T remove(int n) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void replaceAll(UnaryOperator<T> unaryOperator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public T set(int n, T t2) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Override
    public void sort(Comparator<? super T> comparator) {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010*\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0003\u0010\u0001*\u0004\b\u0004\u0010\u00022\b\u0012\u0004\u0012\u0002H\u00020\u0003B'\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00030\u0003\u0012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0003\u0012\u0004\u0012\u00028\u00040\u0006\u00a2\u0006\u0002\u0010\u0007J\t\u0010\f\u001a\u00020\rH\u0096\u0002J\b\u0010\u000e\u001a\u00020\rH\u0016J\u000e\u0010\u000f\u001a\u00028\u0004H\u0096\u0002\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0011\u001a\u00020\u0012H\u0016J\r\u0010\u0013\u001a\u00028\u0004H\u0016\u00a2\u0006\u0002\u0010\u0010J\b\u0010\u0014\u001a\u00020\u0012H\u0016R\u0017\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00030\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00028\u0003\u0012\u0004\u0012\u00028\u00040\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedList$WrappedCollectionIterator;", "O", "T", "", "wrapped", "wrapper", "Lkotlin/Function1;", "(Ljava/util/ListIterator;Lkotlin/jvm/functions/Function1;)V", "getWrapped", "()Ljava/util/ListIterator;", "getWrapper", "()Lkotlin/jvm/functions/Function1;", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "LiKingSense"})
    public static final class WrappedCollectionIterator<O, T>
    implements ListIterator<T>,
    KMappedMarker {
        @NotNull
        private final ListIterator<O> wrapped;
        @NotNull
        private final Function1<O, T> wrapper;

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.wrapped.hasPrevious();
        }

        @Override
        public T next() {
            return (T)this.wrapper.invoke(this.wrapped.next());
        }

        @Override
        public int nextIndex() {
            return this.wrapped.nextIndex();
        }

        @Override
        public T previous() {
            return (T)this.wrapper.invoke(this.wrapped.previous());
        }

        @Override
        public int previousIndex() {
            return this.wrapped.previousIndex();
        }

        @NotNull
        public final ListIterator<O> getWrapped() {
            return this.wrapped;
        }

        @NotNull
        public final Function1<O, T> getWrapper() {
            return this.wrapper;
        }

        public WrappedCollectionIterator(@NotNull ListIterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> wrapper) {
            Intrinsics.checkParameterIsNotNull(wrapped, (String)"wrapped");
            Intrinsics.checkParameterIsNotNull(wrapper, (String)"wrapper");
            this.wrapped = wrapped;
            this.wrapper = wrapper;
        }

        @Override
        public void add(T t2) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }

        @Override
        public void set(T t2) {
            throw new UnsupportedOperationException("Operation is not supported for read-only collection");
        }
    }
}

