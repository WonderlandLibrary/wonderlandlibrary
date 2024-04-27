package net.ccbluex.liquidbounce.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableList;
import kotlin.jvm.internal.markers.KMutableListIterator;
import net.ccbluex.liquidbounce.api.util.WrappedMutableCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000@\n\n\b\n!\n\n\b\n\n\b\n\n\u0000\n\b\n\b\n\n\u0000\n\n\b\n+\n\b\b\u0000*\b\u0000*\b*\b*\bH02HHH02\bH0:\"B5888\u00000\b\t8\u000080\b¢\nJ0\f2\r028H¢J02\r02\f\b80HJ82\r0H¢J028H¢J028H¢J\b80HJ\b802\r0HJ82\r0H¢J82\r028H¢J\b802 02!0H¨#"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableList;", "O", "T", "C", "", "Lnet/ccbluex/liquidbounce/api/util/WrappedMutableCollection;", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "add", "", "index", "", "element", "(ILjava/lang/Object;)V", "addAll", "", "elements", "", "get", "(I)Ljava/lang/Object;", "indexOf", "(Ljava/lang/Object;)I", "lastIndexOf", "listIterator", "", "removeAt", "set", "(ILjava/lang/Object;)Ljava/lang/Object;", "subList", "fromIndex", "toIndex", "WrappedMutableCollectionIterator", "Pride"})
public final class WrappedMutableList<O, T, C extends List<O>>
extends WrappedMutableCollection<O, T, C>
implements List<T>,
KMutableList {
    @Override
    public T get(int index) {
        return this.getWrapper().invoke(((List)this.getWrapped()).get(index));
    }

    @Override
    public int indexOf(Object element) {
        return ((List)this.getWrapped()).indexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    public int lastIndexOf(Object element) {
        return ((List)this.getWrapped()).lastIndexOf(this.getUnwrapper().invoke(element));
    }

    @Override
    public void add(int index, T element) {
        ((List)this.getWrapped()).add(index, this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        int n = index;
        List list = (List)this.getWrapped();
        boolean $i$f$map = false;
        void var6_8 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list2 = (List)destination$iv$iv;
        return list.addAll(n, list2);
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator() {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(), this.getWrapper(), this.getUnwrapper());
    }

    @Override
    @NotNull
    public ListIterator<T> listIterator(int index) {
        return new WrappedMutableCollectionIterator(((List)this.getWrapped()).listIterator(index), this.getWrapper(), this.getUnwrapper());
    }

    public T removeAt(int index) {
        return this.getWrapper().invoke(((List)this.getWrapped()).remove(index));
    }

    @Override
    public T set(int index, T element) {
        return this.getWrapper().invoke(((List)this.getWrapped()).set(index, this.getUnwrapper().invoke(element)));
    }

    @Override
    @NotNull
    public List<T> subList(int fromIndex, int toIndex) {
        return new WrappedMutableList(((List)this.getWrapped()).subList(fromIndex, toIndex), this.getUnwrapper(), this.getWrapper());
    }

    public WrappedMutableList(@NotNull C wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        super((Collection)wrapped, unwrapper, wrapper);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\b\n+\n\b\n\n\b\b\n\n\b\n\n\b\n\b\n\b\u0000*\b*\b2\bH0B;\f\b80880880¢\bJ028H¢J\t0HJ\b0HJ8H¢J\b0HJ\r8H¢J\b0HJ\b0HJ028H¢R880¢\b\n\u0000\b\t\nR\b80¢\b\n\u0000\b\fR880¢\b\n\u0000\b\r\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableList$WrappedMutableCollectionIterator;", "O", "T", "", "wrapped", "wrapper", "Lkotlin/Function1;", "unwrapper", "(Ljava/util/ListIterator;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/ListIterator;", "getWrapper", "add", "", "element", "(Ljava/lang/Object;)V", "hasNext", "", "hasPrevious", "next", "()Ljava/lang/Object;", "nextIndex", "", "previous", "previousIndex", "remove", "set", "Pride"})
    public static final class WrappedMutableCollectionIterator<O, T>
    implements ListIterator<T>,
    KMutableListIterator {
        @NotNull
        private final ListIterator<O> wrapped;
        @NotNull
        private final Function1<O, T> wrapper;
        @NotNull
        private final Function1<T, O> unwrapper;

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
            return this.wrapper.invoke(this.wrapped.next());
        }

        @Override
        public int nextIndex() {
            return this.wrapped.nextIndex();
        }

        @Override
        public T previous() {
            return this.wrapper.invoke(this.wrapped.previous());
        }

        @Override
        public int previousIndex() {
            return this.wrapped.previousIndex();
        }

        @Override
        public void add(T element) {
            this.wrapped.add(this.unwrapper.invoke(element));
        }

        @Override
        public void remove() {
            this.wrapped.remove();
        }

        @Override
        public void set(T element) {
            this.wrapped.set(this.unwrapper.invoke(element));
        }

        @NotNull
        public final ListIterator<O> getWrapped() {
            return this.wrapped;
        }

        @NotNull
        public final Function1<O, T> getWrapper() {
            return this.wrapper;
        }

        @NotNull
        public final Function1<T, O> getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedMutableCollectionIterator(@NotNull ListIterator<O> wrapped, @NotNull Function1<? super O, ? extends T> wrapper, @NotNull Function1<? super T, ? extends O> unwrapper) {
            Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
            Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
            Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
            this.wrapped = wrapped;
            this.wrapper = wrapper;
            this.unwrapper = unwrapper;
        }
    }
}
