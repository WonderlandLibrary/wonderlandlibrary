/*
 * Decompiled with CFR 0.152.
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
extends PropertyEnum<EnumFacing> {
    public static PropertyDirection create(String string, Predicate<EnumFacing> predicate) {
        return PropertyDirection.create(string, Collections2.filter((Collection)Lists.newArrayList((Object[])EnumFacing.values()), predicate));
    }

    protected PropertyDirection(String string, Collection<EnumFacing> collection) {
        super(string, EnumFacing.class, collection);
    }

    public static PropertyDirection create(String string, Collection<EnumFacing> collection) {
        return new PropertyDirection(string, collection);
    }

    public static PropertyDirection create(String string) {
        return PropertyDirection.create(string, (Predicate<EnumFacing>)Predicates.alwaysTrue());
    }
}

