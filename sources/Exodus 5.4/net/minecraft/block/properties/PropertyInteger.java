/*
 * Decompiled with CFR 0.152.
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
extends PropertyHelper<Integer> {
    private final ImmutableSet<Integer> allowedValues;

    protected PropertyInteger(String string, int n, int n2) {
        super(string, Integer.class);
        if (n < 0) {
            throw new IllegalArgumentException("Min value of " + string + " must be 0 or greater");
        }
        if (n2 <= n) {
            throw new IllegalArgumentException("Max value of " + string + " must be greater than min (" + n + ")");
        }
        HashSet hashSet = Sets.newHashSet();
        int n3 = n;
        while (n3 <= n2) {
            hashSet.add(n3);
            ++n3;
        }
        this.allowedValues = ImmutableSet.copyOf((Collection)hashSet);
    }

    @Override
    public String getName(Integer n) {
        return n.toString();
    }

    @Override
    public Collection<Integer> getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyInteger create(String string, int n, int n2) {
        return new PropertyInteger(string, n, n2);
    }

    @Override
    public int hashCode() {
        int n = super.hashCode();
        n = 31 * n + this.allowedValues.hashCode();
        return n;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            if (!super.equals(object)) {
                return false;
            }
            PropertyInteger propertyInteger = (PropertyInteger)object;
            return this.allowedValues.equals(propertyInteger.allowedValues);
        }
        return false;
    }
}

