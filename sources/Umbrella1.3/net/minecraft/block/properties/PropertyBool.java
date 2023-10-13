/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyBool
extends PropertyHelper {
    private final ImmutableSet allowedValues = ImmutableSet.of((Object)true, (Object)false);
    private static final String __OBFID = "CL_00002017";

    protected PropertyBool(String name) {
        super(name, Boolean.class);
    }

    @Override
    public Collection getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyBool create(String name) {
        return new PropertyBool(name);
    }

    public String getName0(Boolean value) {
        return value.toString();
    }

    @Override
    public String getName(Comparable value) {
        return this.getName0((Boolean)value);
    }
}

