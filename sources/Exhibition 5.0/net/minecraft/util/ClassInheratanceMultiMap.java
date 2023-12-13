// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Iterators;
import org.apache.commons.lang3.ClassUtils;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.HashMultimap;
import java.util.Set;
import com.google.common.collect.Multimap;
import java.util.AbstractSet;

public class ClassInheratanceMultiMap extends AbstractSet
{
    private final Multimap field_180218_a;
    private final Set field_180216_b;
    private final Class field_180217_c;
    private static final String __OBFID = "CL_00002266";
    
    public ClassInheratanceMultiMap(final Class p_i45909_1_) {
        this.field_180218_a = (Multimap)HashMultimap.create();
        this.field_180216_b = Sets.newIdentityHashSet();
        this.field_180217_c = p_i45909_1_;
        this.field_180216_b.add(p_i45909_1_);
    }
    
    public void func_180213_a(final Class p_180213_1_) {
        for (final Object var3 : this.field_180218_a.get((Object)this.func_180212_a(p_180213_1_, false))) {
            if (p_180213_1_.isAssignableFrom(var3.getClass())) {
                this.field_180218_a.put((Object)p_180213_1_, var3);
            }
        }
        this.field_180216_b.add(p_180213_1_);
    }
    
    protected Class func_180212_a(final Class p_180212_1_, final boolean p_180212_2_) {
        for (final Class var4 : ClassUtils.hierarchy(p_180212_1_, ClassUtils.Interfaces.INCLUDE)) {
            if (this.field_180216_b.contains(var4)) {
                if (var4 == this.field_180217_c && p_180212_2_) {
                    this.func_180213_a(p_180212_1_);
                }
                return var4;
            }
        }
        throw new IllegalArgumentException("Don't know how to search for " + p_180212_1_);
    }
    
    @Override
    public boolean add(final Object p_add_1_) {
        for (final Class var3 : this.field_180216_b) {
            if (var3.isAssignableFrom(p_add_1_.getClass())) {
                this.field_180218_a.put((Object)var3, p_add_1_);
            }
        }
        return true;
    }
    
    @Override
    public boolean remove(final Object p_remove_1_) {
        boolean var3 = false;
        for (final Class var5 : this.field_180216_b) {
            if (var5.isAssignableFrom(p_remove_1_.getClass())) {
                var3 |= this.field_180218_a.remove((Object)var5, p_remove_1_);
            }
        }
        return var3;
    }
    
    public Iterable func_180215_b(final Class p_180215_1_) {
        return new Iterable() {
            private static final String __OBFID = "CL_00002265";
            
            @Override
            public Iterator iterator() {
                final Iterator var1 = ClassInheratanceMultiMap.this.field_180218_a.get((Object)ClassInheratanceMultiMap.this.func_180212_a(p_180215_1_, true)).iterator();
                return (Iterator)Iterators.filter(var1, p_180215_1_);
            }
        };
    }
    
    @Override
    public Iterator iterator() {
        final Iterator var1 = this.field_180218_a.get((Object)this.field_180217_c).iterator();
        return (Iterator)new AbstractIterator() {
            private static final String __OBFID = "CL_00002264";
            
            protected Object computeNext() {
                return var1.hasNext() ? var1.next() : this.endOfData();
            }
        };
    }
    
    @Override
    public int size() {
        return this.field_180218_a.get((Object)this.field_180217_c).size();
    }
}
