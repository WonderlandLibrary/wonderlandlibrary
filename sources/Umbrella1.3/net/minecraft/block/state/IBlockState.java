/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 */
package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public interface IBlockState {
    public Collection getPropertyNames();

    public Comparable getValue(IProperty var1);

    public IBlockState withProperty(IProperty var1, Comparable var2);

    public IBlockState cycleProperty(IProperty var1);

    public ImmutableMap getProperties();

    public Block getBlock();
}

