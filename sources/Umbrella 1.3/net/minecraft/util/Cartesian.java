/*
 * Decompiled with CFR 0.150.
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
    private static final String __OBFID = "CL_00002327";

    public static Iterable cartesianProduct(Class clazz, Iterable sets) {
        return new Product(clazz, (Iterable[])Cartesian.toArray(Iterable.class, sets), null);
    }

    public static Iterable cartesianProduct(Iterable sets) {
        return Cartesian.arraysAsLists(Cartesian.cartesianProduct(Object.class, sets));
    }

    private static Iterable arraysAsLists(Iterable arrays) {
        return Iterables.transform((Iterable)arrays, (Function)new GetList(null));
    }

    private static Object[] toArray(Class clazz, Iterable it) {
        ArrayList var2 = Lists.newArrayList();
        for (Object var4 : it) {
            var2.add(var4);
        }
        return var2.toArray(Cartesian.createArray(clazz, var2.size()));
    }

    private static Object[] createArray(Class p_179319_0_, int p_179319_1_) {
        return (Object[])Array.newInstance(p_179319_0_, p_179319_1_);
    }

    static class GetList
    implements Function {
        private static final String __OBFID = "CL_00002325";

        private GetList() {
        }

        public List apply(Object[] array) {
            return Arrays.asList(array);
        }

        public Object apply(Object p_apply_1_) {
            return this.apply((Object[])p_apply_1_);
        }

        GetList(Object p_i46022_1_) {
            this();
        }
    }

    static class Product
    implements Iterable {
        private final Class clazz;
        private final Iterable[] iterables;
        private static final String __OBFID = "CL_00002324";

        private Product(Class clazz, Iterable[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }

        public Iterator iterator() {
            return this.iterables.length <= 0 ? Collections.singletonList(Cartesian.createArray(this.clazz, 0)).iterator() : new ProductIterator(this.clazz, this.iterables, null);
        }

        Product(Class p_i46021_1_, Iterable[] p_i46021_2_, Object p_i46021_3_) {
            this(p_i46021_1_, p_i46021_2_);
        }

        static class ProductIterator
        extends UnmodifiableIterator {
            private int index = -2;
            private final Iterable[] iterables;
            private final Iterator[] iterators;
            private final Object[] results;
            private static final String __OBFID = "CL_00002323";

            private ProductIterator(Class clazz, Iterable[] iterables) {
                this.iterables = iterables;
                this.iterators = (Iterator[])Cartesian.createArray(Iterator.class, this.iterables.length);
                for (int var3 = 0; var3 < this.iterables.length; ++var3) {
                    this.iterators[var3] = iterables[var3].iterator();
                }
                this.results = Cartesian.createArray(clazz, this.iterators.length);
            }

            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }

            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    for (Iterator var4 : this.iterators) {
                        if (var4.hasNext()) continue;
                        this.endOfData();
                        break;
                    }
                    return true;
                }
                if (this.index >= this.iterators.length) {
                    this.index = this.iterators.length - 1;
                    while (this.index >= 0) {
                        Iterator var1 = this.iterators[this.index];
                        if (var1.hasNext()) break;
                        if (this.index == 0) {
                            this.endOfData();
                            break;
                        }
                        this.iterators[this.index] = var1 = this.iterables[this.index].iterator();
                        if (!var1.hasNext()) {
                            this.endOfData();
                            break;
                        }
                        --this.index;
                    }
                }
                return this.index >= 0;
            }

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Lifted jumps to return sites
             */
            public Object[] next0() {
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

            public Object next() {
                return this.next0();
            }

            ProductIterator(Class p_i46019_1_, Iterable[] p_i46019_2_, Object p_i46019_3_) {
                this(p_i46019_1_, p_i46019_2_);
            }
        }
    }
}

