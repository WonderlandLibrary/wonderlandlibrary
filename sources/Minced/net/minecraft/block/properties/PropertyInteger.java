// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block.properties;

import com.google.common.base.Optional;
import java.util.Set;
import java.util.Collection;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;

public class PropertyInteger extends PropertyHelper<Integer>
{
    private final ImmutableSet<Integer> allowedValues;
    
    protected PropertyInteger(final String name, final int min, final int max) {
        super(name, Integer.class);
        if (min < 0) {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        }
        if (max <= min) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        }
        final Set<Integer> set = (Set<Integer>)Sets.newHashSet();
        for (int i = min; i <= max; ++i) {
            set.add(i);
        }
        this.allowedValues = (ImmutableSet<Integer>)ImmutableSet.copyOf((Collection)set);
    }
    
    @Override
    public Collection<Integer> getAllowedValues() {
        return (Collection<Integer>)this.allowedValues;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof PropertyInteger && super.equals(p_equals_1_)) {
            final PropertyInteger propertyinteger = (PropertyInteger)p_equals_1_;
            return this.allowedValues.equals((Object)propertyinteger.allowedValues);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.allowedValues.hashCode();
    }
    
    public static PropertyInteger create(final String name, final int min, final int max) {
        return new PropertyInteger(name, min, max);
    }
    
    @Override
    public Optional<Integer> parseValue(final String value) {
        try {
            final Integer integer = Integer.valueOf(value);
            return (Optional<Integer>)(this.allowedValues.contains((Object)integer) ? Optional.of((Object)integer) : Optional.absent());
        }
        catch (NumberFormatException var3) {
            return (Optional<Integer>)Optional.absent();
        }
    }
    
    @Override
    public String getName(final Integer value) {
        return value.toString();
    }
}
