// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.commons.lang3.Validate;
import java.util.List;
import java.util.AbstractList;

public class NonNullList<E> extends AbstractList<E>
{
    private final List<E> delegate;
    private final E defaultElement;
    
    public static <E> NonNullList<E> create() {
        return new NonNullList<E>();
    }
    
    public static <E> NonNullList<E> withSize(final int size, final E fill) {
        Validate.notNull((Object)fill);
        final Object[] aobject = new Object[size];
        Arrays.fill(aobject, fill);
        return new NonNullList<E>(Arrays.asList((E[])aobject), fill);
    }
    
    public static <E> NonNullList<E> from(final E defaultElementIn, final E... elements) {
        return new NonNullList<E>(Arrays.asList(elements), defaultElementIn);
    }
    
    protected NonNullList() {
        this(new ArrayList<Object>(), null);
    }
    
    protected NonNullList(final List<E> delegateIn, @Nullable final E listType) {
        this.delegate = delegateIn;
        this.defaultElement = listType;
    }
    
    @Nonnull
    @Override
    public E get(final int p_get_1_) {
        return this.delegate.get(p_get_1_);
    }
    
    @Override
    public E set(final int p_set_1_, final E p_set_2_) {
        Validate.notNull((Object)p_set_2_);
        return this.delegate.set(p_set_1_, p_set_2_);
    }
    
    @Override
    public void add(final int p_add_1_, final E p_add_2_) {
        Validate.notNull((Object)p_add_2_);
        this.delegate.add(p_add_1_, p_add_2_);
    }
    
    @Override
    public E remove(final int p_remove_1_) {
        return this.delegate.remove(p_remove_1_);
    }
    
    @Override
    public int size() {
        return this.delegate.size();
    }
    
    @Override
    public void clear() {
        if (this.defaultElement == null) {
            super.clear();
        }
        else {
            for (int i = 0; i < this.size(); ++i) {
                this.set(i, this.defaultElement);
            }
        }
    }
}
