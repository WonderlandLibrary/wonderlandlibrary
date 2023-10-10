/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.properties;

import com.google.common.base.Objects;
import net.minecraft.block.properties.IProperty;

public abstract class PropertyHelper<T extends Comparable<T>>
implements IProperty<T> {
    private final Class<T> valueClass;
    private final String name;

    protected PropertyHelper(String name, Class<T> valueClass) {
        this.valueClass = valueClass;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<T> getValueClass() {
        return this.valueClass;
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("name", (Object)this.name).add("clazz", this.valueClass).add("values", this.getAllowedValues()).toString();
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ == null) return false;
        if (this.getClass() != p_equals_1_.getClass()) return false;
        PropertyHelper propertyhelper = (PropertyHelper)p_equals_1_;
        if (!this.valueClass.equals(propertyhelper.valueClass)) return false;
        if (!this.name.equals(propertyhelper.name)) return false;
        return true;
    }

    public int hashCode() {
        return 31 * this.valueClass.hashCode() + this.name.hashCode();
    }
}

