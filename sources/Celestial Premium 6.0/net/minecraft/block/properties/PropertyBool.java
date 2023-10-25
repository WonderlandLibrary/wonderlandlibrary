/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.block.properties;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyBool
extends PropertyHelper<Boolean> {
    private final ImmutableSet<Boolean> allowedValues = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));

    protected PropertyBool(String name) {
        super(name, Boolean.class);
    }

    @Override
    public Collection<Boolean> getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyBool create(String name) {
        return new PropertyBool(name);
    }

    @Override
    public Optional<Boolean> parseValue(String value) {
        return !"true".equals(value) && !"false".equals(value) ? Optional.absent() : Optional.of(Boolean.valueOf(value));
    }

    @Override
    public String getName(Boolean value) {
        return value.toString();
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ instanceof PropertyBool && super.equals(p_equals_1_)) {
            PropertyBool propertybool = (PropertyBool)p_equals_1_;
            return this.allowedValues.equals(propertybool.allowedValues);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.allowedValues.hashCode();
    }
}

