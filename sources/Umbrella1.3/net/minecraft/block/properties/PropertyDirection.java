/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Collections2
 *  com.google.common.collect.Lists
 */
package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class PropertyDirection
extends PropertyEnum {
    private static final String __OBFID = "CL_00002016";

    protected PropertyDirection(String name, Collection values) {
        super(name, EnumFacing.class, values);
    }

    public static PropertyDirection create(String name) {
        return PropertyDirection.create(name, Predicates.alwaysTrue());
    }

    public static PropertyDirection create(String name, Predicate filter) {
        return PropertyDirection.create(name, Collections2.filter((Collection)Lists.newArrayList((Object[])EnumFacing.values()), (Predicate)filter));
    }

    public static PropertyDirection create(String name, Collection values) {
        return new PropertyDirection(name, values);
    }
}

