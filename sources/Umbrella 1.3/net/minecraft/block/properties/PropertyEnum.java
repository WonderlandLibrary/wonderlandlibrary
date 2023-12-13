/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  com.google.common.base.Predicates
 *  com.google.common.collect.Collections2
 *  com.google.common.collect.ImmutableSet
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 */
package net.minecraft.block.properties;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.IStringSerializable;

public class PropertyEnum
extends PropertyHelper {
    private final ImmutableSet allowedValues;
    private final Map nameToValue = Maps.newHashMap();
    private static final String __OBFID = "CL_00002015";

    protected PropertyEnum(String name, Class valueClass, Collection allowedValues) {
        super(name, valueClass);
        this.allowedValues = ImmutableSet.copyOf((Collection)allowedValues);
        for (Enum var5 : allowedValues) {
            String var6 = ((IStringSerializable)((Object)var5)).getName();
            if (this.nameToValue.containsKey(var6)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + var6 + "'");
            }
            this.nameToValue.put(var6, var5);
        }
    }

    @Override
    public Collection getAllowedValues() {
        return this.allowedValues;
    }

    public String getName(Enum value) {
        return ((IStringSerializable)((Object)value)).getName();
    }

    public static PropertyEnum create(String name, Class clazz) {
        return PropertyEnum.create(name, clazz, Predicates.alwaysTrue());
    }

    public static PropertyEnum create(String name, Class clazz, Predicate filter) {
        return PropertyEnum.create(name, clazz, Collections2.filter((Collection)Lists.newArrayList((Object[])clazz.getEnumConstants()), (Predicate)filter));
    }

    public static PropertyEnum create(String name, Class clazz, Enum ... values) {
        return PropertyEnum.create(name, clazz, Lists.newArrayList((Object[])values));
    }

    public static PropertyEnum create(String name, Class clazz, Collection values) {
        return new PropertyEnum(name, clazz, values);
    }

    @Override
    public String getName(Comparable value) {
        return this.getName((Enum)((Object)value));
    }
}

