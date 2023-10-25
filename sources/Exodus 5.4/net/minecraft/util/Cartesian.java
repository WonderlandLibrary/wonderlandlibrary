/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Function
 *  com.google.common.collect.Iterables
 *  com.google.common.collect.Lists
 *  com.google.common.collect.UnmodifiableIterator
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
    private static <T> T[] createArray(Class<? super T> clazz, int n) {
        return (Object[])Array.newInstance(clazz, n);
    }

    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> iterable) {
        return new Product(clazz, Cartesian.toArray(Iterable.class, iterable));
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> iterable) {
        return Cartesian.arraysAsLists(Cartesian.cartesianProduct(Object.class, iterable));
    }

    private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> iterable) {
        ArrayList arrayList = Lists.newArrayList();
        for (T t : iterable) {
            arrayList.add(t);
        }
        return arrayList.toArray(Cartesian.createArray(clazz, arrayList.size()));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> iterable) {
        return Iterables.transform(iterable, new GetList());
    }

    static class Product<T>
    implements Iterable<T[]> {
        private final Class<T> clazz;
        private final Iterable<? extends T>[] iterables;

        @Override
        public Iterator<T[]> iterator() {
            return this.iterables.length <= 0 ? Collections.singletonList(Cartesian.createArray(this.clazz, 0)).iterator() : new ProductIterator(this.clazz, this.iterables);
        }

        private Product(Class<T> clazz, Iterable<? extends T>[] iterableArray) {
            this.clazz = clazz;
            this.iterables = iterableArray;
        }

        static class ProductIterator<T>
        extends UnmodifiableIterator<T[]> {
            private int index = -2;
            private final Iterable<? extends T>[] iterables;
            private final Iterator<? extends T>[] iterators;
            private final T[] results;

            private ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterableArray) {
                this.iterables = iterableArray;
                this.iterators = (Iterator[])Cartesian.createArray(Iterator.class, this.iterables.length);
                int n = 0;
                while (n < this.iterables.length) {
                    this.iterators[n] = iterableArray[n].iterator();
                    ++n;
                }
                this.results = Cartesian.createArray(clazz, this.iterators.length);
            }

            /*
             * Unable to fully structure code
             */
            public T[] next() {
                if (this.hasNext()) ** GOTO lbl5
                throw new NoSuchElementException();
lbl-1000:
                // 1 sources

                {
                    this.results[this.index] = this.iterators[this.index].next();
                    ++this.index;
lbl5:
                    // 2 sources

                    ** while (this.index < this.iterators.length)
                }
lbl6:
                // 1 sources

                return (Object[])this.results.clone();
            }

            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    Iterator<? extends T>[] iteratorArray = this.iterators;
                    int n = this.iterators.length;
                    int n2 = 0;
                    while (n2 < n) {
                        Iterator<T> iterator = iteratorArray[n2];
                        if (!iterator.hasNext()) {
                            this.endOfData();
                            break;
                        }
                        ++n2;
                    }
                    return true;
                }
                if (this.index >= this.iterators.length) {
                    this.index = this.iterators.length - 1;
                    while (this.index >= 0) {
                        Iterator<T> iterator = this.iterators[this.index];
                        if (iterator.hasNext()) break;
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
                return this.index >= 0;
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }
        }
    }

    static class GetList<T>
    implements Function<Object[], List<T>> {
        public List<T> apply(Object[] objectArray) {
            return Arrays.asList(objectArray);
        }

        private GetList() {
        }
    }
}

