/*
 * Decompiled with CFR 0.152.
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

public class PropertyEnum<T extends Enum<T>>
extends PropertyHelper<T> {
    private final ImmutableSet<T> allowedValues;
    private final Map<String, T> nameToValue = Maps.newHashMap();

    protected PropertyEnum(String name, Class<T> valueClass, Collection<T> allowedValues) {
        super(name, valueClass);
        this.allowedValues = ImmutableSet.copyOf(allowedValues);
        for (Enum t2 : allowedValues) {
            String s2 = ((IStringSerializable)((Object)t2)).getName();
            if (this.nameToValue.containsKey(s2)) {
                throw new IllegalArgumentException("Multiple values have the same name '" + s2 + "'");
            }
            this.nameToValue.put(s2, t2);
        }
    }

    @Override
    public Collection<T> getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public String getName(T value) {
        return ((IStringSerializable)value).getName();
    }

    public static <T extends Enum<T>> PropertyEnum<T> create(String name, Class<T> clazz) {
        return PropertyEnum.create(name, clazz, Predicates.alwaysTrue());
    }

    public static <T extends Enum<T>> PropertyEnum<T> create(String name, Class<T> clazz, Predicate<T> filter) {
        return PropertyEnum.create(name, clazz, Collections2.filter(Lists.newArrayList(clazz.getEnumConstants()), filter));
    }

    public static <T extends Enum<T>> PropertyEnum<T> create(String name, Class<T> clazz, T ... values) {
        return PropertyEnum.create(name, clazz, Lists.newArrayList(values));
    }

    public static <T extends Enum<T>> PropertyEnum<T> create(String name, Class<T> clazz, Collection<T> values) {
        return new PropertyEnum<T>(name, clazz, values);
    }
}

