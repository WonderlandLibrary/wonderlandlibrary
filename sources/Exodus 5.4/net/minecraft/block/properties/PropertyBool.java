/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableSet
 */
package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;
import net.minecraft.block.properties.PropertyHelper;

public class PropertyBool
extends PropertyHelper<Boolean> {
    private final ImmutableSet<Boolean> allowedValues = ImmutableSet.of((Object)true, (Object)false);

    public static PropertyBool create(String string) {
        return new PropertyBool(string);
    }

    @Override
    public Collection<Boolean> getAllowedValues() {
        return this.allowedValues;
    }

    @Override
    public String getName(Boolean bl) {
        return bl.toString();
    }

    protected PropertyBool(String string) {
        super(string, Boolean.class);
    }
}

