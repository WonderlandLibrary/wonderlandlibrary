package net.ccbluex.liquidbounce.api.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMutableCollection;
import kotlin.jvm.internal.markers.KMutableIterator;
import net.ccbluex.liquidbounce.api.util.WrappedCollection;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\b\n\n\n\b\n\n\b\n\n\b\n\n\u0000\n\n\u0000\n)\n\b\b\u0000*\b\u0000*\b*\b*\bH02HHH02\bH0:B5888\u00000\b\t8\u000080\b¢\nJ0\f2\r8H¢J0\f2\f\b80HJ\b0HJ\b80HJ0\f2\r8H¢J0\f2\f\b80HJ0\f2\f\b80H¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableCollection;", "O", "T", "C", "", "Lnet/ccbluex/liquidbounce/api/util/WrappedCollection;", "wrapped", "unwrapper", "Lkotlin/Function1;", "wrapper", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "add", "", "element", "(Ljava/lang/Object;)Z", "addAll", "elements", "", "clear", "", "iterator", "", "remove", "removeAll", "retainAll", "WrappedCollectionIterator", "Pride"})
public class WrappedMutableCollection<O, T, C extends Collection<O>>
extends WrappedCollection<O, T, C>
implements Collection<T>,
KMutableCollection {
    @Override
    public boolean add(T element) {
        return this.getWrapped().add(this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean addAll(@NotNull Collection<? extends T> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    @Override
    public void clear() {
        this.getWrapped().clear();
    }

    @Override
    @NotNull
    public Iterator<T> iterator() {
        return new WrappedCollectionIterator(this.getWrapped().iterator(), this.getWrapper());
    }

    @Override
    public boolean remove(Object element) {
        return this.getWrapped().remove(this.getUnwrapper().invoke(element));
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean removeAll(@NotNull Collection<? extends Object> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public boolean retainAll(@NotNull Collection<? extends Object> elements) {
        void $this$mapTo$iv$iv;
        void $this$map$iv;
        Intrinsics.checkParameterIsNotNull(elements, "elements");
        Iterable iterable = elements;
        Function1 function1 = this.getUnwrapper();
        Object c = this.getWrapped();
        boolean $i$f$map = false;
        void var5_6 = $this$map$iv;
        Collection destination$iv$iv = new ArrayList(CollectionsKt.collectionSizeOrDefault($this$map$iv, 10));
        boolean $i$f$mapTo = false;
        for (Object item$iv$iv : $this$mapTo$iv$iv) {
            void transform$iv;
            destination$iv$iv.add(transform$iv.invoke(item$iv$iv));
        }
        List list = (List)destination$iv$iv;
        return c.addAll(list);
    }

    public WrappedMutableCollection(@NotNull C wrapped, @NotNull Function1<? super T, ? extends O> unwrapper, @NotNull Function1<? super O, ? extends T> wrapper) {
        Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
        Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
        Intrinsics.checkParameterIsNotNull(wrapper, "wrapper");
        super(wrapped, unwrapper, wrapper);
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000&\n\n\b\n)\n\b\n\n\b\n\n\b\n\n\u0000\u0000*\b*\b2\bH0B'\f\b80880¢J\t\f0\rHJ8H¢J\b0HR880¢\b\n\u0000\b\b\tR\b80¢\b\n\u0000\b\n¨"}, d2={"Lnet/ccbluex/liquidbounce/api/util/WrappedMutableCollection$WrappedCollectionIterator;", "O", "T", "", "wrapped", "unwrapper", "Lkotlin/Function1;", "(Ljava/util/Iterator;Lkotlin/jvm/functions/Function1;)V", "getUnwrapper", "()Lkotlin/jvm/functions/Function1;", "getWrapped", "()Ljava/util/Iterator;", "hasNext", "", "next", "()Ljava/lang/Object;", "remove", "", "Pride"})
    public static final class WrappedCollectionIterator<O, T>
    implements Iterator<T>,
    KMutableIterator {
        @NotNull
        private final Iterator<O> wrapped;
        @NotNull
        private final Function1<O, T> unwrapper;

        @Override
        public boolean hasNext() {
            return this.wrapped.hasNext();
        }

        @Override
        public T next() {
            return this.unwrapper.invoke(this.wrapped.next());
        }

        @Override
        public void remove() {
            this.wrapped.remove();
        }

        @NotNull
        public final Iterator<O> getWrapped() {
            return this.wrapped;
        }

        @NotNull
        public final Function1<O, T> getUnwrapper() {
            return this.unwrapper;
        }

        public WrappedCollectionIterator(@NotNull Iterator<? extends O> wrapped, @NotNull Function1<? super O, ? extends T> unwrapper) {
            Intrinsics.checkParameterIsNotNull(wrapped, "wrapped");
            Intrinsics.checkParameterIsNotNull(unwrapper, "unwrapper");
            this.wrapped = wrapped;
            this.unwrapper = unwrapper;
        }
    }
}
