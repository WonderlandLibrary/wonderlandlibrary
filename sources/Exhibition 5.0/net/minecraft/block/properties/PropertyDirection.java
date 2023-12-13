// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.properties;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EnumFacing;
import java.util.Collection;

public class PropertyDirection extends PropertyEnum
{
    private static final String __OBFID = "CL_00002016";
    
    protected PropertyDirection(final String name, final Collection values) {
        super(name, EnumFacing.class, values);
    }
    
    public static PropertyDirection create(final String name) {
        return create(name, Predicates.alwaysTrue());
    }
    
    public static PropertyDirection create(final String name, final Predicate filter) {
        return create(name, Collections2.filter((Collection)Lists.newArrayList((Object[])EnumFacing.values()), filter));
    }
    
    public static PropertyDirection create(final String name, final Collection values) {
        return new PropertyDirection(name, values);
    }
}
