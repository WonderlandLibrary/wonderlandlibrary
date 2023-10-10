/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Cartesian {
    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> sets) {
        return new Product(clazz, Cartesian.toArray(Iterable.class, sets));
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> sets) {
        return Cartesian.arraysAsLists(Cartesian.cartesianProduct(Object.class, sets));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays) {
        return Iterables.transform(arrays, new GetList());
    }

    private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> it) {
        ArrayList<T> list = Lists.newArrayList();
        Iterator<T> iterator = it.iterator();
        while (iterator.hasNext()) {
            T t = iterator.next();
            list.add(t);
        }
        return list.toArray(Cartesian.createArray(clazz, list.size()));
    }

    private static <T> T[] createArray(Class<? super T> p_179319_0_, int p_179319_1_) {
        return (Object[])Array.newInstance(p_179319_0_, p_179319_1_);
    }

    static class Product<T>
    implements Iterable<T[]> {
        private final Class<T> clazz;
        private final Iterable<? extends T>[] iterables;

        private Product(Class<T> clazz, Iterable<? extends T>[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }

        @Override
        public Iterator<T[]> iterator() {
            Iterator<T[]> iterator;
            if (this.iterables.length <= 0) {
                iterator = Collections.singletonList(Cartesian.createArray(this.clazz, 0)).iterator();
                return iterator;
            }
            iterator = new ProductIterator(this.clazz, this.iterables);
            return iterator;
        }

        static class ProductIterator<T>
        extends UnmodifiableIterator<T[]> {
            private int index = -2;
            private final Iterable<? extends T>[] iterables;
            private final Iterator<? extends T>[] iterators;
            private final T[] results;

            private ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterables) {
                this.iterables = iterables;
                this.iterators = (Iterator[])Cartesian.createArray(Iterator.class, this.iterables.length);
                int i = 0;
                while (true) {
                    if (i >= this.iterables.length) {
                        this.results = Cartesian.createArray(clazz, this.iterators.length);
                        return;
                    }
                    this.iterators[i] = iterables[i].iterator();
                    ++i;
                }
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }

            @Override
            public boolean hasNext() {
                block6: {
                    Iterator<T> iterator;
                    block7: {
                        block5: {
                            if (this.index == -2) break block5;
                            if (this.index < this.iterators.length) break block6;
                            this.index = this.iterators.length - 1;
                            break block7;
                        }
                        this.index = 0;
                        Iterator<? extends T>[] iteratorArray = this.iterators;
                        int n = iteratorArray.length;
                        int n2 = 0;
                        while (n2 < n) {
                            Iterator<T> iterator1 = iteratorArray[n2];
                            if (!iterator1.hasNext()) {
                                this.endOfData();
                                return true;
                            }
                            ++n2;
                        }
                        return true;
                    }
                    while (this.index >= 0 && !(iterator = this.iterators[this.index]).hasNext()) {
                        if (this.index == 0) {
                            this.endOfData();
                            break;
                        }
                        iterator = this.iterables[this.index].iterator();
                        this.iterators[this.index] = iterator;
                        if (!iterator.hasNext()) {
                            this.endOfData();
                            break;
                        }
                        --this.index;
                    }
                }
                if (this.index < 0) return false;
                return true;
            }

            @Override
            public T[] next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                while (this.index < this.iterators.length) {
                    this.results[this.index] = this.iterators[this.index].next();
                    ++this.index;
                }
                return (Object[])this.results.clone();
            }
        }
    }

    static class GetList<T>
    implements Function<Object[], List<T>> {
        private GetList() {
        }

        @Override
        public List<T> apply(Object[] p_apply_1_) {
            return Arrays.asList(p_apply_1_);
        }
    }
}

