/*
 * Decompiled with CFR 0.152.
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
extends PropertyEnum<EnumFacing> {
    protected PropertyDirection(String name, Collection<EnumFacing> values) {
        super(name, EnumFacing.class, values);
    }

    public static PropertyDirection create(String name) {
        return PropertyDirection.create(name, Predicates.<EnumFacing>alwaysTrue());
    }

    public static PropertyDirection create(String name, Predicate<EnumFacing> filter) {
        return PropertyDirection.create(name, Collections2.filter(Lists.newArrayList(EnumFacing.values()), filter));
    }

    public static PropertyDirection create(String name, Collection<EnumFacing> values) {
        return new PropertyDirection(name, values);
    }
}

