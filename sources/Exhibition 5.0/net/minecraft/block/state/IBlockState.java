// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.block.state;

import net.minecraft.block.Block;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.properties.IProperty;
import java.util.Collection;

public interface IBlockState
{
    Collection getPropertyNames();
    
    Comparable getValue(final IProperty p0);
    
    IBlockState withProperty(final IProperty p0, final Comparable p1);
    
    IBlockState cycleProperty(final IProperty p0);
    
    ImmutableMap getProperties();
    
    Block getBlock();
}
