package net.minecraft.util;

public abstract interface IRegistry
  extends Iterable
{
  public abstract Object getObject(Object paramObject);
  
  public abstract void putObject(Object paramObject1, Object paramObject2);
}
