/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Objects
 */
package net.minecraft.block.properties;

import com.google.common.base.Objects;
import net.minecraft.block.properties.IProperty;

public abstract class PropertyHelper
implements IProperty {
    private final Class valueClass;
    private final String name;
    private static final String __OBFID = "CL_00002018";

    protected PropertyHelper(String name, Class valueClass) {
        this.valueClass = valueClass;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class getValueClass() {
        return this.valueClass;
    }

    public String toString() {
        return Objects.toStringHelper((Object)this).add("name", (Object)this.name).add("clazz", (Object)this.valueClass).add("values", (Object)this.getAllowedValues()).toString();
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            PropertyHelper var2 = (PropertyHelper)p_equals_1_;
            return this.valueClass.equals(var2.valueClass) && this.name.equals(var2.name);
        }
        return false;
    }

    public int hashCode() {
        return 31 * this.valueClass.hashCode() + this.name.hashCode();
    }
}

