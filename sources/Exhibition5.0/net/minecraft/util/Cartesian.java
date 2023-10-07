// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import java.util.NoSuchElementException;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.ArrayList;
import com.google.common.collect.Lists;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class Cartesian
{
    private static final String __OBFID = "CL_00002327";
    
    public static Iterable cartesianProduct(final Class clazz, final Iterable sets) {
        return new Product(clazz, (Iterable[])toArray(Iterable.class, sets), null);
    }
    
    public static Iterable cartesianProduct(final Iterable sets) {
        return arraysAsLists(cartesianProduct(Object.class, sets));
    }
    
    private static Iterable arraysAsLists(final Iterable arrays) {
        return Iterables.transform(arrays, (Function)new GetList(null));
    }
    
    private static Object[] toArray(final Class clazz, final Iterable it) {
        final ArrayList var2 = Lists.newArrayList();
        for (final Object var4 : it) {
            var2.add(var4);
        }
        return var2.toArray(createArray(clazz, var2.size()));
    }
    
    private static Object[] createArray(final Class p_179319_0_, final int p_179319_1_) {
        return (Object[])Array.newInstance(p_179319_0_, p_179319_1_);
    }
    
    static class GetList implements Function
    {
        private static final String __OBFID = "CL_00002325";
        
        private GetList() {
        }
        
        public List apply(final Object[] array) {
            return Arrays.asList(array);
        }
        
        public Object apply(final Object p_apply_1_) {
            return this.apply((Object[])p_apply_1_);
        }
        
        GetList(final Object p_i46022_1_) {
            this();
        }
    }
    
    static class Product implements Iterable
    {
        private final Class clazz;
        private final Iterable[] iterables;
        private static final String __OBFID = "CL_00002324";
        
        private Product(final Class clazz, final Iterable[] iterables) {
            this.clazz = clazz;
            this.iterables = iterables;
        }
        
        @Override
        public Iterator iterator() {
            return (Iterator)((this.iterables.length <= 0) ? Collections.singletonList(createArray(this.clazz, 0)).iterator() : new ProductIterator(this.clazz, this.iterables, null));
        }
        
        Product(final Class p_i46021_1_, final Iterable[] p_i46021_2_, final Object p_i46021_3_) {
            this(p_i46021_1_, p_i46021_2_);
        }
        
        static class ProductIterator extends UnmodifiableIterator
        {
            private int index;
            private final Iterable[] iterables;
            private final Iterator[] iterators;
            private final Object[] results;
            private static final String __OBFID = "CL_00002323";
            
            private ProductIterator(final Class clazz, final Iterable[] iterables) {
                this.index = -2;
                this.iterables = iterables;
                this.iterators = (Iterator[])createArray(Iterator.class, this.iterables.length);
                for (int var3 = 0; var3 < this.iterables.length; ++var3) {
                    this.iterators[var3] = iterables[var3].iterator();
                }
                this.results = createArray(clazz, this.iterators.length);
            }
            
            private void endOfData() {
                this.index = -1;
                Arrays.fill(this.iterators, null);
                Arrays.fill(this.results, null);
            }
            
            public boolean hasNext() {
                if (this.index == -2) {
                    this.index = 0;
                    for (final Iterator var8 : this.iterators) {
                        if (!var8.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }
                    return true;
                }
                if (this.index >= this.iterators.length) {
                    this.index = this.iterators.length - 1;
                    while (this.index >= 0) {
                        Iterator var9 = this.iterators[this.index];
                        if (var9.hasNext()) {
                            break;
                        }
                        if (this.index == 0) {
                            this.endOfData();
                            break;
                        }
                        var9 = this.iterables[this.index].iterator();
                        this.iterators[this.index] = var9;
                        if (!var9.hasNext()) {
                            this.endOfData();
                            break;
                        }
                        --this.index;
                    }
                }
                return this.index >= 0;
            }
            
            public Object[] next0() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                while (this.index < this.iterators.length) {
                    this.results[this.index] = this.iterators[this.index].next();
                    ++this.index;
                }
                return this.results.clone();
            }
            
            public Object next() {
                return this.next0();
            }
            
            ProductIterator(final Class p_i46019_1_, final Iterable[] p_i46019_2_, final Object p_i46019_3_) {
                this(p_i46019_1_, p_i46019_2_);
            }
        }
    }
}
