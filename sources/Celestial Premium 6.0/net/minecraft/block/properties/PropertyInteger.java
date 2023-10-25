/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyInteger
extends PropertyHelper<Integer> {
    private final ImmutableSet<Integer> allowedValues;

    protected PropertyInteger(String name, int min, int max) {
        super(name, Integer.class);
        if (min < 0) {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        }
        if (max <= min) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        }
        HashSet<Integer> set = Sets.newHashSet();
        for (int i = min; i <= max; ++i) {
            set.add(i);
        }
        this.allowedValues = ImmutableSet.copyOf(set);
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof PropertyInteger && super.equals(p_equals_1_)) {
            PropertyInteger propertyinteger = (PropertyInteger)p_equals_1_;
            return this.allowedValues.equals(propertyinteger.allowedValues);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.allowedValues.hashCode();
    }

    public static PropertyInteger create(String name, int min, int max) {
        return new PropertyInteger(name, min, max);
    }

    @Override
    public Optional<Integer> parseValue(String value) {
        try {
            Integer integer = Integer.valueOf(value);
            return this.allowedValues.contains(integer) ? Optional.of(integer) : Optional.absent();
        }
        catch (NumberFormatException var3) {
            return Optional.absent();
        }
    }

    @Override
    public String getName(Integer value) {
        return value.toString();
    }
}

