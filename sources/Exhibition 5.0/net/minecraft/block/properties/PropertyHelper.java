// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.properties;

import com.google.common.base.Objects;

public abstract class PropertyHelper implements IProperty
{
    private final Class valueClass;
    private final String name;
    private static final String __OBFID = "CL_00002018";
    
    protected PropertyHelper(final String name, final Class valueClass) {
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
    
    @Override
    public String toString() {
        return Objects.toStringHelper((Object)this).add("name", (Object)this.name).add("clazz", (Object)this.valueClass).add("values", (Object)this.getAllowedValues()).toString();
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            final PropertyHelper var2 = (PropertyHelper)p_equals_1_;
            return this.valueClass.equals(var2.valueClass) && this.name.equals(var2.name);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.valueClass.hashCode() + this.name.hashCode();
    }
}
