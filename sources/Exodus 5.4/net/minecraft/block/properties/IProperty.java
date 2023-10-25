/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty<T extends Comparable<T>> {
    public Class<T> getValueClass();

    public String getName();

    public Collection<T> getAllowedValues();

    public String getName(T var1);
}

