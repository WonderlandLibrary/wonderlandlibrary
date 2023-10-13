/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Sets
 */
package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyInteger
extends PropertyHelper {
    private final ImmutableSet allowedValues;
    private static final String __OBFID = "CL_00002014";

    protected PropertyInteger(String name, int min, int max) {
        super(name, Integer.class);
        if (min < 0) {
            throw new IllegalArgumentException("Min value of " + name + " must be 0 or greater");
        }
        if (max <= min) {
            throw new IllegalArgumentException("Max value of " + name + " must be greater than min (" + min + ")");
        }
        HashSet var4 = Sets.newHashSet();
        for (int var5 = min; var5 <= max; ++var5) {
            var4.add(var5);
        }
        this.allowedValues = ImmutableSet.copyOf((Collection)var4);
    }

    @Override
    public Collection getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            if (!super.equals(p_equals_1_)) {
                return false;
            }
            PropertyInteger var2 = (PropertyInteger)p_equals_1_;
            return this.allowedValues.equals((Object)var2.allowedValues);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int var1 = super.hashCode();
        var1 = 31 * var1 + this.allowedValues.hashCode();
        return var1;
    }

    public static PropertyInteger create(String name, int min, int max) {
        return new PropertyInteger(name, min, max);
    }

    public String getName0(Integer value) {
        return value.toString();
    }

    @Override
    public String getName(Comparable value) {
        return this.getName0((Integer)value);
    }
}

