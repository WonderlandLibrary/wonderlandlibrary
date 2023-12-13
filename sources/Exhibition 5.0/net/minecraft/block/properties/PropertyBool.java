// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.properties;

import java.util.Collection;
import com.google.common.collect.ImmutableSet;

public class PropertyBool extends PropertyHelper
{
    private final ImmutableSet allowedValues;
    private static final String __OBFID = "CL_00002017";
    
    protected PropertyBool(final String name) {
        super(name, Boolean.class);
        this.allowedValues = ImmutableSet.of((Object)true, (Object)false);
    }
    
    @Override
    public Collection getAllowedValues() {
        return (Collection)this.allowedValues;
    }
    
    public static PropertyBool create(final String name) {
        return new PropertyBool(name);
    }
    
    public String getName0(final Boolean value) {
        return value.toString();
    }
    
    @Override
    public String getName(final Comparable value) {
        return this.getName0((Boolean)value);
    }
}
