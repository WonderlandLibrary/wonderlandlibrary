package net.minecraft.block.properties;

import java.util.Collection;

public abstract interface IProperty
{
  public abstract String getName();
  
  public abstract Collection getAllowedValues();
  
  public abstract Class getValueClass();
  
  public abstract String getName(Comparable paramComparable);
}
