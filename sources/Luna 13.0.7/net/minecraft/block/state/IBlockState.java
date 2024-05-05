package net.minecraft.block.state;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;

public abstract interface IBlockState
{
  public abstract Collection getPropertyNames();
  
  public abstract Comparable getValue(IProperty paramIProperty);
  
  public abstract IBlockState withProperty(IProperty paramIProperty, Comparable paramComparable);
  
  public abstract IBlockState cycleProperty(IProperty paramIProperty);
  
  public abstract ImmutableMap getProperties();
  
  public abstract Block getBlock();
}
